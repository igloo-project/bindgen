package org.bindgen.processor;

import org.junit.Test;

public class GenericsMultipleBoundsTest extends AbstractBindgenTestCase {

	/**
	 * Test types with a multiple bounded (T extends Interface1 & Interface2)
	 * generic definition.
	 */
	@Test
	public void testMultipleBounds() throws Exception {
		ClassLoader cl = this.compile(
			"org/bindgen/processor/generic/MultipleBounds.java",
			"org/bindgen/processor/generic/Interface1.java",
			"org/bindgen/processor/generic/Interface2.java");

		Class<?> multipleBoundsBindingClass = cl.loadClass("org.bindgen.processor.generic.MultipleBoundsBinding");
		assertChildBindings(multipleBoundsBindingClass, "hashCodeBinding", "toStringBinding");
	}

}
