package com.aims.changepasw;

import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import com.aims.DButil.DButil;
public class ChangePasw extends JLabel implements ActionListener
{ 
	
	private static final long serialVersionUID = -4203520323357532827L;
	private String person_id;
	//创建信息提示标签数组
	private JLabel[] jLabel={
			new JLabel("用户名"),new JLabel("原始密码"),
			new JLabel("新密码"),new JLabel("确认新密码")
			};
	private JLabel[] jLabel1={
			new JLabel(""),new JLabel(""),
			new JLabel(""),new JLabel("")
			};
	//创建用户名输入框
	private JTextField userID=new JTextField();
	//创建密码框数组
	private JPasswordField[] jPaswField={
			new JPasswordField(),
	        new JPasswordField(),
	        new JPasswordField()
			};
	//创建操作按钮
	private JButton[] jButton={new JButton("确认"),new JButton("重置")};
	//构造器                          
	public ChangePasw(String person_id)
	{ 
		this.person_id = person_id;
	   //初始化页面
		this.initialFrame();
		//为按钮注册监听器
		this.addListener();
	}
	public void addListener()
	{   //为文本框注册监听器
	    userID.addActionListener(this);
	    //为密码框注册监听器
	    jPaswField[0].addActionListener(this);
	    jPaswField[1].addActionListener(this);
	    jPaswField[2].addActionListener(this);
		userID.setText(person_id);
		userID.setEditable(false);
		
	    //为按钮注册监听器
		jButton[0].addActionListener(this);
		jButton[1].addActionListener(this);
	}
	//初始化页面的方法
	public void initialFrame()
	{   //设为空布局
		this.setLayout(null);
		//将控件放入相应的位置
		for(int i=0;i<jLabel.length;i++)
		{
			jLabel[i].setBounds(230,170+50*i,150,30);
			this.add(jLabel[i]);
			jLabel1[i].setBounds(480,170+50*i,150,30);
			this.add(jLabel1[i]);
			if(i==0)
			{
				userID.setBounds(330,170+50*i,150,30);
				this.add(userID);
			}
			else
			{
				jPaswField[i-1].setBounds(330,170+50*i,150,30);
				this.add(jPaswField[i-1]);
			}
			this.setOpaque(false);
		}
		for(int i = 0;i < jButton.length;i++)
		{
			jButton[i].setBounds(230+i*150,380,100,30);
			jButton[i].setFocusPainted(false);
			jButton[i].setOpaque(false);
			this.add(jButton[i]);
		}
		
	}
	//实现ActionListener接口中的方法
	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource()==userID)//输入用户名并回车后
		{
			jPaswField[0].requestFocus(true);
		}
		else if(e.getSource()==jPaswField[0])//输入原始密码并回车后
		{
			jPaswField[1].requestFocus(true);
		}
		else if(e.getSource()==jPaswField[1])//输入新密码并回车后
		{
			jPaswField[2].requestFocus(true);
		}
		else if(e.getSource()==jPaswField[2])//输入确认新密码并回车后
		{
			jButton[0].requestFocus(true);
		}
		else if(e.getSource()==jButton[1])
		{////按下重置按钮的处理代码
		     //将输入信息清空
			resetPaswfileds();
		}
		else if(e.getSource()==jButton[0])
		{
			//按下确认按钮的处理代码
		    //用于判断密码格式的正则式字符串
			String patternStr="[0-9a-zA-Z]{6,12}";
			String user_id=userID.getText().trim();//获取用户名
			if(user_id.equals(""))
			{//用户名为空
				JOptionPane.showMessageDialog(this,"请输入用户名",
				                "错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String oldPwd=new String(jPaswField[0].getPassword()).trim();//获取原始密码
			if(oldPwd.equals(""))
			{//原始密码为空
				JOptionPane.showMessageDialog(this,"请输入原始密码",
				                  "错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else
			{
				ResultSet rs = DButil.getLoginInfo(person_id);
				try {
					if(rs.next())
					{
						if(!rs.getString(2).equals(oldPwd))
						{
							jLabel1[1].setText("原始密码输入错误");
							return;
						}
						else
							jLabel1[1].setText("");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			String newPwd=new String(jPaswField[1].getPassword()).trim();//获取新密码
			if(newPwd.equals(""))
			{//新密码为空
				JOptionPane.showMessageDialog(this,"请输入新密码",
				                "错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!newPwd.matches(patternStr))
			{//新密码不符合格式
				JOptionPane.showMessageDialog(this,
				                  "密码只能是6到12位的字母或数字",
				                  "错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String newPwd1=new String(jPaswField[2].getPassword()).trim();//获取确认密码
			if(!newPwd.equals(newPwd1))
			{
				this.jLabel1[3].setText("确认密码与新密码不符");
				return;
			}
			else
			{
				this.jLabel1[3].setText("");
			}
			try
			{   //初始化数据库连接并更改密码
				if(DButil.updatePasw(user_id, newPwd))
				{//更改成功
					resetPaswfileds();
					JOptionPane.showMessageDialog(this,"密码修改成功",
					           "提示",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{//更改失败
					JOptionPane.showMessageDialog(this,
					      "修改失败，请检查您的用户名或密码是否正确",
					      "错误",JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	private void resetPaswfileds() {
		for(int i=0;i<jPaswField.length;i++)
		{
			jPaswField[i].setText("");
		}
	}
	public void setFocus()
	{
		userID.requestFocus(true);
	}
	
	public static void main(String[] args)
	{
		ChangePasw cpt=new ChangePasw("U201215072");
		JFrame jframe=new JFrame();
		jframe.add(cpt);
		jframe.setBounds(70,20,700,650);
		jframe.setVisible(true);
	}
}