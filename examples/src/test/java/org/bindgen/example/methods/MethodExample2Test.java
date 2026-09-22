package org.bindgen.example.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MethodExample2Test {

	@Test
	public void testThreeNames() {
		MethodExample2Binding b = new MethodExample2Binding(new MethodExample2());
		assertEquals("1", b.name().get());
		assertEquals("", b.getName());
		assertEquals("2", b.getNameBinding().get());
	}

}
