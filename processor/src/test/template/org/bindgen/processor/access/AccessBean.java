package org.bindgen.processor.access;

import org.bindgen.Bindable;

@Bindable
public class AccessBean {

	public String publicField;
	protected String protectedField;
	String packageField;
	@SuppressWarnings("unused")
	private String privateField;

	private String publicMethod;
	private String protectedMethod;
	private String packageMethod;
	private String privateMethod;

	public String getPublicMethod() {
		return this.publicMethod;
	}

	public void setPublicMethod(String publicMethod) {
		this.publicMethod = publicMethod;
	}

	protected String getProtectedMethod() {
		return this.protectedMethod;
	}

	protected void setProtectedMethod(String protectedMethod) {
		this.protectedMethod = protectedMethod;
	}

	String getPackageMethod() {
		return this.packageMethod;
	}

	void setPackageMethod(String packageMethod) {
		this.packageMethod = packageMethod;
	}

	@SuppressWarnings("unused")
	private String getPrivateMethod() {
		return this.privateMethod;
	}

	@SuppressWarnings("unused")
	private void setPrivateMethod(String privateMethod) {
		this.privateMethod = privateMethod;
	}

}
