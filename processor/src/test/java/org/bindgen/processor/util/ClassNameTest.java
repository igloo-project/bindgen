package org.bindgen.processor.util;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ClassNameTest {

	@Test
	public void stripRepeatedTypeVarsOfOuterClasses() {
		assertEquals(
        "java.util.Map.Entry<K, V>",
        new ClassName("java.util.Map<K, V>.Entry<K, V>").get());
    assertEquals(
        "java.util.Foo.Entry<K extends java.util.Bar<K>>",
        new ClassName("java.util.Foo<K extends java.util.Bar<K>>.Entry<K extends java.util.Bar<K>>").get());
	}

}
