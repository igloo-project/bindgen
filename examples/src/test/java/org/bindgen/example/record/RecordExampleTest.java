package org.bindgen.example.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class RecordExampleTest {

	@Test
	public void testSimpleRecordComponentBindings() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		assertEquals("Paris", b.city().get());
		assertEquals("75001", b.zipCode().get());
	}

	@Test
	public void testRecordComponentBindingsAreReadOnly() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		assertTrue(b.city().getBindingIsReadOnly(), "Record component bindings should be read-only");
		assertTrue(b.zipCode().getBindingIsReadOnly(), "Record component bindings should be read-only");
	}

	@Test
	public void testRecordBindingName() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		assertEquals("city", b.city().getName());
		assertEquals("zipCode", b.zipCode().getName());
	}

	@Test
	public void testRecordWithPrimitiveComponent() {
		PersonRecord person = new PersonRecord("Alice", 30, null);
		PersonRecordBinding b = new PersonRecordBinding(person);

		assertEquals("Alice", b.name().get());
		assertEquals(Integer.valueOf(30), b.age().get());
	}

	@Test
	public void testNestedRecordBindings() {
		AddressRecord address = new AddressRecord("Lyon", "69001");
		PersonRecord person = new PersonRecord("Bob", 25, address);
		PersonRecordBinding b = new PersonRecordBinding(person);

		assertEquals("Lyon", b.address().city().get());
		assertEquals("69001", b.address().zipCode().get());
	}

	@Test
	public void testStatelessRecordBinding() {
		AddressRecord address = new AddressRecord("Marseille", "13001");
		PersonRecord person = new PersonRecord("Charlie", 40, address);

		PersonRecordBinding b = new PersonRecordBinding(); // no instance set
		assertEquals("Marseille", b.address().city().getWithRoot(person));
	}

	@Test
	public void testSetOnRecordComponentThrowsException() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		try {
			b.city().set("Lyon");
			fail("Expected RuntimeException when setting a record component binding");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("read only"));
		}
	}

	@Test
	public void testSetWithRootOnRecordComponentThrowsException() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		PersonRecord person = new PersonRecord("Alice", 30, address);

		PersonRecordBinding b = new PersonRecordBinding();

		try {
			b.address().city().setWithRoot(person, "Lyon");
			fail("Expected RuntimeException when setting a record component binding via setWithRoot");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().contains("read only"));
		}
	}

	@Test
	public void testStatelessRecordBindingSafely() {
		PersonRecord person = new PersonRecord("Dave", 35, null);

		PersonRecordBinding b = new PersonRecordBinding(); // no instance set
		assertNull(b.address().city().getSafelyWithRoot(person));
		assertNull(b.address().city().getSafelyWithRoot(null));
	}

}
