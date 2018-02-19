package org.bindgen.processor.circular;

import org.bindgen.Bindable;

@Bindable
public class SelectedIndividual {

	@SuppressWarnings("unused")
	private Individual<?> individual;

	public Organisation<?, ?> getOrganisation() {
		return null;
	}
}
