package org.bindgen.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.bindgen.example.Parents.Foo;
import org.bindgen.example.Parents.FooChild;
import org.bindgen.example.parents.FooBinding;
import org.bindgen.example.parents.FooChildBinding;
import org.junit.jupiter.api.Test;

public class ParentBindingTest {

	@Test
	public void testParentBindingIsNullByDefault() {
		FooBinding b = new FooBinding();
		assertNull(b.getParentBinding());
	}

	@Test
	public void testParentBindingOfFieldProperty() {
		FooBinding b = new FooBinding();
		assertSame(b, b.bar().getParentBinding());
	}

	@Test
	public void testParentBindingOfMethodProperty() {
		FooBinding b = new FooBinding();
		assertSame(b, b.baz().getParentBinding());
	}

	@Test
	public void testToString() {
		FooChildBinding fcb = new FooChildBinding();
		assertEquals("FooChildBinding(null)", fcb.toString());
		assertEquals("FooChildBinding(null).foo()", fcb.foo().toString());
		assertEquals("FooChildBinding(null).foo().baz()", fcb.foo().baz().toString());

		// Now set FooChild
		fcb.set(new FooChild());
		assertEquals("FooChildBinding(child)", fcb.toString());
		assertEquals("FooChildBinding(child).foo(null)", fcb.foo().toString());
		assertEquals("FooChildBinding(child).foo(null).baz()", fcb.foo().baz().toString());

		// No set Foo
		fcb.get().foo = new Foo();
		assertEquals("FooChildBinding(child)", fcb.toString());
		assertEquals("FooChildBinding(child).foo(foo)", fcb.foo().toString());
		assertEquals("FooChildBinding(child).foo(foo).baz(baz)", fcb.foo().baz().toString());
	}

	@Test
	public void testGetPath() {
		FooChildBinding fcb = new FooChildBinding();
		assertEquals("#root", fcb.getPath());
		assertEquals("foo", fcb.foo().getPath());
		assertEquals("foo.baz", fcb.foo().baz().getPath());
	}
}
