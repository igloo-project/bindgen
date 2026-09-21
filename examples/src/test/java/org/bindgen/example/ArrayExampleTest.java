package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ArrayExampleTest {

	@Test
	public void testPrimitive() {
		ArrayExample a = new ArrayExample();
		ArrayExampleBinding b = new ArrayExampleBinding(a);

		b.foo().set(new boolean[] { true });
		assertTrue(a.foo[0]);
	}

	@Test
	public void testBoxed() {
		ArrayExample a = new ArrayExample();
		ArrayExampleBinding b = new ArrayExampleBinding(a);

		b.fooBig().set(new Boolean[] { true });
		assertTrue(a.fooBig[0]);
	}

	@Test
	public void testToString() {
		ArrayExampleBinding b = new ArrayExampleBinding();
		assertEquals("ArrayExampleBinding(null).bar()", b.bar().toString());
		assertEquals("bar", b.bar().getPath());
	}
}
