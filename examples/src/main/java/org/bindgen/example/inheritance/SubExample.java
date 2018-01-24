package org.bindgen.example.inheritance;

public class SubExample extends BaseExample {

	public String name;
	public String subOnly;

	@Override
	public SubExampleBinding getBinding() {
		return new SubExampleBinding(this);
	}

	@Override
	public void go() {
		this.name = "insub";
	}

}
