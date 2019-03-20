package org.bindgen.processor.generators;

import static org.bindgen.processor.CurrentEnv.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Set;
import java.util.TreeSet;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Join;

import org.bindgen.Binding;
import org.bindgen.processor.GenerationQueue;
import org.bindgen.processor.Processor;
import org.bindgen.processor.util.BoundClass;
import org.bindgen.processor.util.ClassName;
import org.bindgen.processor.util.Util;

/** Generates a BindKeyword class with "bind" static helper methods.
 *
 * The idea is that by static importing <code>BindKeyword.bind</code>,
 * client code could use the short cut <code>bind(someInstance)</code>
 * to get a {@link Binding} for that object without typing out the full
 * <code>new SomeInstanceBinding(someInstance)</code>.
 *
 * This requires some gymnastics because in the Eclipse environment,
 * the {@link Processor} instance is recreated for
 * each save/processing cycle. So we have to cache all of the bindings
 * we've seen on the file system we can persist them across processor
 * instances. 
 */
public class BindKeywordGenerator {

	private static final String PACKAGE_NAME = "org.bindgen";
	private static final String CLASS_NAME = "BindKeyword";
	private final GenerationQueue queue;
	private final GClass bindClass = new GClass(PACKAGE_NAME + "." + CLASS_NAME);
	private final Set<String> classNames = new TreeSet<String>();

	/** @param queue the {@link GenerationQueue} only used for logging */
	public BindKeywordGenerator(GenerationQueue queue) {
		this.queue = queue;
	}

	/** Adds/updates <code>bind</code> methods to the <code>BindKeyword</code> class for each of the <code>newlyWritten</code> class names. */
	public void generate(Set<String> newlyWritten) {
		this.readClassNamesFromBindKeywordFileIfExists();
		this.classNames.addAll(newlyWritten);
		this.addBindMethods();
		this.addSuppressWarnings();
		this.writeBindKeywordFile();
		this.writeBindKeywordClass();
	}

	private void addSuppressWarnings() {
		this.bindClass.addAnnotation("@SuppressWarnings(\"all\")");
	}

	private void addBindMethods() {
		for (String className : this.classNames) {
			TypeElement e = getElementUtils().getTypeElement(className);
			if (e == null) {
				continue;
			}
			if (getElementUtils().getPackageOf(e).isUnnamed()) {
				continue;
			}
			this.addBindMethod(className, (DeclaredType) e.asType());
		}
	}

	private void addBindMethod(String className, DeclaredType type) {
		ClassName bindingType = new BoundClass((TypeElement) getTypeUtils().asElement(type)).getBindingClassName();
		this.queue.log("Adding " + className + ", " + type + ", " + bindingType.get());
		if (type.getTypeArguments().size() > 0) {
			GMethod method = this.bindClass.getMethod("bind({}<{}> o)", className, Join.commaSpace(bindingType.getGenericPartWithoutBrackets()));
			method.returnType("{}", bindingType);
			method.typeParameters(Join.commaSpace(new ClassName(Util.getTypeName(type)).getGenericsWithBounds()));
			method.setStatic();
			method.body.line("return new {}(o);", bindingType);
		} else {
			GMethod method = this.bindClass.getMethod("bind({} o)", className);
			method.returnType(bindingType.toString());
			method.setStatic();
			method.body.line("return new {}(o);", bindingType);
		}
	}

	/** Finds class names cached in <code>SOURCE_OUTPUT/BindKeyword.txt</code>, if it exists, and adds them to <code>this.classNames</code>. */
	private void readClassNamesFromBindKeywordFileIfExists() {
		try {
			this.queue.log("READING " + CLASS_NAME + ".txt");
			FileObject fo = getFiler().getResource(StandardLocation.SOURCE_OUTPUT, PACKAGE_NAME, CLASS_NAME + ".txt");
			if (fo.getLastModified() > 0) {
				String line;
				BufferedReader input = new BufferedReader(new InputStreamReader(fo.openInputStream()));
				while ((line = input.readLine()) != null) {
					this.classNames.add(line);
				}
				input.close();
				this.queue.log("WAS THERE");
			} else {
				this.queue.log("NOT THERE");
			}
		} catch (IOException io) {
			getMessager().printMessage(Kind.ERROR, io.getMessage());
		}
	}

	private void writeBindKeywordFile() {
		try {
			this.queue.log("WRITING " + CLASS_NAME + ".txt");
			FileObject fo = getFiler().createResource(StandardLocation.SOURCE_OUTPUT, PACKAGE_NAME, CLASS_NAME + ".txt");
			OutputStream output = fo.openOutputStream();
			for (String className : this.classNames) {
				output.write(className.getBytes());
				output.write("\n".getBytes());
			}
			output.close();
		} catch (IOException io) {
			this.queue.log("ERROR: " + io.getMessage());
			getMessager().printMessage(Kind.ERROR, io.getMessage());
		}
	}

	private void writeBindKeywordClass() {
		try {
			this.queue.log("WRITING " + CLASS_NAME + ".java");
			JavaFileObject jfo = getFiler().createSourceFile(this.bindClass.getFullName());
			Writer w = jfo.openWriter();
			w.write(this.bindClass.toCode());
			w.close();
		} catch (IOException io) {
			this.queue.log("ERROR: " + io.getMessage());
			getMessager().printMessage(Kind.ERROR, io.getMessage());
		}
	}

}
