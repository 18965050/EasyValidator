package cn.xyz.chaos.validator.validators;

import org.junit.Assert;
import org.junit.Test;

public class RequiredValidatorTest {

	private RequiredValidator	requiredValidator	= new RequiredValidator();

	@Test
	public void test_isValid_null() {
		boolean valid = requiredValidator.isValid(null, null);
		Assert.assertEquals(valid, false);
	}

	@Test
	public void test_isValid_empty() {
		boolean valid = requiredValidator.isValid("", null);
		Assert.assertEquals(valid, false);
	}

	@Test
	public void test_isValid_blank() {
		boolean valid = requiredValidator.isValid(" ", null);
		Assert.assertEquals(valid, false);
	}

	@Test
	public void test_isValid() {
		boolean valid = requiredValidator.isValid("xyz ", null);
		Assert.assertEquals(valid, true);
	}
}
