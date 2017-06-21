package org.bindgen;

@FunctionalInterface
public interface Setter<R, T> {

	void set(R root, T value);

}
