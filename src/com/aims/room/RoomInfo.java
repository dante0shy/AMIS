package com.aims.room;

import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.aims.DButil.DButil;
import com.aims.student.StuInfo;

public class RoomInfo extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 905514631335311989L;
	private String roomKeyWord;
	private ResultSet rs;
//	private String Power = DButil.Power;
	private String Power ;
	private Vector<String> roomInfo = new Vector<String>();
	private String[] temp = new String[4];
	private JScrollPane jScrollPane;
	private JLabel jlNote = new JLabel();
	private JLabel jlPhoto = new JLabel();
	private JTextArea jNote = new JTextArea();
	private JTextField[] jTextField = {
			new JTextField(),new JTextField(),new JTextField()
	};
	private JLabel[] jLabel = {
			new JLabel("楼栋名"),new JLabel("宿舍号"),
			new JLabel("剩余电量(度)")
	};
	private JButton[] jButton ={
			new JButton("修改"),new JButton("重置"),
			new JButton("更换图片")};
	public RoomInfo(String roomKeyWord)
	{
		this.roomKeyWord = roomKeyWord;
		//初始化页面
		this.initialFrame();
		showRoomInfo();
		//为按钮注册监听器
		this.addListener();

	}
	
	private void initialFrame() {
		this.setLayout(null);
		jlPhoto.setBounds(350,20,150,170);//设置联系人图像JLabel的大小和位置
		jlPhoto.setOpaque(false);
		this.add(jlPhoto);
		jlNote.setBounds(20, 250, 50, 200);
		jlNote.setText("<HTML><font size=7>宿<br>舍<br>通<br>告</font></html>");
		this.add(jlNote);
		jNote.setLineWrap(true);
		jNote.setTabSize(4);
		jNote.setEditable(false);
		jScrollPane = new JScrollPane(jNote);
		jScrollPane.setBounds(70,240,560,260);
		this.add(jScrollPane);
		for(int i = 0;i < jLabel.length;i++)
		{
			jLabel[i].setBounds(50,40+60*i,100,30);
			this.add(jLabel[i]);
		}
		for (int i = 0; i < jTextField.length; i++) {
			jTextField[i].setBounds(130,40+60*i,150,30);
			jTextField[i].setEditable(false);
			this.add(jTextField[i]);			
		}
		Power = DButil.Power;
		if(!Power.equals(DButil.Student))
		{
			jButton[0].setBounds(90, 520, 100, 30);
			jButton[0].setFocusPainted(false);
			this.add(jButton[0]);
			jButton[1].setBounds(510, 520, 100, 30);
			jButton[1].setFocusPainted(false);
			this.add(jButton[1]);
			jButton[2].setBounds(375, 190, 100, 30);
			jButton[2].setFocusPainted(false);
			this.add(jButton[2]);
			jTextField[2].setEditable(true);
			jNote.setEditable(true);
		}
		if(Power.equals(DButil.SuperAdmin))
		{
			jTextField[0].setEditable(true);
			jTextField[1].setEditable(true);
		}
		this.setOpaque(false);
	}
	
	private void showRoomInfo() {
		try {
			String roomKeyWord = this.roomKeyWord;
			if(DButil.Power == DButil.Student)
				rs = DButil.searchRoomInfoWay2(roomKeyWord);
			else 
				rs = DButil.searchRoomInfoWay1(roomKeyWord);
			System.out.println(rs);
			if(rs.next())
			{
				for(int i = 0;i<jTextField.length;i++)
				{
					temp[i] = rs.getString(i+1);
					jTextField[i].setText(temp[i]);
				}
			}
			String picId = rs.getString(4).trim()+rs.getString(2).trim();//D11522
			temp[temp.length-1] = rs.getString(5);
			String note = temp[temp.length-1];
			Image roomPic = DButil.getPic(picId);//从数据库得到此人的图像
			DButil.setPic(roomPic,jlPhoto,RoomInfo.this,150,170);
			jNote.setText("\t"+note);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private void addListener() {
		for(int i = 0;i < jButton.length;i++)
		{
			jButton[i].addActionListener(this);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jButton[0])
		{
			getRoomInfo(roomKeyWord);
			DButil.updateRoomInfo(roomInfo);
		}
		else if(e.getSource() == jButton[1])
		{
			showRoomInfo();
		}
		else if(e.getSource() == jButton[2])
		{
			Vector temp = DButil.choosePic(this);
			if(!temp.isEmpty())
			{
				Image icon1 = (Image) temp.get(0);
				String FileName = (String) temp.get(1);
				if(icon1 != null)
				{
					DButil.setPic(icon1,jlPhoto,RoomInfo.this,150,170);
					DButil.insertPic(roomKeyWord,FileName);
					System.out.println("JFileChooser---->"+FileName);
				}
			}
		}
	}
	
	public void getRoomInfo(String roomKeyWord){
		roomInfo.clear();
		ResultSet rs1 = DButil.searchRoomInfoWay1(roomKeyWord);
		System.out.println(rs1);
		try {
			if(rs1.next())
			{
				roomInfo.add(rs1.getString(4).trim());
				for(int i = 0;i < jTextField.length;i++)
					roomInfo.add(jTextField[i].getText().trim());
				roomInfo.add(jNote.getText().trim());
				System.out.println(roomInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		RoomInfo room=new RoomInfo("U201215072");
		JFrame jframe=new JFrame();
		jframe.add(room);
		jframe.setBounds(70,20,700,650);
		jframe.setVisible(true);
	}


}
