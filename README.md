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
	1. 在你的springmvc配置文件(对应DispatcherServlet)中添加
	
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

	2. easyValidator中已提供了默认的校验规则, 在classpath:validator/internal/validator.xml中.如果需要扩展校验规则, 请在项目的classpath: validator/validator.xml或validator/internal/validtor.xml中定义.举例: 
	
	```
	<?xml version="1.0" encoding="UTF-8"?>
	<validators xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="classpath://validator/internal/validator.xsd">
	  <validator name="mail" extends="regex" args="\\w+@\\w+\\.\\w+" />
	</validators>	
	```  

	3. 对要校验的实体类定义校验规则. 对象规则文件放在classpath: validator/ 目录下, 文件名以*.entity.xml结尾. 举例:
	
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
 	
	4. 对需要验证的Controller方法中验证对象添加@ValidEasy注解. 注解value如果不配置, 默认使用方法名作为分组名称. 也可配置为多个分组, 验证时使用*.entity.xml中相应的多个分组结合验证. 
	
	5. 验证不通过时, 错误信息集合可通过ModelAndView.getModel().get("_error")来获取.

## 默认校验器

## 扩展校验器

## 和Hibernate Validator比较