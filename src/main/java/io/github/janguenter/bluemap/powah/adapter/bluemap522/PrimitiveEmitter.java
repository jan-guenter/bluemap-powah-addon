/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap522;

import de.bluecolored.bluemap.core.map.TextureGallery;
import de.bluecolored.bluemap.core.map.hires.TileModel;
import de.bluecolored.bluemap.core.map.hires.TileModelView;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.ResourcePack;
import de.bluecolored.bluemap.core.resources.pack.resourcepack.blockstate.Variant;
import de.bluecolored.bluemap.core.util.Key;
import de.bluecolored.bluemap.core.world.block.BlockNeighborhood;

/** Small cuboid and face emitter for cable and facade geometry. */
final class PrimitiveEmitter {

    private static final Variant IDENTITY = new Variant(ResourcePack.MISSING_BLOCK_MODEL);

    private final TextureGallery textures;

    PrimitiveEmitter(TextureGallery textures) {
        this.textures = textures;
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    void entityBox(
            BlockNeighborhood block,
            TileModelView target,
            Key texture,
            float x0,
            float y0,
            float z0,
            float x1,
            float y1,
            float z1,
            float textureU,
            float textureV,
            float modelWidth,
            float modelHeight,
            float textureWidth,
            float textureHeight
    ) {
        float width = (x1 - x0) * modelWidth;
        float height = (y1 - y0) * modelWidth;
        float depth = (z1 - z0) * modelWidth;
        float u0 = textureU / textureWidth;
        float u1 = (textureU + depth) / textureWidth;
        float u2 = (textureU + depth + width) / textureWidth;
        float topEnd = (textureU + depth + width + width) / textureWidth;
        float eastEnd = (textureU + depth + width + depth) / textureWidth;
        float southEnd = (textureU + depth + width + depth + width) / textureWidth;
        float v0 = textureV / textureHeight;
        float v1 = (textureV + depth) / textureHeight;
        float v2 = (textureV + depth + height) / textureHeight;
        quad(block, target, texture, CableSide.DOWN,
                x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1,
                u1, v0, u2, v1);
        quad(block, target, texture, CableSide.UP,
                x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0,
                u2, v0, topEnd, v1);
        quad(block, target, texture, CableSide.NORTH,
                x1, y0, z0, x0, y0, z0, x0, y1, z0, x1, y1, z0,
                u1, v1, u2, v2);
        quad(block, target, texture, CableSide.SOUTH,
                x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1,
                eastEnd, v1, southEnd, v2);
        quad(block, target, texture, CableSide.WEST,
                x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0,
                u0, v1, u1, v2);
        quad(block, target, texture, CableSide.EAST,
                x1, y0, z1, x1, y0, z0, x1, y1, z0, x1, y1, z1,
                u2, v1, eastEnd, v2);
    }

    void box(
            BlockNeighborhood block,
            TileModelView target,
            Key texture,
            float x0,
            float y0,
            float z0,
            float x1,
            float y1,
            float z1
    ) {
        quad(block, target, texture, CableSide.DOWN,
                x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1,
                x0, z0, x1, z1);
        quad(block, target, texture, CableSide.UP,
                x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0,
                x0, z0, x1, z1);
        quad(block, target, texture, CableSide.NORTH,
                x1, y0, z0, x0, y0, z0, x0, y1, z0, x1, y1, z0,
                x0, y0, x1, y1);
        quad(block, target, texture, CableSide.SOUTH,
                x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1,
                x0, y0, x1, y1);
        quad(block, target, texture, CableSide.WEST,
                x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0,
                z0, y0, z1, y1);
        quad(block, target, texture, CableSide.EAST,
                x1, y0, z1, x1, y0, z0, x1, y1, z0, x1, y1, z1,
                z0, y0, z1, y1);
    }

    void facadeRectangle(
            BlockNeighborhood block,
            TileModelView target,
            Key texture,
            CableSide side,
            float u0,
            float v0,
            float u1,
            float v1,
            int argb
    ) {
        switch (side) {
            case DOWN -> quad(block, target, texture, side,
                    u0, 0F, v0, u1, 0F, v0, u1, 0F, v1, u0, 0F, v1,
                    u0, v0, u1, v1, argb);
            case UP -> quad(block, target, texture, side,
                    u0, 1F, v1, u1, 1F, v1, u1, 1F, v0, u0, 1F, v0,
                    u0, v0, u1, v1, argb);
            case NORTH -> quad(block, target, texture, side,
                    u1, v0, 0F, u0, v0, 0F, u0, v1, 0F, u1, v1, 0F,
                    u0, v0, u1, v1, argb);
            case SOUTH -> quad(block, target, texture, side,
                    u0, v0, 1F, u1, v0, 1F, u1, v1, 1F, u0, v1, 1F,
                    u0, v0, u1, v1, argb);
            case WEST -> quad(block, target, texture, side,
                    0F, v0, u0, 0F, v0, u1, 0F, v1, u1, 0F, v1, u0,
                    u0, v0, u1, v1, argb);
            case EAST -> quad(block, target, texture, side,
                    1F, v0, u1, 1F, v0, u0, 1F, v1, u0, 1F, v1, u1,
                    u0, v0, u1, v1, argb);
        }
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    private void quad(
            BlockNeighborhood block,
            TileModelView target,
            Key texture,
            CableSide side,
            float ax,
            float ay,
            float az,
            float bx,
            float by,
            float bz,
            float cx,
            float cy,
            float cz,
            float dx,
            float dy,
            float dz,
            float u0,
            float v0,
            float u1,
            float v1
    ) {
        quad(block, target, texture, side,
                ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz,
                u0, v0, u1, v1, 0xFFFF_FFFF);
    }

    @SuppressWarnings("checkstyle:ParameterNumber")
    private void quad(
            BlockNeighborhood block,
            TileModelView target,
            Key texture,
            CableSide side,
            float ax,
            float ay,
            float az,
            float bx,
            float by,
            float bz,
            float cx,
            float cy,
            float cz,
            float dx,
            float dy,
            float dz,
            float u0,
            float v0,
            float u1,
            float v1,
            int argb
    ) {
        int start = target.add(2);
        TileModel model = target.getTileModel();
        model.setPositions(start, ax, ay, az, bx, by, bz, cx, cy, cz);
        model.setPositions(start + 1, ax, ay, az, cx, cy, cz, dx, dy, dz);
        model.setUvs(start, u0, v1, u1, v1, u1, v0);
        model.setUvs(start + 1, u0, v1, u1, v0, u0, v0);
        int material = textures.get(texture);
        model.setMaterialIndex(start, material);
        model.setMaterialIndex(start + 1, material);
        float red = ((argb >>> 16) & 0xFF) / 255F;
        float green = ((argb >>> 8) & 0xFF) / 255F;
        float blue = (argb & 0xFF) / 255F;
        model.setColor(start, red, green, blue);
        model.setColor(start + 1, red, green, blue);
        model.setAOs(start, 1F, 1F, 1F);
        model.setAOs(start + 1, 1F, 1F, 1F);
        FaceLighting.Sample light = FaceLighting.sample(block, side.direction(), IDENTITY, 0);
        model.setSunlight(start, light.sunlight());
        model.setSunlight(start + 1, light.sunlight());
        model.setBlocklight(start, light.blocklight());
        model.setBlocklight(start + 1, light.blocklight());
    }
}
