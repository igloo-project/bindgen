package org.bindgen.example;

import static org.bindgen.BindKeyword.bind;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BindKeywordTest {

	@Test
	public void testWithFieldExample() {
		SimpleBean e = new SimpleBean("name");
		assertEquals("name", bind(e).name().get());
	}

}
