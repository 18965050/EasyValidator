package cn.xyz.chaos.validator.validators;

import java.util.Date;

import cn.xyz.chaos.validator.ValidContext;

public class FutureValidator extends AbstractValidator {

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		if (object == null) {
			return true;
		}
		return ((Date) object).after(new Date());
	}

}
