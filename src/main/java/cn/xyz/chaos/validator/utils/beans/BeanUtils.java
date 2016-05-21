package cn.xyz.chaos.validator.utils.beans;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xyz.chaos.validator.utils.beans.resolver.DefaultResolver;
import cn.xyz.chaos.validator.utils.beans.resolver.Resolver;

/**
 * Bean属性工具类. 实现内部属性值的读取.
 * 
 * @author mfan
 * 
 */
public class BeanUtils {
	private static final Logger		logger		= LoggerFactory.getLogger(BeanUtils.class);
	private static final Resolver	resolver	= new DefaultResolver();

	/**
	 * <pre>
	 * 获取对象的属性值
	 * 支持集合或递归对象属性赋值
	 * </pre>
	 * 
	 * @param object 对象实例
	 * @param name 属性名
	 * @return 属性的值
	 */
	public static Object getProperty(Object object, String name) {
		try {
			return getNestedProperty(object, name);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static Object getNestedProperty(Object object, String name)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		if ((object == null) || (name == null)) {
			return null;
		}
		// Resolve nested references
		while (name != null) {
			String next = resolver.next(name);
			Object nObject = null;
			if (object instanceof Map) {
				nObject = getPropertyOfMapBean((Map) object, next);
			} else if (resolver.isMapped(next)) {
				nObject = getMappedProperty(object, next);
			} else if (resolver.isIndexed(next)) {
				nObject = getIndexedProperty(object, next);
			} else {
				nObject = getSimpleProperty(object, next);
			}
			if (nObject == null) {
				return null;
			}
			object = nObject;
			name = resolver.remove(name);
		}
		return object;
	}

	private static Object getPropertyOfMapBean(Map object, String name)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (resolver.isMapped(name)) {
			String property = resolver.getProperty(name);
			if ((property == null) || (property.length() == 0)) {
				name = resolver.getKey(name);
			}
		}
		if (resolver.hasNested(name) || resolver.isIndexed(name) || resolver.isMapped(name)) {
			throw new IllegalArgumentException(
					"Indexed or mapped properties are not supported on" + " objects of type Map: " + name);
		}
		return object.get(name);
	}

	private static Object getMappedProperty(Object object, String name)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		if ((object == null) || (name == null)) {
			return null;
		}
		Map map = (Map) getSimpleProperty(object, resolver.getProperty(name));
		return getPropertyOfMapBean(map, name);
	}

	private static Object getIndexedProperty(Object object, String name)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		if ((object == null) || (name == null)) {
			return null;
		}
		int index = resolver.getIndex(name);
		name = resolver.getProperty(name);
		if ((name != null) && (name.length() > 0)) {
			object = getSimpleProperty(object, name);
		}
		if (object == null) {
			return null;
		} else if (object.getClass().isArray()) {
			return Array.get(object, index);
		} else if (object instanceof List) {
			return ((List) object).get(index);
		}
		return null;
	}

	private static Object getSimpleProperty(Object object, String name) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchFieldException, NoSuchMethodException {
		if ((object == null) || (name == null)) {
			return null;
		}
		if (resolver.hasNested(name) || resolver.isIndexed(name) || resolver.isMapped(name)) {
			throw new IllegalArgumentException(
					"Not allowed: Property '" + name + "' on object class '" + object.getClass() + "'");
		}
		Method getter = MethodUtils.getGetterCache(object.getClass(), name);
		if (getter == null) {
			try {
				return MethodUtils.invokeGetterMethod(object, getter);
			} catch (Exception e) {
			}
		}
		return FieldUtils.readField(object, name, true);
	}
}