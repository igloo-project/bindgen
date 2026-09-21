package org.bindgen.example.methods;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.bindgen.Binding;
import org.bindgen.ContainerBinding;
import org.junit.jupiter.api.Test;

public class MethodExampleTest {

	@Test
	public void testReadWrite() {
		MethodExample e = new MethodExample("1", "fred");
		MethodExampleBinding b = new MethodExampleBinding(e);

		assertEquals("fred", b.name().get());

		b.name().set("bob");
		assertEquals("bob", e.getName());
	}

	@Test
	public void testReadOnly() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);

		assertEquals("1", b.id().get());

		try {
			b.id().set("name1");
			fail();
		} catch (RuntimeException re) {
			assertEquals("id is read only", re.getMessage());
		}

		assertEquals(true, b.id().getBindingIsReadOnly());
	}

	@Test
	public void testBoolean() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertEquals(false, b.good().get().booleanValue());

		b.good().set(true);
		assertEquals(true, e.isGood());
	}

	@Test
	public void testToString() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertEquals("method", b.toStringBinding().get());
	}

	@Test
	public void testHasMethod() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertEquals(false, b.stuff().get().booleanValue());
	}

	@Test
	public void testBooleanThatIsAKeywordFallsBackOnMethodName() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertEquals(false, b.isNew().get().booleanValue());
	}

	@Test
	public void testGetBindings() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertEquals(21, b.getChildBindings().size());

		boolean foundName = false;
		for (Binding<?> sub : b.getChildBindings()) {
			if (sub.getName().equals("name")) {
				foundName = true;
			}
		}
		assertTrue(foundName);
	}

	@Test
	public void testList() {
		MethodExampleBinding b = new MethodExampleBinding();
		assertEquals(String.class, ((ContainerBinding) b.list()).getContainedType());
	}

	@Test
	public void testNull() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertEquals(false, b.isNull().get().booleanValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWildcards() {
		MethodExample e = new MethodExample("1", "name");
		@SuppressWarnings("rawtypes")
		Wildcards rawWildcards = new Wildcards();
		e.setWildcards(rawWildcards);
		MethodExampleBinding b = new MethodExampleBinding(e);
		b.wildcards().a().set("a string");
		// needs casting b.wildcards().b().set("anything");
	}

	@Test
	public void testProtected() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		// was causing read only exception
		b.protectedProperty().set(1);
		assertEquals(2, e.protectedProperty);
	}

	@Test
	public void testOneChar() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertNull(b.m().get());
		b.m().set("onechar");
		assertEquals("onechar", e.getM());
	}

	@Test
	public void testNoArg() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		assertEquals(new Integer(1), b.noArg().get());
	}

	@Test
	public void testArray() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);

		String[] arrayProp = { "foo", "bar" };
		String[] arrayProp2 = { "foobar" };

		e.setArrayProp(arrayProp);
		assertArrayEquals(arrayProp, b.arrayProp().get());
		b.arrayProp().set(arrayProp2);
		assertArrayEquals(arrayProp2, b.arrayProp().get());
	}
}
