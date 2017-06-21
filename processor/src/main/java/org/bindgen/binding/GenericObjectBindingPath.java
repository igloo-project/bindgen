package org.bindgen.binding;

import java.util.Collections;
import java.util.List;

import org.bindgen.Binding;
import org.bindgen.BindingRoot;
import org.bindgen.Getter;
import org.bindgen.Setter;

/**
 * A binding that represents a generic object.
 *
 * This binding is usually used when there is no type-specific binding because,
 * for example, the type is outside of bindgen's scope.
 *
 * Note this is still abstract--the {@code getType}, {@code getName}, and
 * {@code getWithRoot} methods will be defined by the {@code MyXxx} concrete
 * class.
 *
 * @author igor.vaynberg
 *
 * @param <R>
 *            type of root object
 */
public class GenericObjectBindingPath<R, P, T> extends AbstractBinding<R, P, T> {

	private static final long serialVersionUID = 1L;

	public GenericObjectBindingPath() {
	}

	public GenericObjectBindingPath(String name, Class<?> type, BindingRoot<R, P> parentBinding, Getter<P, T> getter,
			Setter<P, T> setter) {
		super(name, type, parentBinding, getter, setter);
	}

	@Override
	public List<Binding<?>> getChildBindings() {
		return Collections.emptyList();
	}

	@Override
	public Class<?> getType() {
		return this.bindingType;
	}

}
