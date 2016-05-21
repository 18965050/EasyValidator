package cn.xyz.chaos.validator.config.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.xyz.chaos.validator.config.AbstractXmlValidatorResolver;
import cn.xyz.chaos.validator.config.XmlElement;

public class Dom4jXmlValidatorResolver extends AbstractXmlValidatorResolver {

	@Override
	public List<XmlElement> resolveXml0() throws DocumentException {
		List<XmlElement> result = new ArrayList<XmlElement>();
		SAXReader reader = new SAXReader();
		InputStream[] inputs = listValidInputStreams();
		Document[] documents = new Document[] { reader.read(inputs[0]), reader.read(inputs[1]) };
		for (Document document : documents) {
			result.add(resolveXml(document.getRootElement()));
		}
		return result;
	}

	@Override
	public List<XmlElement> resolveXml1() throws DocumentException {
		List<XmlElement> result = new ArrayList<XmlElement>();
		SAXReader reader = new SAXReader();
		for (File file : listEntityFiles()) {
			Document document = reader.read(file);
			result.add(resolveXml(document.getRootElement()));
		}
		return result;
	}

	private XmlElement resolveXml(Element element) {
		Map<String, String> attrs = new HashMap<String, String>();//
		List<Attribute> attrList = element.attributes();
		for (Attribute attr : attrList) {
			attrs.put(attr.getName(), attr.getValue());
		}
		XmlElement xmlel = new XmlElement(element.getName(), attrs);
		List<Element> list = element.elements();
		for (Element el : list) {
			XmlElement xmlel0 = resolveXml(el);
			if (xmlel0 != null) {
				xmlel.elements().add(xmlel0);
			}
		}
		return xmlel;
	}
}
