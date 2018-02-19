package org.bindgen;

import java.io.Serializable;

@FunctionalInterface
public interface Setter<R, T> extends Serializable {

	void set(R root, T value);

}
