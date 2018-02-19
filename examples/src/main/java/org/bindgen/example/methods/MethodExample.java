package org.bindgen.example.methods;

import java.util.List;

import org.bindgen.Bindable;
import org.bindgen.Binding;

@Bindable
public class MethodExample {

	// a read-only property
	private String id;
	// a read/write property
	private String name;
	// a boolean property
	private boolean good;
	// Had been colliding on set
	private boolean set;
	private boolean get;
	private List<String> list;
	private List<?> unknown; // Was causing errors with ContainerBinding
	// isNull -> null would be a keyword
	private boolean isNull;
	private Wildcards<String, ?, ?> wildcards; // Was causing errors with wildcard in 2nd position
	// Even though protected, the setter takes precendence
	protected int protectedProperty;
	// a 1-char method
	private String m;
	// a read/write array
	private String[] arrayProp;

	public MethodExample(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "method";
	}

	public boolean isGood() {
		return this.good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public boolean hasStuff() {
		return false;
	}

	// Putting the @deprecated here ensures a warning would show up if this "to"
	// prefix got recognized
	@Deprecated
	public boolean tobacco() {
		return false;
	}

	// This method would be a property "new" which is a keyword
	public boolean isNew() {
		return false;
	}

	// Returning a binding to myself should cause recursion or anything--used to
	// cause errors
	public Binding<?> getBinding() {
		return new MethodExampleBinding(this);
	}

	// Putting the @deprecated here ensures a warning would show up if this array
	// was not skipped
	@Deprecated
	public String[] getStrings() {
		return null;
	}

	public boolean isSet() {
		return this.set;
	}

	public void setSet(boolean set) {
		this.set = set;
	}

	public boolean isGet() {
		return this.get;
	}

	public void setGet(boolean get) {
		this.get = get;
	}

	public Boolean getReadOnlyButSetterIsNotPublic() {
		return true;
	}

	// This used to cause a compile error in the binding
	protected void setReadOnlyButSetterIsNotPublic(Boolean b) {
	}

	public List<String> getList() {
		return this.list;
	}

	public List<?> getUnknown() {
		return this.unknown;
	}

	public boolean isNull() {
		return this.isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public Wildcards<String, ?, ?> getWildcards() {
		return this.wildcards;
	}

	public void setWildcards(Wildcards<String, ?, ?> wildcards) {
		this.wildcards = wildcards;
	}

	public String getM() {
		return this.m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public int noArg() {
		return 1;
	}

	protected int isProtectedProperty() {
		return this.protectedProperty;
	}

	protected void setProtectedProperty(int protectedProperty) {
		// show the setter working
		this.protectedProperty = protectedProperty + 1;
	}

	public String[] getArrayProp() {
		return this.arrayProp;
	}

	public void setArrayProp(String[] arrayProp) {
		this.arrayProp = arrayProp;
	}
}
