package cn.xyz.chaos.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解名称必须以Valid开头以便SpringValidator识别调用验证
 * 
 * @author mfan
 */

/**
 * 参见 {@link org.springframework.web.method.annotation.ModelAttributeMethodProcessor#validateIfApplicable}
 * @author lvchenggang
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEasy {

	/**
	 * <pre>
	 * 分组名称集合.多个分组中的规则叠加
	 * 未配置时使用方法名作为分组名称
	 * </pre>
	 * 
	 * @return
	 */
	String[]value() default {};
}
