package com.aims.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.*;

import com.aims.DButil.DButil;
public class Viewer2 extends JFrame implements ActionListener
{ 
	private ResultSet rs;
	private Map<String, String> apar_info = new HashMap<String,String>();
	private Vector<String> viewInfo = new Vector<String>();
	private JPanel jp1 = new JPanel();
	private JPanel jp2 = new JPanel();
	private JScrollPane jScrollPane;
	private JSplitPane jsp = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,jp1,jp2);
	JLabel[] jTitle ={
			new JLabel(),new JLabel()
	};
	JLabel[] jLabel1 = {
			new JLabel("访问楼栋"),new JLabel("姓名"),
			new JLabel("受访学生学号"),new JLabel("访问原因")
	};
	JLabel[] jLabel2 = {
			new JLabel("访问楼栋"),new JLabel("访问日期")
	};
	private JComboBox[] jcb = {
		new JComboBox(),new JComboBox()	
	};
	
	private JButton[] jButton = {
			new JButton("清空"),new JButton("登记")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField()
	};
	
	private JTextArea jTextArea = new JTextArea();
	
	public Viewer2()
	{ 
		this.initialData();
	   //初始化页面
		this.initialFrame();
		//为按钮注册监听器
		this.addListener();
	}
	
	private void initialData() {
		rs = DButil.getAparInfo();
		try {
			while(rs.next())
			{
				String apar_name = rs.getString(1).trim();
				String apar_id = rs.getString(2).trim();
				apar_info.put(apar_name, apar_id);
			}
			Set keyset = apar_info.keySet();
			Iterator i = keyset.iterator();
			while(i.hasNext())
			{
				String apar_name = (String)i.next();
				jcb[0].addItem(apar_name);
				jcb[1].addItem(apar_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//初始化页面的方法
	public void initialFrame()
	{   
		initialJPanel1();
		initialJPanel2();
		this.add(jsp);
		jsp.setDividerSize(2);
		jsp.setDividerLocation(250);
		this.setTitle("访问登记");
		Image image=new ImageIcon("./images/AIMS.png").getImage();  
		this.setIconImage(image);
		Dimension screenSize = Toolkit.
	             getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=1200;//本窗体宽度
		int h=650;//本窗体高度
		//设置窗体出现在屏幕中央
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	
	private void initialJPanel1() {
		jp1.setLayout(null);
		jTitle[0].setText("<HTML><font size=7>访客登记</font></html>");
		jTitle[0].setBounds(50, 20, 200, 50);
		jp1.add(jTitle[0]);
		for(int i = 0;i < jLabel1.length; i++)
		{
			jLabel1[i].setBounds(20, 100+70*i, 100, 20);
			jp1.add(jLabel1[i]);
		}
		jp1.setBackground(Color.white);
		jcb[0].setBounds(20, 130, 150, 30);
		jp1.add(jcb[0]);
		jTextField[0].setBounds(20, 200, 150, 30);
		jp1.add(jTextField[0]);
		jTextField[1].setBounds(20, 270, 150, 30);
		jTextField[1].setText("U201215072");
		jp1.add(jTextField[1]);
		jTextArea.setTabSize(4);
		jTextArea.setLineWrap(true);
		jScrollPane = new JScrollPane(jTextArea);
		jScrollPane.setBounds(20, 340, 210, 130);
		jp1.add(jScrollPane);
		
		jButton[0].setBounds( 20, 500, 100, 30);
		jp1.add(jButton[0]);
		jButton[1].setBounds(130, 500, 100, 30);
		jp1.add(jButton[1]);
	}
	
	private void initialJPanel2() {
		jp2.setLayout(null);
		jTitle[1].setText("<HTML><font size=7>登记记录</font></html>");
		jTitle[1].setBounds(40, 20, 200, 50);
		jp2.add(jTitle[1]);
		
		for(int i = 0;i < jLabel2.length;i++)
		{
			jLabel2[i].setBounds(280+i*250, 35, 100, 20);
			jp2.add(jLabel2[i]);
		}
		jcb[1].setBounds(350, 30, 150, 30);
		jp2.add(jcb[1]);
	}

	public void addListener()
	{   
		jButton[0].addActionListener(this);
		jButton[1].addActionListener(this);
		jcb[0].addActionListener(this);
	}
	//实现ActionListener接口中的方法
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == jButton[0])
		{
			resetText();
		}
		else if(e.getSource() == jButton[1])
		{
			viewInfo.clear();
			String apar_name = (String)jcb[0].getSelectedItem();
			String apar_id = apar_info.get(apar_name);
			viewInfo.add(apar_id);
			
			String view_id = DButil.createAparID(apar_id);
			System.out.println(view_id+apar_id+apar_name);
			viewInfo.add(view_id);
			
			String stu_id = jTextField[1].getText().trim();
			boolean s = DButil.isStuInThisApar(stu_id, apar_id);
			if(!s){ JOptionPane.showMessageDialog(null,"此学生不居住在本楼栋，请检查后再试！"); return;}
			viewInfo.add(stu_id);

			String name = jTextField[0].getText().trim();
			if(name.equals("")){ JOptionPane.showMessageDialog(null,"本人姓名不可为空！"); return;}
			viewInfo.add(name);
			
			
			String event = jTextArea.getText().trim();
			if(event.equals("")){ JOptionPane.showMessageDialog(null,"来访时间不可为空"); return;}
			viewInfo.add(event);
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			String date = df.format(new Date());// new Date()为获取当前系统时间
			viewInfo.add(date);
			
			System.out.println(apar_id+view_id+stu_id+name+event+date);
			if(DButil.updateViewInfo(viewInfo))
			{
				resetText();
			};
		}
		else if(e.getSource() == jcb[1])
		{
		}
		
	}

	private void resetText() {
		jTextField[0].setText("");
		jTextField[1].setText("");
		jTextArea.setText("");
	}
	public static void main(String args[])
	{
		new Viewer2();
	}
}
