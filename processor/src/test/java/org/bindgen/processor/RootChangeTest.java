package org.bindgen.processor;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class RootChangeTest extends AbstractBindgenTestCase {

	@Test
	public void testBindingRootOverride() throws Exception {
		String bindingRoot = "org.bindgen.processor.noarg.AB";
		String testedClass = "org.bindgen.processor.noarg.ComplexData";

		this.setBindingPathSuperClass(bindingRoot);
		ClassLoader loader = this.compile(filePath(bindingRoot), filePath(testedClass));

		Class<?> bindingClass = loader.loadClass(testedClass + "BindingPath");

		assertNotNull(bindingClass);
		assertEquals(bindingRoot, bindingClass.getSuperclass().getName());
	}
}
