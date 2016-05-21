package cn.xyz.chaos.validator.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xyz.chaos.validator.EasyFieldError;
import cn.xyz.chaos.validator.EasyValidatorUtilities;
import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.data.Action;
import cn.xyz.chaos.validator.data.ScriptAction;
import cn.xyz.chaos.validator.utils.JsUtils;

public class ScriptActionValidator implements ActionValidator<ScriptAction> {

	@Override
	public List<EasyFieldError> validator(EasyValidatorUtilities utilities, ValidContext validContext,
			ScriptAction action, ActionValidatorChain chain) {
		List<EasyFieldError> errors = new ArrayList<EasyFieldError>();
		try {
			// js执行上下文
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("target", validContext.getTarget());
			map.put("ctx", validContext);
			map.put("utils", utilities);
			Boolean result = (Boolean) JsUtils.execJs(action.getExpression(), map);
			if (result == Boolean.TRUE) {
				for (Action act : action.getActions()) {
					List<EasyFieldError> error = chain.validator(utilities, validContext, act);
					if (error != null && !error.isEmpty()) {
						errors.addAll(error);
					}
				}
			}
		} catch (Exception e) {
			logger.error("s", e);
		}
		return errors;
	}

	@Override
	public boolean supports(Action action) {
		return action instanceof ScriptAction;
	}

}
