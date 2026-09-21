package org.bindgen.example.inheritance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.bindgen.Binding;

public class SubExampleTest {

	private SubHolder holder = new SubHolder();

	@Test
	public void testFoo() {
		assertNull(this.holder.sub);
		SubExample sub = new SubExample();

		SubHolderBinding b = new SubHolderBinding(this.holder);
		Binding<? super SubExample> bind = b.sub();
		bind.set(sub);
		assertSame(sub, this.holder.sub);

		b.sub().set(sub);
		assertSame(sub, this.holder.sub);
	}

	@Test
	public void testSuperAttribute() {
		assertNull(this.holder.sub);

		SubExample sub = new SubExample();
		sub.description = "existingInSuper";
		SubExampleBinding b = new SubExampleBinding(sub);
		assertEquals("existingInSuper", b.description().get());

		assertEquals(6, b.getChildBindings().size()); // name (and nameField), description, subOnly, hashCode, toString
	}

	@Test
	public void testOverriddenCallable() {
		SubExample sub = new SubExample();
		SubExampleBinding b = new SubExampleBinding(sub);
		b.go().run();
		assertEquals("insub", sub.name);
	}

	@Test
	public void testOverriddenCallableWithABaseBinding() {
		SubExample sub = new SubExample();
		BaseExampleBinding b = new BaseExampleBinding(sub);
		b.go().run();
		assertEquals("insub", sub.name);

		assertTrue(b.get() instanceof SubExample);
		// 4 == description, name, hashCode and toString   -   no subOnly
		assertEquals(4, b.getChildBindings().size());
	}

}
