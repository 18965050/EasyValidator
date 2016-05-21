package cn.xyz.chaos.validator.action;

import java.util.List;

import cn.xyz.chaos.validator.EasyFieldError;
import cn.xyz.chaos.validator.EasyValidatorUtilities;
import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.data.Action;

/**
 * Action校验执行器链
 * 
 * @author mfan
 */
public class ActionValidatorChain {

	public List<EasyFieldError> validator(EasyValidatorUtilities utilities, ValidContext validContext, Action action) {
		validContext.setAction(action);
		for (ActionValidator actionValidator : utilities.getContext().getActionValidators()) {
			if (actionValidator.supports(validContext.getAction())) {
				return actionValidator.validator(utilities, validContext, action, this);
			}
		}
		return null;
	}
}
