package cn.xyz.chaos.validator.utils.beans;

import cn.xyz.chaos.validator.utils.Assert;
import cn.xyz.chaos.validator.utils.StringUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Created by lvchenggang on 14/12/12 012.
 */
public class FieldUtils {

	public static Object readField(final Object target, final String fieldName, final boolean forceAccess)
			throws IllegalAccessException, NoSuchFieldException {
		Assert.isTrue(target != null, "target object must not be null");
		final Class<?> cls = target.getClass();
		final Field field = getField(cls, fieldName, forceAccess);
		Assert.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
		// already forced access above, don't repeat it here:
		return readField(field, target, false);
	}

	public static Field getField(final Class<?> cls, final String fieldName, final boolean forceAccess)
			throws NoSuchFieldException {
		Assert.isTrue(cls != null, "The class must not be null");
		Assert.isTrue(StringUtils.isNotBlank(fieldName), "The field name must not be blank/empty");
		// Sun Java 1.3 has a bugged implementation of getField hence we write the
		// code ourselves

		// getField() will return the Field object with the declaring class
		// set correctly to the class that declares the field. Thus requesting the
		// field on a subclass will return the field from the superclass.
		//
		// priority order for lookup:
		// searchclass private/protected/package/public
		// superclass protected/package/public
		// private/different package blocks access to further superclasses
		// implementedinterface public
		NoSuchFieldException noSuchFieldException = null;
		// check up the superclass hierarchy
		for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
			try {
				final Field field = acls.getDeclaredField(fieldName);
				// getDeclaredField checks for non-public scopes as well
				// and it returns accurate results
				if (!Modifier.isPublic(field.getModifiers())) {
					if (forceAccess) {
						field.setAccessible(true);
					} else {
						continue;
					}
				}
				return field;
			} catch (final NoSuchFieldException ex) { // NOPMD
				noSuchFieldException = ex;
			}
		}
		throw noSuchFieldException;
	}

	public static Object readField(final Field field, final Object target, final boolean forceAccess)
			throws IllegalAccessException {
		Assert.isTrue(field != null, "The field must not be null");
		if (forceAccess && !field.isAccessible()) {
			field.setAccessible(true);
		} else {
			setAccessibleWorkaround(field);
		}
		return field.get(target);
	}

	static boolean setAccessibleWorkaround(final AccessibleObject o) {
		if (o == null || o.isAccessible()) {
			return false;
		}
		final Member m = (Member) o;
		if (!o.isAccessible() && Modifier.isPublic(m.getModifiers())
				&& isPackageAccess(m.getDeclaringClass().getModifiers())) {
			try {
				o.setAccessible(true);
				return true;
			} catch (final SecurityException e) { // NOPMD
				// ignore in favor of subsequent IllegalAccessException
			}
		}
		return false;
	}

	private static final int	ACCESS_TEST	= Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

	/**
	 * Returns whether a given set of modifiers implies package access.
	 * 
	 * @param modifiers to test
	 * @return {@code true} unless {@code package}/{@code protected}/{@code private} modifier detected
	 */
	static boolean isPackageAccess(final int modifiers) {
		return (modifiers & ACCESS_TEST) == 0;
	}
}
