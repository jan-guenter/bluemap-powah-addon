# Notices

This add-on adapts stable state semantics and primitive geometry from Powah
6.2.10 under LGPL-3.0-only. It bundles no Powah models, textures, classes, or
binaries; those resources are resolved from the operator-installed exact
Powah JAR.

The BlueMap integration uses the public MIT BlueMap interfaces. The add-on
compiles the MIT-licensed `FaceLighting` source from BlueMap Add-on Render
Core `0.1.0-alpha.2`, commit
`24b84efdc8235f3f1323e1a8e9fd033080e3a79e`. The shared source originates in
the MIT BlueMap Sophisticated Add-on history; this migration does not
relicense Powah's former LGPL-marked local file. The standalone render-core
JAR is not bundled or installed.

The add-on also compiles four MIT-licensed sources from BlueMap Add-on Adapter API
`0.1.0-alpha.2`, commit `e81f08bc4bfbf02d810ec8949a019130e2e61634`.
Its standalone JAR is not bundled or installed.
