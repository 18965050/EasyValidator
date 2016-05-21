package cn.xyz.chaos.validator.spring;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.xyz.chaos.validator.ValidEasy;

/**
 * <pre>
 * 请求拦截器.用于:
 * (1) Controller方法触发前给EasyValidator设置Request级别的方法参数集合
 * (2) Render时判断是否校验错误, 有错时将错误信息以"_errors"作为key存放到Model中
 * (3) 请求完成时不管是否抛出异常,进行资源释放.
 * </pre>
 * 
 * @author lvchenggang
 *
 */
public class EasyValidatorInterceptor extends HandlerInterceptorAdapter {
	private final Logger									logger	= LoggerFactory.getLogger(getClass());
	private final Map<Method, List<EasyValidatorHolder>>	mapper	= new HashMap<Method, List<EasyValidatorHolder>>();

	private final String ERROR_MODEL_KEY = "_errors";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
			if (!mapper.containsKey(method)) {
				List<EasyValidatorHolder> easyvList = new ArrayList<EasyValidatorHolder>();
				for (MethodParameter methodParameter : methodParameters) {
					ValidEasy validator = methodParameter.getParameterAnnotation(ValidEasy.class);
					if (validator != null) {
						easyvList.add(new EasyValidatorHolder(method, methodParameter.getParameterType(), validator));
					}
				}
				mapper.put(method, easyvList);
			}
			EasyValidator.Holder.set(mapper.get(method));
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if ((modelAndView != null) && (modelAndView.getModel() != null)) {
			Map<String, Object> model = modelAndView.getModel();
			Set<String> modelKeySet = model.keySet();
			List<BindingResult> bindingResList = new LinkedList<BindingResult>();
			if (!CollectionUtils.isEmpty(modelKeySet)) {
				for (String modelKey : modelKeySet) {
					if (modelKey.startsWith(BindingResult.MODEL_KEY_PREFIX)) {
						BindingResult bindingRes = BindingResultUtils.getBindingResult(model,
								modelKey.substring(BindingResult.MODEL_KEY_PREFIX.length()));
						if ((bindingRes != null) && bindingRes.hasFieldErrors()) {
							bindingResList.add(bindingRes);
						}
					}
				}
			}

			/*
			 * 不删除model中原有BindingResult对象, 目的是可供其他组件使用, 比如spring标签 <form:errors /> 等等
			 */
			if (!CollectionUtils.isEmpty(bindingResList)) {
				Map<String, String> errorMap = new LinkedHashMap<String, String>();
				for (BindingResult bindingRes : bindingResList) {
					List<FieldError> errorList = bindingRes.getFieldErrors();
					for (FieldError error : errorList) {
						// 排除掉对象绑定抛出的异常.比如String-->Number类型转换
						if (!error.isBindingFailure()) {
							errorMap.put(error.getField(), error.getDefaultMessage());
						}
					}
				}
				// 添加进model中
				model.put(ERROR_MODEL_KEY, errorMap);
			}
		}
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		EasyValidator.Holder.remove();
		super.afterCompletion(request, response, handler, ex);
	}

}
