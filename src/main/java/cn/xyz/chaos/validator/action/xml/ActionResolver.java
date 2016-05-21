package cn.xyz.chaos.validator.action.xml;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xyz.chaos.validator.config.XmlElement;
import cn.xyz.chaos.validator.data.Action;
import cn.xyz.chaos.validator.data.Entity;

/**
 * Action解析器
 * 
 * @author mfan
 */

public interface ActionResolver<T extends Action> {
	Logger logger = LoggerFactory.getLogger(ActionResolver.class);

	/**
	 * 依据标签来判断是否支持
	 * @param xmlElement
	 * @return
	 */
	boolean supports(XmlElement xmlElement);

	/**
	 * 解析XML转换为Action对象集合
	 * @param element
	 * @param entity
	 * @param action
	 * @param chain
	 * @return
	 */
	List<T> resolve(XmlElement element, Entity entity, Action action, ActionResolverChain chain);
}
