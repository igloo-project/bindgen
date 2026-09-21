package org.bindgen.example.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

public class AccessTest {

	@Test
	public void testPackageAccess() throws Exception {
		Method m = BeanBindingPath.class.getDeclaredMethod("packageField");
		assertEquals(false, Modifier.isPublic(m.getModifiers()));
		assertEquals(false, Modifier.isPrivate(m.getModifiers()));
		assertEquals(false, Modifier.isProtected(m.getModifiers()));
	}

	@Test
	public void testProtectedAccess() throws Exception {
		Method m = BeanBindingPath.class.getDeclaredMethod("protectedField");
		assertEquals(true, Modifier.isProtected(m.getModifiers()));
	}

	@Test
	public void testPublicAccess() throws Exception {
		Method m = BeanBindingPath.class.getDeclaredMethod("publicField");
		assertEquals(true, Modifier.isPublic(m.getModifiers()));
	}

	@Test
	public void testPrivateDoesNotGetExposed() throws Exception {
		try {
			BeanBindingPath.class.getDeclaredMethod("privateField");
			fail();
		} catch (NoSuchMethodException nsme) {
			// expected
		}
	}

}
