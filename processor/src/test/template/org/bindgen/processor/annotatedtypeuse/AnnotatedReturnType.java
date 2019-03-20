package org.bindgen.processor.annotatedtypeuse;

import org.bindgen.Bindable;

@Bindable
public class AnnotatedReturnType {

    public @TypeAnnotation String annotatedTypeField;

    private @TypeAnnotation String annotatedString;

    public @TypeAnnotation String getAnnotatedString() {
        return this.annotatedString;
    }

    public void setAnnotatedString(@TypeAnnotation String annotatedString) {
        this.annotatedString = annotatedString;
    }

    @Override
    public @TypeAnnotation String toString() {
        return super.toString();
    }
}
