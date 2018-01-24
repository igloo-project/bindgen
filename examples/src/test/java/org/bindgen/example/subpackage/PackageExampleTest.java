package org.bindgen.example.subpackage;

import org.junit.Assert;
import junit.framework.TestCase;

public class PackageExampleTest extends TestCase {

	public void testReadWrite() {
		PackageExample e = new PackageExample("name");
		PackageExampleBinding b = new PackageExampleBinding(e);

		Assert.assertEquals("name", b.name().get());

		b.name().set("name1");
		Assert.assertEquals("name1", e.name);
	}

}
