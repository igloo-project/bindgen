package org.bindgen.processor.generators;

import static org.bindgen.processor.CurrentEnv.*;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Generated;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import org.bindgen.Binding;
import org.bindgen.BindingRoot;
import org.bindgen.Getter;
import org.bindgen.Setter;
import org.bindgen.processor.GenerationQueue;
import org.bindgen.processor.Processor;
import org.bindgen.processor.util.BoundClass;
import org.bindgen.processor.util.Util;

import joist.sourcegen.Argument;
import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Copy;

/**
 * Generates a <code>XxxBinding</code> class for a given {@link TypeElement}.
 *
 * Two classes are generated: one class is an abstract
 * <code>XxxBindingPath</code> which has a generic parameter <code>R</code> to
 * present one part in a binding evaluation path rooted at type a type
 * <code>R</code>.
 *
 * The second class is the <code>XxxBinding</code> which extends its
 * <code>XxxBindingPath</code> but provides the type parameter <code>R</code> as
 * <code>Xxx</code>, meaning that <code>XxxBinding</code> can be used as the
 * starting point for binding paths rooted at a <code>Xxx</code>.
 */
public class BindingClassGenerator {

	private final GenerationQueue queue;
	private final TypeElement element;
	private final BoundClass name;
	private final List<String> foundSubBindings = new ArrayList<String>();
	private final Set<Element> sourceElements = new HashSet<Element>();

	private GClass pathBindingClass;
	private GClass rootBindingClass;

	public BindingClassGenerator(GenerationQueue queue, TypeElement element) {
		this.queue = queue;
		this.element = element;
		this.name = new BoundClass(element);
	}

	public void generate() {
		// initialize class declaration (*BindingPath)
		this.initializePathBindingClass();
		// managed at abstract level ; name provided at binding generation time
		// this.addGetName();
		// this.addGetType();

		// add all properties methods
		this.addProperties();
		// add getter that provides property list
		this.addGetChildBindings();

		// initialize class declaration (*Binding)
		this.initializeRootBindingClass();
		// add constructors
		this.addConstructors();
		// add getWithRoot method
		this.addGetWithRoot();
		// add getSafelyWithRoot method
		this.addGetSafelyWithRoot();

		this.addGeneratedTimestamp();
		this.addSerialVersionUID();
		this.saveCode(this.pathBindingClass);
		this.saveCode(this.rootBindingClass);
	}

	/**
	 * Initialize a BindingPath class for the current type; BindingPath is a
	 * root-less Binding.
	 */
	private void initializePathBindingClass() {
		// class name
		this.pathBindingClass = new GClass(this.name.getBindingPathClassDeclaration());
		// parent class; may be provided by configuration
		this.pathBindingClass.baseClassName(this.name.getBindingPathClassSuperClass());
		// TODO: try to generate code without warning
		this.pathBindingClass.addAnnotation("@SuppressWarnings(\"all\")");
		// add a protected no-args constructor
		this.pathBindingClass.getConstructor().setProtected();
		// add a complete constructor
		// - String name
		// - Class<?> type
		// - BindingRoot<P, R> parentBinding,
		// - Getter<P, T> getter
		// - Setter<P, T> setter
		// call parent constructor with same args

		final String rootTypeArgument = name.getRootTypeArgument();
		final String parentTypeArgument = name.getParentTypeArgument();

		this.pathBindingClass.getConstructor(
			new Argument(String.class.getName(), "name"),
			new Argument(String.format("%1$s<?>", Class.class.getName()), "type"),
			new Argument(
				String.format("%1$s<%2$s, %3$s>", BindingRoot.class.getName(), rootTypeArgument, parentTypeArgument),
				"parentBinding"),
			new Argument(
				String.format("%1$s<%3$s, %2$s>", Getter.class.getName(), this.name.get().toString(), parentTypeArgument),
				"getter"),
			new Argument(
				String.format("%1$s<%3$s, %2$s>", Setter.class.getName(), this.name.get().toString(), parentTypeArgument),
				"setter"))
			.setBody("super(name, type, parentBinding, getter, setter);\n");
		// another constructor without type argument
		this.pathBindingClass.getConstructor(
			new Argument(String.class.getName(), "name"),
			new Argument(
				String.format("%1$s<%2$s, %3$s>", BindingRoot.class.getName(), rootTypeArgument, parentTypeArgument),
				"parentBinding"),
			new Argument(
				String.format("%1$s<%3$s, %2$s>", Getter.class.getName(), this.name.get().toString(), parentTypeArgument),
				"getter"),
			new Argument(
				String.format("%1$s<%3$s, %2$s>", Setter.class.getName(), this.name.get().toString(), parentTypeArgument),
				"setter"))
		.setBody("super(name, parentBinding, getter, setter);\n");
		// same only with type
		this.pathBindingClass.getConstructor(
				new Argument(String.format("%1$s<?>", Class.class.getName()), "type"))
			.setBody("super(type);\n");
	}

