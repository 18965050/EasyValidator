package cn.xyz.chaos.validator.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xyz.chaos.validator.EasyFieldError;
import cn.xyz.chaos.validator.EasyValidatorUtilities;
import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.data.Action;

/**
 * Action校验执行器
 * 
 * @author mfan
 */
public interface ActionValidator<T extends Action> {
	Logger logger = LoggerFactory.getLogger(ActionValidator.class);

	boolean supports(Action action);

	/**
	 * 校验执行
	 * @param utilities
	 * @param validContext
	 * @param action
	 * @param chain
	 * @return
	 */
	List<EasyFieldError> validator(EasyValidatorUtilities utilities, ValidContext validContext, T action,
			ActionValidatorChain chain);
}
