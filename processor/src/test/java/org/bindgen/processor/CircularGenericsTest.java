package org.bindgen.processor;

import org.junit.Test;

/** Tests 'circular' generics references. */
public class CircularGenericsTest extends AbstractBindgenTestCase {

	@Test
	public void testCircular() throws Exception {
		ClassLoader cl = this.compile(
			"org/bindgen/processor/circular/Individual.java",
			"org/bindgen/processor/circular/Organisation.java",
			"org/bindgen/processor/circular/SelectedIndividual.java");

		Class<?> selectedIndividualBindingClass = cl.loadClass("org.bindgen.processor.circular.SelectedIndividualBinding");
		assertChildBindings(selectedIndividualBindingClass, "hashCodeBinding", "organisation", "toStringBinding");
	}
}
