package org.bindgen;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a property (field or method) binding.
 *
 * @param T the type of the leaf object of the binding
 */
public interface Binding<T> extends NamedBinding, Serializable {

	/** @return the value for this binding */
	T get();

	/** @param value the new value for this binding */
	void set(T value);

	/** @return the type <code>T</code> for this binding */
	Class<?> getType();

	/** @return the parent binding, e.g. parent if we are foo in binding.parent().foo() */
	Binding<?> getParentBinding();

	/** @return the root binding, e.g. myBinding if we are foo in new MyBinding().parent().foo() */
	Binding<?> getRootBinding();

	/** @return the bindings of the attributes for our current instance. */
	List<Binding<?>> getChildBindings();

	/** @return an OGNL-like String representation of the binding. */
	String getPath();

	/** @return whether a {@code #get} or {@code #set} will NPE because of a null parent */
	boolean getIsSafe();

	/** @return whether the binding is for a read-only field/getter or not. */
	boolean getBindingIsReadOnly();

	/** @return the value for this binding or {@code null} if a parent path's value is {@code null} */
	T getSafely();

}
