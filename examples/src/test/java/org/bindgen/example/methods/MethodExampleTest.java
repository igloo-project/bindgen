package org.bindgen.example.methods;

import org.bindgen.Binding;
import org.bindgen.ContainerBinding;
import org.junit.Assert;

import junit.framework.TestCase;

public class MethodExampleTest extends TestCase {

	public void testReadWrite() {
		MethodExample e = new MethodExample("1", "fred");
		MethodExampleBinding b = new MethodExampleBinding(e);

		Assert.assertEquals("fred", b.name().get());

		b.name().set("bob");
		Assert.assertEquals("bob", e.getName());
	}

	public void testReadOnly() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);

		Assert.assertEquals("1", b.id().get());

		try {
			b.id().set("name1");
			Assert.fail();
		} catch (RuntimeException re) {
			Assert.assertEquals("id is read only", re.getMessage());
		}

		Assert.assertEquals(true, b.id().getBindingIsReadOnly());
	}

	public void testBoolean() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals(false, b.good().get().booleanValue());

		b.good().set(true);
		Assert.assertEquals(true, e.isGood());
	}

	public void testToString() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals("method", b.toStringBinding().get());
	}

	public void testHasMethod() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals(false, b.stuff().get().booleanValue());
	}

	public void testBooleanThatIsAKeywordFallsBackOnMethodName() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals(false, b.isNew().get().booleanValue());
	}

	public void testGetBindings() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals(21, b.getChildBindings().size());

		boolean foundName = false;
		for (Binding<?> sub : b.getChildBindings()) {
			if (sub.getName().equals("name")) {
				foundName = true;
			}
		}
		Assert.assertTrue(foundName);
	}

	public void testList() {
		MethodExampleBinding b = new MethodExampleBinding();
		Assert.assertEquals(String.class, ((ContainerBinding) b.list()).getContainedType());
	}

	public void testNull() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals(false, b.isNull().get().booleanValue());
	}

	@SuppressWarnings("unchecked")
	public void testWildcards() {
		MethodExample e = new MethodExample("1", "name");
		@SuppressWarnings("rawtypes")
		Wildcards rawWildcards = new Wildcards();
		e.setWildcards(rawWildcards);
		MethodExampleBinding b = new MethodExampleBinding(e);
		b.wildcards().a().set("a string");
		// needs casting b.wildcards().b().set("anything");
	}

	public void testProtected() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		// was causing read only exception
		b.protectedProperty().set(1);
		Assert.assertEquals(2, e.protectedProperty);
	}

	public void testOneChar() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals(null, b.m().get());
		b.m().set("onechar");
		Assert.assertEquals("onechar", e.getM());
	}

	public void testNoArg() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);
		Assert.assertEquals(new Integer(1), b.noArg().get());
	}

	public void testArray() {
		MethodExample e = new MethodExample("1", "name");
		MethodExampleBinding b = new MethodExampleBinding(e);

		String[] arrayProp = { "foo", "bar" };
		String[] arrayProp2 = { "foobar" };

		e.setArrayProp(arrayProp);
		Assert.assertArrayEquals(arrayProp, b.arrayProp().get());
		b.arrayProp().set(arrayProp2);
		Assert.assertArrayEquals(arrayProp2, b.arrayProp().get());
	}
}
