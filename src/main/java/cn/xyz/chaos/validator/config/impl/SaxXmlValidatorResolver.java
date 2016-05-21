package cn.xyz.chaos.validator.config.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.xyz.chaos.validator.config.AbstractXmlValidatorResolver;
import cn.xyz.chaos.validator.config.XmlElement;

public class SaxXmlValidatorResolver extends AbstractXmlValidatorResolver {

	SAXParserFactory	saxparserfactory	= SAXParserFactory.newInstance();

	@Override
	public List<XmlElement> resolveXml0() throws ParserConfigurationException, SAXException, IOException {
		List<XmlElement> result = new ArrayList<XmlElement>();
		SaxHandler sh1 = new SaxHandler();
		SaxHandler sh2 = new SaxHandler();
		SAXParser saxparser = saxparserfactory.newSAXParser();
		InputStream[] inputs = listValidInputStreams();
		saxparser.parse(inputs[0], sh1);
		saxparser.parse(inputs[1], sh2);
		result.add(sh1.getFirstXmlElement());
		result.add(sh2.getFirstXmlElement());
		return result;
	}

	@Override
	public List<XmlElement> resolveXml1() throws ParserConfigurationException, SAXException, IOException {
		List<XmlElement> result = new ArrayList<XmlElement>();
		for (File file : listEntityFiles()) {
			SAXParser saxparser = saxparserfactory.newSAXParser();
			SaxHandler sax = new SaxHandler();
			saxparser.parse(file, sax);
			result.add(sax.getFirstXmlElement());
		}
		return result;
	}

	class SaxHandler extends DefaultHandler {
		private List<XmlElement>	list	= new ArrayList<XmlElement>();

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < attributes.getLength(); i++) {
				map.put(attributes.getQName(i), attributes.getValue(i));
			}
			XmlElement el = new XmlElement(qName, map);
			list.add(el);
			for (int i = list.size() - 1; i >= 0; i--) {
				XmlElement element = list.get(i);
				if (!element.tagName().equals(qName)) {
					element.elements().add(el);
					break;
				}
			}
			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (list.size() > 1) {
				list.remove(list.size() - 1);
			}
			super.endElement(uri, localName, qName);
		}

		public XmlElement getFirstXmlElement() {
			return list.get(0);
		}
	}
}
