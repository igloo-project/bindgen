package org.bindgen.example.inheritance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class InterfaceInheritanceTest {

	@Test
	public void testA() {
		InterfaceBBinding b = new InterfaceBBinding();
		b.set(new InterfaceBImpl());
		assertEquals("a", b.fromA().get());
		assertEquals("aa", b.fromAA().get());
		assertEquals("b", b.fromB().get());
	}

}
