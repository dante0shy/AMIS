package com.aims.welcome;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Map;

import javax.swing.*;

import com.aims.DButil.DButil;
public class Welcome extends JLabel {
	private static final long serialVersionUID = -6084396013307461295L;
	private String str;//要显示的字符串的引用
	private String status = "学员";
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
			city = "未知";
			location  = "未知";
		}
		else
		{
			String s[] = content.split(" ");
			ip = s[0];
			location = s[1];
			System.out.println(location);
			int start = location.indexOf("省")+1;
			int end   = location.indexOf("市");
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
			status = "管理员";
		else if(DButil.Power.equals(DButil.SuperAdmin))
			status = "超级管理员";
		Map<String,String> map = weather.getWeather(city);
		String[] weather ={
				"今天白天："+map.get(nodes[1]),"最高温度："+map.get(nodes[2])+"℃ ",
				"今天夜间："+map.get(nodes[3]),"最低温度："+map.get(nodes[4])+"℃ "
		};
		jLabel[0].setText("<html><font size=6>欢迎  <font color=#ff0041>"+status+"</font>  "+str+"  登陆</font></html>");
		jLabel[1].setText("<html><font size=6>您的登陆地址为：<u>"+location+"</u></font></html>");
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