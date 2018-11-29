package org.bindgen.processor.generators;

import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import org.bindgen.ContainerBinding;
import org.bindgen.processor.util.BoundClass;
import org.bindgen.processor.util.BoundProperty;
import org.bindgen.processor.util.Util;

import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;

/**
 * Generates bindings for fields
 */
public class FieldPropertyGenerator extends AbstractGenerator implements PropertyGenerator {

	private final Element field;
	private final String fieldName;
	private final BoundProperty property;
	private final boolean isFinal;

	public FieldPropertyGenerator(GClass outerClass, BoundClass boundClass, TypeElement outerElement, Element field,
			String propertyName) throws WrongGeneratorException {
		super(outerClass);
		this.field = field;
		this.fieldName = this.field.getSimpleName().toString();

		this.property = new BoundProperty(outerElement, boundClass, this.field, this.field.asType(), propertyName);
		if (this.property.shouldSkip()) {
			throw new WrongGeneratorException();
		}
		this.isFinal = this.field.getModifiers().contains(javax.lang.model.element.Modifier.FINAL);
	}

	@Override
	public boolean hasSubBindings() {
		return true;
	}

	@Override
	protected void generateInner() {
		if (this.property.name.getDeclaredType() != null
				&& !this.property.name.getDeclaredType().getTypeArguments().isEmpty()) {
			this.addInnerClass();
			this.addInnerClassGetName();
			this.addInnerClassGetParent();
			this.addInnerClassGet();
			this.addInnerClassGetWithRoot();
			this.addInnerClassGetSafelyWithRoot();
			this.addInnerClassSet();
			this.addInnerClassSetWithRoot();
			this.addInnerClassGetContainedTypeIfNeeded();
			this.addInnerClassSerialVersionUID();
			this.addInnerClassIsReadOnlyOverrideIfNeeded();
		}
	}

	@Override
	protected void addOuterClassBindingField() {
		this.outerClass.getField(this.property.getName()).type(this.property.getBindingClassFieldDeclaration());
	}

	private void addOuterOldClassGet() {
		GMethod fieldGet = this.outerClass.getMethod(this.property.getName() + "()");
		fieldGet.setAccess(Util.getAccess(this.field));
		fieldGet.returnType(this.property.getBindingClassFieldDeclaration());
		fieldGet.body.line("if (this.{} == null) {", this.property.getName());
		fieldGet.body.line("    this.{} = new {}();", this.property.getName(),
				this.property.getBindingRootClassInstantiation());
		fieldGet.body.line("}");
		fieldGet.body.line("return this.{};", this.property.getName());
	}

	@Override
	protected void addOuterClassGet() {
		if (this.property.name.getDeclaredType() != null
				&& !this.property.name.getDeclaredType().getTypeArguments().isEmpty()) {
			this.addOuterOldClassGet();
		} else {
			String bindingType = this.property.getInnerClassSuperClass();
			GMethod fieldGet = this.outerClass.getMethod(this.property.getName() + "()");
			fieldGet.setAccess(Util.getAccess(this.field));
			fieldGet.returnType(this.property.getBindingClassFieldDeclaration());
			if (bindingType.contains("U1")) {
				fieldGet.typeParameters("U0, U1");
			} else if (bindingType.contains("U0")) {
				fieldGet.typeParameters("U0");
			}
			fieldGet.body.line("if (this.{} == null) {", this.property.getName());

			String type;
			if (this.property.isForGenericTypeParameter() || this.property.isArray()) {
				type = "null";
			} else {
				type = String.format("%s.class", this.property.getReturnableType());
			}

			String setterLambda = "null /* (item, value) -> item.{}(value) */";
			// if (this.hasSetterMethod()) {
			// setterLambda = "(item, value) -> item.{}(value)";
			// }
			if (!this.isFinal) {
				setterLambda = "(item, value) -> item.{} = value";
			} else {
				setterLambda = null;
			}

			if (!"null".equals(type)) {
				fieldGet.body.line(
						String.format("    this.{} = new {}(\"{}\", {}, this, (item) -> item.{}, %s);", setterLambda),
						this.property.getName(), this.property.getInnerClassSuperClass(), this.property.getName(), type,
						this.fieldName, this.fieldName);
			} else if (this.property.isArray()) {
				fieldGet.body.line(
						String.format("    this.{} = new {}(\"{}\", {}, this, (item) -> item.{}, %s);", setterLambda),
						this.property.getName(), this.property.getInnerClassSuperClass(), this.property.getName(), null,
						this.fieldName, this.fieldName);
			} else {
				fieldGet.body.line(
						String.format("    this.{} = new {}(\"{}\", this, (item) -> item.{}, %s);", setterLambda),
						this.property.getName(), this.property.getInnerClassSuperClass(), this.property.getName(),
						this.fieldName, this.fieldName);
			}
			fieldGet.body.line("}");
			fieldGet.body.line("return this.{};", this.property.getName());
		}
	}

	private void addInnerClass() {
		this.innerClass = this.outerClass.getInnerClass(this.property.getInnerClassDeclaration()).notStatic();
		this.innerClass.setAccess(Util.getAccess(this.field));
		this.innerClass.baseClassName(this.property.getInnerClassSuperClass());
		if (this.property.isForGenericTypeParameter() || this.property.isArray()) {
			this.innerClass.getMethod("getType").returnType("Class<?>").body.line("return null;");
		} else if (!this.property.shouldGenerateBindingClassForType()) {
			// since no binding class will be generated for the type of this
			// field we may not inherit getType() in MyBinding class (if, for
			// example, MyBinding extends GenericObjectBindingPath) and so we
			// have to implement it ouselves
			this.innerClass.getMethod("getType").returnType("Class<?>").body.line("return {}.class;",
					this.property.getReturnableType());
		}
	}

