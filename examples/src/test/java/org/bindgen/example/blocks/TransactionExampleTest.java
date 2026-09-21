package org.bindgen.example.blocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class TransactionExampleTest {

	@Test
	public void testBusinessLogicMethod() throws Exception {
		TransactionExample te = new TransactionExample();
		TransactionExampleBinding teb = new TransactionExampleBinding(te);
		TransactionBlock block = teb.businessLogic();
		assertEquals(true, block.result("good").booleanValue());
	}

	@Test
	public void testBusinessLogicThatFailsMethod() throws Exception {
		TransactionExample te = new TransactionExample();
		TransactionExampleBinding teb = new TransactionExampleBinding(te);
		TransactionBlock block = teb.businessLogicThatCanFail();
		try {
			block.result("good");
			fail();
		} catch (Exception e) {
			assertEquals("I failed", e.getMessage());
		}
	}

}
