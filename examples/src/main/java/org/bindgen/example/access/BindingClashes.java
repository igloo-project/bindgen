package org.bindgen.example.access;

import org.bindgen.Bindable;

@Bindable
public class BindingClashes {

	public String property = "0";

	public String type() {
		return "1";
	}

	public String getType() {
		return "2";
	}

	public String path() {
		return "a";
	}

	public String getPath() {
		return "b";
	}

}
