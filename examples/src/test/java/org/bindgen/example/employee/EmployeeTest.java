package org.bindgen.example.employee;

import org.bindgen.Binding;
import org.junit.Assert;

import junit.framework.TestCase;

public class EmployeeTest extends TestCase {

	public void testEmployee() {
		Employee e = new Employee();
		e.name = "bob";
		e.department = "accounting";

		EmployeeBinding eb = new EmployeeBinding(e);
		Assert.assertEquals("bob", new TextBox(eb.name()).toString());
		Assert.assertEquals("accounting", new TextBox(eb.department()).toString());

		Assert.assertEquals("name", new TextBox(eb.name()).getName());
		Assert.assertEquals("department", new TextBox(eb.department()).getName());
	}

	public void testEmployer() {
		Employer e = new Employer();
		e.name = "at&t";

		EmployerBinding eb = new EmployerBinding(e);
		Assert.assertEquals("at&t", new TextBox(eb.name()).toString());
		Assert.assertEquals("name", new TextBox(eb.name()).getName());
	}

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
		Assert.assertEquals("bob", new TextBox(eb.name()).toString());
		Assert.assertEquals("accounting", new TextBox(eb.department()).toString());
		Assert.assertEquals("at&t", new TextBox(eb.employer().name()).toString());

		Assert.assertEquals("employer", new TextBox(eb.employer()).getName());

		// Simulate form POST processing
		new TextBox(eb.name()).set("newBob");
		new TextBox(eb.employer().name()).set("newAt&t");
		Assert.assertEquals("newBob", ee.name);
		Assert.assertEquals("newAt&t", er.name);
	}

	public void testEmployerThroughEmployeeStateless() {
		Employer er = new Employer();
		er.name = "at&t";
		Employee ee = new Employee();
		ee.employer = er;

		EmployeeBinding eb = new EmployeeBinding(); // no instance set
		Assert.assertEquals("at&t", eb.employer().name().getWithRoot(ee));
	}

	public void testEmployerThroughEmployeeStatelessSafely() {
		Employee ee = new Employee();
		ee.employer = null; // leave null

		EmployeeBinding eb = new EmployeeBinding(); // no instance set
		Assert.assertEquals(null, eb.employer().name().getSafelyWithRoot(ee));

		Assert.assertEquals(null, eb.employer().name().getSafelyWithRoot(null));
	}

	public void testSetEmployer() {
		Employer er1 = new Employer();
		er1.name = "at&t";

		Employer er2 = new Employer();
		er2.name = "exigence";

		Employee ee = new Employee();
		ee.employer = er1;

		EmployeeBinding eb = new EmployeeBinding(ee);
		Assert.assertEquals("at&t", new TextBox(eb.employer().name()).toString());

		eb.employer().set(er2);
		Assert.assertEquals("exigence", new TextBox(eb.employer().name()).toString());
	}

	public void testDelayedEmployee() {
		Employee e1 = new Employee("bob");
		Employee e2 = new Employee("fred");

		EmployeeBinding eb = new EmployeeBinding();
		TextBox tb = new TextBox(eb.name());

		eb.set(e1);
		Assert.assertEquals("bob", tb.toString());

		eb.set(e2);
		Assert.assertEquals("fred", tb.toString());
	}

	public void testEmployerNameWhenNullFails() {
		Employee ee = new Employee("bob");
		EmployeeBinding eb = new EmployeeBinding(ee);

		Binding<String> erName = eb.employer().name();
		try {
			erName.get();
			Assert.fail();
		} catch (NullPointerException npe) {
		}

		try {
			erName.set("at&t");
			Assert.fail();
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
