package org.bindgen.binding;

import java.util.ArrayList;
import java.util.List;

import org.bindgen.Binding;
import org.bindgen.BindingRoot;
import org.bindgen.Getter;
import org.bindgen.Setter;

/**
 * A base implementation of {@link BindingRoot} to hold the starting
 * <code>T</code> value for evaluating bindings paths.
 */
public class AbstractBinding<R, P, T> implements BindingRoot<R, T> {

	private static final long serialVersionUID = 1L;

	protected String bindingName;
	protected BindingRoot<R, P> bindingParentBinding;
	protected Getter<P, T> bindingGetter;
	protected Setter<P, T> bindingSetter;
	protected Class<?> bindingType;

	protected T _value;

	public AbstractBinding() {
	}

	public AbstractBinding(String name, Class<?> type, BindingRoot<R, P> parentBinding, Getter<P, T> getter,
			Setter<P, T> setter) {
		this.bindingName = name;
		this.bindingParentBinding = parentBinding;
		this.bindingGetter = getter;
		this.bindingSetter = setter;
		this.bindingType = type;
	}

	@Override
	public T get() {
		if (this.bindingParentBinding != null && this.bindingGetter != null) {
			return this.bindingGetter.get(this.bindingParentBinding.get());
		} else {
			return this._value;
		}
	}

	@Override
	public void set(T value) {
		if (this.bindingParentBinding != null && this.bindingSetter == null) {
			throw new RuntimeException(this.getName() + " is read only");
		} else if (this.bindingSetter != null) {
			this.bindingSetter.set(this.bindingParentBinding.get(), value);
		} else {
			this._value = value;
		}
	}

	@Override
	public void setWithRoot(R root, T value) {
		if (this.bindingParentBinding != null && this.bindingSetter == null) {
			throw new RuntimeException(this.getName() + " is read only");
		} else if (this.bindingSetter != null) {
			this.bindingSetter.set(this.bindingParentBinding.getWithRoot(root), value);
		} else {
			this._value = value;
		}
	}

	@Override
	public List<Binding<?>> getChildBindings() {
		return new ArrayList<Binding<?>>();
	}

	@Override
	public Binding<?> getParentBinding() {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Binding<R> getRootBinding() {
		Binding<?> parent = this.getParentBinding();
		if (parent == null) {
			// We should be in a BindingRoot<R, R> => Cast is safe
			return (Binding<R>) this;
		} else {
			// Parent's root type is expected to be the same as ours => Cast is
			// safe
			return (Binding<R>) parent.getRootBinding();
		}
	}

	@Override
	public String toString() {
		if (this.getParentBinding() == null) {
			// This is kind of lame, but GWT doesn't support getSimpleName, so
			// use getName
			String className = this.getClass().getName();
			String simpleName = className.substring(className.lastIndexOf(".") + 1);
			return simpleName + "(" + this.get() + ")";
		} else {
			Object value = this.getIsSafe() ? this.get() : "";
			return this.getParentBinding().toString() + "." + this.getName() + "(" + value + ")";
		}
	}

	@Override
	public boolean getIsSafe() {
		if (this.getParentBinding() == null) {
			return true;
		} else if (this.getParentBinding().getIsSafe()) {
			return this.getParentBinding().get() != null;
		} else {
			return false;
		}
	}

	@Override
	public String getPath() {
		if (this.getParentBinding() == null) {
			return "#root";
		} else if (this.getParentBinding().getParentBinding() == null) {
			return this.getName();
		} else {
			return this.getParentBinding().getPath() + "." + this.getName();
		}
	}

	@Override
	public T getSafely() {
		if (this.getIsSafe()) {
			return this.get();
		} else {
			return null;
		}
	}

	@Override
	public boolean getBindingIsReadOnly() {
		return this.bindingParentBinding == null || this.bindingSetter != null;
	}

	@Override
	public String getName() {
		if (this.bindingName != null) {
			return this.bindingName;
		} else {
			return "";
		}
	}

	@Override
	public T getWithRoot(R root) {
		if (this.bindingParentBinding != null && this.bindingGetter != null) {
			return this.bindingGetter.get(this.bindingParentBinding.getWithRoot(root));
		} else {
			return this._value;
		}
	}

	@Override
	public T getSafelyWithRoot(R root) {
		if (this.bindingParentBinding != null && this.bindingGetter != null) {
			P parent = this.bindingParentBinding.getSafelyWithRoot(root);
			if (parent != null) {
				return this.bindingGetter.get(parent);
			} else {
				return null;
			}
		} else {
			return this._value;
		}
	}

	@Override
	public Class<?> getType() {
		return this.bindingType;
	}

}
