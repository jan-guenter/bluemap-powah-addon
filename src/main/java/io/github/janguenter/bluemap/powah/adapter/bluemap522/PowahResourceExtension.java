/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.hires.block.BlockRendererType;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePackExtension;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.texture.Texture;
import de.bluecolored.bluemap.core.util.Key;
import io.github.janguenter.bluemap.powah.profile.ExactPowahArtifactDetector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/** Exact-artifact activation and target-only renderer wrapping. */
final class PowahResourceExtension implements ResourcePackExtension {

    private final ResourcePack resourcePack;
    private final BlockRendererType renderer;
    private final PowahRuntime runtime;

    PowahResourceExtension(
            ResourcePack resourcePack,
            BlockRendererType renderer,
            PowahRuntime runtime
    ) {
        this.resourcePack = resourcePack;
        this.renderer = renderer;
        this.runtime = runtime;
    }

    @Override
    public void loadResources(Iterable<Path> roots) {
        if (Boolean.getBoolean("bluemap.powah.disabled")) {
            runtime.inactive("operator-disabled");
        } else if (!ExactPowahArtifactDetector.matches(roots)) {
            runtime.inactive("exact-powah-artifact-not-found");
        } else {
            runtime.activate();
        }
    }

    @Override
    public Set<Key> collectUsedTextureKeys() {
        return PowahCatalog.TEXTURES;
    }

    @Override
    public void bake() {
        if (!runtime.active()) {
            return;
        }
        try {
            installEnergyChargeTexture();
            VariantRendererCatalog catalog = VariantRendererCatalog.wrap(resourcePack, renderer);
            runtime.catalog(resourcePack, catalog);
            System.out.println("BlueMap Powah add-on active: wrapped " + catalog.size()
                    + " variants across 15 stable-appearance blocks.");
        } catch (IOException | RuntimeException exception) {
            runtime.inactive("energy-charge-texture-invalid");
        }
    }

    private void installEnergyChargeTexture() throws IOException {
        if (resourcePack.getTextures().get(PowahCatalog.ENERGY_CHARGE_RENDER) != null) {
            throw new IOException("synthetic energy-charge texture key collision");
        }
        Texture source = resourcePack.getTextures().get(PowahCatalog.ENERGY_CHARGE_SOURCE);
        if (source == null) {
            throw new IOException("energy-charge source texture missing");
        }
        BufferedImage sourceImage = source.getTextureImage();
        BufferedImage converted = additiveToAlpha(sourceImage);
        resourcePack.getTextures().put(
                PowahCatalog.ENERGY_CHARGE_RENDER,
                Texture.from(PowahCatalog.ENERGY_CHARGE_RENDER, converted)
        );
    }

    /** Approximates Powah's SRC_ALPHA/ONE pass with BlueMap's regular alpha blend. */
    static BufferedImage additiveToAlpha(BufferedImage source) throws IOException {
        if (source.getWidth() != 20 || source.getHeight() != 10) {
            throw new IOException("energy-charge texture dimensions changed");
        }
        BufferedImage converted = new BufferedImage(
                source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int rgb = source.getRGB(x, y);
                int red = rgb >>> 16 & 0xFF;
                int green = rgb >>> 8 & 0xFF;
                int blue = rgb & 0xFF;
                converted.setRGB(x, y, red == 0 && green == 0 && blue == 0
                        ? 0 : 0x80EC_DAFF);
            }
        }
        return converted;
    }
}
