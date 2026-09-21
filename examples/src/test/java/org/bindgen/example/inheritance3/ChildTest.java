package org.bindgen.example.inheritance3;

import org.junit.jupiter.api.Test;

public class ChildTest {

	@Test
	public void testChild() {
		Child c = new Child();
		ChildBinding b = new ChildBinding(c);
		b.value().set("foo");
	}

}
