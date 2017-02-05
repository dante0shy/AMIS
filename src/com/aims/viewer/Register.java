package com.aims.viewer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.aims.DButil.DButil;

public class Register extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 7736219759295993301L;
	private ResultSet rs;
	private Map<String, String> apar_info = new HashMap<String,String>();
	private Vector<String> viewInfo = new Vector<String>();
	private JScrollPane jScrollPane;
	private JLabel jTitle = new JLabel();
	private JLabel[] jLabel1 = {
			new JLabel("访问楼栋"),new JLabel("姓名"),
			new JLabel("受访学生学号"),new JLabel("访问原因")
	};
	
	private JComboBox<String> jcb = new JComboBox<String>();
	
	private JButton[] jButton = {
			new JButton("清空"),new JButton("登记")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField()
	};
	
	private JTextArea jTextArea = new JTextArea();
	
	public Register()
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
			Set<String> keyset = apar_info.keySet();
			Iterator<String> i = keyset.iterator();
			while(i.hasNext())
			{
				String apar_name = (String)i.next();
				jcb.addItem(apar_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//初始化页面的方法
	public void initialFrame()
	{   
		initialJPanel1();
		this.setVisible(true);
	}
	
	
	private void initialJPanel1() {
		this.setLayout(null);
		jTitle.setText("<HTML><font size=7>访客登记</font></html>");
		jTitle.setBounds(50, 20, 200, 50);
		this.add(jTitle);
		for(int i = 0;i < jLabel1.length; i++)
		{
			jLabel1[i].setBounds(20, 100+70*i, 100, 20);
			this.add(jLabel1[i]);
		}
		this.setBackground(Color.white);
		jcb.setBounds(20, 130, 150, 30);
		jcb.setFocusable(false);
		this.add(jcb);
		jTextField[0].setBounds(20, 200, 150, 30);
		this.add(jTextField[0]);
		jTextField[1].setBounds(20, 270, 150, 30);
		this.add(jTextField[1]);
		jTextArea.setTabSize(4);
		jTextArea.setLineWrap(true);
		jScrollPane = new JScrollPane(jTextArea);
		jScrollPane.setBounds(20, 340, 210, 130);
		this.add(jScrollPane);
		for(int i = 0;i < jButton.length;i++)
		{
			jButton[i].setBounds( 20+i*110, 500, 100, 30);
			jButton[i].setFocusPainted(false);
			jButton[i].setOpaque(false);
			this.add(jButton[i]);
		}
		
		this.setOpaque(false);
	}
		
	public void addListener()
	{   
		jButton[0].addActionListener(this);
		jButton[1].addActionListener(this);
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
			String apar_name = (String)jcb.getSelectedItem();
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
			if(event.equals("")){ JOptionPane.showMessageDialog(null,"来访原因不可为空"); return;}
			else if(event.length() > 30){ JOptionPane.showMessageDialog(null,"来访原因不可超过30字"); return;}
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
	}

	private void resetText() {
		jTextField[0].setText("");
		jTextField[1].setText("");
		jTextArea.setText("");
	}
	
	public static void main(String[] args)
	{		
		Register cpt=new Register();
		JFrame jframe=new JFrame();
		jframe.add(cpt);
		jframe.setBounds(70,20,280,650);
		jframe.setVisible(true);	
	}
}
