package org.bindgen.example.record;

import org.bindgen.Bindable;

@Bindable
public record PersonRecord(String name, int age, AddressRecord address) {
}
