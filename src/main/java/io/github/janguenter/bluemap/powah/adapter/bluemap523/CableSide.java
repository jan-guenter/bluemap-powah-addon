/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap523;

import de.bluecolored.bluemap.core.util.Direction;

/** Minecraft direction order plus the rotations used by the family blockstates. */
enum CableSide {
    DOWN(0, -1, 0, Direction.DOWN, 270F, 0F),
    UP(0, 1, 0, Direction.UP, 90F, 0F),
    NORTH(0, 0, -1, Direction.NORTH, 0F, 180F),
    SOUTH(0, 0, 1, Direction.SOUTH, 0F, 0F),
    WEST(-1, 0, 0, Direction.WEST, 0F, 90F),
    EAST(1, 0, 0, Direction.EAST, 0F, 270F);

    private final int x;
    private final int y;
    private final int z;
    private final Direction direction;
    private final float xRotation;
    private final float yRotation;

    CableSide(
            int x,
            int y,
            int z,
            Direction direction,
            float xRotation,
            float yRotation
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.xRotation = xRotation;
        this.yRotation = yRotation;
    }

    int x() {
        return x;
    }

    int y() {
        return y;
    }

    int z() {
        return z;
    }

    Direction direction() {
        return direction;
    }

    float xRotation() {
        return xRotation;
    }

    float yRotation() {
        return yRotation;
    }

    static CableSide byOrdinal(int ordinal) {
        CableSide[] values = values();
        return ordinal >= 0 && ordinal < values.length ? values[ordinal] : null;
    }

    static CableSide byName(String name) {
        if (name == null) {
            return null;
        }
        for (CableSide side : values()) {
            if (side.name().equalsIgnoreCase(name)) {
                return side;
            }
        }
        return null;
    }
}
