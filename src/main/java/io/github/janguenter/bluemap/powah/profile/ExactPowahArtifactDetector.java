/*
 * SPDX-License-Identifier: LGPL-3.0-only
 */
package io.github.janguenter.bluemap.powah.profile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Exact-byte activation gate for the Powah artifact installed by All the Mons 1.2.0. */
public final class ExactPowahArtifactDetector {

    private static final long SIZE = 2_737_991L;
    private static final String SHA256 =
            "0e604a7356111c1dd44a00ea42fc1aa960d9faeb978261349df1138fcee4d0b4";

    private ExactPowahArtifactDetector() {
    }

    public static boolean matches(Iterable<Path> roots) {
        int inspected = 0;
        for (Path root : roots) {
            if (++inspected > 4_096 || Thread.currentThread().isInterrupted()) {
                return false;
            }
            try {
                if (root != null && Files.isRegularFile(root) && Files.size(root) == SIZE
                        && SHA256.equals(digest(root))) {
                    return true;
                }
            } catch (IOException exception) {
                return false;
            }
        }
        return false;
    }

    private static String digest(Path path) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[64 * 1_024];
            try (InputStream input = Files.newInputStream(path)) {
                int read;
                while ((read = input.read(buffer)) >= 0) {
                    digest.update(buffer, 0, read);
                }
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 unavailable", exception);
        }
    }
}
