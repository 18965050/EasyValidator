package cn.xyz.chaos.validator.data;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 校验字段
 * 一个Field对应多个Valid
 * </pre>
 * 
 * @author lvchenggang
 *
 */
public class Field extends ToStringObject {

	// 字段名称
	private final String	property;
	// 默认配置的校验器
	private final String	validAlias;

	private final List<Valid> valids = new ArrayList<Valid>();

	public Field(String property, String validAlias) {
		this.property = property;
		this.validAlias = validAlias;
	}

	public String getProperty() {
		return property;
	}

	public String getValidAlias() {
		return validAlias;
	}

	public void addValid(Valid valid) {
		this.valids.add(valid);
	}

	public List<Valid> getValids() {
		return valids;
	}

}
