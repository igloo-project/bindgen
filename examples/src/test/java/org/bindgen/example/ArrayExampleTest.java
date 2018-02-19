package org.bindgen.example;

import org.junit.Assert;
import junit.framework.TestCase;

public class ArrayExampleTest extends TestCase {

	public void testPrimitive() {
		ArrayExample a = new ArrayExample();
		ArrayExampleBinding b = new ArrayExampleBinding(a);

		b.foo().set(new boolean[] { true });
		Assert.assertTrue(a.foo[0]);
	}

	public void testBoxed() {
		ArrayExample a = new ArrayExample();
		ArrayExampleBinding b = new ArrayExampleBinding(a);

		b.fooBig().set(new Boolean[] { true });
		Assert.assertTrue(a.fooBig[0]);
	}

	public void testToString() {
		ArrayExampleBinding b = new ArrayExampleBinding();
		Assert.assertEquals("ArrayExampleBinding(null).bar()", b.bar().toString());
		Assert.assertEquals("bar", b.bar().getPath());
	}
}
