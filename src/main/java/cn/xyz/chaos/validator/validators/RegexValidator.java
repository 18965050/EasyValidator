/**
 *
 */
package cn.xyz.chaos.validator.validators;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xyz.chaos.validator.ValidContext;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.Valid;
import cn.xyz.chaos.validator.utils.Assert;
import cn.xyz.chaos.validator.utils.StringUtils;

/**
 * @author mfan
 */
public class RegexValidator extends AbstractValidator {
	// 缓存提升性能
	private Map<String, Pattern>	map	= new HashMap<String, Pattern>();

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		if (object == null) {
			return true;
		}
		Valid valid = validContext.getValid();
		// 格式如：\\w+@\\w+\\.\\w+
		String regex = attr(valid, XmlValidatorResolver.XML_ATT_REGEX);
		Assert.isTrue(StringUtils.isNotBlank(regex), "The regex must not be null");
		String flag = attr(valid, XmlValidatorResolver.XML_ATT_FLAG);
		// 模式
		int flags = 0;
		if (flag != null) {
			for (String f : StringUtils.split(StringUtils.trimAllWhitespace(flag), StringUtils.COMMA)) {
				flags = flags | Flag.valueOf(f).getValue();
			}
		}
		String key = regex + (flag == null ? "" : "-" + flag);
		Pattern p = map.get(key);
		if (p == null) {
			p = Pattern.compile(regex, flags);
			map.put(key, p);
		}
		Matcher m = p.matcher(object.toString());
		return m.matches();
	}

	public static enum Flag {
		UNIX_LINES(java.util.regex.Pattern.UNIX_LINES), CASE_INSENSITIVE(java.util.regex.Pattern.CASE_INSENSITIVE), COMMENTS(
				java.util.regex.Pattern.COMMENTS), MULTILINE(java.util.regex.Pattern.MULTILINE), DOTALL(
				java.util.regex.Pattern.DOTALL), UNICODE_CASE(java.util.regex.Pattern.UNICODE_CASE), CANON_EQ(
				java.util.regex.Pattern.CANON_EQ);

		private final int	value;

		private Flag(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
