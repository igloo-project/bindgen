package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.bindgen.example.EnumExample.Foo;

public class EnumExampleTest {

	@Test
	public void testEnum() {
		EnumExample e = new EnumExample();
		EnumExampleBinding b = new EnumExampleBinding(e);
		b.foo().set(Foo.ONE);
		assertEquals(Foo.ONE, e.foo);
	}

}
