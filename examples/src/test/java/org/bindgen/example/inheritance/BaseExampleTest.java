package org.bindgen.example.inheritance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class BaseExampleTest {

	@Test
	public void testSubBindings() {
		SubExample sub = new SubExample();
		SubExampleBinding subb = new SubExampleBinding(sub);
		subb.name().set("foo");
		subb.subOnly().set("bar");

		assertEquals("foo", sub.name);
		assertEquals("bar", sub.subOnly);

		// because of the clash with the base class 'name', we get an extra 'nameField' that still points to 'SubExample.name'
		subb.nameField().set("foo");
		assertEquals("foo", sub.name);

		// 5 == base description, sub name, sub subOnly, hashCode and toString
		// +1 currently for the parent name and child nameField
		assertEquals(6, subb.getChildBindings().size());
	}

	@Test
	public void testSubBindingsWithRealSub() {
		SubExampleBinding subb = new SubExampleBinding();
		subb.set(new SubExample());
		assertNull(subb.subOnly().get());
	}

	@Test
	public void testSubBindingsWithBase() {
		// SubExampleBinding subb = new SubExampleBinding();
		try {
			// subb.set(new BaseExample());
			// Assert.fail();
		} catch (ClassCastException cce) {
			// Okay
		}
	}

}
