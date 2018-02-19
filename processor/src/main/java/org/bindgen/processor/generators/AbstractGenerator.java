package org.bindgen.processor.generators;

import joist.sourcegen.GClass;

public abstract class AbstractGenerator {

	protected final GClass outerClass;
	protected GClass innerClass;

	public AbstractGenerator(GClass outerClass) {
		super();
		this.outerClass = outerClass;
	}

	public final void generate() {
		this.generateOuter();
		this.generateInner();
	}

	protected abstract void generateInner();

	private void generateOuter() {
		this.addOuterClassGet();
		this.addOuterClassBindingField();
	}

	protected abstract void addOuterClassGet();

	protected abstract void addOuterClassBindingField();

}