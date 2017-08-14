package com.safly.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeanFactory {

	public static Object getBean(String id){
		
		
		try {
			SAXReader reader = new SAXReader();
			String path = BeanFactory.class.getClassLoader().getResource("bean.xml").getPath();
			Document doc = reader.read(path);
			Element element = (Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			String className = element.attributeValue("class");
			Class clazz = Class.forName(className);
			Object object = clazz.newInstance();
			
			return object;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
