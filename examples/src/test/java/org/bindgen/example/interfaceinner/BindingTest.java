package org.bindgen.example.interfaceinner;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.bindgen.example.interfaceinner.Outer.Inner;
import org.bindgen.example.interfaceinner.outer.InnerBinding;
import org.junit.jupiter.api.Test;

public class BindingTest {
  
  @Test
	public void testInnerBinding() {
		InnerBinding b = new InnerBinding(new Inner());
		b.something().set("string1");
		assertEquals("string1", b.something().get());
	}
}