	/**
	 * TypeBinding class declaration.
	 */
	private void initializeRootBindingClass() {
		this.rootBindingClass = new GClass(this.name.getBindingRootClassDeclaration());
		this.rootBindingClass.baseClassName(this.name.getBindingRootClassSuperClass());
		// TODO: try to generate code without warning
		this.rootBindingClass.addAnnotation("@SuppressWarnings(\"all\")");
	}

	/**
	 * getWithRoot() custom method implementation (return root instead of calling
	 * parent binding)
	 */
	private void addGetWithRoot() {
		GMethod getWithRoot = this.rootBindingClass.getMethod("getWithRoot").argument(this.name.get(), "root")
				.returnType(this.name.get());
		getWithRoot.body.line("return root;");
	}

	/**
	 * @see BindingClassGenerator#addGetWithRoot()
	 */
	private void addGetSafelyWithRoot() {
		GMethod getSafelyWithRoot = this.rootBindingClass.getMethod("getSafelyWithRoot")
				.argument(this.name.get(), "root").returnType(this.name.get());
		getSafelyWithRoot.body.line("return root;");
	}

	/**
	 * Add generation's time information
	 */
	private void addGeneratedTimestamp() {
		if (getConfig().skipGeneratedTimestamps()) {
			return;
		}
		String value = Processor.class.getName();
		String date = new SimpleDateFormat("dd MMM yyyy hh:mm").format(new Date());
		this.pathBindingClass.addImports(Generated.class);
		this.pathBindingClass.addAnnotation("@Generated(value = \"" + value + "\", date = \"" + date + "\")");
		this.rootBindingClass.addImports(Generated.class);
		this.rootBindingClass.addAnnotation("@Generated(value = \"" + value + "\", date = \"" + date + "\")");
	}

	/**
	 * Two constructors: root-less (noargs) or root-aware (one-arg); type is passed
	 * to parent constructor
	 */
	private void addConstructors() {
		this.rootBindingClass.getConstructor().body.line("super({}.class);\n", this.name.getTypeWithoutGenerics());
		GMethod constructor = this.rootBindingClass.getConstructor(this.name.get() + " value");
		constructor.body.line("super({}.class);", this.name.getTypeWithoutGenerics());
		constructor.body.line("this.set(value);");
	}

	/**
	 * Process all properties: propertyName() methods
	 */
	private void addProperties() {
		for (PropertyGenerator pg : this.getPropertyGenerators()) {
			pg.generate();
			this.enqueuePropertyTypeIfNeeded(pg);
			this.addToSubBindingsIfNeeded(pg);
		}
	}

	/**
	 * Add type to generation queue
	 */
	private void enqueuePropertyTypeIfNeeded(PropertyGenerator pg) {
		for (TypeElement te : pg.getPropertyTypeElements()) {
			if (te != null && getConfig().shouldGenerateBindingFor(te)) {
				this.queue.enqueueIfNew(te);
			}
		}
	}

