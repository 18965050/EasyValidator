package cn.xyz.chaos.validator.action;

import java.util.Arrays;
import java.util.List;

import cn.xyz.chaos.validator.EasyFieldError;
import cn.xyz.chaos.validator.EasyValidatorUtilities;
import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.data.Action;
import cn.xyz.chaos.validator.data.Field;
import cn.xyz.chaos.validator.data.FieldAction;
import cn.xyz.chaos.validator.data.Valid;
import cn.xyz.chaos.validator.utils.beans.BeanUtils;
import cn.xyz.chaos.validator.validators.Validator;

/**
 * <pre>
 * 字段校验器.一个字段对应多个Valid
 * </pre>
 * 
 * @author lvchenggang
 *
 */
public class FieldActionValidator implements ActionValidator<FieldAction> {

	@Override
	public List<EasyFieldError> validator(EasyValidatorUtilities utilities, ValidContext validContext,
			FieldAction action, ActionValidatorChain chain) {
		Field field = action.getField();
		List<Valid> valids = field.getValids();
		for (Valid valid : valids) {
			Validator validator = utilities.getContext().getValidator(valid.getName());
			if (validator != null) {
				validContext.setValid(valid);
				// 验证值
				Object object = BeanUtils.getProperty(validContext.getTarget(), field.getProperty());
				if (!validator.isValid(object, validContext)) {
					EasyFieldError error = new EasyFieldError(field.getProperty(), valid.getMsg(), valid.attrs());
					validContext.getErrors().add(error);
					return Arrays.asList(error);
				}
			}
		}
		return null;
	}

	@Override
	public boolean supports(Action action) {
		return action instanceof FieldAction;
	}

}
