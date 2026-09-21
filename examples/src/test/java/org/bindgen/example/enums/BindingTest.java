package org.bindgen.example.enums;

import org.junit.jupiter.api.Test;

public class BindingTest {
	@Test
	public void testEnum() {
		new FunEnumBinding(FunEnum.FIRST).funLevel().get();
	}
}
