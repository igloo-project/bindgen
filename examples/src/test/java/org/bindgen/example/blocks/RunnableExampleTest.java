package org.bindgen.example.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import org.bindgen.NamedBinding;

public class RunnableExampleTest {

	@Test
	public void testRun() {
		RunnableExample e = new RunnableExample();
		assertFalse(e.isStuffDone());

		RunnableExampleBinding b = new RunnableExampleBinding(e);
		Runnable r = b.doStuff();
		assertFalse(e.isStuffDone());

		r.run();
		assertTrue(e.isStuffDone());
	}

	@Test
	public void testRunName() {
		RunnableExample e = new RunnableExample();
		RunnableExampleBinding b = new RunnableExampleBinding(e);
		Runnable r = b.doStuff();
		assertEquals("doStuff", ((NamedBinding) r).getName());
	}

}
