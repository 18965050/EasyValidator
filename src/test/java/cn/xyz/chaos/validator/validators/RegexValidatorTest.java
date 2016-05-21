package cn.xyz.chaos.validator.validators;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.Valid;

/**
 * Created by mengfanjun on 14/12/10 010.
 */
public class RegexValidatorTest {

	@Test
	public void test_regex() {
		ValidContext validContext = Mockito.mock(ValidContext.class);
		Valid valid = Mockito.mock(Valid.class);
		Mockito.when(validContext.getValid()).thenReturn(valid);
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_REGEX)).thenReturn("\\d+");
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_FLAG)).thenReturn(null);
		RegexValidator regexValidator = new RegexValidator();
		Assert.assertTrue(regexValidator.isValid(10010, validContext));
		Assert.assertFalse(regexValidator.isValid("Abc", validContext));

		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_REGEX)).thenReturn("[a-z]+");
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_FLAG)).thenReturn(null);
		Assert.assertFalse(regexValidator.isValid(10010, validContext));
		Assert.assertTrue(regexValidator.isValid("abc", validContext));
		Assert.assertFalse(regexValidator.isValid("ABC", validContext));

		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_FLAG)).thenReturn("CASE_INSENSITIVE");
		Assert.assertTrue(regexValidator.isValid("ABC", validContext));
		Assert.assertTrue(regexValidator.isValid("abc", validContext));
	}
}
