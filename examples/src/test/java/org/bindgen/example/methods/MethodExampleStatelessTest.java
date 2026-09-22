package org.bindgen.example.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.bindgen.java.lang.StringBindingPath;

import org.junit.jupiter.api.Test;

public class MethodExampleStatelessTest {

	@Test
	public void testReadWrite() {
		MethodExampleBinding b = new MethodExampleBinding();
		StringBindingPath<MethodExample, MethodExample> name = b.name();

		MethodExample e1 = new MethodExample("1", "fred");
		MethodExample e2 = new MethodExample("2", "bob");

		assertEquals("fred", name.getWithRoot(e1));
		assertEquals("bob", name.getWithRoot(e2));

		name.setWithRoot(e1, "fred2");
		name.setWithRoot(e2, "bob2");
		assertEquals("fred2", e1.getName());
		assertEquals("bob2", e2.getName());

	}

	@Test
	public void testReadOnly() {
		MethodExampleBinding b = new MethodExampleBinding();
		MethodExample e1 = new MethodExample("1", "fred");

		try {
			b.id().setWithRoot(e1, "name1");
			fail();
		} catch (RuntimeException re) {
			assertEquals("id is read only", re.getMessage());
		}
	}

}
