package cn.xyz.chaos.validator.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 * 实体对象.一个实体对象包含多个Field及多个Group
 * </pre>
 * 
 * @author lvchenggang
 *
 */
public class Entity extends ToStringObject {
	private final String				clazz;
	private final Map<String, Field>	fields	= new LinkedHashMap<String, Field>();
	private final Map<String, Group>	groups	= new LinkedHashMap<String, Group>();

	public Entity(String clazz) {
		this.clazz = clazz;
	}

	public String getClazz() {
		return clazz;
	}

	public Map<String, Field> getFields() {
		return fields;
	}

	public Map<String, Group> getGroups() {
		return groups;
	}

	// /////////////////////////////////////////////////
	public Field getField(String property) {
		return fields.get(property);
	}

	public Group getGroup(String property) {
		return groups.get(property);
	}

	public void addField(String property, Field field) {
		this.fields.put(property, field);
	}

	public void addGroup(String property, Group group) {
		this.groups.put(property, group);
	}
}