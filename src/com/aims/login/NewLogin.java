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
	//����������ַ���˿ںš��û��������������
	private JTextField jTextFile = new JTextField();
	
	private JPasswordField pasw = new JPasswordField() 
    {  
		private static final long serialVersionUID = -4411225445163644877L;

		public void paste(){  
              
        }  
    }; 
	private JRadioButton[] jrbArray=//������ѡ��ť����
	        {
				new JRadioButton(""),
	        	new JRadioButton("",true),
	        	new JRadioButton(""),
	        	new JRadioButton(""),        	
	        };
	//������
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
		jTextFile.setFont(new Font("����", Font.PLAIN, 20));
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
		this.jblogin.addActionListener(this);//Ϊ��½��ťע�������
		this.jbquit.addActionListener(this);//Ϊ���ð�ťע�������
		this.jTextFile.addActionListener(this);//Ϊ�û����ı���ע�������
		this.pasw.addActionListener(this);
		for(int i = 0;i < 4;i++)
		{
			this.jrbArray[i].addActionListener(this);							
		}
	}
	
	//ʵ��ActionListener�ӿ��еķ���
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
			{//���µ�½��ť
				jlMessage.setFont(new java.awt.Font(" ������", Font.PLAIN, 18));
				this.jlMessage.setText("Loding,���Ժ�...");
				String person_id=this.jTextFile.getText().trim();
				if(person_id.equals("")){
					JOptionPane.showMessageDialog(this,"�������û���","����",
					                               JOptionPane.ERROR_MESSAGE);
					return;
				}
				String pwd=new String(this.pasw.getPassword());
				if(pwd.equals("")){
					JOptionPane.showMessageDialog(this,"����������","����",
					                           JOptionPane.ERROR_MESSAGE);
				}
				int type = 0;
				while(!this.jrbArray[type].isSelected())
				{
					type++;
				}//��ȡ��½����
				if(type == 0)
				{
					System.out.println("viewer login");
					new Viewer();
					this.dispose();//�رյ�½���ڲ��ͷ���Դ
				}
				else
				{
					try{   
						ResultSet rs = DButil.getLoginInfo(person_id);
						System.out.println(rs);
						if(rs.next())
						{
							if(!rs.getString(2).equals(pwd))
							{//����������ʾ����
								JOptionPane.showMessageDialog(this,"�û������������","����",
										JOptionPane.ERROR_MESSAGE);
							}else
							{
								if(rs.getString(4).equals(online))
								{
									JOptionPane.showMessageDialog(this,"�û�������","����",
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
										this.dispose();//�رյ�½���ڲ��ͷ���Դ
									}
										
									
									
								}
							}
			            }
						else
						{
							JOptionPane.showMessageDialog(this,"�û������������","����",
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
		                 "��ȷ��Ҫ�˳���ϵͳ��","ϵͳ�˳�",
		                  JOptionPane.YES_NO_OPTION,
		                   JOptionPane.QUESTION_MESSAGE);
		      		if(i==0)
		      		{
		      			System.exit(0);
		      		}
			}
			else if(e.getSource() == jTextFile){//�������û������س�ʱ
				this.pasw.requestFocus(true);
			}
			else if(e.getSource() == pasw){//���������벢�س�ʱ	
				this.jblogin.requestFocus(true);
			}
		}
		
	public static void main(String[] args) {       
		new NewLogin().setVisible(true);    
	}
}
