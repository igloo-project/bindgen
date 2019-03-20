package org.bindgen.processor;

import org.junit.Test;

/**
 * @author Nándor Előd Fekete
 */
public class AnnotatedTypeUseTest extends AbstractBindgenTestCase {

    @Test
    public void testAnnotatedTypeUse() throws Exception {
        final ClassLoader cl = this.compile("org/bindgen/processor/annotatedtypeuse/TypeAnnotation.java",
            "org/bindgen/processor/annotatedtypeuse/AnnotatedReturnType.java");
        final Class<?> aClass = cl.loadClass("org.bindgen.processor.annotatedtypeuse.AnnotatedReturnTypeBinding");
        assertChildBindings(aClass,
            "hashCodeBinding", "toStringBinding", "annotatedTypeField",
            "annotatedString");
    }

    @Test
    public void testOutOfScopeAnnotatedTypeUse() throws Exception {
        this.setScope("org.bindgen.processor.annotatedtypeuse");
        final ClassLoader cl = this.compile("org/bindgen/processor/annotatedtypeuse/TypeAnnotation.java",
            "org/bindgen/processor/annotatedtypeuse/AnnotatedReturnType.java",
            "org/bindgen/processor/outofscope/OutOfScope.java");
        final Class<?> aClass = cl.loadClass("org.bindgen.processor.annotatedtypeuse.AnnotatedReturnTypeBinding");
        assertChildBindings(aClass,
            "hashCodeBinding", "toStringBinding", "annotatedTypeField",
            "annotatedString");
    }



}
