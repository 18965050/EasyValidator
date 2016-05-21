package cn.xyz.chaos.validator.utils.beans;

import static java.util.Locale.ENGLISH;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Method工具类.
 * 
 * @author mfan
 * 
 */
public class MethodUtils {
	private static final Logger					logger					= LoggerFactory.getLogger(MethodUtils.class
																				.getClass());
	private static final Object[]				EMPTY_OBJECT_PARAMS		= new Object[0];
	private static final Class[]				EMPTY_CLASS_PARAMETERS	= new Class[0];
	private static final Map<String, Method>	getterCache				= new HashMap<String, Method>();

	private MethodUtils() {
	}

	public static Object invokeGetterMethod(final Object object, final Method method) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		return method.invoke(object, EMPTY_OBJECT_PARAMS);
	}

	public static Method getGetterCache(Class<?> clazz, String property) {
		if ((clazz == null) || (property == null)) {
			return null;
		}
		String key = clazz.getCanonicalName() + "." + property;
		Method getter = getterCache.get(key);
		if (getter == null) {
			try {
				getter = getGetter(clazz, property);
			} catch (Exception e) {
				// logger.debug(e.getMessage(), e);
			}
			getterCache.put(key, getter);
		}
		return getter;
	}

	/**
	 * 查找getXXX与isXXX的属性Getter方法
	 * 
	 * @param clazz 类元
	 * @param property 属性名
	 * @return 属性Getter方法
	 * @throws NoSuchMethodException Getter不存时抛出
	 */
	private static Method getGetter(Class<?> clazz, String property) throws NoSuchMethodException, SecurityException {
		if ((clazz == null) || (property == null)) {
			return null;
		}
		String upper = property.substring(0, 1).toUpperCase(ENGLISH) + property.substring(1);
		try {
			return getGetterMethod(clazz, "get" + upper);
		} catch (NoSuchMethodException e1) {
			try {
				return getGetterMethod(clazz, "is" + upper);
			} catch (NoSuchMethodException e2) {
				return getGetterMethod(clazz, property);
			}
		}
	}

	/**
	 * 获取类的方法 (保证返回方法的公开性)
	 * 
	 * @param clazz 类
	 * @param methodName 方法名
	 * @return 公开的方法
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private static Method getGetterMethod(Class<?> clazz, String methodName) throws NoSuchMethodException,
			SecurityException {
		if ((clazz == null) || (methodName == null)) {
			return null;
		}
		Method getter = clazz.getMethod(methodName, EMPTY_CLASS_PARAMETERS);
		getter = getAccessibleMethod(getter, clazz);
		return getter;
	}

	// 获取可访问方法
	private static Method getAccessibleMethod(Method method, Class<?> clazz) throws NoSuchMethodException,
			SecurityException {
		if (method == null) {
			return null;
		}
		if (!method.isAccessible()) {
			try {
				method.setAccessible(true);
			} catch (SecurityException e) { // 当不允许关闭访问控制时, 采用向上查找公开方法
				String methodName = method.getName();
				try {
					method = searchPublicMethod(clazz, methodName);
				} catch (NoSuchMethodException e1) {
					method = searchPublicMethod(clazz.getInterfaces(), methodName);
				}
			}
		}
		return method;
	}

	// 查找公开的方法 (辅助方法)
	private static Method searchPublicMethod(Class<?>[] classes, String methodName) throws NoSuchMethodException,
			SecurityException {
		if ((classes != null) && (classes.length > 0)) {
			for (Class<?> cls : classes) {
				try {
					return searchPublicMethod(cls, methodName);
				} catch (NoSuchMethodException e) {
					// ignore, continue
				}
			}
		}
		throw new NoSuchMethodException(); // 未找到方法
	}

	// 查找公开的方法 (辅助方法)
	private static Method searchPublicMethod(Class<?> cls, String methodName) throws NoSuchMethodException,
			SecurityException {
		Method method = cls.getMethod(methodName, EMPTY_CLASS_PARAMETERS);
		if (isPublicMethod(method)
				&& ((method.getParameterTypes() == null) || (method.getParameterTypes().length == 0))) { // 再保证方法是公开的
			return method;
		}
		throw new NoSuchMethodException(); // 未找到方法
	}

	private static boolean isPublicMethod(Method method) {
		return (method != null) && ((method.getModifiers() & Modifier.PUBLIC) == 1);
	}
}
