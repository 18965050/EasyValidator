package cn.xyz.chaos.validator.validators;

import java.math.BigDecimal;
import java.math.BigInteger;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.utils.Assert;
import cn.xyz.chaos.validator.utils.StringUtils;

/**
 * Check that the number being validated is greater than or equal to the minimum
 * value specified.
 * Check that the character sequence (e.g. string) being validated represents a number, and has a value
 * more than or equal to the minimum value specified.
 * 
 * @author mfan
 */
public class MinValidator extends AbstractValidator {

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		// null values are valid
		if (object == null) {
			return true;
		}
		String xmlMin = attr(validContext.getValid(), XmlValidatorResolver.XML_ATT_MIN);
		Assert.isTrue(StringUtils.isNotBlank(xmlMin), "The min must not be null");
		long minValue = Long.parseLong(xmlMin);
		if (object instanceof BigDecimal) {
			return ((BigDecimal) object).compareTo(BigDecimal.valueOf(minValue)) != -1;
		} else if (object instanceof BigInteger) {
			return ((BigInteger) object).compareTo(BigInteger.valueOf(minValue)) != -1;
		} else if (object instanceof Number) {
			long longValue = ((Number) object).longValue();
			return longValue >= minValue;
		} else if (object instanceof CharSequence) {
			try {
				return new BigDecimal(object.toString()).compareTo(BigDecimal.valueOf(minValue)) != -1;
			} catch (NumberFormatException nfe) {
				return false;
			}
		}
		return false;
	}
}
