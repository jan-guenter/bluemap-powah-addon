/*
 * SPDX-License-Identifier: LGPL-3.0-only
 *
 * Geometry and persisted-state semantics are adapted from Powah 6.2.10.
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.MaxCapacityReachedException;
import de.bluecolored.bluemap.core.map.hires.RenderSettings;
import de.bluecolored.bluemap.core.map.hires.TileModel;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.map.hires.block.BlockRenderer;
import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.util.math.Color;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;

import java.util.IdentityHashMap;
import java.util.Map;

/** Preserves Powah's stock JSON model and adds its stable block-entity geometry. */
final class PowahRenderer implements BlockRenderer {

    private static final float CABLE_MIN = 6.5F / 16F;
    private static final float CABLE_MAX = 9.5F / 16F;
    private static final float PLATE_MIN = 5.5F / 16F;
    private static final float PLATE_MAX = 10.5F / 16F;
    private static final ThreadLocal<Boolean> HOST_FALLBACK =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    private final ResourcePack resourcePack;
    private final TextureGallery textures;
    private final RenderSettings settings;
    private final PowahRuntime runtime;
    private final VariantRendererCatalog catalog;
    private final PrimitiveEmitter emitter;
    private final Map<BlockRendererType, BlockRenderer> hosts = new IdentityHashMap<>();
    private final ThreadLocal<Visit> visits = ThreadLocal.withInitial(Visit::new);

    PowahRenderer(
            ResourcePack resourcePack,
            TextureGallery textures,
            RenderSettings settings,
            PowahRuntime runtime,
            VariantRendererCatalog catalog
    ) {
        this.resourcePack = resourcePack;
        this.textures = textures;
        this.settings = settings;
        this.runtime = runtime;
        this.catalog = catalog;
        this.emitter = new PrimitiveEmitter(textures);
    }

    @Override
    public void render(
            BlockNeighborhood block,
            Variant variant,
            TileModelView target,
            Color mapColor
    ) {
        int safeStart = target.getStart();
        try {
            renderPowah(block, variant, target, mapColor);
        } catch (Error error) {
            PowahRuntime.throwIfFatal(error);
            runtime.failSoftMinimal("renderer-outer", error);
            target.getTileModel().reset(safeStart);
            target.initialize(safeStart);
            if (HOST_FALLBACK.get()) {
                return;
            }
            HOST_FALLBACK.set(Boolean.TRUE);
            try {
                BlockRendererType.DEFAULT.create(resourcePack, textures, settings)
                        .render(block, variant, target, mapColor);
            } catch (Error fallbackError) {
                PowahRuntime.throwIfFatal(fallbackError);
                target.getTileModel().reset(safeStart);
                target.initialize(safeStart);
                runtime.failSoftMinimal("renderer-default-fallback", fallbackError);
            } finally {
                HOST_FALLBACK.set(Boolean.FALSE);
            }
        }
    }

    private void renderPowah(
            BlockNeighborhood block,
            Variant variant,
            TileModelView target,
            Color mapColor
    ) {
        BlockRendererType hostType = catalog == null
                ? BlockRendererType.DEFAULT : catalog.original(variant);
        hosts.computeIfAbsent(hostType, type -> type.create(resourcePack, textures, settings))
                .render(block, variant, target, mapColor);
        if (!runtime.active() || !visits.get().first(block, target.getTileModel())) {
            return;
        }
        int overlayStart = target.getTileModel().size();
        try {
            String id = block.getBlockState().getId().getFormatted();
            if (PowahCatalog.reactor(id)) {
                reactor(block, target, id);
            } else if (PowahCatalog.cable(id)) {
                cable(block, target, id);
            } else if ("powah:energizing_orb".equals(id)) {
                orb(block, target);
            }
        } catch (MaxCapacityReachedException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            target.getTileModel().reset(overlayStart);
            target.initialize(overlayStart);
            runtime.report("overlay-render-failed-" + exception.getClass().getSimpleName());
        }
    }

    private void reactor(BlockNeighborhood block, TileModelView target, String id) {
        ReactorBlockEntityData data = block.getBlockEntity()
                instanceof ReactorBlockEntityData found ? found : null;
        boolean core = "true".equals(block.getBlockState().getProperties().get("core"));
        String tier = PowahCatalog.tier(id);
        if (data != null && data.built()) {
            if (!core) {
                return;
            }
            emitter.entityBox(block, target, Key.parse("powah:model/tile/reactor"),
                    -1F, 0F, -1F, 2F, 4F, 2F,
                    0F, 0F, 16F, 128F, 256F, 128F);
            if (!"starter".equals(tier)) {
                emitter.entityBox(block, target, Key.parse("powah:model/tile/reactor_" + tier),
                        -1.001F, -0.001F, -1.001F, 2.001F, 4.001F, 2.001F,
                        0F, 0F, 16F, 128F, 256F, 128F);
            }
            return;
        }
        emitter.entityBox(block, target,
                Key.parse("powah:model/tile/reactor_block_" + tier),
                0F, 0F, 0F, 1F, 1F, 1F,
                0F, 0F, 16F, 32F, 64F, 32F);
    }

