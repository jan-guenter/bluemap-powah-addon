/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.RenderSettings;
import de.bluecolored.bluemap.core.map.hires.block.BlockRenderer;
import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.util.Keyed;
import de.bluecolored.bluemap.core.util.Registry;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;

/** BlueMap 5.22 internal ABI registration boundary. */
public final class BlueMap522Adapter {

    private static final PowahRuntime RUNTIME = PowahRuntime.INSTANCE;
    private static final BlockRendererType RENDERER = new BlockRendererType.Impl(
            Key.parse("bluemap_powah:stable_geometry"), BlueMap522Adapter::createRenderer
    );
    private static final ResourcePack.Extension<PowahResourceExtension> EXTENSION =
            new PowahResourceExtensionType(RENDERER, RUNTIME);
    private static final BlockEntityType REACTOR = new BlockEntityType.Impl(
            Key.parse("powah:reactor"), ReactorBlockEntityData.class
    );
    private static final BlockEntityType REACTOR_PART = new BlockEntityType.Impl(
            Key.parse("powah:reactor_part"), ReactorBlockEntityData.class
    );
    private static final BlockEntityType ENERGY_CABLE = new BlockEntityType.Impl(
            Key.parse("powah:energy_cable"), EnergyCableBlockEntityData.class
    );

    private BlueMap522Adapter() {
    }

    public static synchronized boolean install() {
        if (!canRegister(BlockRendererType.REGISTRY, RENDERER)
                || !canRegister(ResourcePack.Extension.REGISTRY, EXTENSION)
                || !canRegister(BlockEntityType.REGISTRY, REACTOR)
                || !canRegister(BlockEntityType.REGISTRY, REACTOR_PART)
                || !canRegister(BlockEntityType.REGISTRY, ENERGY_CABLE)) {
            RUNTIME.inactive("registry-collision");
            return false;
        }
        return register(BlockRendererType.REGISTRY, RENDERER)
                && register(ResourcePack.Extension.REGISTRY, EXTENSION)
                && register(BlockEntityType.REGISTRY, REACTOR)
                && register(BlockEntityType.REGISTRY, REACTOR_PART)
                && register(BlockEntityType.REGISTRY, ENERGY_CABLE);
    }

    private static BlockRenderer createRenderer(
            ResourcePack pack,
            TextureGallery gallery,
            RenderSettings settings
    ) {
        try {
            return new PowahRenderer(pack, gallery, settings, RUNTIME, RUNTIME.catalog(pack));
        } catch (Error error) {
            PowahRuntime.throwIfFatal(error);
            RUNTIME.failSoftMinimal("renderer-construction", error);
            return BlockRendererType.DEFAULT.create(pack, gallery, settings);
        }
    }

    private static <T extends Keyed> boolean canRegister(Registry<T> registry, T candidate) {
        T existing = registry.get(candidate.getKey());
        return existing == null || existing == candidate;
    }

    private static <T extends Keyed> boolean register(Registry<T> registry, T candidate) {
        T existing = registry.get(candidate.getKey());
        if (existing == null) {
            registry.register(candidate);
            existing = registry.get(candidate.getKey());
        }
        return existing == candidate;
    }
}
