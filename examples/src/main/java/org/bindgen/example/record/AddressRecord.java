package org.bindgen.example.record;

import org.bindgen.Bindable;

@Bindable
public record AddressRecord(String city, String zipCode) {
}
