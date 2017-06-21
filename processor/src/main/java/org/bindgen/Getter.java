package org.bindgen;

@FunctionalInterface
public interface Getter<R, T> {

	T get(R root);

}
