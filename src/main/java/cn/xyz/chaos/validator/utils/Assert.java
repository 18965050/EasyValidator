package cn.xyz.chaos.validator.utils;

/**
 * Created by lvchenggang on 14/12/12 012.
 */
public abstract class Assert {
	public static void isTrue(final boolean expression, final String message, final Object... values) {
		if (expression == false) {
			throw new IllegalArgumentException(String.format(message, values));
		}
	}
}