	/**
	 * Add to subBindings (used to generate binding list)
	 */
	private void addToSubBindingsIfNeeded(PropertyGenerator pg) {
		if (pg.hasSubBindings()) {
			this.foundSubBindings.add(pg.getPropertyName());
		}
	}

	/**
	 * Add method that provides binding list
	 */
	private void addGetChildBindings() {
		this.pathBindingClass.addImports(Binding.class, List.class);
		GMethod children = this.pathBindingClass.getMethod("getChildBindings").returnType("List<Binding<?>>")
				.addAnnotation("@Override");
		children.body.line("List<Binding<?>> bindings = new java.util.ArrayList<Binding<?>>();");
		for (String foundSubBinding : this.foundSubBindings) {
			children.body.line("bindings.add(this.{}());", foundSubBinding);
		}
		children.body.line("return bindings;");
	}

	private void saveCode(GClass gc) {
		try {
			JavaFileObject jfo = getFiler().createSourceFile(gc.getFullName(),
					Copy.array(Element.class, Copy.list(this.sourceElements)));
			Writer w = jfo.openWriter();
			w.write(gc.toCode());
			w.close();
			this.queue.log("Saved " + gc.getFullName());
		} catch (IOException io) {
			getMessager().printMessage(Kind.ERROR, io.getMessage(), this.element);
		} catch (NullPointerException npe) {
			throw npe;
		}
	}

	private List<PropertyGenerator> getPropertyGenerators() {
		// factory ordering specifies binding precedence rules
		List<PropertyGenerator.GeneratorFactory> factories = new ArrayList<PropertyGenerator.GeneratorFactory>();
		// these bindings will not mangle their property names
		factories.add(new MethodPropertyGenerator.Factory(AccessorPrefix.NONE));
		factories.add(new MethodCallableGenerator.Factory());
		// these bindings will try to drop their prefix and use a shorter name
		// (e.g. getFoo -> foo)
		factories.add(new MethodPropertyGenerator.Factory(AccessorPrefix.GET));
		factories.add(new MethodPropertyGenerator.Factory(AccessorPrefix.HAS));
		factories.add(new MethodPropertyGenerator.Factory(AccessorPrefix.IS));
		// the field binding will use its name or append Field if it was already
		// taken by get/has/is
		factories.add(new FieldPropertyGenerator.Factory());

		Set<String> namesTaken = new HashSet<String>();
		namesTaken.add("getName");
		namesTaken.add("getPath");
		namesTaken.add("getType");
		namesTaken.add("getParentBinding");
		namesTaken.add("getChildBindings");

		List<Element> elements = this.getAccessibleElements();
		List<PropertyGenerator> generators = new ArrayList<PropertyGenerator>();

		for (PropertyGenerator.GeneratorFactory f : factories) {
			for (Iterator<Element> i = elements.iterator(); i.hasNext();) {
				Element enclosed = i.next();
				try {
					PropertyGenerator pg = f.newGenerator(this.pathBindingClass, this.name, this.element, enclosed,
							namesTaken);
					if (namesTaken.contains(pg.getPropertyName())) {
						continue;
					} else {
						namesTaken.add(pg.getPropertyName());
					}
					i.remove(); // element is handled, skip any further
								// generators
					generators.add(pg);
					this.sourceElements.add(enclosed);
				} catch (WrongGeneratorException e) {
					// try next
				}
			}
		}
		return generators;
	}

	private void addSerialVersionUID() {
		this.rootBindingClass.getField("serialVersionUID").type("long").setStatic().setFinal().initialValue("1L");
		this.pathBindingClass.getField("serialVersionUID").type("long").setStatic().setFinal().initialValue("1L");
	}

	private List<Element> getAccessibleElements() {
		List<Element> elements = new ArrayList<Element>();
		for (Element enclosed : getElementUtils().getAllMembers(this.element)) {
			if (Util.isAccessibleIfGenerated(this.element, enclosed)) {
				elements.add(enclosed);
			}
		}
		return elements;
	}
}