    private void cable(BlockNeighborhood block, TileModelView target, String id) {
        EnergyCableBlockEntityData data = block.getBlockEntity()
                instanceof EnergyCableBlockEntityData found ? found : null;
        if (data == null) {
            return;
        }
        String tier = PowahCatalog.tier(id);
        for (int side = 0; side < 6; side++) {
            if ((data.connections() & 1 << side) == 0 || data.transfer(side) == 3) {
                continue;
            }
            String mode = switch (data.transfer(side)) {
                case 1 -> "in";
                case 2 -> "out";
                default -> "all";
            };
            endpoint(block, target,
                    Key.parse("powah:model/tile/energy_cable_" + tier + '_' + mode), side);
        }
    }

    private void endpoint(BlockNeighborhood block, TileModelView target, Key texture, int side) {
        switch (side) {
            case 0 -> {
                emitter.entityBox(block, target, texture,
                        CABLE_MIN, 0F, CABLE_MIN, CABLE_MAX, 6F / 16F, CABLE_MAX,
                        38F, 0F, 16F, 32F, 64F, 32F);
                emitter.entityBox(block, target, texture,
                        PLATE_MIN, 0F, PLATE_MIN, PLATE_MAX, 1F / 16F, PLATE_MAX,
                        26F, 20F, 16F, 32F, 64F, 32F);
            }
            case 1 -> {
                emitter.entityBox(block, target, texture,
                        CABLE_MIN, 10F / 16F, CABLE_MIN, CABLE_MAX, 1F, CABLE_MAX,
                        38F, 10F, 16F, 32F, 64F, 32F);
                emitter.entityBox(block, target, texture,
                        PLATE_MIN, 15F / 16F, PLATE_MIN, PLATE_MAX, 1F, PLATE_MAX,
                        26F, 20F, 16F, 32F, 64F, 32F);
            }
            case 2 -> {
                emitter.entityBox(block, target, texture,
                        CABLE_MIN, CABLE_MIN, 0F, CABLE_MAX, CABLE_MAX, 6F / 16F,
                        0F, 0F, 16F, 32F, 64F, 32F);
                emitter.entityBox(block, target, texture,
                        PLATE_MIN, PLATE_MIN, 0F, PLATE_MAX, PLATE_MAX, 1F / 16F,
                        0F, 20F, 16F, 32F, 64F, 32F);
            }
            case 3 -> {
                emitter.entityBox(block, target, texture,
                        CABLE_MIN, CABLE_MIN, 10F / 16F, CABLE_MAX, CABLE_MAX, 1F,
                        0F, 10F, 16F, 32F, 64F, 32F);
                emitter.entityBox(block, target, texture,
                        PLATE_MIN, PLATE_MIN, 15F / 16F, PLATE_MAX, PLATE_MAX, 1F,
                        0F, 20F, 16F, 32F, 64F, 32F);
            }
            case 4 -> {
                emitter.entityBox(block, target, texture,
                        0F, CABLE_MIN, CABLE_MIN, 6F / 16F, CABLE_MAX, CABLE_MAX,
                        19F, 0F, 16F, 32F, 64F, 32F);
                emitter.entityBox(block, target, texture,
                        0F, PLATE_MIN, PLATE_MIN, 1F / 16F, PLATE_MAX, PLATE_MAX,
                        13F, 20F, 16F, 32F, 64F, 32F);
            }
            case 5 -> {
                emitter.entityBox(block, target, texture,
                        10F / 16F, CABLE_MIN, CABLE_MIN, 1F, CABLE_MAX, CABLE_MAX,
                        19F, 7F, 16F, 32F, 64F, 32F);
                emitter.entityBox(block, target, texture,
                        15F / 16F, PLATE_MIN, PLATE_MIN, 1F, PLATE_MAX, PLATE_MAX,
                        13F, 20F, 16F, 32F, 64F, 32F);
            }
            default -> throw new IllegalArgumentException("unsupported cable side " + side);
        }
    }

    private void orb(BlockNeighborhood block, TileModelView target) {
        float x = 0.5F;
        float y = 0.5F;
        float z = 0.5F;
        switch (block.getBlockState().getProperties().getOrDefault("facing", "up")) {
            case "down" -> y += 0.1F;
            case "up" -> y -= 0.1F;
            case "north" -> z += 0.1F;
            case "south" -> z -= 0.1F;
            case "west" -> x += 0.1F;
            case "east" -> x -= 0.1F;
            default -> {
            }
        }
        float half = 9F / 32F;
        int glowStart = target.getTileModel().size();
        emitter.entityBox(block, target, PowahCatalog.ENERGY_CHARGE_RENDER,
                x - half, y - half, z - half, x + half, y + half, z + half,
                0F, 0F, 80F / 9F, 10F, 20F, 10F);
        for (int face = glowStart; face < target.getTileModel().size(); face++) {
            target.getTileModel().setSunlight(face, 15);
            target.getTileModel().setBlocklight(face, 15);
        }
    }

    /** Suppresses duplicate overlay emission for multipart host blockstates. */
    private static final class Visit {

        private TileModel tile;
        private int x = Integer.MIN_VALUE;
        private int y = Integer.MIN_VALUE;
        private int z = Integer.MIN_VALUE;

        boolean first(BlockNeighborhood block, TileModel currentTile) {
            if (tile == currentTile && x == block.getX() && y == block.getY() && z == block.getZ()) {
                return false;
            }
            tile = currentTile;
            x = block.getX();
            y = block.getY();
            z = block.getZ();
            return true;
        }
    }
}
