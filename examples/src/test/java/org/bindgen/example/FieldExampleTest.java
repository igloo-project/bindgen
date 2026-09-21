package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.bindgen.ContainerBinding;

public class FieldExampleTest {

	@Test
	public void testReadWrite() {
		FieldExample e = new FieldExample("name");
		FieldExampleBinding b = new FieldExampleBinding(e);

		assertEquals("name", b.name().get());

		b.name().set("name1");
		assertEquals("name1", e.name);
	}

	@Test
	public void testFinal() {
		FieldExampleBinding b = new FieldExampleBinding();
		assertEquals(true, b.finalField().getBindingIsReadOnly());
	}

	@Test
	public void testListReadWrite() {
		FieldExample e = new FieldExample("name");
		FieldExampleBinding b = new FieldExampleBinding(e);

		List<String> list = b.list().get();
		list.add("foo");

		assertEquals("foo", e.list.get(0));
		assertSame(list, b.list().get());
		assertEquals(String.class, ((ContainerBinding) b.list()).getContainedType());
	}

	@Test
	public void testPrimitive() {
		FieldExample e = new FieldExample("name");
		FieldExampleBinding b = new FieldExampleBinding(e);
		assertFalse(b.good().get());

		b.good().set(true);
		assertTrue(e.good);
	}

	@Test
	public void testOneCharge() {
		FieldExample e = new FieldExample("name");
		FieldExampleBinding b = new FieldExampleBinding(e);
		assertNull(b.f().get());
		b.f().set("foo");
		assertEquals("foo", e.f);
	}

	@Test
	public void testGet() {
		FieldExample e = new FieldExample("name");
		e.get = true;
		FieldExampleBinding b = new FieldExampleBinding(e);
		assertEquals(true, b.getField().get().booleanValue());
	}

}
