package cn.xyz.chaos.validator;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class EasyFieldError implements Serializable {

	private final String				field;
	private final String				msg;
	private final Map<String, String>	args;

	public EasyFieldError(String field, String msg, Map<String, String> args) {
		super();
		this.field = field;
		this.msg = msg;
		this.args = args;
	}

	public String getField() {
		return field;
	}

	public String getMsg() {
		return msg;
	}

	public Map<String, String> getArgs() {
		return args;
	}
}
