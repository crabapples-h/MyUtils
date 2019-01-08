package com.utils.net;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 工具类-读取XML文件获取接口信息
 * (此工具依赖于interface.xml配置文件)
 * 2019年1月8日 下午3:05:15
 * @author H
 * TODO 写接口测试页面用
 * Admin
 */
public class XmlUtil {
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getInterfaceInfo() throws DocumentException   {
		SAXReader reader = new SAXReader();
		Document document = reader.read(this.getClass().getClassLoader().getResourceAsStream("interface.xml"));
		List<Element> elements_I = document.getRootElement().elements("interface");
		List<Map<String,Object>> interfaces = new LinkedList<Map<String,Object>>();
		for (Element element : elements_I) {
			List<Element> args = element.elements("args");
			List<Map<String,Object>> params = new LinkedList<Map<String,Object>>();
			Map<String,Object> data = new TreeMap<String,Object>();
			for (Element element2 : args) {
				Map<String,Object> param = new TreeMap<String,Object>();
				param.put("name", element2.attributeValue("name"));
				param.put("type", element2.attributeValue("type"));
				param.put("value", element2.attributeValue("value"));
				param.put("defaultValue", element2.attributeValue("default"));
				params.add(param);
			}
			data.put("text", element.attributeValue("text"));
			data.put("method", element.attributeValue("method"));
			data.put("address", element.attributeValue("address"));
			data.put("params", params);
			interfaces.add(data);
		}
		return interfaces;
	}
}
