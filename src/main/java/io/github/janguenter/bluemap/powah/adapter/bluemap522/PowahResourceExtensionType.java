/*
 * SPDX-License-Identifier: MIT
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.util.Key;

/** Resource-pack extension factory registered before resource loading. */
final class PowahResourceExtensionType implements ResourcePack.Extension<PowahResourceExtension> {

    private static final Key KEY = Key.parse("bluemap_powah:prototype");

    private final BlockRendererType renderer;
    private final PowahRuntime runtime;

    PowahResourceExtensionType(BlockRendererType renderer, PowahRuntime runtime) {
        this.renderer = renderer;
        this.runtime = runtime;
    }

    @Override
    public Key getKey() {
        return KEY;
    }

    @Override
    public PowahResourceExtension create(ResourcePack pack) {
        return new PowahResourceExtension(pack, renderer, runtime);
    }
}
