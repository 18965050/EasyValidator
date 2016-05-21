package cn.xyz.chaos.validator.validators;

import static java.util.regex.Pattern.*;

import java.net.IDN;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.xyz.chaos.validator.ValidContext;

public class EmailValidator extends AbstractValidator {
	private static String	ATOM			= "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
	private static String	DOMAIN			= ATOM + "+(\\." + ATOM + "+)*";
	private static String	IP_DOMAIN		= "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

	/**
	 * Regular expression for the local part of an email address (everything before '@')
	 */
	private Pattern			localPattern	= java.util.regex.Pattern.compile(ATOM + "+(\\." + ATOM + "+)*",
													CASE_INSENSITIVE);

	/**
	 * Regular expression for the domain part of an email address (everything after '@')
	 */
	private Pattern			domainPattern	= java.util.regex.Pattern.compile(DOMAIN + "|" + IP_DOMAIN,
													CASE_INSENSITIVE);

	@Override
	public boolean isValid(Object object, ValidContext validContext) {
		if (object == null) {
			return true;
		}
		String value = object.toString();
		if ((value == null) || (value.length() == 0)) {
			return true;
		}

		// split email at '@' and consider local and domain part separately
		String[] emailParts = value.toString().split("@");
		if (emailParts.length != 2) {
			return false;
		}

		// if we have a trailing dot in local or domain part we have an invalid email address.
		// the regular expression match would take care of this, but IDN.toASCII drops trailing the trailing '.'
		// (imo a bug in the implementation)
		if (emailParts[0].endsWith(".") || emailParts[1].endsWith(".")) {
			return false;
		}

		if (!matchPart(emailParts[0], localPattern)) {
			return false;
		}

		return matchPart(emailParts[1], domainPattern);
	}

	private boolean matchPart(String part, Pattern pattern) {
		try {
			part = IDN.toASCII(part);
		} catch (IllegalArgumentException e) {
			// occurs when the label is too long (>63, even though it should probably be 64 - see
			// http://www.rfc-editor.org/errata_search.php?rfc=3696,
			// practically that should not be a problem)
			return false;
		}
		Matcher matcher = pattern.matcher(part);
		return matcher.matches();
	}
}
