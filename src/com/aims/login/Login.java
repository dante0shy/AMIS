package com.aims.login;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

import com.aims.DButil.DButil;
import com.aims.admin.AdminClient;
public class Login extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4397488783839273074L;
	private String online = "1";
	private JPanel jp = new JPanel() {
		 /**
		 * 
		 */
		private static final long serialVersionUID = 6380398373168807406L;

		public void paintComponent(Graphics g) {
			 String path = "./images/background0.jpg";
			 super.paintComponent(g);
			 ImageIcon img = new ImageIcon(path);
			 g.drawImage(img.getImage(), 0, 0, null);
			 }
	}; //创建用来存放空间的容器
	private JLabel[] jlabel ={
			new JLabel("端 口 号"),//创建提示标签
			new JLabel("Server IP"),
			new JLabel("用 户 名"),
			new JLabel("密    码"),
			new JLabel("")
	};
	
	//创建主机地址、端口号、用户名和密码输入框
	private JTextField[] jTextFile ={
			new JTextField(),//hostaddress
			new JTextField(),//hostport
			new JTextField(),//user_id
	};
	private JPasswordField pasw = new JPasswordField() 
    {  
        /**
		 * 
		 */
		private static final long serialVersionUID = -4411225445163644877L;

		public void paste(){  
              
        }  
    }; 
	private JRadioButton[] jrbArray=//创建单选按钮数组
	        {
				new JRadioButton("外来人员"),
	        	new JRadioButton("学    生"),
	        	new JRadioButton("管理人员",true),
	        	new JRadioButton("超级管理员"),        	
	        };
	//创建组
	private ButtonGroup bg = new ButtonGroup();
	//ImageIcon icon = new ImageIcon(path);
	private JButton jblogin = new JButton("Login");
	private JButton jbreset = new JButton("Reset");
	static {
		try {
			UIManager.setLookAndFeel(
			         UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}
	//构造器
	public Login()
	{ 
	    this.addListener();
		initialFrame();//初始化界面
	}

	public void addListener(){
		this.jblogin.addActionListener(this);//为登陆按钮注册监听器
		this.jbreset.addActionListener(this);//为重置按钮注册监听器

		for(int i = 0;i < 3;i++)
		{
			this.jTextFile[i].addActionListener(this);//为用户名文本框注册监听器
		}
		this.pasw.addActionListener(this);
		for(int i = 0;i < 4;i++)
		{
			this.jrbArray[i].addActionListener(this);							
		}
	}
	public void initialFrame()
	{
		//设为空布局
		jp.setLayout(null);
		//将控件添加到容器相应位置
		for(int i = 0;i < 4;i++)
		{
			if(i != 3)
			{
				this.jTextFile[i].setBounds(140,50+i*30,110,25);
				//this.jTextFile[i].setBorder(null);
				//this.jTextFile[i].setBackground(null);
				//this.jTextFile[i].setOpaque(false);
				this.jp.add(jTextFile[i]);
				
			}
			else 
			{
				this.pasw.setBounds(140,50+i*30,110,25);
				this.jp.add(pasw);
			}
			this.jlabel[i].setBounds(50,50+i*30,110,25);
			this.jp.add(jlabel[i]);
			this.jp.add(jrbArray[i]);
			jrbArray[i].setContentAreaFilled(false); 
			this.bg.add(jrbArray[i]);
		}
		this.jlabel[4].setBounds(90,250,200,25);
		this.jp.add(jlabel[4]);
		
		this.jrbArray[0].setBounds(50,170,100,20);
		this.jrbArray[1].setBounds(160,170,100,20);
		this.jrbArray[2].setBounds(50,190,100,20);
		this.jrbArray[3].setBounds(160,190,100,20);
	
		this.jblogin.setBounds(50,215,85,25);
		this.jbreset.setBounds(160,215,85,25);
		this.jblogin.setOpaque(false);
		this.jbreset.setOpaque(false);
		this.jp.add(jblogin);
		this.jp.add(jbreset);
		
		this.add(jp);
		
		//设置窗口的标题、大小、位置以及可见性
		this.setTitle("登陆AIMS");
		String path1 = "./images/AIMS.png";
		Image image=new ImageIcon(path1).getImage();  
 		this.setIconImage(image);
		this.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=300;//本窗体宽度
		int h=320;//本窗体高度
		this.setBounds(centerX-w/2,centerY-h/2,w,h);//设置窗体出现在屏幕中央
		this.setVisible(true);
		//将填写姓名的文本框设为默认焦点
		this.jTextFile[0].requestFocus(true);
		this.jTextFile[0].setText("localhost");
		this.jTextFile[1].setText("1433");
		this.jTextFile[2].setText("M201215072");
		this.pasw.setText("19930514");
	}
	
	//实现ActionListener接口中的方法
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == this.jrbArray[0])
		{
			this.jTextFile[0].setText("localhost");
    		this.jTextFile[1].setText("1433");
    		this.jTextFile[2].setText("anonymous");
    		this.pasw.setText("123456");
    		for(int i = 0;i < 3;i++)
    		{
    			this.jTextFile[i].setEditable(false);    			
    		}      	
        	this.pasw.setEditable(false);
		}
		else if(e.getSource() == this.jrbArray[1]||e.getSource() == this.jrbArray[2]||e.getSource() == this.jrbArray[3])
		{
			this.jTextFile[0].setText("localhost");
    		this.jTextFile[1].setText("1433");
    		this.jTextFile[2].setText("");
    		this.pasw.setText("");
			this.jTextFile[0].setEditable(true);
        	this.jTextFile[1].setEditable(true);
        	this.jTextFile[2].setEditable(true);
        	this.pasw.setEditable(true);
		}
		System.out.println("over");
		if(e.getSource() == this.jblogin)
		{//按下登陆按钮
			System.out.println("in");
			this.jlabel[4].setText("正在验证，请稍后...");//设置提示信息
			String xxx = new String(this.pasw.getPassword());
			System.out.println(xxx);
			//获取用户输入的主机地址、端口号、用户名与密码
			String hostadd=this.jTextFile[0].getText().trim();
			if(hostadd.equals("")){
				JOptionPane.showMessageDialog(this,"请输入主机地址","错误",
				                                  JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			String port=this.jTextFile[1].getText();
			if(port.equals("")){
				JOptionPane.showMessageDialog(this,"请输入端口号","错误",
				                                  JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			String person_id=this.jTextFile[2].getText().trim();
			if(person_id.equals("")){
				JOptionPane.showMessageDialog(this,"请输入用户名","错误",
				                               JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			String pwd=new String(this.pasw.getPassword());
			if(pwd.equals("")){
				JOptionPane.showMessageDialog(this,"请输入密码","错误",
				                           JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			int type = 0;
			while(!this.jrbArray[type].isSelected())
			{
				type++;
			}//获取登陆类型
			try{   
				ResultSet rs = DButil.getLoginInfo(person_id);
				System.out.println(rs);
				if(rs.next())
				{
					if(!rs.getString(2).equals(pwd))
					{//弹出错误提示窗口
						JOptionPane.showMessageDialog(this,"用户名或密码错误","错误",
								JOptionPane.ERROR_MESSAGE);
						jlabel[4].setText("");
					}else
					{
						if(rs.getString(4).equals(online))
						{
							JOptionPane.showMessageDialog(this,"用户已在线","错误",
									JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							if(rs.getString(3).equals(type+""))
							{
								rs.close();
								if(DButil.loginSys(person_id))
									new AdminClient(person_id);
								System.out.println("login");
								this.dispose();//关闭登陆窗口并释放资源
							}
							
						}
					}
	            }
			}
			catch(SQLException ea){ea.printStackTrace();}
		}
		else if(e.getSource() == this.jbreset){//按下重置按钮,清空输入信息
			this.jTextFile[2].setText("");
			this.pasw.setText("");
		}
		else if(e.getSource() == jTextFile[2]){//当输入用户名并回车时
			this.pasw.requestFocus(true);
		}
		else if(e.getSource() == pasw){//当输入密码并回车时	
			this.jblogin.requestFocus(true);
		}
		else if(e.getSource() == this.jTextFile[0]){//当输入主机地址并回车时
			this.jTextFile[1].requestFocus(true);
		}
		else if(e.getSource() == this.jTextFile[1]){//当输入端口号并回车时
			this.jTextFile[2].requestFocus(true);
		}
	}
	public static void main(String[] args)
	{
		new Login();
	}
}