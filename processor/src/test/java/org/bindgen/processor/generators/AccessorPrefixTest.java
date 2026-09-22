package org.bindgen.processor.generators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AccessorPrefixTest {

	@Test
	public void testMatches() {
    assertTrue(AccessorPrefix.GET.matches("getFoo"));
    assertFalse(AccessorPrefix.GET.matches("getfoo"));
    assertFalse(AccessorPrefix.GET.matches("foo"));
    
    assertTrue(AccessorPrefix.NONE.matches("foo"));
    assertFalse(AccessorPrefix.NONE.matches("getFoo"));
    assertFalse(AccessorPrefix.NONE.matches("isFoo"));
    assertFalse(AccessorPrefix.NONE.matches("hasFoo"));
	}

}
