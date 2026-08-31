/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap523;

import de.bluecolored.bluemap.core.world.mca.blockentity.MCABlockEntity;
import de.bluecolored.bluenbt.NBTName;

/** Retains the persisted external connections and per-side transfer modes. */
public final class EnergyCableBlockEntityData extends MCABlockEntity {

    private Object cs;

    @NBTName("side_transfer_type")
    private int[] sideTransferType;

    public EnergyCableBlockEntityData() {
    }

    int connections() {
        return cs instanceof Byte value ? Byte.toUnsignedInt(value)
                : cs instanceof Integer value ? value : 0;
    }

    int transfer(int side) {
        return sideTransferType != null && side >= 0 && side < sideTransferType.length
                ? sideTransferType[side] : 0;
    }
}
