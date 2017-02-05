package com.aims.welcome;

/**
 * java��ȡ��������Ԥ������
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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

/**
 * ����xml�ĵ������������ĵ���url
 *
 */
public class WeatherDemo {	
	private static String mark = "";
	InputStream inStream;	
	Element root;
	public InputStream getInStream() {		
		return inStream;		
	}
	public void setInStream(InputStream inStream) {		
		this.inStream = inStream;		
	}
	public Element getRoot() {		
		return root;		
	}
	public void setRoot(Element root) {		
		this.root = root;		
	}

	public WeatherDemo() {		
	}

	/**
	 * ͨ������������ȡ���˽ӿ���Ϣ
	 * @param inStream
	 */
	public WeatherDemo(InputStream inStream) {		
		if (inStream != null) {	
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);				
				root = doc.getDocumentElement();				
			} catch (ParserConfigurationException e) {				
				e.printStackTrace();			
			} catch (SAXException e) {			
				e.printStackTrace();				
			} catch (IOException e) {				
				e.printStackTrace();				
			}
		}
	}
	public WeatherDemo(String path) {		
		InputStream inStream = null;		
		try {			
			inStream = new FileInputStream(path);			
		} catch (FileNotFoundException e1) {			
			e1.printStackTrace();		
		}		
		if (inStream != null) {		
			this.inStream = inStream;			
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();		
			try {			
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();			
				Document doc = domBuilder.parse(inStream);			
				root = doc.getDocumentElement();				
			} catch (ParserConfigurationException e) {				
				e.printStackTrace();				
			} catch (SAXException e) {				
				e.printStackTrace();				
			} catch (IOException e) {				
				e.printStackTrace();				
			}
		}
	}
	public WeatherDemo(URL url) {		
		InputStream inStream = null;		
		try {		
			inStream = url.openStream();			
			if (inStream != null) 
			{			
				this.inStream = inStream;
				DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} 
		}
		catch (Exception e) 
		{
			mark = "error";
		}
		
	}

	/**
	 * 
	 * @param nodes
	 * @return �����ڵ���ֵ�Էֺŷָ�
	 */
	public Map<String, String> getValue(String[] nodes) {
		if (inStream == null || root==null) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		// ��ʼ��ÿ���ڵ��ֵΪnull
		for (int i = 0; i < nodes.length; i++) {
			map.put(nodes[i], null);
		}
		// ������һ�ڵ�
		NodeList topNodes = root.getChildNodes();
		if (topNodes != null) {
			for (int i = 0; i < topNodes.getLength(); i++) {
				Node book = topNodes.item(i);
				if (book.getNodeType() == Node.ELEMENT_NODE) {
					for (int j = 0; j < nodes.length; j++) {
						for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling()) {
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								if (node.getNodeName().equals(nodes[j])) {
									String val = node.getTextContent();
									String temp = map.get(nodes[j]);
									if (temp != null && !temp.equals("")) {
										temp = temp + ";" + val;
									} else {
										temp = val;
									}
									map.put(nodes[j], temp);
								}
							}
						}
					}
				}
			}
		}
		return map;
	}
	public static Map<String,String> getWeather(String city) {
		try {
			city = URLEncoder.encode(city, "GBK");
		} catch (UnsupportedEncodingException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		String link="http://php.weather.sina.com.cn/xml.php?city="+city+"&password=DJOYnieT8234jlsK&day=0";	
		URL url;
		String[] nodes = {"city","status1","temperature1","status2","temperature2"};
		try {		
			url = new URL(link);
			Map<String, String> map = new HashMap();
			String unknown = "unknown";
			WeatherDemo parser = new WeatherDemo(url);
			if(mark.equals(""))
				map = parser.getValue(nodes);
			else
			{
				for(int i= 0;i<nodes.length;i++)
					map.put(nodes[i], unknown);
			}	
			return map;
//			System.out.println(map.get(nodes[0])+" ������죺"+map.get(nodes[1])+" ����¶ȣ�"+map.get(nodes[2])+"�� ����ҹ�䣺"+map.get(nodes[3])+" ����¶ȣ�"+map.get(nodes[4])+"�� ");
		} catch (Exception e) {
			return null;
		}
	}
}
