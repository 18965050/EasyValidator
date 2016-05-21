package cn.xyz.chaos.validator.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static final String	EMPTY				= "";
	public static final String	COMMA				= ",";
	public static final String	MIDLINE				= "-";
	public static final String	FOLDER_SEPARATOR	= "/";

	public static boolean isEmpty(final CharSequence cs) {
		return (cs == null) || (cs.length() == 0);
	}

	public static boolean isNotEmpty(final CharSequence cs) {
		return !StringUtils.isEmpty(cs);
	}

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if ((cs == null) || ((strLen = cs.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(final CharSequence cs) {
		return !StringUtils.isBlank(cs);
	}

	// Trim
	// -----------------------------------------------------------------------
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	public static String trimToNull(String str) {
		String ts = trim(str);
		return isEmpty(ts) ? null : ts;
	}

	public static String trimToEmpty(String str) {
		return str == null ? EMPTY : str.trim();
	}

	public static String trimAllWhitespace(String str) {
		if (length(str) == 0) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		int index = 0;
		while (sb.length() > index) {
			if (Character.isWhitespace(sb.charAt(index))) {
				sb.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return sb.toString();
	}

	public static int length(CharSequence cs) {
		return cs == null ? 0 : cs.length();
	}

	public static String[] split(String str, String regex) {
		return str == null ? null : str.split(regex);
	}

	private static Pattern	VARIABLE_PATTERN	= Pattern.compile("\\$\\s*\\{?\\s*([\\._0-9a-zA-Z]+)\\s*\\}?");

	public static String replaceProperty(String expression, Map<String, String> params) {
		if ((expression == null) || (expression.length() == 0) || (expression.indexOf('$') < 0)) {
			return expression;
		}
		Matcher matcher = VARIABLE_PATTERN.matcher(expression);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) { // 逐个匹配
			String key = matcher.group(1);
			String value = System.getProperty(key);
			if ((value == null) && (params != null)) {
				value = params.get(key);
			}
			if (value == null) {
				value = "";
			}
			matcher.appendReplacement(sb, Matcher.quoteReplacement(value));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

}
