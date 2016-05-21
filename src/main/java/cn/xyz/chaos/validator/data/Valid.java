package cn.xyz.chaos.validator.data;

import java.util.Map;

/**
 * 校验规则
 * @author lvchenggang
 *
 */
public class Valid extends ToStringObject {
	// 别名-支持同一个Field下使用同一个校验器(即name相同，在同一个Field中定义的valid-alias)
	private final String alias;

	// 名称. 实际上相当于type
	private final String name;

	// 错误提示信息
	private final String				msg;
	private final Map<String, String>	attrs;

	public Valid(String alias, String name, String msg, Map<String, String> attrs) {
		this.alias = (alias == null ? name : alias);
		this.name = name;
		this.msg = msg;
		this.attrs = attrs;
	}

	public String getAlias() {
		return alias;
	}

	public String getName() {
		return name;
	}

	public String getMsg() {
		return msg;
	}

	public String attr(String key) {
		return attrs.get(key);
	}

	public Map<String, String> attrs() {
		return attrs;
	}

}
