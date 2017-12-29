.. bindgen documentation master file, created by
   sphinx-quickstart on Wed May 17 16:49:27 2017.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

========================
OWSI-Core Documentation
========================

Overview
--------

Bindgen is a type-safe alternative to expression languages like UL and OGNL.

It provides:

* A succinct way to pass your domain object properties as get-able/set-able `Binding`_ objects.
* Compile-time checking of binding expressions that will break if your domain model changes.

See `examples`_ for the binding syntax.

--------

Bindgen uses code generation, but is implemented as a JDK6 annotation processor to provide (in Eclipse) a seamless editing/generation experience. The generated code is kept up to date as soon as "save" is hit.

When save is hit, Bindgen inspects the class that just changed and generates a mirror `XxxBinding` class that has type-safe methods that return `Binding` instances that wrap around each of your class's public properties (fields or methods).

Again, see the `examples`_ for more details.

Sections
--------
.. toctree::
   :maxdepth: 1
   :caption: Release notes

   setup.md
   config.md
   changelog.md
   screencasts.md
   performance.md
   building.md
   community.md
   examples.md
   examplesFooBinding.md


.. _examples: examples.html
.. _binding: http://github.com/stephenh/bindgen/blob/master/processor/src/main/java/org/bindgen/Binding.java
