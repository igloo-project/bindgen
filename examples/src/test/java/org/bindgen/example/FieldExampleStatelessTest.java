package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bindgen.java.lang.StringBindingPath;

import org.junit.jupiter.api.Test;

public class FieldExampleStatelessTest {

	@Test
	public void testReadWrite() {
		FieldExampleBinding b = new FieldExampleBinding();
		StringBindingPath<FieldExample, FieldExample> name = b.name();

		FieldExample e1 = new FieldExample("one");
		FieldExample e2 = new FieldExample("two");

		assertEquals("one", name.getWithRoot(e1));
		assertEquals("two", name.getWithRoot(e2));

		name.setWithRoot(e1, "one2");
		name.setWithRoot(e2, "two2");

		assertEquals("one2", e1.name);
		assertEquals("two2", e2.name);
	}

	@Test
	public void testPrimitive() {
		FieldExampleBinding b = new FieldExampleBinding();
		FieldExample e1 = new FieldExample("name");
		assertFalse(e1.good);

		b.good().setWithRoot(e1, true);
		assertTrue(e1.good);
	}

}
