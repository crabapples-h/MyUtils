package com.MissX.utils.net;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.MissX.utils.Enum.OrderBy;

/**
 * 工具类-读取XML文件获取接口信息
 * (此工具依赖于interface.xml配置文件)
 * 2019年1月8日 下午3:05:15
 * @author H
 * TODO 写接口测试页面用
 * Admin
 */
public class InterfaceTestUtil {
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> CreateInterfaceTestData(OrderBy order) throws DocumentException   {
		SAXReader reader = new SAXReader();
		Document document = reader.read(this.getClass().getClassLoader().getResourceAsStream("interface.xml"));
		List<Element> elements_I = document.getRootElement().elements("interface");
		List<Map<String,Object>> interfaces = null;
		if(OrderBy.isASC(order)) {
			interfaces = ASC(elements_I);
			}
		if(OrderBy.isDESC(order)) {
			interfaces = DESC(elements_I);
		}
		return interfaces;
	}
	
	private List<Map<String, Object>> ASC(List<Element> elements_I){
		List<Map<String,Object>> interfaces = new LinkedList<Map<String,Object>>();
		for (int i = elements_I.size()-1;i >= 0;i--) {
			List<Element> args = elements_I.get(i).elements("args");
			List<Map<String,Object>> params = new LinkedList<Map<String,Object>>();
			Map<String,Object> data = new LinkedHashMap<String,Object>();
			for(int j = args.size()-1;j >= 0;j--) {
				Map<String,Object> param = new LinkedHashMap<String,Object>();
				param.put("name", args.get(j).attributeValue("name"));
				param.put("type", args.get(j).attributeValue("type"));
				param.put("value", args.get(j).attributeValue("value"));
				param.put("defaultValue", args.get(j).attributeValue("default"));
				params.add(param);
			}
			data.put("text", elements_I.get(i).attributeValue("text"));
			data.put("method", elements_I.get(i).attributeValue("method"));
			data.put("address", elements_I.get(i).attributeValue("address"));
			data.put("params", params);
			interfaces.add(data);
		}
		return interfaces;
	}
	
	private List<Map<String, Object>> DESC(List<Element> elements_I){
		List<Map<String,Object>> interfaces = new LinkedList<Map<String,Object>>();
		for (Element element : elements_I) {
			List<Element> args = element.elements("args");
			List<Map<String,Object>> params = new LinkedList<Map<String,Object>>();
			Map<String,Object> data = new LinkedHashMap<String,Object>();
			for (Element element2 : args) {
				Map<String,Object> param = new LinkedHashMap<String,Object>();
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
