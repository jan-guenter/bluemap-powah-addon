/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap523;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.RenderSettings;
import de.bluecolored.bluemap.core.map.hires.block.BlockRenderer;
import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.mca.blockentity.BlockEntityType;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.RegistryGuard;
import io.github.janguenter.bluemap.addon.adapter.api.bluemap523.ResourceExtensionType;

/** Exact BlueMap 5.23 feature-backport registration boundary. */
public final class BlueMap523Adapter {

    private static final PowahRuntime RUNTIME = PowahRuntime.INSTANCE;
    private static final Key EXTENSION_KEY = Key.parse("bluemap_powah:prototype");
    private static final BlockRendererType RENDERER = new BlockRendererType.Impl(
            Key.parse("bluemap_powah:stable_geometry"), BlueMap523Adapter::createRenderer
    );
    private static final ResourcePack.Extension<PowahResourceExtension> EXTENSION =
            new ResourceExtensionType<>(
                    EXTENSION_KEY,
                    pack -> new PowahResourceExtension(pack, RENDERER, RUNTIME)
            );
    private static final BlockEntityType REACTOR = new BlockEntityType.Impl(
            Key.parse("powah:reactor"), ReactorBlockEntityData.class
    );
    private static final BlockEntityType REACTOR_PART = new BlockEntityType.Impl(
            Key.parse("powah:reactor_part"), ReactorBlockEntityData.class
    );
    private static final BlockEntityType ENERGY_CABLE = new BlockEntityType.Impl(
            Key.parse("powah:energy_cable"), EnergyCableBlockEntityData.class
    );

    private BlueMap523Adapter() {
    }

    public static synchronized boolean install() {
        if (!RegistryGuard.canRegister(BlockRendererType.REGISTRY, RENDERER)
                || !RegistryGuard.canRegister(ResourcePack.Extension.REGISTRY, EXTENSION)
                || !RegistryGuard.canRegister(BlockEntityType.REGISTRY, REACTOR)
                || !RegistryGuard.canRegister(BlockEntityType.REGISTRY, REACTOR_PART)
                || !RegistryGuard.canRegister(BlockEntityType.REGISTRY, ENERGY_CABLE)) {
            RUNTIME.inactive("registry-collision");
            return false;
        }
        return RegistryGuard.register(BlockRendererType.REGISTRY, RENDERER)
                && RegistryGuard.register(ResourcePack.Extension.REGISTRY, EXTENSION)
                && RegistryGuard.register(BlockEntityType.REGISTRY, REACTOR)
                && RegistryGuard.register(BlockEntityType.REGISTRY, REACTOR_PART)
                && RegistryGuard.register(BlockEntityType.REGISTRY, ENERGY_CABLE);
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

    static ResourcePack.Extension<PowahResourceExtension> extensionType() {
        return EXTENSION;
    }
}
