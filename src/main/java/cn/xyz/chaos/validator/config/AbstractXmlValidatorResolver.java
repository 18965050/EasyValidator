package cn.xyz.chaos.validator.config;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.xyz.chaos.validator.action.ActionValidator;
import cn.xyz.chaos.validator.action.xml.ActionResolver;
import cn.xyz.chaos.validator.action.xml.ActionResolverChain;
import cn.xyz.chaos.validator.data.*;
import cn.xyz.chaos.validator.utils.StringUtils;
import cn.xyz.chaos.validator.validators.Validator;

public abstract class AbstractXmlValidatorResolver implements XmlValidatorResolver {
	protected final Logger	logger	= LoggerFactory.getLogger(getClass());

	protected String		cfgXmlDirectory;

	public AbstractXmlValidatorResolver() {
		this(null);
	}

	public AbstractXmlValidatorResolver(String path) {
		if (path == null) {
			cfgXmlDirectory = new File(XmlValidatorResolver.class.getClassLoader().getResource(VALIDATOR_FILE)
					.getPath()).getParent();
		} else {
			cfgXmlDirectory = path;
		}
		logger.debug("config file directory:" + cfgXmlDirectory);
	}

	@Override
	public List<ActionResolver> resolveActionResolver() {
		List<ActionResolver> resolvers = new ArrayList<ActionResolver>();
		try {
			for (XmlElement root : resolveXml0()) {
				for (XmlElement el : root.elements(XML_EL_ACTION_RESOLVER)) {
					ActionResolver validator = (ActionResolver) Class.forName(el.attr(XML_ATT_CLASS)).newInstance();
					resolvers.add(validator);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return resolvers;
	}

	@Override
	public List<ActionValidator> resolveActionValidator() {
		List<ActionValidator> validators = new ArrayList<ActionValidator>();
		try {
			for (XmlElement root : resolveXml0()) {
				for (XmlElement el : root.elements(XML_EL_ACTION_VALIDATOR)) {
					ActionValidator validator = (ActionValidator) Class.forName(el.attr(XML_ATT_CLASS)).newInstance();
					validators.add(validator);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return validators;
	}

	@Override
	public List<Validator> resolveValidator() {
		List<Validator> validators = new ArrayList<Validator>();
		Map<String, String> clazzMap = new HashMap<String, String>();
		try {
			for (XmlElement root : resolveXml0()) {
				for (XmlElement el : root.elements(XML_EL_VALIDATOR)) {
					/** name+(class|extends)+args+mode */
					String name = el.attr(XML_ATT_NAME);
					String clazz = el.attr(XML_ATT_CLASS);
					if (clazz != null) {
						clazzMap.put(name, clazz);
					} else {
						String exts = el.attr(XML_ATT_EXTENDS);
						clazz = clazzMap.get(exts);
					}
					Validator validator = (Validator) Class.forName(clazz).newInstance();
					validator.setName(name);
					validator.attrs(el.attrs());
					validators.add(validator);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return validators;
	}

	/**
	 * 校验器配置解析
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract List<XmlElement> resolveXml0() throws Exception;

	/**
	 * Entity校验规则解析
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract List<XmlElement> resolveXml1() throws Exception;

	@Override
	public List<Entity> resolverEntity() {
		ActionResolverChain actionResolverChain = new ActionResolverChain(resolveActionResolver());
		List<Entity> list = new ArrayList<Entity>();
		try {
			for (XmlElement root : resolveXml1()) {
				Entity entity = new Entity(root.attr(XML_ATT_CLASS));
				XmlElement fieldsEl = root.element(XML_EL_FIELDS);
				for (XmlElement fieldEl : fieldsEl.elements(XML_EL_FIELD)) {
					// 各字段中定义的默认的valid校验规则，如没有定义则为全部
					String validAlias = StringUtils.trimAllWhitespace(fieldEl.attr(XML_ATT_VALID_ALIAS));
					Field field = new Field(fieldEl.attr(XML_ATT_PROPERTY), validAlias);
					// 加载所有valid，在接下来的解析分组时再确定各分组中的valid
					for (XmlElement validEl : fieldEl.elements(XML_EL_VALID)) {
						Valid valid = new Valid(validEl.attr(XML_ATT_ALIAS), validEl.attr(XML_ATT_NAME),
								validEl.attr(XML_ATT_MSG), validEl.attrs());
						field.addValid(valid);
					}
					entity.addField(field.getProperty(), field);
				}
				XmlElement groupsEls = root.element(XML_EL_GROUPS);
				// 解析各分组定义校验
				for (XmlElement groupEl : groupsEls.elements(XML_EL_GROUP)) {
					Group group = new Group(groupEl.attr(XML_ATT_NAME));
					// 此分组如扩展了其他分组，则加入其他分组的所有字段
					String groupExtends = groupEl.attr(XML_ATT_EXTENDS);
					if (groupExtends != null) {
						Group groupExtend = entity.getGroup(groupExtends);
						group.addActions(groupExtend.getActions());
					}
					for (XmlElement actionEl : groupEl.elements()) {
						List<Action> actions = actionResolverChain.resolve(actionEl, entity, null);
						if (actions != null) {
							group.addActions(actions);
						}
					}
					entity.addGroup(group.getName(), group);
				}
				list.add(entity);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}

	public File[] listEntityFiles() {
		File file = new File(cfgXmlDirectory);
		File[] files = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(VALIDATOR_ENTITY_FILE_SUFFIX);
			}
		});
		return files;
	}

	public InputStream[] listValidInputStreams() {
		InputStream[] inputStreams = new InputStream[2];
		inputStreams[0] = this.getClass().getClassLoader().getResourceAsStream(VALIDATOR_INTERNAL_FILE);
		inputStreams[1] = this.getClass().getClassLoader().getResourceAsStream(VALIDATOR_FILE);
		return inputStreams;
	}
}