	private void addInnerClassGetName() {
		GMethod getName = this.innerClass.getMethod("getName").returnType(String.class).addAnnotation("@Override");
		getName.body.line("return \"{}\";", this.property.getName());
	}

	private void addInnerClassGetParent() {
		GMethod getParent = this.innerClass.getMethod("getParentBinding").returnType("Binding<?>")
				.addAnnotation("@Override");
		getParent.body.line("return {}.this;", this.outerClass.getSimpleName());
	}

	private void addInnerClassGet() {
		GMethod get = this.innerClass.getMethod("get").returnType(this.property.getSetType())
				.addAnnotation("@Override");
		get.body.line("return {}{}.this.get().{};", //
				this.property.getCastForReturnIfNeeded(), this.outerClass.getSimpleName(), this.fieldName);
	}

	private void addInnerClassGetWithRoot() {
		GMethod getWithRoot = this.innerClass.getMethod("getWithRoot");
		getWithRoot.argument(property.getBoundClass().getRootTypeArgument(), "root")
			.returnType(this.property.getSetType()).addAnnotation("@Override");
		getWithRoot.body.line("return {}{}.this.getWithRoot(root).{};", //
				this.property.getCastForReturnIfNeeded(), this.outerClass.getSimpleName(), this.fieldName);
	}

	private void addInnerClassGetSafelyWithRoot() {
		GMethod m = this.innerClass.getMethod("getSafelyWithRoot");
		m.argument(property.getBoundClass().getRootTypeArgument(), "root")
			.returnType(this.property.getSetType()).addAnnotation("@Override");
		m.body.line("if ({}.this.getSafelyWithRoot(root) == null) {", this.outerClass.getSimpleName());
		m.body.line("    return null;");
		m.body.line("} else {");
		m.body.line("    return {}{}.this.getSafelyWithRoot(root).{};", //
				this.property.getCastForReturnIfNeeded(), this.outerClass.getSimpleName(), this.fieldName);
		m.body.line("}");
	}

	private void addInnerClassSet() {
		GMethod set = this.innerClass.getMethod("set").argument(this.property.getSetType(), this.property.getName());
		set.addAnnotation("@Override");
		if (this.isFinal) {
			set.body.line("throw new RuntimeException(this.getName() + \" is read only\");");
			return;
		}
		set.body.line("{}.this.get().{} = {};", //
				this.outerClass.getSimpleName(), this.fieldName, this.property.getName());
	}

	private void addInnerClassSetWithRoot() {
		GMethod setWithRoot = this.innerClass.getMethod("setWithRoot({} root, {} {})",
			property.getBoundClass().getRootTypeArgument(),
			this.property.getSetType(),
			this.property.getName()
		);
		setWithRoot.addAnnotation("@Override");
		if (this.isFinal) {
			setWithRoot.body.line("throw new RuntimeException(this.getName() + \" is read only\");");
			return;
		}
		setWithRoot.body.line("{}.this.getWithRoot(root).{} = {};", //
				this.outerClass.getSimpleName(), this.fieldName, this.property.getName());
	}

	private void addInnerClassGetContainedTypeIfNeeded() {
		if (this.property.isForListOrSet() && !this.property.matchesTypeParameterOfParent()) {
			this.innerClass.implementsInterface(ContainerBinding.class);
			GMethod getContainedType = this.innerClass.getMethod("getContainedType").returnType("Class<?>")
					.addAnnotation("@Override");
			getContainedType.body.line("return {};", this.property.getContainedType());
		}
	}

	private void addInnerClassSerialVersionUID() {
		this.innerClass.getField("serialVersionUID").type("long").setStatic().setFinal().initialValue("1L");
	}

	private void addInnerClassIsReadOnlyOverrideIfNeeded() {
		if (this.isFinal) {
			this.innerClass.getMethod("getBindingIsReadOnly").returnType(boolean.class).body.line("return true;");
		}
	}

	@Override
	public List<TypeElement> getPropertyTypeElements() {
		return Util.collectTypeElements(this.property.getType());
	}

	@Override
	public String getPropertyName() {
		return this.property.getName();
	}

	@Override
	public String toString() {
		return this.field.toString();
	}

	public static class Factory implements GeneratorFactory {
		@Override
		public FieldPropertyGenerator newGenerator(GClass outerClass, BoundClass boundClass, TypeElement outerElement,
				Element possibleField, Collection<String> namesTaken) throws WrongGeneratorException {
			if (possibleField.getKind() != ElementKind.FIELD) {
				throw new WrongGeneratorException();
			}

			String propertyName = possibleField.getSimpleName().toString();
			if (namesTaken.contains(propertyName)) {
				propertyName += "Field";
			}
			while (Util.isObjectMethodName(propertyName) || Util.isBindingMethodName(propertyName)) {
				propertyName += "Field"; // Still invalid
			}

			return new FieldPropertyGenerator(outerClass, boundClass, outerElement, possibleField, propertyName);
		}
	}
}
