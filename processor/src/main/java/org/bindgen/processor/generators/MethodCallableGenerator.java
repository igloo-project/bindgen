package org.bindgen.processor.generators;

import static org.bindgen.processor.CurrentEnv.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;

import org.bindgen.NamedBinding;
import org.bindgen.processor.CurrentEnv;
import org.bindgen.processor.util.BoundClass;
import org.bindgen.processor.util.Util;

import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Inflector;

public class MethodCallableGenerator extends AbstractGenerator implements PropertyGenerator {

	private final ExecutableElement method;
	private final String methodName;
	private TypeElement blockType;
	private ExecutableElement blockMethod;

	public MethodCallableGenerator(GClass outerClass, ExecutableElement method) throws WrongGeneratorException {
		super(outerClass);
		this.method = method;
		this.methodName = this.method.getSimpleName().toString();
		if (!this.shouldGenerate()) {
			throw new WrongGeneratorException();
		}
	}

	@Override
	public boolean hasSubBindings() {
		return false;
	}

	private boolean shouldGenerate() {
		if (getConfig().skipAttribute(this.method.getEnclosingElement(), this.methodName)) {
			return false;
		}
		for (String classNameToAttempt : getConfig().blockTypesToAttempt()) {
			if (this.blockTypeMatchesMethod(classNameToAttempt)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void generateInner() {
		this.addInnerClass();
		this.addInnerClassMethod();
		this.addInnerClassGetName();
		this.addInnerClassSerialVersionUID();
	}

	private boolean blockTypeMatchesMethod(String attemptClassName) {
		TypeElement attemptType = getElementUtils().getTypeElement(attemptClassName);
		if (attemptType == null) {
			return false;
		}
		List<ExecutableElement> methods = ElementFilter.methodsIn(attemptType.getEnclosedElements());
		if (methods.size() != 1) {
			return false; // We only like classes with 1 method
		}
		ExecutableElement methodToMatch = methods.get(0);
		if (this.doBlockReturnTypesMatch(methodToMatch) //
				&& this.doBlockParamsMatch(methodToMatch) && this.doBlockThrowsMatch(methodToMatch)) {
			this.blockType = attemptType;
			this.blockMethod = methodToMatch;
			return true;
		}
		return false;
	}

	@Override
	protected void addOuterClassBindingField() {
		this.outerClass.getField(this.methodName).type(this.blockType.getQualifiedName().toString());
	}

	@Override
	protected void addOuterClassGet() {
		GMethod get = this.outerClass.getMethod(this.methodName)
				.returnType(this.blockType.getQualifiedName().toString());
		get.setAccess(Util.getAccess(this.method));
		get.body.line("if (this.{} == null) {", this.methodName);
		get.body.line("    this.{} = new My{}Binding();", this.methodName, Inflector.capitalize(this.methodName));
		get.body.line("}");
		get.body.line("return this.{};", this.methodName);
	}

	private void addInnerClass() {
		this.innerClass = this.outerClass.getInnerClass("My{}Binding", Inflector.capitalize(this.methodName))
				.notStatic();
		this.innerClass.setAccess(Util.getAccess(this.method));
		this.innerClass.implementsInterface(this.blockType.getQualifiedName().toString());
		this.innerClass.implementsInterface(NamedBinding.class);
	}

	private void addInnerClassMethod() {
		GMethod run = this.innerClass.getMethod(this.blockMethod.getSimpleName().toString());
		run.returnType(Util.getTypeName(this.blockMethod.getReturnType()));
		run.body.line("{}{}.this.get().{}({});", //
				this.getReturnPrefixIfNeeded(), this.outerClass.getSimpleName(), this.methodName, this.getArguments());
		this.addMethodParameters(run);
		this.addMethodThrows(run);
	}

	private void addInnerClassGetName() {
		GMethod getName = this.innerClass.getMethod("getName").returnType(String.class);
		getName.body.line("return \"{}\";", this.methodName);
	}

	private void addInnerClassSerialVersionUID() {
		this.innerClass.getField("serialVersionUID").type("long").setStatic().setFinal().initialValue("1L");
	}

	@Override
	public String getPropertyName() {
		return this.methodName;
	}

	@Override
	public List<TypeElement> getPropertyTypeElements() {
		return new ArrayList<TypeElement>();
	}

	private boolean doBlockReturnTypesMatch(ExecutableElement methodToMatch) {
		return getTypeUtils().isSameType(methodToMatch.getReturnType(), this.method.getReturnType());
	}

	private boolean doBlockParamsMatch(ExecutableElement methodToMatch) {
		if (methodToMatch.getParameters().size() != this.getMethodAsType().getParameterTypes().size()) {
			return false;
		}
		Types typeUtils = CurrentEnv.getTypeUtils();
		for (int i = 0; i < methodToMatch.getParameters().size(); i++) {
			if (!typeUtils.isSameType(methodToMatch.getParameters().get(i).asType(),
					this.getMethodAsType().getParameterTypes().get(i))) {
				return false;
			}
		}
		return true;
	}

	private boolean doBlockThrowsMatch(ExecutableElement methodToMatch) {
		Types typeUtils = CurrentEnv.getTypeUtils();
		for (TypeMirror throwsType : this.method.getThrownTypes()) {
			boolean matchesOne = false;
			for (TypeMirror otherType : methodToMatch.getThrownTypes()) {
				if (typeUtils.isSameType(otherType, throwsType)) {
					matchesOne = true;
				}
			}
			if (!matchesOne) {
				return false;
			}
		}
		return true;
	}

	private void addMethodParameters(GMethod run) {
		for (VariableElement foo : this.blockMethod.getParameters()) {
			run.argument(Util.getTypeName(foo.asType()), foo.getSimpleName().toString());
		}
	}

	private void addMethodThrows(GMethod run) {
		for (TypeMirror type : this.method.getThrownTypes()) {
			run.addThrows(Util.getTypeName(type));
		}
	}

	// Figure out whether we need a "return" or not
	private String getReturnPrefixIfNeeded() {
		return this.blockMethod.getReturnType().getKind() == TypeKind.VOID ? "" : "return ";
	}

	private String getArguments() {
		String arguments = "";
		for (VariableElement foo : this.blockMethod.getParameters()) {
			arguments += foo.getSimpleName().toString() + ", ";
		}
		if (arguments.length() > 0) {
			arguments = arguments.substring(0, arguments.length() - 2); // remove last ", "
		}
		return arguments;
	}

	private ExecutableType getMethodAsType() {
		return (ExecutableType) this.method.asType();
	}

	public static class Factory implements GeneratorFactory {
		@Override
		public MethodCallableGenerator newGenerator(GClass outerClass, BoundClass boundClass, TypeElement outerElement,
				Element possibleMethod, Collection<String> namesTaken) throws WrongGeneratorException {
			if (possibleMethod.getKind() != ElementKind.METHOD) {
				throw new WrongGeneratorException();
			}
			MethodCallableGenerator pg = new MethodCallableGenerator(outerClass, (ExecutableElement) possibleMethod);
			if (namesTaken.contains(pg.getPropertyName())) {
				throw new WrongGeneratorException(); // do not generate bindings with compilation errors
			}

			return pg;
		}
	}

}
