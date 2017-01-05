package org.bindgen.binding;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.bindgen.Binding;
import org.bindgen.BindingRoot;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;

/**
 * A base implementation of {@link BindingRoot} to hold the starting
 * <code>T</code> value for evaluating bindings paths.
 */
public class AbstractReflectBinding<R0, T> extends AbstractCoreBinding<R0, T> {

	private static final long serialVersionUID = 1L;
	protected final BindingRoot<R0, ?> _parentBinding;
	protected final Class<?> _parentType;
	protected final Class<?> _targetType;
	protected final String _propertyName;
	protected final String _fieldName;
	protected final Method _getMethod;
	protected final Method _setMethod;
	protected T _value;

	public AbstractReflectBinding() {
		this(null, null, null, null, null, null, null);
	}

	public AbstractReflectBinding(BindingRoot<R0, ?> parentBinding, Class<?> parentType, Class<?> targetType,
			String propertyName, String fieldName, String getMethod, String setMethod) {
		super();
		this._parentBinding = parentBinding;
		this._parentType = parentType;
		this._targetType = targetType;
		this._propertyName = propertyName;
		this._fieldName =  fieldName;
		if (this._parentType == null || this._fieldName != null) {
			this._getMethod = null;
			this._setMethod = null;
		} else {
			try {
				if (getMethod != null) {
					this._getMethod = this._parentType.getMethod(getMethod, new Class<?>[0]);
				} else {
					this._getMethod = null;
				}
				if (setMethod != null) {
					this._setMethod = this._parentType.getMethod(setMethod, new Class<?>[] { targetType });
				} else {
					this._setMethod = null;
				}
			} catch (Exception e) {
				throw new RuntimeException(String.format("Unable to generate Binding for %s.%s",
						this._parentType.getSimpleName(), this._propertyName));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getWithRoot(R0 root) {
		if (this._parentBinding == null) {
			return (T) root;
		} else {
			Object parent = this._parentBinding.getWithRoot(root);
			if (this._fieldName != null) {
				try {
					return (T) parent.getClass().getField(this._fieldName).get(parent);
				} catch (Exception e) {
					throw new RuntimeException(String.format("Unable to extract field %s.%s",
							parent.getClass().getSimpleName(), this._propertyName));
				}
			} else {
				try {
					return (T) this._getMethod.invoke(parent, new Object[0]);
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getSafelyWithRoot(R0 root) {
		if (this._parentBinding == null) {
			return (T) root;
		} else {
			Object parent = this._parentBinding.getWithRoot(root);
			if (parent == null) {
				return null;
			}
			if (this._fieldName != null) {
				try {
					return (T) parent.getClass().getField(this._fieldName).get(parent);
				} catch (Exception e) {
					throw new RuntimeException(String.format("Unable to extract field %s.%s",
							parent.getClass().getSimpleName(), this._propertyName));
				}
			} else {
				try {
					return (T) this._getMethod.invoke(parent, new Object[0]);
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		if (this._parentBinding == null) {
			return this._value;
		} else {
			Object parent = this._parentBinding.get();
			if (this._fieldName != null) {
				try {
					return (T) parent.getClass().getField(this._fieldName).get(parent);
				} catch (Exception e) {
					throw new RuntimeException(String.format("Unable to extract field %s.%s",
							parent.getClass().getSimpleName(), this._propertyName));
				}
			} else {
				try {
					return (T) this._getMethod.invoke(parent, new Object[0]);
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
		}
	}

	@Override
	public void set(T value) {
		if (this._parentBinding == null) {
			this._value = value;
		} else {
			Object parent = this._parentBinding.get();
			if (this._fieldName != null) {
				try {
					parent.getClass().getField(this._fieldName).set(parent, value);
				} catch (Exception e) {
					throw new RuntimeException(String.format("Unable to extract field %s.%s",
							parent.getClass().getSimpleName(), this._propertyName));
				}
			} else {
				try {
					this._setMethod.invoke(parent, value);
				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
		}
	}

	@Override
	public void setWithRoot(R0 root, T value) {
		if (this._parentBinding == null) {
			throw new RuntimeException("Should be overridden by a field/method-specific binding.");
		} else {
			Object parent = this._parentBinding.getWithRoot(root);
			try {
				this._setMethod.invoke(parent, value);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
	}

	@Override
	public Binding<?> getParentBinding() {
		return this._parentBinding;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Binding<R0> getRootBinding() {
		Binding<?> parent = this.getParentBinding();
		if (parent == null) {
			// We should be in a BindingRoot<R, R> => Cast is safe
			return (Binding<R0>) this;
		} else {
			// Parent's root type is expected to be the same as ours => Cast is
			// safe
			return (Binding<R0>) parent.getRootBinding();
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
		return this._parentBinding == null || this._setMethod != null;
	}

	@Override
	public Class<?> getType() {
		return this._targetType;
	}

	@Override
	public List<Binding<?>> getChildBindings() {
		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return this._propertyName != null ? this._propertyName : "";
	}

}
