package org.bindgen.example.methods;

import org.bindgen.java.lang.StringBindingPath;

import org.junit.Assert;
import junit.framework.TestCase;

public class MethodExampleStatelessTest extends TestCase {

	public void testReadWrite() {
		MethodExampleBinding b = new MethodExampleBinding();
		StringBindingPath<MethodExample, MethodExample> name = b.name();

		MethodExample e1 = new MethodExample("1", "fred");
		MethodExample e2 = new MethodExample("2", "bob");

		Assert.assertEquals("fred", name.getWithRoot(e1));
		Assert.assertEquals("bob", name.getWithRoot(e2));

		name.setWithRoot(e1, "fred2");
		name.setWithRoot(e2, "bob2");
		Assert.assertEquals("fred2", e1.getName());
		Assert.assertEquals("bob2", e2.getName());

	}

	public void testReadOnly() {
		MethodExampleBinding b = new MethodExampleBinding();
		MethodExample e1 = new MethodExample("1", "fred");

		try {
			b.id().setWithRoot(e1, "name1");
			Assert.fail();
		} catch (RuntimeException re) {
			Assert.assertEquals("id is read only", re.getMessage());
		}
	}

}
