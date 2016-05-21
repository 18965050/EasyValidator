package cn.xyz.chaos.validator.validators;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.Valid;

/**
 * Created by mengfanjun on 14/12/10 010.
 */
public class MaxValidatorTest {

	@Test
	public void test_max() {
		ValidContext validContext = Mockito.mock(ValidContext.class);
		Valid valid = Mockito.mock(Valid.class);
		Mockito.when(validContext.getValid()).thenReturn(valid);
		Mockito.when(valid.attr(XmlValidatorResolver.XML_ATT_MAX)).thenReturn("5");
		MaxValidator maxValidator = new MaxValidator();
		Assert.assertTrue(maxValidator.isValid(4, validContext));
		Assert.assertTrue(maxValidator.isValid(5, validContext));
		Assert.assertFalse(maxValidator.isValid(6, validContext));

		Assert.assertTrue(maxValidator.isValid(4L, validContext));
		Assert.assertTrue(maxValidator.isValid(5L, validContext));
		Assert.assertFalse(maxValidator.isValid(6L, validContext));

		Assert.assertTrue(maxValidator.isValid(new BigDecimal(4), validContext));
		Assert.assertTrue(maxValidator.isValid(new BigDecimal(5), validContext));
		Assert.assertFalse(maxValidator.isValid(new BigDecimal(6), validContext));

	}
}
