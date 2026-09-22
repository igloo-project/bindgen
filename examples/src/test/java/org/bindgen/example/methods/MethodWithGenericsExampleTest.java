package org.bindgen.example.methods;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MethodWithGenericsExampleTest {

	@Test
	public void testReadWrite() {
		MethodWithGenericsExample e = new MethodWithGenericsExample();
		MethodWithGenericsExampleBinding b = new MethodWithGenericsExampleBinding(e);

		List<String> originalList = e.getList();
		assertSame(originalList, b.list().get());

		b.list().set(new ArrayList<String>());
		assertNotSame(originalList, b.list().get());
	}

}
