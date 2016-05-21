/**
 * Author lvchenggang 
 * XYZ Reserved
 * Created on 2016年5月20日 下午5:09:11
 */
package cn.xyz.chaos.validator.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author lvchenggang
 *
 */
public class JSONUtils {

	private static ObjectMapper mapper = new ObjectMapper();

	public static String toJSONString(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			return "";
		}
	}

	public static Object parse(String jsonStr, Class cls) {
		try {
			return mapper.readValue(jsonStr, cls);
		} catch (Exception e) {
			return null;
		}
	}

}
