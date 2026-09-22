package org.bindgen.example.access;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BindingClashesTest {

	@Test
	public void testType() {
		BindingClashes c = new BindingClashes();
		BindingClashesBinding b = new BindingClashesBinding(c);

		assertEquals(BindingClashes.class, b.getType());
		assertEquals("1", b.type().get());
		assertEquals(String.class, b.type().getType());
		assertEquals("2", b.getTypeBinding().get());
		assertEquals(String.class, b.property().getType());
	}

	@Test
	public void testPath() {
		BindingClashes c = new BindingClashes();
		BindingClashesBinding b = new BindingClashesBinding(c);

		assertEquals("#root", b.getPath());
		assertEquals("a", b.path().get());
		assertEquals("b", b.getPathBinding().get());
	}

}
