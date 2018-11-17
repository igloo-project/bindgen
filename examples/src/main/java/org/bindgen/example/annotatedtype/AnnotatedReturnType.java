package org.bindgen.example.annotatedtype;

import org.bindgen.Bindable;

@Bindable
public class AnnotatedReturnType {

    private @TypeAnnotation String annotatedString;

    public @TypeAnnotation String getAnnotatedString() {
        return this.annotatedString;
    }

    public void setAnnotatedString(@TypeAnnotation String annotatedString) {
        this.annotatedString = annotatedString;
    }
}
