package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashSet;

import org.bindgen.ContainerBinding;
import org.junit.jupiter.api.Test;

public class CollectionExampleTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testGetSet() {
		CollectionExample e = new CollectionExample();
		e.things = new HashSet();

		CollectionExampleBinding b = new CollectionExampleBinding(e);
		assertSame(e.things, b.things().get());
		assertNull(((ContainerBinding) b.things()).getContainedType());
	}
}
