## 4.0.0 (2018-01-xx)

Features:

* JDK >= 8 support
* Use lambda to implement bindings: allow to drop a lot of inner My*Binding classes; improve compilation time and IDE
  performance on project with > 100 binding classes
* code cleanup
* drop outdated documentation
* drop ivy, ant support
* drop automatic formatters

## 3.0.0 (2016-01-16)

Features:

  - do not build binding that are already present
  - use toolchain to support jdk7 and jdk8 builds (jdk7 builds are flagged
    with a .jdk7 qualifier)
