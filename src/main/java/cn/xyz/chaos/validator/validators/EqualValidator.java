package cn.xyz.chaos.validator.validators;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;

/**
 * 多值比较校验器，支持Number、Date、String型比较
 * 提供比较模式（mode）有：相等（EQ）、不相等（NE）、小于（LT）、小于等于（LE）、大于（GT）、大于等于（GE）
 * 参数（args）格式如：logPassword,logPassword1
 * 
 * @author mfan
 */
public class EqualValidator extends CompareValidator {
	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		attrs.put(XmlValidatorResolver.XML_ATT_FLAG, EQ);
		return super.isValid(object, validContext);
	}
}
