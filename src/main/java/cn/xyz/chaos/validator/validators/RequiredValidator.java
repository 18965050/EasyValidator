/**
 *
 */
package cn.xyz.chaos.validator.validators;

import cn.xyz.chaos.validator.ValidContext;

/**
 * NotNull && NotBlank
 * 
 * @author mfan
 * @since 2009-8-20
 */
public class RequiredValidator extends AbstractValidator {

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		if ((object == null) || (object.toString().trim().length() == 0)) {
			return false;
		}
		return true;
	}
}
