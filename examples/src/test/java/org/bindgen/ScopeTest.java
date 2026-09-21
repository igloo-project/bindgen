package org.bindgen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.bindgen.binding.GenericObjectBindingPath;
import org.bindgen.inscope.AddressIn;
import org.bindgen.inscope.Person;
import org.bindgen.inscope.PersonBinding;
import org.bindgen.outofscope.AddressOut;

public class ScopeTest {

	@Test
	public void testShouldGenerateGenericBindingForOutOfScopeProperty() throws Exception {
		final Class<?> generic = GenericObjectBindingPath.class;
		final Class<?> binding = new PersonBinding().addressOut().getClass();
		assertTrue(generic.isAssignableFrom(binding));
	}

	@Test
	public void testInnerClassIsStillTypeSafe() {
		Person p = new Person();
		PersonBinding b = new PersonBinding(p);

		AddressOut a = new AddressOut();
		b.addressOut().set(a);
		assertSame(a, b.addressOut().get());

		assertEquals(AddressOut.class, b.addressOut().getType());
	}

	@Test
	public void testWithinScopeIsGenerated() {
		Person p = new Person();
		PersonBinding b = new PersonBinding(p);

		AddressIn a = new AddressIn();
		b.addressIn().set(a);
		assertSame(a, b.addressIn().get());

		assertEquals(AddressIn.class, b.addressIn().getType());

		b.addressIn().city().set("Foo");
	}
}
