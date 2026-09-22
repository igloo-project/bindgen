package org.bindgen.processor;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Tests a child class inheritance a generic getter/setter. */
public class InheritanceTest extends AbstractBindgenTestCase {

	@Test
	public void testChild() throws Exception {
		ClassLoader cl = this.compile("org/bindgen/processor/inheritance/Base.java",
				"org/bindgen/processor/inheritance/Child.java");

		Class<?> cClass = cl.loadClass("org.bindgen.processor.inheritance.Child");
		Class<?> cbClass = cl.loadClass("org.bindgen.processor.inheritance.ChildBinding");
		assertChildBindings(cbClass, "hashCodeBinding", "list", "toStringBinding", "value", "valueField");

		Object child = cClass.getDeclaredConstructor().newInstance();
		Object childBinding = cbClass.getConstructor(cClass).newInstance(child);
		Object valueBinding = cbClass.getMethod("value").invoke(childBinding);
		Object valueFieldBinding = cbClass.getMethod("valueField").invoke(childBinding);

		// set via the binding
		valueBinding.getClass().getMethod("set", Object.class).invoke(valueBinding, "FOO");
		// get via the class
		assertEquals("FOO", cClass.getMethod("value").invoke(child));

		// set via the class
		cClass.getMethod("value", Object.class).invoke(child, "BAR");
		// get via the binding
		assertEquals("BAR", valueBinding.getClass().getMethod("get").invoke(valueBinding));

		// set via the field binding
		valueFieldBinding.getClass().getMethod("set", Object.class).invoke(valueFieldBinding, "ZAZ");
		// get via the field binding
		assertEquals("ZAZ", valueFieldBinding.getClass().getMethod("get").invoke(valueFieldBinding));
	}

}
