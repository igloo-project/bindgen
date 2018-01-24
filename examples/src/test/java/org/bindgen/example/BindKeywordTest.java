package org.bindgen.example;

import static org.bindgen.BindKeyword.bind;
import org.junit.Assert;
import junit.framework.TestCase;

public class BindKeywordTest extends TestCase {

	public void testWithFieldExample() {
		SimpleBean e = new SimpleBean("name");
		Assert.assertEquals("name", bind(e).name().get());
	}

}
