package org.bindgen.example.record;

import org.junit.Assert;

import junit.framework.TestCase;

public class RecordExampleTest extends TestCase {

	public void testSimpleRecordComponentBindings() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		Assert.assertEquals("Paris", b.city().get());
		Assert.assertEquals("75001", b.zipCode().get());
	}

	public void testRecordComponentBindingsAreReadOnly() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		Assert.assertTrue("Record component bindings should be read-only", b.city().getBindingIsReadOnly());
		Assert.assertTrue("Record component bindings should be read-only", b.zipCode().getBindingIsReadOnly());
	}

	public void testRecordBindingName() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		Assert.assertEquals("city", b.city().getName());
		Assert.assertEquals("zipCode", b.zipCode().getName());
	}

	public void testRecordWithPrimitiveComponent() {
		PersonRecord person = new PersonRecord("Alice", 30, null);
		PersonRecordBinding b = new PersonRecordBinding(person);

		Assert.assertEquals("Alice", b.name().get());
		Assert.assertEquals(Integer.valueOf(30), b.age().get());
	}

	public void testNestedRecordBindings() {
		AddressRecord address = new AddressRecord("Lyon", "69001");
		PersonRecord person = new PersonRecord("Bob", 25, address);
		PersonRecordBinding b = new PersonRecordBinding(person);

		Assert.assertEquals("Lyon", b.address().city().get());
		Assert.assertEquals("69001", b.address().zipCode().get());
	}

	public void testStatelessRecordBinding() {
		AddressRecord address = new AddressRecord("Marseille", "13001");
		PersonRecord person = new PersonRecord("Charlie", 40, address);

		PersonRecordBinding b = new PersonRecordBinding(); // no instance set
		Assert.assertEquals("Marseille", b.address().city().getWithRoot(person));
	}

	public void testSetOnRecordComponentThrowsException() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		AddressRecordBinding b = new AddressRecordBinding(address);

		try {
			b.city().set("Lyon");
			Assert.fail("Expected RuntimeException when setting a record component binding");
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("read only"));
		}
	}

	public void testSetWithRootOnRecordComponentThrowsException() {
		AddressRecord address = new AddressRecord("Paris", "75001");
		PersonRecord person = new PersonRecord("Alice", 30, address);

		PersonRecordBinding b = new PersonRecordBinding();

		try {
			b.address().city().setWithRoot(person, "Lyon");
			Assert.fail("Expected RuntimeException when setting a record component binding via setWithRoot");
		} catch (RuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("read only"));
		}
	}

	public void testStatelessRecordBindingSafely() {
		PersonRecord person = new PersonRecord("Dave", 35, null);

		PersonRecordBinding b = new PersonRecordBinding(); // no instance set
		Assert.assertNull(b.address().city().getSafelyWithRoot(person));
		Assert.assertNull(b.address().city().getSafelyWithRoot(null));
	}

}
