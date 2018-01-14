package org.bindgen.processor.noarg;

import org.bindgen.BindingRoot;
import org.bindgen.Getter;
import org.bindgen.Setter;
import org.bindgen.binding.AbstractBinding;

public class AB<R, P, T> extends AbstractBinding<R, P, T> {

	public AB() {
		super();
	}

	public AB(String name, Class<?> type, BindingRoot<R, P> parentBinding, Getter<P, T> getter, Setter<P, T> setter) {
		super(name, type, parentBinding, getter, setter);
	}

	private static final long serialVersionUID = 1L;
}
