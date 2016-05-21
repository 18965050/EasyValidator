package cn.xyz.chaos.validator.validators;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.utils.Assert;
import cn.xyz.chaos.validator.utils.StringUtils;

/**
 * character sequence
 * array
 * Collection
 * Map
 * 
 * @author mengfanjun
 * 
 */
public class SizeValidator extends AbstractValidator {

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		if (object == null) {
			return true;
		}
		String xmlMin = attr(validContext.getValid(), XmlValidatorResolver.XML_ATT_MIN);
		Assert.isTrue(StringUtils.isNotBlank(xmlMin), "The min must not be null");
		String xmlMax = attr(validContext.getValid(), XmlValidatorResolver.XML_ATT_MAX);
		Assert.isTrue(StringUtils.isNotBlank(xmlMax), "The max must not be null");
		int min = Integer.parseInt(xmlMin);
		int max = Integer.parseInt(xmlMax);
		validateParameters(min, max);
		int size = 0;
		if (object instanceof CharSequence) {
			size = ((CharSequence) object).length();
		} else if (object.getClass().isArray()) {
			size = Array.getLength(object);
		} else if (object instanceof Collection<?>) {
			size = ((Collection) object).size();
		} else if (object instanceof Map<?, ?>) {
			size = ((Map) object).size();
		} else {
			return false;
		}
		return (size >= min) && (size <= max);
	}

	private void validateParameters(int min, int max) {
		if ((min < 0) || (max < min)) {
			throw new IllegalArgumentException(String.format("参数错误：min=$d, max=$d", min, max));
		}
	}
}
