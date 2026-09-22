package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GenericsExampleTest {

	@Test
	public void testField() {
		GenericsExample<String> ge = new GenericsExample<String>();
		GenericsExampleBinding<String> geb = new GenericsExampleBinding<String>(ge);

		ge.bar = "1";
		assertEquals("1", geb.bar().get());

		geb.bar().set("2");
		assertEquals("2", ge.bar);
	}

	@Test
	public void testMethod() {
		GenericsExample<String> ge = new GenericsExample<String>();
		GenericsExampleBinding<String> geb = new GenericsExampleBinding<String>(ge);

		ge.setFoo("1");
		assertEquals("1", geb.foo().get());

		geb.foo().set("2");
		assertEquals("2", ge.getFoo());
	}

}
