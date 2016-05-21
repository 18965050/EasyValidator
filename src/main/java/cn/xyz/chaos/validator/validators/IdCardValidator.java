package cn.xyz.chaos.validator.validators;

import cn.xyz.chaos.validator.ValidContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class IdCardValidator extends AbstractValidator {

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		if (object == null) {
			return true;
		}
		// TODO 待完善
		throw new NotImplementedException();
	}

}
