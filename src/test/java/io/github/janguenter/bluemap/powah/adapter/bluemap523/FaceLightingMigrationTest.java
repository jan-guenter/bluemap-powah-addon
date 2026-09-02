/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.adapter.bluemap523;

import io.github.janguenter.bluemap.addon.render.core.adapter.bluemap523.FaceLighting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FaceLightingMigrationTest {

    @Test
    void usesTheSharedSamplerWithoutKeepingTheLocalCopy() {
        assertEquals(
                "io.github.janguenter.bluemap.addon.render.core.adapter.bluemap523",
                FaceLighting.class.getPackageName()
        );
        assertThrows(ClassNotFoundException.class, () -> Class.forName(
                "io.github.janguenter.bluemap.powah.adapter.bluemap523.FaceLighting"
        ));
    }
}
