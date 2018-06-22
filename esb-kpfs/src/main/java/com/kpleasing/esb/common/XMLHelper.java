package com.kpleasing.esb.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLHelper {

	public static String getXMLByNoteName(String xml, String tagName) {
		//这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			InputStream is =  XMLHelper.getStringStream(xml);
		    Document document = builder.parse(is);
		    
		    NodeList nodeList = document.getElementsByTagName(tagName);
		    Node node;
		    
		    int i = 0;
			while (i < nodeList.getLength()) {
			    node = nodeList.item(i);
	
				if (node instanceof Element) {
					return node.getTextContent();
				}
			}
		    
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
       
		
	}

	
	
	/**
	 * XML文档转换成Map键值对
	 * @param xml
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Map<String, Object> getMapFromXML(String xml) throws ParserConfigurationException, IOException, SAXException {

        //这里用Dom的方式解析回包的最主要目的是防止API新增回包字段
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is =  XMLHelper.getStringStream(xml);
        Document document = builder.parse(is);

        //获取到document里面的根子节点
        NodeList allNodes = document.getFirstChild().getChildNodes();
        
        return iterNodeList(allNodes, new HashMap<String, Object>());
    }
	
	/**
	 * 获取到document里面的全部结点
	 * @param allNodes
	 * @param map
	 * @return
	 */
	private static Map<String, Object> iterNodeList(NodeList allNodes, Map<String, Object> map) {
		Node node;
		int i = 0;
		while (i < allNodes.getLength()) {
			node = allNodes.item(i);

			if (node instanceof Element) {
				Node cNode = node.getFirstChild();
				if (cNode !=null && cNode.getNodeValue() != null && !cNode.getNodeValue().trim().equals("")) {
					map.put(node.getNodeName(), node.getTextContent());
				} else {
					iterNodeList(node.getChildNodes(), map);
				}
			}
			i++;
		}
		return map;
	}
	
	/**
	 * 
	 * @param sInputString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static InputStream getStringStream(String sInputString) throws UnsupportedEncodingException {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes("UTF-8"));
        }
        return tInputStringStream;
    }
	
	
	

}
