# EasyValidator

## 缘由
SpringMVC中数据校验, 我们往往采用JSR-303结合Hibernate Validation实现来进行数据校验. 

JSR-303提供的默认注解是 javax.validation.Valid, 但这个注解有个缺陷, 不支持分组. SpringMVC注意到了这个问题, 提供了另一个注解 org.springframework.validation.annotation.Validated, 使用value注解属性来进行分组. 并结合Hibernate的验证注解来实现对数据的校验. 

但我们在实际使用中, 发现存在有如下的问题:
- 分组不够明晰. @Validated注解value类型为Class[]类型, 而验证分组名主要是根据调用方法名来区分的. 为了分组, 需要创建不同的Class对象. 

- 验证功能有限. JSR-303及Hibernate中提供的验证注解只有 @NotNull, @NotEmpty, @Digits, @Pattern, @Max, @Min, @NotBlank, @Size, @Min, @Max等, 都是针对单个属性的数据校验. 不能满足对象属性之间的约束限制. 比如座机和手机号码只能输入一个; 或当性别为男性, 体重&gt;120斤, 性别为女性, 体重&gt;100斤 这种情况, 就不能校验了. 

- 复用问题. 比如有个IP地址的正则规则, 当使用@Pattern正则匹配时, 需要每个@Pattern注解都配置正则表达式. 当然, Hibernate Validation提供了对 javax.validation.ConstraintValidator 扩展, 但还是需要些实现类. 不是很方便

- 以注解的方式对字段进行校验,虽然使用方便. 但代码看起来会比较乱. 尤其在分组校验时, 很难一眼看出要校验哪些字段. 校验的规则是什么. 

基于以上原因. 我们开发了EasyValidator, 和SpringMVC的校验相结合,提供强大的校验规则, 实现数据的快速, 直观校验.   

## 如何使用
1. 安装easyValidator. 可直接依赖,也可通过mvn install或deploy的方式安装到maven库中. 

2. 配置使用

	2.1 在你的springmvc配置文件(对应DispatcherServlet)中添加
	
	```
	<!--1. 创建EasyValidator Bean对象-->
	<bean id="easyValidator" class="cn.xyz.chaos.validator.spring.EasyValidator" />
	
	<!--2. 将easyValidator配置到springmvc中 -->
	<mvc:annotation-driven validator="easyValidator" ...>
		......
	</mvc:annotation-driven>

	<!--3. 配置EasyValidator拦截器 -->
	<mvc:interceptors>
		<bean class="cn.xyz.chaos.validator.spring.EasyValidatorInterceptor" />
		......
	</mvc:interceptors>
	```

	2.2 easyValidator中已提供了默认的校验规则, 在classpath:validator/internal/validator.xml中.如果需要扩展校验规则, 请在项目的classpath: validator/validator.xml或validator/internal/validtor.xml中定义.举例: 
	
	```
	<?xml version="1.0" encoding="UTF-8"?>
	<validators xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="classpath://validator/internal/validator.xsd">
	  <validator name="mail" extends="regex" args="\\w+@\\w+\\.\\w+" />
	</validators>	
	```  

	2.3 对要校验的实体类定义校验规则. 对象规则文件放在classpath: validator/ 目录下, 文件名以*.entity.xml结尾. 举例:
	
	```
	<!--UserDTO.entity.xml-->
	<?xml version="1.0" encoding="UTF-8"?>
	<entity class="cn.xyz.chaos.validator.test.UserDTO" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xsi:noNamespaceSchemaLocation="classpath://validator/val.xsd">

	  <!-- 字段校验规则定义集合 -->	
	  <fields>
		<!--定义每个字段的校验规则. 一个字段可有多个校验规则. 规则名称和validator.xml中配置的规则匹配. 使用cn.xyz.chaos.validator.action.FieldActionValidator验证-->
	    <field property="logUsername">
	      <valid name="required" msg="用户名不能为空" />
	      <valid name="length" min="4" max="16" msg="用户名长度为4-16个字符" />
	      <valid name="regex" args="\w+" msg="用户名为字符a-z,1-9,_" />
	    </field>

		<!--字段可配置默认规则. 供组中字段校验使用-->
	    <field property="logPassword" valid-alias="required,length,regex">
	      <valid name="required" msg="密码不能为空" />
	      <valid name="length" args="4,16" msg="密码长度为4-16个字符" />
	      <valid name="regex" args="\w+" msg="密码为字符a-z,1-9" />
		  <!--当校验规则相同, 但参数不同时, 可通过alias区别, 避免规则替换. 当alias未配置时, 使用name作为alias-->
	      <valid alias="num" name="regex" args="\d+" msg="必须为数字" />
	    </field>

	    <field property="logPassword2">
	      <valid name="required" msg="密码不能为空" />
		  <!--binary Operator规则使用-->
	      <valid name="equal" args="logPassword,logPassword2" msg="两次输入不一致" />
	    </field>

	    <field property="mobile">
	      <valid name="required" msg="手机号码不能为空" />
	      <valid name="regex" args="\d{11}" msg="手机号码格式错误" />
	    </field>

	    <field property="phone">
	      <valid name="required" msg="电话号码不能为空" />
	      <valid name="regex" args="\d+" msg="电话号码格式错误" />
	    </field>
	  </fields>

	  <!--组-->	
	  <groups>
		<!--组规则定义. 一般来说, 一个请求对应的Controller方法对应一个分组. 但可能存在对应多个分组, 参考下面的@ValidEasy说明-->
		<!--组名一般为方法名-->
	    <group name="login">
	      <field property="logUsername" />
	      <field property="logPassword" />
	    </group>

	    <group name="register">
	      <field property="logUsername" />
	      <field property="logPassword" />
		  <!--脚本验证. 使用cn.xyz.chaos.validator.action.ScriptActionValidator-->	
	      <script test="target.logPassword == 'mima'">
	        <field property="logPassword2" />
	      </script>

	      <script test="target.logUsername == 'mfan'">
	        <field property="mobile" />
	        <field property="phone" />
	      </script>
	    </group>

	    <group name="register1">
	      <field property="logUsername" />
	      <field property="logPassword" />
	      <script test="target.logUsername != 'mfan'">
	        <field property="mobile" />
	        <field property="phone" />
	      </script>
	    </group>

	    <group name="register2">
	      <field property="logUsername" />
	      <field property="logPassword" valid-alias="required,length,num" />
	      <field property="logPassword2" />
	    </group>
	  </groups>
	</entity>
	```
 	
	2.4 对需要验证的Controller方法中验证对象添加@ValidEasy注解. 注解value如果不配置, 默认使用方法名作为分组名称. 也可配置为多个分组, 验证时使用*.entity.xml中相应的多个分组结合验证. 
	
	2.5 验证不通过时, 错误信息集合可通过ModelAndView.getModel().get("_error")来获取.

