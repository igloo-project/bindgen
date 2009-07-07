package org.exigencecorp.bindgen.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import joist.sourcegen.GClass;
import joist.sourcegen.GField;
import joist.sourcegen.GMethod;

import org.exigencecorp.bindgen.AbstractBinding;
import org.exigencecorp.bindgen.ContainerBinding;

public class FieldPropertyGenerator implements PropertyGenerator {

    private final GenerationQueue queue;
    private final GClass bindingClass;
    private final Element field;
    private final Property2 property;
    private final boolean isFinal;
    private GClass innerClass;

    public FieldPropertyGenerator(GenerationQueue queue, GClass bindingClass, Element field) {
        this.queue = queue;
        this.bindingClass = bindingClass;
        this.field = field;
        this.property = new Property2(//
            this.queue.boxIfNeeded(this.field.asType()),
            (TypeElement) this.field.getEnclosingElement(),
            this.field.getSimpleName().toString());
        this.isFinal = this.field.getModifiers().contains(javax.lang.model.element.Modifier.FINAL);
    }

    @Override
    public boolean isCallable() {
        return false;
    }

    public boolean shouldGenerate() {
        if (this.property.isForBinding() || this.property.shouldSkip() || this.property.isNameless()) {
            return false;
        }
        return true;
    }

    public void generate() {
        this.addOuterClassGet();
        this.addOuterClassBindingField();
        this.addInnerClass();
        this.addInnerClassGetName();
        this.addInnerClassGetParent();
        this.addInnerClassGet();
        this.addInnerClassGetWithRoot();
        this.addInnerClassSet();
        this.addInnerClassSetWithRoot();
        this.addInnerClassGetContainedTypeIfNeeded();
    }

    private void addOuterClassBindingField() {
        GField f = this.bindingClass.getField(this.property.getName()).type(this.property.getBindingClassFieldDeclaration());
        if (this.property.isRawType()) {
            f.addAnnotation("@SuppressWarnings(\"unchecked\")");
        }
    }

    private void addOuterClassGet() {
        GMethod fieldGet = this.bindingClass.getMethod(this.property.getName() + "()");
        if (this.property.isForGenericTypeParameter()) {
            fieldGet.returnType(this.property.getInnerClass());
        } else {
            fieldGet.returnType(this.property.getBindingTypeForPathWithR());
        }
        fieldGet.body.line("if (this.{} == null) {", this.property.getName());
        fieldGet.body.line("    this.{} = new {}();", this.property.getName(), this.property.getBindingRootClassInstantiation());
        fieldGet.body.line("}");
        fieldGet.body.line("return this.{};", this.property.getName());
        if (this.property.isRawType()) {
            fieldGet.addAnnotation("@SuppressWarnings(\"unchecked\")");
        }
    }

    private void addInnerClass() {
        this.innerClass = this.bindingClass.getInnerClass(this.property.getInnerClass()).notStatic();
        if (this.property.isForGenericTypeParameter()) {
            this.innerClass.baseClassName("{}<R, {}>", AbstractBinding.class.getName(), this.property.getGenericElement());
            this.innerClass.getMethod("getType").returnType("Class<?>").body.line("return null;");
        } else {
            this.innerClass.baseClassName(this.property.getInnerClassSuperClass());
            if (this.property.hasWildcards() || this.property.isRawType()) {
                this.innerClass.addAnnotation("@SuppressWarnings(\"unchecked\")");
            }
        }
    }

    private void addInnerClassGetName() {
        GMethod getName = this.innerClass.getMethod("getName").returnType(String.class).addAnnotation("@Override");
        getName.body.line("return \"{}\";", this.property.getName());
    }

    private void addInnerClassGetParent() {
        GMethod getParent = this.innerClass.getMethod("getParentBinding").returnType("Binding<?>").addAnnotation("@Override");
        getParent.body.line("return {}.this;", this.bindingClass.getSimpleClassNameWithoutGeneric());
    }

    private void addInnerClassGet() {
        GMethod get = this.innerClass.getMethod("get").returnType(this.property.getSetType()).addAnnotation("@Override");
        get.body.line("return {}{}.this.get().{};",//
            this.property.getCastForReturnIfNeeded(),
            this.bindingClass.getSimpleClassNameWithoutGeneric(),
            this.property.getName());
        if (this.property.isFixingRawType) {
            get.addAnnotation("@SuppressWarnings(\"unchecked\")");
        }
    }

    private void addInnerClassGetWithRoot() {
        GMethod getWithRoot = this.innerClass.getMethod("getWithRoot");
        getWithRoot.argument("R", "root").returnType(this.property.getSetType()).addAnnotation("@Override");
        getWithRoot.body.line("return {}{}.this.getWithRoot(root).{};",//
            this.property.getCastForReturnIfNeeded(),
            this.bindingClass.getSimpleClassNameWithoutGeneric(),
            this.property.getName());
        if (this.property.isFixingRawType) {
            getWithRoot.addAnnotation("@SuppressWarnings(\"unchecked\")");
        }
    }

    private void addInnerClassSet() {
        GMethod set = this.innerClass.getMethod("set").argument(this.property.getSetType(), this.property.getName());
        if (this.isFinal) {
            set.body.line("throw new RuntimeException(this.getName() + \" is read only\");");
            return;
        }
        set.body.line("{}.this.get().{} = {};",//
            this.bindingClass.getSimpleClassNameWithoutGeneric(),
            this.property.getName(),
            this.property.getName());
    }

    private void addInnerClassSetWithRoot() {
        GMethod setWithRoot = this.innerClass.getMethod("setWithRoot(R root, {} {})", this.property.getSetType(), this.property.getName());
        if (this.isFinal) {
            setWithRoot.body.line("throw new RuntimeException(this.getName() + \" is read only\");");
            return;
        }
        setWithRoot.body.line(
            "{}.this.getWithRoot(root).{} = {};",
            this.bindingClass.getSimpleClassNameWithoutGeneric(),
            this.property.getName(),
            this.property.getName());
    }

    private void addInnerClassGetContainedTypeIfNeeded() {
        if (this.property.isForListOrSet()) {
            String contained = this.property.getGenericPartWithoutBrackets();
            if (!this.matchesTypeParameterOfParent(contained)) {
                this.innerClass.implementsInterface(ContainerBinding.class);
                GMethod containedType = this.innerClass.getMethod("getContainedType").returnType("Class<?>").addAnnotation("@Override");
                containedType.body.line("return {}.class;", contained);
            }
        }
    }

    private boolean matchesTypeParameterOfParent(String type) {
        if (this.property.hasWildcards()) {
            return true;
        }
        for (TypeParameterElement e : ((TypeElement) this.field.getEnclosingElement()).getTypeParameters()) {
            if (e.toString().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public TypeElement getPropertyTypeElement() {
        return this.property.getElement();
    }

    public String getPropertyName() {
        return this.property.getName();
    }

}
