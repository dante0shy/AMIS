package com.aims.admin;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.aims.DButil.DButil;
import com.aims.datechooser.DateChooser;

public class AdminInfo extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 5487554357144947220L;
	private ResultSet rs;
	private String admin_id;
	private JLabel jlPhoto = new JLabel();//用来存放个人照片
	private String Power ;
	private DateChooser dateChooser = DateChooser.getInstance("yyyy-MM-dd");
	private Vector<String> adminInfo = new Vector<String>();
	private JLabel[] jLabel = {
			new JLabel("工号"),new JLabel("姓名"),new JLabel("身份"),
			new JLabel("性别"),new JLabel("籍贯"),new JLabel("出生年月日"),
			new JLabel("联系电话")//,new JLabel("")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField(),new JTextField(),
			new JTextField(),new JTextField(),new JTextField(),
			new JTextField()
	};
	private JButton[] jButton = {
			new JButton("修改"),new JButton("更换照片"),
			
	};
	public AdminInfo(String admin_id)
	{
		this.admin_id = admin_id;
		
		//初始化页面
		this.initialFrame();
		//为按钮注册监听器
		this.addListener();
	}

	private void initialFrame() {
		this.setLayout(null);
		jlPhoto.setBounds(380,130,150,170);//设置联系人图像JLabel的大小和位置
		jlPhoto.setOpaque(false);
		this.add(jlPhoto);
		for(int i = 0;i < jLabel.length;i++)
		{
			jLabel[i].setBounds(80,50+50*i,100,30);
			this.add(jLabel[i]);
		}		
		dateChooser.register(this.jTextField[5]);
		for (int i = 0; i < jTextField.length; i++) {
			jTextField[i].setBounds(160,50+50*i,150,30);
			this.add(jTextField[i]);			
		}
		jTextField[0].setEditable(false);
		jTextField[5].setEditable(false);
		for(int i = 0;i < jButton.length;i++)
		{
			jButton[i].setBounds(200+i*200,440,100,30);
			jButton[i].setOpaque(false);
			this.add(jButton[i]);
		}
		System.out.println(admin_id);
		this.jTextField[0].setText(admin_id);							
		showInfo();
	}
	
	private void addListener() {
		this.jButton[0].addActionListener(this);
		this.jButton[1].addActionListener(this);
		this.setOpaque(false);
	}
	
	public static void main(String[] args)
	{
		AdminInfo admin=new AdminInfo("M201215072");
		JFrame jframe=new JFrame();
		jframe.add(admin);
		jframe.setBounds(70,20,700,650);
		jframe.setVisible(true);
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e)  {
		if(e.getSource() == jButton[0])
		{
			getAdminInfo(admin_id);
			DButil.updateAdminInfo(adminInfo);
			showInfo();
		}
		if(e.getSource() == jButton[1])
		{
			Vector temp = DButil.choosePic(this);
			if(!temp.isEmpty())
			{
				Image icon1 = (Image) temp.get(0);
				String FileName = (String) temp.get(1);
				if(icon1 != null)
				{
					DButil.setPic(icon1,jlPhoto,AdminInfo.this,150,170);
					DButil.insertPic(admin_id,FileName);
					System.out.println("JFileChooser---->"+FileName);
				}
			}
		}
		
	}

	
	private void getAdminInfo(String admin_id) {
		adminInfo.clear();
		ResultSet rs1 = DButil.searchAdminInfo(admin_id);
		System.out.println(rs1);
		try {
			if(rs1.next())
			{
				for(int i = 0;i < jTextField.length;i++)
					adminInfo.add(jTextField[i].getText().trim());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void showInfo() {
		try {
			String person_id = jTextField[0].getText().toString();
			rs = DButil.searchAdminInfo(person_id);
			System.out.println(rs);
			if(rs.next())
			{
				for(int i = 1;i<jTextField.length;i++)
				{
					jTextField[i].setText(rs.getString(i+1));
				}
			}
			System.out.println(adminInfo);
			Image Pic=DButil.getPic(person_id);//从数据库得到此人的图像
			DButil.setPic(Pic,jlPhoto,AdminInfo.this,150,170);;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}