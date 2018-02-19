package org.bindgen.processor.util;

import static org.bindgen.processor.CurrentEnv.*;

import java.util.List;

import javax.lang.model.element.TypeElement;

import org.bindgen.processor.CurrentEnv;

import joist.util.Join;

/**
 * Given a TypeMirror type of a field/method property, provides information
 * about its binding outer/inner class.
 */
public class BoundClass {

	private final TypeElement element;
	private final ClassName name;

	public BoundClass(TypeElement element) {
		this.element = element;
		this.name = new ClassName(Util.boxIfNeeded(element.asType()).toString());
	}

	public boolean hasGenerics() {
		return !this.element.getTypeParameters().isEmpty();
	}

	/**
	 * @return binding type, e.g. bindgen.java.lang.StringBinding,
	 *         bindgen.app.EmployeeBinding
	 */
	public ClassName getBindingClassName() {
		String bindingName = getConfig().baseNameForBinding(this.name) + "Binding" + this.name.getGenericPart();
		return new ClassName(Util.lowerCaseOuterClassNames(this.element, bindingName));
	}

	public String getBindingPathClassDeclaration() {
		List<String> typeArgs = this.name.getGenericsWithBounds();
		typeArgs.add(0, "R");
		typeArgs.add(1, "P");
		return this.getBindingClassName().getWithoutGenericPart() + "Path" + "<" + Join.commaSpace(typeArgs) + ">";
	}

	public String getBindingPathClassSuperClass() {
		return CurrentEnv.getConfig().bindingPathSuperClassName() + "<R, P, " + this.name.get() + ">";
	}

	public String getBindingRootClassDeclaration() {
		if (this.name.getGenericsWithBounds().size() == 0) {
			return this.getBindingClassName().getWithoutGenericPart();
		} else {
			return this.getBindingClassName().getWithoutGenericPart() + "<" + Join.commaSpace(this.name.getGenericsWithBounds()) + ">";
		}
	}

	public String getBindingRootClassSuperClass() {
		List<String> typeArgs = this.name.getGenericsWithoutBounds();
		typeArgs.add(0, this.get());
		typeArgs.add(1, this.get());
		return this.getBindingClassName().getWithoutGenericPart() + "Path" + "<" + Join.commaSpace(typeArgs) + ">";
	}

	/** @return "com.app.Type<String, String>" if the type is "com.app.Type<String, String>" */
	public String get() {
		return this.name.get();
	}

	/** @return "com.app.Type<String, String>" if the type is "com.app.Type<String, String>" */
	public String toString() {
		return this.name.get();
	}

}
