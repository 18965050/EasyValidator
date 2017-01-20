package cn.xyz.chaos.validator.validators;

import java.util.Map;

import cn.xyz.chaos.validator.ValidContext;

/**
 * Created by lvchenggang on 14/12/9 009.
 */
public interface Validator {

	/**
	 * 获取名称
	 * 
	 * @return 校验器名称
	 */
	String getName();

	void setName(String name);

	void attrs(Map<String, String> attrs);

	/**
	 * 验证
	 * 
	 * @param object 待验证值
	 * @param validContext 校验上下文
	 * @return if isValid ok is true, other return false
	 */
	boolean isValid(Object object, ValidContext validContext);
}
