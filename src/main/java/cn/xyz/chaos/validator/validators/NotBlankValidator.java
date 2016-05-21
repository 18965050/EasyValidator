package cn.xyz.chaos.validator.validators;

import cn.xyz.chaos.validator.ValidContext;

public class NotBlankValidator extends AbstractValidator {

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		if (object == null) {
			return true;
		}
		return String.valueOf(object).trim().length() > 0;
	}

}
