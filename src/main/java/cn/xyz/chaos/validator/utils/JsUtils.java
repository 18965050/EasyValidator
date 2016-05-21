package cn.xyz.chaos.validator.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsUtils {

	/**
	 * 记录日志类
	 */
	private static Logger				logger	= LoggerFactory.getLogger(JsUtils.class);
	// 获取脚本引擎
	private static ScriptEngineManager	mgr		= new ScriptEngineManager();
	private static ScriptEngine			engine	= mgr.getEngineByName("javascript");

	/**
	 * 后置处理，执行js脚本
	 * 
	 * @param js
	 * @throws Exception
	 */
	public static Object execJs(String js, Map<String, Object> map) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("execJs js : " + js);
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				logger.debug("execJs map : " + entry.getKey() + "---" + entry.getValue());
			}// end while
		}// end if
		if ("".equals(js) || (js == null)) {
			logger.error("execJs error : javascript content is null");
		} else if ((map == null) || (map.size() <= 0)) {
			logger.error("execJs error : map content is null");
		} else {
			// 绑定数据
			ScriptContext newContext = new SimpleScriptContext();
			Bindings bind = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
			bind.putAll(map);
			try {
				engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
				return engine.eval(js);
			} catch (Exception e) {
				logger.error("execJs exception : execute javascript exception", e);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		JsUtils js = new JsUtils();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("logUsername", "1");
		try {
			js.execJs("logUsername == '1'", map);
			System.out.println(js.execJs("logUsername == '1'", map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}