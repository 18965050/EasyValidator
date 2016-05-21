package cn.xyz.chaos.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xyz.chaos.validator.action.ActionValidator;
import cn.xyz.chaos.validator.action.xml.ActionResolver;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.config.impl.SaxXmlValidatorResolver;
import cn.xyz.chaos.validator.data.Entity;
import cn.xyz.chaos.validator.validators.Validator;

/**
 * 校验框架配置信息上下文
 * 
 * @author mfan
 */
public class EasyValidatorContext {
	private final XmlValidatorResolver		resolver;
	private final Map<String, Entity>		entities			= new HashMap<String, Entity>();
	private final Map<String, Validator>	validators			= new HashMap<String, Validator>();
	private final List<ActionValidator>		actionValidators	= new ArrayList<ActionValidator>();
	private final List<ActionResolver>		actionResolvers		= new ArrayList<ActionResolver>();

	public EasyValidatorContext() {
		this.resolver = new SaxXmlValidatorResolver();
		initializeContext();
	}

	public EasyValidatorContext(XmlValidatorResolver resolver) {
		this.resolver = resolver;
		initializeContext();
	}

	/**
	 * 初始化
	 */
	public synchronized void initializeContext() {
		// clear all cached for debug
		actionValidators.clear();
		actionResolvers.clear();
		validators.clear();
		entities.clear();
		actionValidators.addAll(resolver.resolveActionValidator());
		actionResolvers.addAll(resolver.resolveActionResolver());
		List<Validator> valids = resolver.resolveValidator();
		for (Validator validator : valids) {
			validators.put(validator.getName(), validator);
		}
		List<Entity> list = resolver.resolverEntity();
		for (Entity entity : list) {
			entities.put(entity.getClazz(), entity);
		}
	}

	public Validator getValidator(String name) {
		if (name == null) {
			return null;
		}
		return validators.get(name);
	}

	public Entity getEntity(String clazz) {
		return entities.get(clazz);
	}

	public List<ActionValidator> getActionValidators() {
		return actionValidators;
	}

	/**
	 * 注册业务校验器
	 * 
	 * @param validator
	 */
	public void registerValidator(Validator validator) {
		this.validators.put(validator.getName(), validator);
	}
}
