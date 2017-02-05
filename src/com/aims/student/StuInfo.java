package com.aims.student;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.aims.DButil.DButil;
import com.aims.datechooser.DateChooser;

public class StuInfo extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 5487554357144947220L;
	private ResultSet rs;
	private String stu_id;
	private JLabel jlPhoto = new JLabel();//用来存放个人照片
	private String Power ;
	private DateChooser dateChooser = DateChooser.getInstance("yyyy-MM-dd");
	private Vector<String> stuInfo = new Vector<String>();
	private Map<String, String> coll_info  = new HashMap<String,String>();
	private Map<String, String> dept_info  = new HashMap<String,String>();
	private Map<String, String> class_info = new HashMap<String,String>();
	private JLabel[] jLabel = {
		new JLabel("学号"),new JLabel("姓名"),new JLabel("学院"),
		new JLabel("专业"),new JLabel("班级"),new JLabel("性别"),
		new JLabel("籍贯"),new JLabel("家庭住址"),new JLabel("出生年月日"),
		new JLabel("联系电话")//,new JLabel("")
	};
	private JTextField[] jTextField = {
		new JTextField(),new JTextField(),new JTextField(),
		new JTextField(),new JTextField(),new JTextField(),
		new JTextField(),new JTextField(),new JTextField(),
		new JTextField(),
	};
	private String[] sex = {"男","女"};
	private JComboBox[] jcb = {
		new JComboBox<>(),new JComboBox<>(),new JComboBox<>(),new JComboBox<>(sex)
	};
	private JButton[] jButton = {
		new JButton("修改"),new JButton("更换照片"),		
	};
	public StuInfo(String stu_id)
	{
		this.stu_id = stu_id;
		//初始化页面
		this.initialData();
		this.initialFrame();
		//为按钮注册监听器
		this.addListener();
	}

	private void initialData() {
		addCombox1Info();
		addCombox2Info();
		addCombox3Info();
		
	}

	private void initialFrame() {
		this.setLayout(null);
		jlPhoto.setBounds(400,50,150,170);//设置联系人图像JLabel的大小和位置
		jlPhoto.setOpaque(false);
		this.add(jlPhoto);
		for(int i = 0;i < jLabel.length-3;i++)
		{
			jLabel[i].setBounds(100,50+50*i,100,30);
			this.add(jLabel[i]);
		}		
		for(int i=jLabel.length-3;i<jLabel.length;i++)
		{
			jLabel[i].setBounds(350,50+50*(i-3),100,30);
			this.add(jLabel[i]);
		}
		dateChooser.register(this.jTextField[8]);
		for (int i = 0; i < jTextField.length-3; i++) {
			jTextField[i].setBounds(180,50+50*i,150,30);
			if(i>=2&&i<=5);
			else
				this.add(jTextField[i]);			
		}
		for(int i=jTextField.length-3;i<jTextField.length;i++)
		{
			jTextField[i].setBounds(430,50+50*(i-3),150,30);
			this.add(jTextField[i]);
		}
		jTextField[8].setEditable(false);
		for(int i = 2;i<6;i++)
		{
//			this.remove(jTextField[i]);
			if(i != 5)
				jcb[i-2].setBounds(180,50+50*i,150,30);
			else
				jcb[i-2].setBounds(180,50+50*i,50,30);
			jcb[i-2].setFocusable(false);
			
			this.add(jcb[i-2]);
		}
		for(int i = 0;i < jButton.length;i++)
		{
			jButton[i].setBounds(150+i*300,440,100,25);
			jButton[i].setFocusPainted(false);
			jButton[i].setOpaque(false);
			this.add(jButton[i]);
		}
		this.jTextField[0].setText(this.stu_id);			
		Power = DButil.Power;
		System.out.println("power"+Power);
		if(Power.equals(DButil.Student))
		{
			for(int i = 0;i < 2;i++)
			{
				this.jTextField[i].setEditable(false);
			}
			for(int i = 0;i < jcb.length-1;i++)
				this.jcb[i].setEnabled(false);
		}
		else if(Power.equals(DButil.Admin))
		{
			for(int i = 0;i < 2;i++)
			{
				this.jTextField[i].setEditable(false);
			}
		}		
		this.setOpaque(false);
		showInfo();
	}
	
	private void addCombox1Info() {
		rs = DButil.getCollegeInfo();
		try {
			while(rs.next())
			{
				String coll_name = rs.getString(1).trim();
				String coll_id   = rs.getString(2).trim();
				coll_info.put(coll_name, coll_id);
			}
			rs.close();
			Set<String> keyset = coll_info.keySet();
			Iterator<String> i = keyset.iterator();
			while(i.hasNext())
			{
				String coll_name = (String)i.next();
				jcb[0].addItem(coll_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void addCombox2Info() {
		String coll_name = (String)jcb[0].getSelectedItem();
		String coll_id = coll_info.get(coll_name);
		jcb[1].removeAllItems();
		dept_info.clear();
		rs = DButil.searchAllDeptInfo(coll_id);
		try{
			while(rs.next())
			{
			    String dept_id  = rs.getString(1).trim();
			    String dept_name= rs.getString(2).trim();
			    dept_info.put(dept_name,dept_id);
			}
			Set<String> keyset1 = dept_info.keySet();
			Iterator<String> i1 = keyset1.iterator();
			while(i1.hasNext())
			{
				String dept_name = (String)i1.next();
				jcb[1].addItem(dept_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void addCombox3Info() {
		String dept_name = (String)jcb[1].getSelectedItem();
		String dept_id = dept_info.get(dept_name);
		jcb[2].removeAllItems();
		class_info.clear();
		rs = DButil.searchClassInfo(dept_id);
		try{
			while(rs.next())
			{
			    String class_id  = rs.getString(1).trim();
			    String class_name= rs.getString(2).trim();
			    class_info.put(class_name,class_id);
			}
			Set<String> keyset1 = class_info.keySet();
			Iterator<String> i2 = keyset1.iterator();
			while(i2.hasNext())
			{
				String class_name = (String)i2.next();
				jcb[2].addItem(class_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void addListener() {
		this.jButton[0].addActionListener(this);
		this.jButton[1].addActionListener(this);
		jcb[0].addActionListener(this);
		jcb[1].addActionListener(this);
	}
	
	public static void main(String[] args)
	{
		StuInfo stu=new StuInfo("U201215074");
		JFrame jframe=new JFrame();
		jframe.add(stu);
		jframe.setBounds(70,20,700,650);
		jframe.setVisible(true);
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e)  {
		if(e.getSource() == jcb[0])
		{
			addCombox2Info();
			addCombox3Info();
		}
		else if(e.getSource() == jcb [1])
		{
			addCombox3Info();
		}
		
		else if(e.getSource() == jButton[0])
		{
			this.getStuInfo(stu_id);
			setTextfileds();
			String name = jcb[0].getSelectedItem().toString();
			stuInfo.add(coll_info.get(name));
			name = jcb[1].getSelectedItem().toString();
			stuInfo.add(dept_info.get(name));
			name = jcb[2].getSelectedItem().toString();
			stuInfo.add(class_info.get(name));
			System.out.println("-----------------"+stuInfo);
			DButil.updateStuInfo(stuInfo);
			showInfo();
		}
		else if(e.getSource() == jButton[1])
		{
			Vector temp = DButil.choosePic(this);
			if(!temp.isEmpty())
			{
				Image icon1 = (Image) temp.get(0);
				String FileName = (String) temp.get(1);
				if(icon1 != null)
				{
					DButil.setPic(icon1,jlPhoto,StuInfo.this,150,170);
					DButil.insertPic(stu_id,FileName);
					System.out.println("JFileChooser---->"+FileName);
				}
			}
		}
	}

	private void setTextfileds() {
		for(int i=2;i<6;i++)
		{
			System.out.println(jcb[i-2].getSelectedItem().toString());
			jTextField[i].setText(jcb[i-2].getSelectedItem().toString());
		}
	}

	
	public void getStuInfo(String person_id){
		stuInfo.clear();
		ResultSet rs1 = DButil.searchStuInfo(person_id,"stu_id","");
		System.out.println(rs1);
		try {
			if(rs1.next())
			{
				for(int i = 0;i < jTextField.length;i++)
					stuInfo.add(jTextField[i].getText().trim());
				for(int j = jTextField.length+1;j <= 10;j++)
					stuInfo.add(rs1.getString(j).trim());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void showInfo() {
		try {
			String person_id = jTextField[0].getText().toString();
			rs = DButil.searchStuInfo(person_id,"stu_id","");
			System.out.println(rs);
			if(rs.next())
			{
				for(int i = 1;i<jTextField.length;i++)
				{
					jTextField[i].setText(rs.getString(i+1));
				}
			}
			System.out.println(stuInfo);
			Image Pic=DButil.getPic(person_id);//从数据库得到此人的图像
			DButil.setPic(Pic,jlPhoto,StuInfo.this,150,170);
			jcb[0].setSelectedItem(jTextField[2].getText());
			addCombox2Info();
			jcb[1].setSelectedItem(jTextField[3].getText());
			addCombox3Info();
			jcb[2].setSelectedItem(jTextField[4].getText());
			jcb[3].setSelectedItem(jTextField[5].getText());
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
