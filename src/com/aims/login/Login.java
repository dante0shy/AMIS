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
	}; //����������ſռ������
	private JLabel[] jlabel ={
			new JLabel("�� �� ��"),//������ʾ��ǩ
			new JLabel("Server IP"),
			new JLabel("�� �� ��"),
			new JLabel("��    ��"),
			new JLabel("")
	};
	
	//����������ַ���˿ںš��û��������������
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
	private JRadioButton[] jrbArray=//������ѡ��ť����
	        {
				new JRadioButton("������Ա"),
	        	new JRadioButton("ѧ    ��"),
	        	new JRadioButton("������Ա",true),
	        	new JRadioButton("��������Ա"),        	
	        };
	//������
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
	//������
	public Login()
	{ 
	    this.addListener();
		initialFrame();//��ʼ������
	}

	public void addListener(){
		this.jblogin.addActionListener(this);//Ϊ��½��ťע�������
		this.jbreset.addActionListener(this);//Ϊ���ð�ťע�������

		for(int i = 0;i < 3;i++)
		{
			this.jTextFile[i].addActionListener(this);//Ϊ�û����ı���ע�������
		}
		this.pasw.addActionListener(this);
		for(int i = 0;i < 4;i++)
		{
			this.jrbArray[i].addActionListener(this);							
		}
	}
	public void initialFrame()
	{
		//��Ϊ�ղ���
		jp.setLayout(null);
		//���ؼ���ӵ�������Ӧλ��
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
		
		//���ô��ڵı��⡢��С��λ���Լ��ɼ���
		this.setTitle("��½AIMS");
		String path1 = "./images/AIMS.png";
		Image image=new ImageIcon(path1).getImage();  
 		this.setIconImage(image);
		this.setResizable(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=300;//��������
		int h=320;//������߶�
		this.setBounds(centerX-w/2,centerY-h/2,w,h);//���ô����������Ļ����
		this.setVisible(true);
		//����д�������ı�����ΪĬ�Ͻ���
		this.jTextFile[0].requestFocus(true);
		this.jTextFile[0].setText("localhost");
		this.jTextFile[1].setText("1433");
		this.jTextFile[2].setText("M201215072");
		this.pasw.setText("19930514");
	}
	
	//ʵ��ActionListener�ӿ��еķ���
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
		{//���µ�½��ť
			System.out.println("in");
			this.jlabel[4].setText("������֤�����Ժ�...");//������ʾ��Ϣ
			String xxx = new String(this.pasw.getPassword());
			System.out.println(xxx);
			//��ȡ�û������������ַ���˿ںš��û���������
			String hostadd=this.jTextFile[0].getText().trim();
			if(hostadd.equals("")){
				JOptionPane.showMessageDialog(this,"������������ַ","����",
				                                  JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			String port=this.jTextFile[1].getText();
			if(port.equals("")){
				JOptionPane.showMessageDialog(this,"������˿ں�","����",
				                                  JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			String person_id=this.jTextFile[2].getText().trim();
			if(person_id.equals("")){
				JOptionPane.showMessageDialog(this,"�������û���","����",
				                               JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			String pwd=new String(this.pasw.getPassword());
			if(pwd.equals("")){
				JOptionPane.showMessageDialog(this,"����������","����",
				                           JOptionPane.ERROR_MESSAGE);
				jlabel[4].setText("");return;
			}
			int type = 0;
			while(!this.jrbArray[type].isSelected())
			{
				type++;
			}//��ȡ��½����
			try{   
				ResultSet rs = DButil.getLoginInfo(person_id);
				System.out.println(rs);
				if(rs.next())
				{
					if(!rs.getString(2).equals(pwd))
					{//����������ʾ����
						JOptionPane.showMessageDialog(this,"�û������������","����",
								JOptionPane.ERROR_MESSAGE);
						jlabel[4].setText("");
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
								if(DButil.loginSys(person_id))
									new AdminClient(person_id);
								System.out.println("login");
								this.dispose();//�رյ�½���ڲ��ͷ���Դ
							}
							
						}
					}
	            }
			}
			catch(SQLException ea){ea.printStackTrace();}
		}
		else if(e.getSource() == this.jbreset){//�������ð�ť,���������Ϣ
			this.jTextFile[2].setText("");
			this.pasw.setText("");
		}
		else if(e.getSource() == jTextFile[2]){//�������û������س�ʱ
			this.pasw.requestFocus(true);
		}
		else if(e.getSource() == pasw){//���������벢�س�ʱ	
			this.jblogin.requestFocus(true);
		}
		else if(e.getSource() == this.jTextFile[0]){//������������ַ���س�ʱ
			this.jTextFile[1].requestFocus(true);
		}
		else if(e.getSource() == this.jTextFile[1]){//������˿ںŲ��س�ʱ
			this.jTextFile[2].requestFocus(true);
		}
	}
	public static void main(String[] args)
	{
		new Login();
	}
}