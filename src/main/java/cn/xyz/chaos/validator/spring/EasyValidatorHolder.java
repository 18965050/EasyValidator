package cn.xyz.chaos.validator.spring;

import java.lang.reflect.Method;

import cn.xyz.chaos.validator.ValidEasy;

public class EasyValidatorHolder {
	private final Method	method;
	private final Class<?>	parameterType;
	private final ValidEasy validEasy;

	public EasyValidatorHolder(Method method, Class<?> parameterType, ValidEasy validEasy) {
		super();
		this.method = method;
		this.parameterType = parameterType;
		this.validEasy = validEasy;
	}

	public Method getMethod() {
		return method;
	}

	public Class<?> getParameterType() {
		return parameterType;
	}

	public ValidEasy getValidEasy() {
		return validEasy;
	}

}
