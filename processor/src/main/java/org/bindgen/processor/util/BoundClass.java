package org.bindgen.processor.util;

import static org.bindgen.processor.CurrentEnv.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;

import org.bindgen.processor.CurrentEnv;

import joist.util.Join;

/**
 * Given a TypeMirror type of a field/method property, provides information
 * about its binding outer/inner class.
 */
public class BoundClass {

	private final TypeElement element;
	private final ClassName name;
	private final String rootTypeArgument;
	private final String parentTypeArgument;

	public BoundClass(TypeElement element) {
		this.element = element;
		this.name = new ClassName(Util.boxIfNeeded(element.asType()).toString());
		this.parentTypeArgument = findAvailableTypeArgumentName(element, "P", "PARENT", "PARENT_TYPE");
		this.rootTypeArgument = findAvailableTypeArgumentName(element, "R", "ROOT", "ROOT_TYPE");
	}

	private String findAvailableTypeArgumentName(TypeElement element, String... suggestedNames) {
		if (suggestedNames.length < 1) {
			throw new IllegalArgumentException("suggested names should contain at least one element");
		}
		final LinkedHashSet<String> availableNames = new LinkedHashSet<>();
		availableNames.addAll(Arrays.asList(suggestedNames));
		final Set<String> usedNames = element.getTypeParameters()
			.stream()
			.map(TypeParameterElement::getSimpleName)
			.map(Name::toString)
			.collect(Collectors.toSet());
		availableNames.removeAll(usedNames);

		if (!availableNames.isEmpty()) {
			return availableNames.iterator().next();
		} else {
			String template = suggestedNames[0] + "_%d";
			int i=0;
			String result;
			while (usedNames.contains(result = String.format(template, i))) {
				i++;
			}
			return result;
		}
	}

	public boolean hasGenerics() {
		return !this.element.getTypeParameters().isEmpty();
	}

	public String getParentTypeArgument() {
		return parentTypeArgument;
	}

	public String getRootTypeArgument() {
		return rootTypeArgument;
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
		typeArgs.add(0, rootTypeArgument);
		typeArgs.add(1, parentTypeArgument);
		return this.getBindingClassName().getWithoutGenericPart() + "Path" + "<" + Join.commaSpace(typeArgs) + ">";
	}

	public String getBindingPathClassSuperClass() {
		return String.format("%s<%s, %s, %s>",
			CurrentEnv.getConfig().bindingPathSuperClassName(), rootTypeArgument, parentTypeArgument, this.name.get());
	}

	public String getBindingRootClassDeclaration() {
		if (this.name.getGenericsWithBounds().size() == 0) {
			return this.getBindingClassName().getWithoutGenericPart();
		} else {
			return this.getBindingClassName().getWithoutGenericPart() + "<"
					+ Join.commaSpace(this.name.getGenericsWithBounds()) + ">";
		}
	}

	public String getBindingRootClassSuperClass() {
		List<String> typeArgs = this.name.getGenericsWithoutBounds();
		typeArgs.add(0, this.get());
		typeArgs.add(1, this.get());
		return this.getBindingClassName().getWithoutGenericPart() + "Path" + "<" + Join.commaSpace(typeArgs) + ">";
	}

	public String getTypeWithoutGenerics() {
		// may include OuterType: example OuterType.InnerType
		return this.name.getWithoutGenericPart();
	}

	/**
	 * @return "com.app.Type<String, String>" if the type is "com.app.Type<String,
	 *         String>"
	 */
	public String get() {
		return this.name.get();
	}

	/**
	 * @return "com.app.Type<String, String>" if the type is "com.app.Type<String,
	 *         String>"
	 */
	@Override
	public String toString() {
		return this.name.get();
	}

}
