package org.bindgen;

import java.io.Serializable;

@FunctionalInterface
public interface Getter<R, T> extends Serializable {

	T get(R root);

}
