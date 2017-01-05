package org.bindgen.processor.circular;

import javax.annotation.Generated;

@SuppressWarnings("all")
@Generated(value = "org.bindgen.processor.Processor", date = "13 sept. 2016 12:16")
public class SelectedIndividualBinding extends SelectedIndividualBindingPath<SelectedIndividual> {

    private static final long serialVersionUID = 1L;

    public SelectedIndividualBinding() {
    }

    public SelectedIndividualBinding(SelectedIndividual value) {
        this.set(value);
    }

    public SelectedIndividual getWithRoot(SelectedIndividual root) {
        return root;
    }

    public SelectedIndividual getSafelyWithRoot(SelectedIndividual root) {
        return root;
    }

}
