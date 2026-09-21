package org.bindgen.example.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.bindgen.Binding;
import org.junit.jupiter.api.Test;

public class EmployeeTest {

	@Test
	public void testEmployee() {
		Employee e = new Employee();
		e.name = "bob";
		e.department = "accounting";

		EmployeeBinding eb = new EmployeeBinding(e);
		assertEquals("bob", new TextBox(eb.name()).toString());
		assertEquals("accounting", new TextBox(eb.department()).toString());

		assertEquals("name", new TextBox(eb.name()).getName());
		assertEquals("department", new TextBox(eb.department()).getName());
	}

	@Test
	public void testEmployer() {
		Employer e = new Employer();
		e.name = "at&t";

		EmployerBinding eb = new EmployerBinding(e);
		assertEquals("at&t", new TextBox(eb.name()).toString());
		assertEquals("name", new TextBox(eb.name()).getName());
	}

	@Test
	public void testEmployerThroughEmployee() {
		Employer er = new Employer();
		er.name = "at&t";

		Employee ee = new Employee();
		ee.name = "bob";
		ee.department = "accounting";
		ee.employer = er;

		EmployeeBinding eb = new EmployeeBinding(ee);
		// Assert.assertTrue(StringBinding.class.isAssignableFrom(eb.name().getClass()));

		// Simulate page rendering
		assertEquals("bob", new TextBox(eb.name()).toString());
		assertEquals("accounting", new TextBox(eb.department()).toString());
		assertEquals("at&t", new TextBox(eb.employer().name()).toString());

		assertEquals("employer", new TextBox(eb.employer()).getName());

		// Simulate form POST processing
		new TextBox(eb.name()).set("newBob");
		new TextBox(eb.employer().name()).set("newAt&t");
		assertEquals("newBob", ee.name);
		assertEquals("newAt&t", er.name);
	}

	@Test
	public void testEmployerThroughEmployeeStateless() {
		Employer er = new Employer();
		er.name = "at&t";
		Employee ee = new Employee();
		ee.employer = er;

		EmployeeBinding eb = new EmployeeBinding(); // no instance set
		assertEquals("at&t", eb.employer().name().getWithRoot(ee));
	}

	@Test
	public void testEmployerThroughEmployeeStatelessSafely() {
		Employee ee = new Employee();
		ee.employer = null; // leave null

		EmployeeBinding eb = new EmployeeBinding(); // no instance set
		assertNull(eb.employer().name().getSafelyWithRoot(ee));

		assertNull(eb.employer().name().getSafelyWithRoot(null));
	}

	@Test
	public void testSetEmployer() {
		Employer er1 = new Employer();
		er1.name = "at&t";

		Employer er2 = new Employer();
		er2.name = "exigence";

		Employee ee = new Employee();
		ee.employer = er1;

		EmployeeBinding eb = new EmployeeBinding(ee);
		assertEquals("at&t", new TextBox(eb.employer().name()).toString());

		eb.employer().set(er2);
		assertEquals("exigence", new TextBox(eb.employer().name()).toString());
	}

	@Test
	public void testDelayedEmployee() {
		Employee e1 = new Employee("bob");
		Employee e2 = new Employee("fred");

		EmployeeBinding eb = new EmployeeBinding();
		TextBox tb = new TextBox(eb.name());

		eb.set(e1);
		assertEquals("bob", tb.toString());

		eb.set(e2);
		assertEquals("fred", tb.toString());
	}

	@Test
	public void testEmployerNameWhenNullFails() {
		Employee ee = new Employee("bob");
		EmployeeBinding eb = new EmployeeBinding(ee);

		Binding<String> erName = eb.employer().name();
		try {
			erName.get();
			fail();
		} catch (NullPointerException npe) {
		}

		try {
			erName.set("at&t");
			fail();
		} catch (NullPointerException npe) {
		}
	}

	public static class TextBox {
		Binding<Object> binding;

		@SuppressWarnings("unchecked")
		public TextBox(Binding<?> binding) {
			this.binding = (Binding<Object>) binding;
		}

		public String getName() {
			return this.binding.getName();
		}

		@Override
		public String toString() {
			return this.binding.get().toString();
		}

		public void set(String value) {
			this.binding.set(value);
		}
	}

}
