package org.bindgen.processor;

import org.junit.Test;

/**
 * @author Nándor Előd Fekete
 */
public class ConflictingGenericTypeArgumentTest extends AbstractBindgenTestCase {

    @Test
    public void testConflictingGenericTypeArguments() throws Exception {
        final ClassLoader cl = this.compile("org/bindgen/processor/typearguments/ConflictingTypeArguments.java");
        final Class<?> aClass = cl.loadClass("org.bindgen.processor.typearguments.ConflictingTypeArgumentsBinding");
        assertChildBindings(aClass, "hashCodeBinding", "toStringBinding", "property", "anotherProperty");
    }

}
