package com.aims.login;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.aims.DButil.DButil;
import com.aims.admin.AdminClient;
import com.aims.freamlistener.FrameListener;
import com.aims.student.StudentClient;
import com.aims.superadmin.SuperAdminClient;
import com.aims.viewer.Viewer;
import com.sun.awt.AWTUtilities;

public class NewLogin extends JFrame implements ActionListener
{    
	private static final long serialVersionUID = -4397488783839273074L;
	private String online = "1";
	//创建主机地址、端口号、用户名和密码输入框
	private JTextField jTextFile = new JTextField();
	
	private JPasswordField pasw = new JPasswordField() 
    {  
		private static final long serialVersionUID = -4411225445163644877L;

		public void paste(){  
              
        }  
    }; 
	private JRadioButton[] jrbArray=//创建单选按钮数组
	        {
				new JRadioButton(""),
	        	new JRadioButton("",true),
	        	new JRadioButton(""),
	        	new JRadioButton(""),        	
	        };
	//创建组
	private ButtonGroup bg = new ButtonGroup();
	private JButton jblogin = new JButton("Login");
	private JButton jbquit = new JButton("Quit");
	private JLabel jlMessage = new JLabel("");
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
	public NewLogin() {        
		setUndecorated(true);        
		final ImageIcon image = new ImageIcon("./images/login.png");        
		JPanel jPanel = new JPanel() {            
			private static final long serialVersionUID = 4058837180013498340L;

			@Override            
			public void paint(Graphics g) {               
				image.paintIcon(this, g, 0, 0); 
				super.paint(g);
				}        
			};        
		this.init(jPanel);
		this.addListener();
		jPanel.setSize(image.getIconWidth(), image.getIconHeight());        
		jPanel.setOpaque(false);        
		getContentPane().add(jPanel, BorderLayout.CENTER);        
		setSize(image.getIconWidth(), image.getIconHeight());        
		AWTUtilities.setWindowOpaque(this, false);        
		setLocationRelativeTo(null);  
		FrameListener moveListener = new FrameListener(this);
        
        addMouseListener(moveListener);
        addMouseMotionListener(moveListener);
	}    
	private void init(JPanel jpanel) {        
		jpanel.setLayout(null); 
		this.jTextFile.setBounds(65, 75, 190, 25);
		jTextFile.setFont(new Font("楷体", Font.PLAIN, 20));
		this.jTextFile.setOpaque(false);
		this.jTextFile.setBorder(null);
		jpanel.add(jTextFile);
		this.pasw.setBounds(65, 133, 190, 25);
		this.pasw.setOpaque(false);
		this.pasw.setBorder(null);
		jpanel.add(pasw);
		
		this.jrbArray[0].setBounds(40 ,175, 20,20);
		this.jrbArray[1].setBounds(150,175, 20,20);
		this.jrbArray[2].setBounds(40 ,200, 20,20);
		this.jrbArray[3].setBounds(150,200, 20,20);
		for(int i = 0;i<4;i++)
		{
			jpanel.add(jrbArray[i]);
			jrbArray[i].setContentAreaFilled(false); 
			this.bg.add(jrbArray[i]);
		}		
	
		this.jlMessage.setBounds(30, 270, 200, 25);
		jpanel.add(jlMessage);
		this.jblogin.setBounds(25,235,100,30);
		this.jblogin.setOpaque(false);
		jpanel.add(jblogin);
		this.jbquit.setBounds(170,235,100,30);
		this.jbquit.setOpaque(false);
		jpanel.add(jbquit);
		
		String path1 = "./images/AIMS.png";
		Image image=new ImageIcon(path1).getImage();  
 		this.setIconImage(image);
	}  
	
	public void addListener(){
		this.jblogin.addActionListener(this);//为登陆按钮注册监听器
		this.jbquit.addActionListener(this);//为重置按钮注册监听器
		this.jTextFile.addActionListener(this);//为用户名文本框注册监听器
		this.pasw.addActionListener(this);
		for(int i = 0;i < 4;i++)
		{
			this.jrbArray[i].addActionListener(this);							
		}
	}
	
	//实现ActionListener接口中的方法
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == this.jrbArray[0])
			{
	    		this.jTextFile.setText("anonymous");
	    		this.pasw.setText("123456");
	    		for(int i = 0;i < 3;i++)
	    		{
	    			this.jTextFile.setEditable(false);    			
	    		}      	
	        	this.pasw.setEditable(false);
			}
			else if(e.getSource() == this.jrbArray[1]||e.getSource() == this.jrbArray[2]||e.getSource() == this.jrbArray[3])
			{
	    		this.jTextFile.requestFocus(true);
				this.jTextFile.setEditable(true);
	        	this.pasw.setEditable(true);
			}
			if(e.getSource() == this.jblogin)
			{//按下登陆按钮
				jlMessage.setFont(new java.awt.Font(" 新宋体", Font.PLAIN, 18));
				this.jlMessage.setText("Loding,请稍后...");
				String person_id=this.jTextFile.getText().trim();
				if(person_id.equals("")){
					JOptionPane.showMessageDialog(this,"请输入用户名","错误",
					                               JOptionPane.ERROR_MESSAGE);
					return;
				}
				String pwd=new String(this.pasw.getPassword());
				if(pwd.equals("")){
					JOptionPane.showMessageDialog(this,"请输入密码","错误",
					                           JOptionPane.ERROR_MESSAGE);
				}
				int type = 0;
				while(!this.jrbArray[type].isSelected())
				{
					type++;
				}//获取登陆类型
				if(type == 0)
				{
					System.out.println("viewer login");
					new Viewer();
					this.dispose();//关闭登陆窗口并释放资源
				}
				else
				{
					try{   
						ResultSet rs = DButil.getLoginInfo(person_id);
						System.out.println(rs);
						if(rs.next())
						{
							if(!rs.getString(2).equals(pwd))
							{//弹出错误提示窗口
								JOptionPane.showMessageDialog(this,"用户名或密码错误","错误",
										JOptionPane.ERROR_MESSAGE);
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
										DButil.Power = type+"";
										System.out.println("power"+DButil.Power);
										if(DButil.Power.equals(DButil.Admin))
										{
											if(DButil.loginSys(person_id))
												new AdminClient(person_id);
											System.out.println("admin login");
										}
										else if(DButil.Power.equals(DButil.Student))
										{
											new StudentClient(person_id);
										}
										else
										{
											new SuperAdminClient(person_id);
										}
										this.dispose();//关闭登陆窗口并释放资源
									}
										
									
									
								}
							}
			            }
						else
						{
							JOptionPane.showMessageDialog(this,"用户名或密码错误","错误",
									JOptionPane.ERROR_MESSAGE);
							this.jlMessage.setText("");
						}
					}
					catch(SQLException ea){
						ea.printStackTrace();
						}
				}
			}
			else if(e.getSource() == jbquit)
			{
				int i=JOptionPane.showConfirmDialog(null,
		                 "您确认要退出出系统吗？","系统退出",
		                  JOptionPane.YES_NO_OPTION,
		                   JOptionPane.QUESTION_MESSAGE);
		      		if(i==0)
		      		{
		      			System.exit(0);
		      		}
			}
			else if(e.getSource() == jTextFile){//当输入用户名并回车时
				this.pasw.requestFocus(true);
			}
			else if(e.getSource() == pasw){//当输入密码并回车时	
				this.jblogin.requestFocus(true);
			}
		}
		
	public static void main(String[] args) {       
		new NewLogin().setVisible(true);    
	}
}
