package cn.xyz.chaos.validator.action.xml;

import java.util.List;

import cn.xyz.chaos.validator.config.XmlElement;
import cn.xyz.chaos.validator.data.Action;
import cn.xyz.chaos.validator.data.Entity;

/**
 * Action校验解析器链
 * 
 * @author mfan
 */
public class ActionResolverChain {
	private List<ActionResolver>	resolvers;

	public ActionResolverChain(List<ActionResolver> resolvers) {
		this.resolvers = resolvers;
	}

	public List<Action> resolve(XmlElement element, Entity entity, Action action) {
		for (ActionResolver actionResolver : resolvers) {
			if (actionResolver.supports(element)) {
				return actionResolver.resolve(element, entity, action, this);
			}
		}
		return null;
	}
}
