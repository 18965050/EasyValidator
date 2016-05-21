package cn.xyz.chaos.validator.action.xml;

import java.util.Arrays;
import java.util.List;

import cn.xyz.chaos.validator.config.XmlElement;
import cn.xyz.chaos.validator.config.XmlValidatorResolver;
import cn.xyz.chaos.validator.data.*;
import cn.xyz.chaos.validator.utils.StringUtils;

public class FieldActionResolver implements ActionResolver<FieldAction> {

	@Override
	public boolean supports(XmlElement xmlElement) {
		return XmlValidatorResolver.XML_EL_ACTION_FIELD.equals(xmlElement.tagName());
	}

	@Override
	public List<FieldAction> resolve(XmlElement element, Entity entity, Action action, ActionResolverChain chain) {
		FieldAction actionField = null;
		String property = element.attr(XmlValidatorResolver.XML_ATT_PROPERTY);
		Field field = entity.getField(property);
		if (field == null) {
			logger.error("{}-配置校验字段'{}'信息不存在", entity.getClazz(), property);
			return null;
		}
		// 指定其中校验规则（valid），按[goups->group->field[valid-alias]]优先
		String validAlias = StringUtils.trimAllWhitespace(element.attr(XmlValidatorResolver.XML_ATT_VALID_ALIAS));
		if (validAlias == null) {
			// 其次[fileds->field[valid-alias]]
			validAlias = field.getValidAlias();
		}
		if (validAlias == null) {
			actionField = new FieldAction(field);
			addAction(action, actionField);
			return Arrays.asList(actionField);
		}
		Field newField = entity.getField(property + StringUtils.MIDLINE + validAlias);
		if (newField == null) {// 之前不存在则新构建一个
			List<String> fieldList = Arrays.asList(StringUtils.split(validAlias, StringUtils.COMMA));
			if (!fieldList.isEmpty()) {
				newField = new Field(property, validAlias);
				for (Valid valid : field.getValids()) {
					if (fieldList.contains(valid.getAlias())) {/* 按alias搜索 */
						newField.addValid(valid);
					}
				}
			}
			// 补充字段定义
			entity.addField(property + StringUtils.MIDLINE + validAlias, newField);
		}
		actionField = new FieldAction(newField);
		addAction(action, actionField);
		return Arrays.asList(actionField);
	}

	private void addAction(Action action, FieldAction actionField) {
		if (action != null) {
			action.addAction(actionField);
		}
	}
}
