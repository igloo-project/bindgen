import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ClassInDefaultPackageTest {
	@Test
	public void testClass() {
		ClassInDefaultPackage c = new ClassInDefaultPackage();
		ClassInDefaultPackageBinding b = new ClassInDefaultPackageBinding(c);
		b.name().set("c");
		assertEquals("c", c.name);
	}

}
