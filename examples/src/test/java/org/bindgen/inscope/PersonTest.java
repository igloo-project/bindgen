package org.bindgen.inscope;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class PersonTest {

	@Test
	public void testExists() {
		assertNotNull(this.newInstance(AddressInBinding.class));
		assertNotNull(this.newInstance(HouseInBinding.class));
		assertNotNull(this.newInstance(CarInBinding.class));
	}

	private Object newInstance(Class<?> type) {
		try {
			return type.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
  }

}
