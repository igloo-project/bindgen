package org.bindgen.processor.circular;

import java.util.List;
import javax.annotation.Generated;
import org.bindgen.Binding;
import org.bindgen.BindingRoot;
import org.bindgen.binding.AbstractReflectBinding;

@SuppressWarnings("all")
@Generated(value = "org.bindgen.processor.Processor", date = "13 sept. 2016 12:16")
public class SelectedIndividualBindingPath<R> extends AbstractReflectBinding<R, SelectedIndividual> {

    private AbstractReflectBinding<R, Integer> hashCodeBinding;
    private AbstractReflectBinding<R, String> toStringBinding;
    private OrganisationBindingPath<R, ?, ?> organisation;
    private static final long serialVersionUID = 1L;

    public SelectedIndividualBindingPath() {
    }

    public SelectedIndividualBindingPath(BindingRoot<R, ?> parentBinding, Class<?> parentType, Class<SelectedIndividual> targetType, String propertyName, String fieldName, String getMethod, String setMethod) {
        super(parentBinding, parentType, targetType, fieldName, propertyName, getMethod, setMethod);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Class<?> getType() {
        return org.bindgen.processor.circular.SelectedIndividual.class;
    }

    public AbstractReflectBinding<R, Integer> hashCodeBinding() {
        if (this.hashCodeBinding == null) {
            this.hashCodeBinding = new org.bindgen.binding.AbstractReflectBinding<R, java.lang.Integer>(this, (Class<org.bindgen.processor.circular.SelectedIndividual>)(Object) org.bindgen.processor.circular.SelectedIndividual.class, (Class<java.lang.Integer>)(Object)java.lang.Integer.class, "hashCodeBinding", null, "hashCode", null);
        }
        return this.hashCodeBinding;
    }

    public AbstractReflectBinding<R, String> toStringBinding() {
        if (this.toStringBinding == null) {
            this.toStringBinding = new org.bindgen.binding.AbstractReflectBinding<R, java.lang.String>(this, (Class<org.bindgen.processor.circular.SelectedIndividual>)(Object) org.bindgen.processor.circular.SelectedIndividual.class, (Class<java.lang.String>)(Object)java.lang.String.class, "toStringBinding", null, "toString", null);
        }
        return this.toStringBinding;
    }

    public <U0 extends org.bindgen.processor.circular.Individual<U1>, U1 extends org.bindgen.processor.circular.Organisation<U0, U1>> OrganisationBindingPath<R, ?, ?> organisation() {
        if (this.organisation == null) {
            this.organisation = new org.bindgen.processor.circular.OrganisationBindingPath<R, U0, U1>(this, (Class<org.bindgen.processor.circular.SelectedIndividual>)(Object) org.bindgen.processor.circular.SelectedIndividual.class, (Class<org.bindgen.processor.circular.Organisation<U0 extends org.bindgen.processor.circular.Individual<U1>, U1 extends org.bindgen.processor.circular.Organisation<U0, U1>>>)(Object)org.bindgen.processor.circular.Organisation.class, "organisation", null, "getOrganisation", null);
        }
        return this.organisation;
    }

    @Override
    public List<Binding<?>> getChildBindings() {
        List<Binding<?>> bindings = new java.util.ArrayList<Binding<?>>();
        bindings.add(this.hashCodeBinding());
        bindings.add(this.toStringBinding());
        bindings.add(this.organisation());
        return bindings;
    }

}
