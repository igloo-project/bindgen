## 6.0.0 (2023-08)

* Java 17 (source and target) release

## 5.0.0 (2021-07)

* Java 11 (source and target) release

## 4.0.2 (2018-11)

### Bugfix

* Load bindgen.properties in a path containing spaces (thanks to nandor.fekete@gmail.com)
* Fix symbol conflict for generic types: symbol availability is now extracted (thanks to nandor.fekete@gmail.com)
* Fix broken getType() method for some corner cases

## 4.0.1 (2018-11-17)

### Bugfix

* #8: fix to support type annotations (thanks to @nfekete)

## 4.0.0 (2018-02-19)

Features:

* JDK >= 8 support
* Use lambda to implement bindings: allow to drop a lot of inner My*Binding classes; improve compilation time and IDE
  performance on project with > 100 binding classes
* there still are old style bindings (inline My*BindingPath) for direct property access or complex generic types
* code cleanup
* drop outdated documentation
* drop ivy, ant support
* drop automatic formatters

## 3.0.0 (2016-01-16)

Features:

  - do not build binding that are already present
  - use toolchain to support jdk7 and jdk8 builds (jdk7 builds are flagged
    with a .jdk7 qualifier)
