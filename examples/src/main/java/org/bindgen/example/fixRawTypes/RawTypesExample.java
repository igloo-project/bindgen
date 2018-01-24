package org.bindgen.example.fixRawTypes;

import java.util.Enumeration;

import org.bindgen.Bindable;

@Bindable
public class RawTypesExample {

	public Enumeration<String> fieldGiven;
	@SuppressWarnings("rawtypes")
	public Enumeration fieldRaw;
	@SuppressWarnings("rawtypes")
	public Enumeration fieldFixed;
	@SuppressWarnings("rawtypes")
	private Enumeration methodRaw;
	@SuppressWarnings("rawtypes")
	private Enumeration methodFixed;
	private Enumeration<String> methodGiven;

	@SuppressWarnings("rawtypes")
	public Enumeration getMethodFixed() {
		return this.methodFixed;
	}

	@SuppressWarnings("rawtypes")
	public void setMethodFixed(Enumeration e) {
		this.methodFixed = e;
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getMethodRaw() {
		return this.methodRaw;
	}

	@SuppressWarnings("rawtypes")
	public void setMethodRaw(Enumeration e) {
		this.methodRaw = e;
	}

	public Enumeration<String> getMethodGiven() {
		return this.methodGiven;
	}

	public void setMethodGiven(Enumeration<String> given) {
		this.methodGiven = given;
	}

}
