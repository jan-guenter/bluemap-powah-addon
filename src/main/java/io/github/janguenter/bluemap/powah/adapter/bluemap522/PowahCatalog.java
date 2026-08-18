/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap522;

import de.bluecolored.bluemap.core.util.Key;

import java.util.LinkedHashSet;
import java.util.Set;

/** Deliberately small stable-appearance allowlist. */
final class PowahCatalog {

    static final Key ENERGY_CHARGE_SOURCE = Key.parse("powah:model/tile/energy_charge");
    static final Key ENERGY_CHARGE_RENDER = Key.parse("bluemap_powah:energy_charge_blended");
    static final String[] TIERS = {
        "starter", "basic", "hardened", "blazing", "niotic", "spirited", "nitro"
    };
    static final Set<String> BLOCKS = blocks();
    static final Set<Key> TEXTURES = textures();

    private PowahCatalog() {
    }

    static boolean reactor(String id) {
        return id.startsWith("powah:reactor_") && BLOCKS.contains(id);
    }

    static boolean cable(String id) {
        return id.startsWith("powah:energy_cable_") && BLOCKS.contains(id);
    }

    static String tier(String id) {
        return id.substring(id.lastIndexOf('_') + 1);
    }

    private static Set<String> blocks() {
        LinkedHashSet<String> ids = new LinkedHashSet<>();
        for (String tier : TIERS) {
            ids.add("powah:reactor_" + tier);
            ids.add("powah:energy_cable_" + tier);
        }
        ids.add("powah:energizing_orb");
        return Set.copyOf(ids);
    }

    private static Set<Key> textures() {
        LinkedHashSet<Key> keys = new LinkedHashSet<>();
        keys.add(Key.parse("powah:model/tile/reactor"));
        keys.add(ENERGY_CHARGE_SOURCE);
        for (String tier : TIERS) {
            keys.add(Key.parse("powah:model/tile/reactor_block_" + tier));
            if (!"starter".equals(tier)) {
                keys.add(Key.parse("powah:model/tile/reactor_" + tier));
            }
            keys.add(Key.parse("powah:model/tile/energy_cable_" + tier + "_all"));
            keys.add(Key.parse("powah:model/tile/energy_cable_" + tier + "_in"));
            keys.add(Key.parse("powah:model/tile/energy_cable_" + tier + "_out"));
        }
        return Set.copyOf(keys);
    }
}
