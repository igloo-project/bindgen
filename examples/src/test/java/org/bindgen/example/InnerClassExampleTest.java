package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class InnerClassExampleTest {

	@Test
	public void testInner1() {
		InnerClassExample1 foo = new InnerClassExample1();
		InnerClassExample1.InnerClass fooInner = foo.newInnerClass();
		fooInner.getBind().name().set("set");
		assertEquals("set", fooInner.name);
	}

	@Test
	public void testInner2() {
		InnerClassExample2 foo = new InnerClassExample2();
		InnerClassExample2.InnerClass fooInner = foo.newInnerClass();
		fooInner.getBind().different().set("set");
		assertEquals("set", fooInner.different);
	}

}
