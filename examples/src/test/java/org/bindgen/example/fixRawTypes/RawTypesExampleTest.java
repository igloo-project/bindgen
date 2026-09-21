package org.bindgen.example.fixRawTypes;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.jupiter.api.Test;

public class RawTypesExampleTest {

	private Hashtable<String, String> hash = new Hashtable<String, String>();
	private Enumeration<String> e = this.hash.keys();

	@Test
	public void testField() {
		RawTypesExample r = new RawTypesExample();
		RawTypesExampleBinding b = new RawTypesExampleBinding(r);

		b.fieldGiven().set(this.e);
		assertSame(this.e, b.fieldGiven().get());

		b.fieldFixed().set(this.e);
		assertSame(this.e, b.fieldFixed().get());
	}

	@Test
	public void testMethod() {
		RawTypesExample r = new RawTypesExample();
		RawTypesExampleBinding b = new RawTypesExampleBinding(r);

		b.methodGiven().set(this.e);
		assertSame(this.e, b.methodGiven().get());

		b.methodFixed().set(this.e);
		assertSame(this.e, b.methodFixed().get());
	}

}