## 默认校验器

| 校验器   | 名称    | 参数    | 说明   |
| ----:|----:|:----|:----|
| RequiredValidator | required | - | 非空或不为"" |
| NullValidator | null | - | 为空 |
| NotNullValidator | notnull | - | 非空 |
| NotBlankValidator | notblank | - | 可为空但不能为"" |
| RegexValidator | regex | regex | 正则 |
| LengthValidator | length | min; max | 字符串长度校验 |
| MaxValidator | max | max | 数字或数字字符串最大值校验 |
| MinValidator | min | min | 数字或数字字符串最小值校验 |
| RangeValidator | range | min; max | 数字或数字字符串范围校验 |
| RestrictValidator | restrict | restrict(多个值以逗号分隔) | 限定值校验 |
| SizeValidator | size | min; max | 对集合类型指的是大小, 对字符类型指的是长度 |
| CompareValidator | compare | flag(EQ,NE,LT,LE,GT,GE); ref(属性字段集合. 集合大小>=2. ) | 第一个属性和后续属性进行比较校验 |
| EqualValidator | equal |  ref(属性字段集合. 集合大小>=2. 多个字段以逗号分隔) | 属性是否相同校验. 扩展自CompareValidator |
| AtMostValidator | atmost | max; ref(属性字段集合. 多个字段以逗号分隔) | 被校验对象最多有几个属性可以有值校验 |
| AtLeastValidator | atleast | min; ref(属性字段集合. 多个字段以逗号分隔) | 被校验对象最少有几个属性可以有值校验 |
| PastValidator | past | - | 日期对象在当前日期之前校验 |
| FutureValidator | future | - | 日期对象在当前日期之后校验 |
| UrlValidator | url | url | 对象或对象字面量是否符合URL规范校验 |
| EmailValidator | email | - | 对象是否符合Email规范校验.对象可为空 |
| IdCardValidator | idcard | - | 身份证格式校验 |

## 扩展校验

扩展校验包括两部分内容: 

### 扩展校验规则
扩展校验规则有两种方式:

#### 通过继承 AbstractValidator 类扩展 
- 继承AbstractValidator类实现验证器扩展XxxValidator

	```
	package cn.xyz.chaos.examples.showcase.web.controller.validator;
	
	import cn.xyz.chaos.validator.ValidContext;
	import cn.xyz.chaos.validator.validators.AbstractValidator;
	
	/**
	 * 自定义扩展校验器
	 * 
	 * @author lvchenggang
	 */
	public class XxxValidator extends AbstractValidator {
	
		@Override
		public boolean isValid(Object object, ValidContext validContext) {
			// TODO具体校验规则
			return false;
		}
	
	}
	
	```

- 在validator.xml中配置此XxxValidator，并配置相关参数

	```
	<validators>
		<validator name="xxx" class="cn.xyz.chaos.examples.showcase.web.controller.validator.XxxValidator" />
	</validators>
	```

#### 通过扩展已有校验规则
```
<validators>
	<validator name="mail" extends="regex" args="\\w+@\\w+\\.\\w+" />
</validators>
```

### 扩展校验器
- 自定义校验器, 继承ActionValidator类

	```
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
	
	```

- 扩展xml校验执行器配置解析器. 解析器继承自ActionResolver. 比如:

	```
	<validators>
		<action-resolver class="cn.xyz.chaos.validator.action.xml.FieldActionResolver"/>
	</validators>
	```

## 和Hibernate Validator比较

| 校验框架 | 优势 | 劣势 |
| ----:|:----|----:|
| EasyValidator | (1) 校验规则丰富, 支持自定义, 且扩展方便 <br /> (2) 支持对象上下文, 可进行校验属性和其他对象属性关联 <br /> (3) xml方式配置, 配置清晰明了| 只能对Controller方法参数对象进行校验 |
| HibernateValidator | (1) 注解校验. 配置简单. <br /> (2) 可对任意方法的参数对象进行校验 <br /> (3) 支持校验规则扩展 | (1) 分组校验代码管理比较混乱 <br /> (2) 不支持对象属性关联 |