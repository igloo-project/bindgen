package org.bindgen.example.subpackage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PackageExampleTest {

	@Test
	public void testReadWrite() {
		PackageExample e = new PackageExample("name");
		PackageExampleBinding b = new PackageExampleBinding(e);

		assertEquals("name", b.name().get());

		b.name().set("name1");
		assertEquals("name1", e.name);
	}

}
