/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap523;

import de.bluecolored.bluemap.core.world.mca.blockentity.MCABlockEntity;

/** Retains only the stable formed/unformed reactor state. */
public final class ReactorBlockEntityData extends MCABlockEntity {

    private Object built;

    public ReactorBlockEntityData() {
    }

    boolean built() {
        return built instanceof Byte byteValue ? byteValue != 0
                : built instanceof Boolean booleanValue && booleanValue;
    }
}
