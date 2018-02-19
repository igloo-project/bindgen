package org.bindgen.example;

import org.junit.Assert;
import junit.framework.TestCase;

import org.bindgen.example.EnumExample.Foo;

public class EnumExampleTest extends TestCase {

	public void testEnum() {
		EnumExample e = new EnumExample();
		EnumExampleBinding b = new EnumExampleBinding(e);
		b.foo().set(Foo.ONE);
		Assert.assertEquals(Foo.ONE, e.foo);
	}

}
