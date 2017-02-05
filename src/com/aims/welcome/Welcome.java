package com.aims.welcome;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.*;

import com.aims.DButil.DButil;
public class Welcome extends JLabel {
	private static final long serialVersionUID = -6084396013307461295L;
	private String str;//Ҫ��ʾ���ַ���������
	private String status = "ѧԱ";
	private String ip;
	private String location;
	private String city;
	private WeatherDemo weather = new WeatherDemo();
	private String[] nodes = {"city","status1","temperature1","status2","temperature2"};
	private GetIp getIP = new GetIp();
	private JLabel label;
	private JLabel[] jLabel = {
			new JLabel(),new JLabel(),new JLabel(),
			new JLabel(),new JLabel(),new JLabel()
	};
	public Welcome(String str){
		this.str=str;
		String content = getIP.getWebIp();
		System.out.println(content);
		if(content.startsWith("0"))
		{	
			city = "δ֪";
			location  = "δ֪";
		}
		else
		{
			String s[] = content.split(" ");
			ip = s[0];
			location = s[1];
			System.out.println(location);
			int start = location.indexOf("ʡ")+1;
			int end   = location.indexOf("��");
			try {
				city = location.substring(start, end);
			} catch (Exception e) {
			}
		}
		this.initialFrame();
	}
	public void initialFrame(){
		this.setLayout(null);
		if(DButil.Power.equals(DButil.Admin))
			status = "����Ա";
		else if(DButil.Power.equals(DButil.SuperAdmin))
			status = "��������Ա";
		Map<String,String> map = weather.getWeather(city);
		String[] weather ={
				"������죺"+map.get(nodes[1]),"����¶ȣ�"+map.get(nodes[2])+"�� ",
				"����ҹ�䣺"+map.get(nodes[3]),"����¶ȣ�"+map.get(nodes[4])+"�� "
		};
		jLabel[0].setText("<html><font size=6>��ӭ  <font color=#ff0041>"+status+"</font>  "+str+"  ��½</font></html>");
		jLabel[1].setText("<html><font size=6>���ĵ�½��ַΪ��<u>"+location+"</u></font></html>");
		for(int i=0;i<weather.length;i++)
		{
			jLabel[2+i].setText("<html><font size=5>"+weather[i]+"</font></html>");
		}
		for(int i = 0;i < jLabel.length;i++){
			jLabel[i].setBounds(100, 200+i*40, 600, 40);
			this.add(jLabel[i]);
		}

	}
}