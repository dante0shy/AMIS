package com.aims.apartment;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.aims.DButil.DButil;

public class AparInfo extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2126573018700616929L;
	private String personID;
	private String aparID;
	private String picId; 
	private String Power;
	private String[] temp = new String[3];
	private Vector<String> aparInfo = new Vector<String>();
	private ResultSet rs;
	private JScrollPane jScrollPaneNote;
	private JScrollPane jScrollPaneAddress;
	private JLabel jlPhoto = new JLabel();
	private JLabel jlNote = new JLabel();
	private JTextArea jNote = new JTextArea();
	private JTextArea jAddress = new JTextArea();
	private JTextField[] jTextField = {
			new JTextField()
	};
	private JLabel[] jLabel = {
			new JLabel("¥����"),new JLabel("¥����ַ"),
	};
	private JButton[] jbutton ={
			new JButton("�޸�"),new JButton("����"),
			new JButton("����ͼƬ")};
	
	public AparInfo(String aparID)
	{
		personID = DButil.personID;
		Power = DButil.Power;
		this.aparID = aparID;
		//��ʼ��ҳ��
		this.initialFrame();
		showAparInfo();
		//Ϊ��ťע�������
		this.addListener();

	}
	
	
	private void initialFrame() {
		this.setLayout(null);
		//������ϵ��ͼ��JLabel�Ĵ�С��λ��
		jlPhoto.setBounds(350,20,150,170);
		jlPhoto.setOpaque(false);
		this.add(jlPhoto);
		//����ͨ����Ϣ�Ĵ�С��λ��
		jAddress.setLineWrap(true);
		jAddress.setEditable(false);
		jScrollPaneAddress = new JScrollPane(jAddress);
		jScrollPaneAddress.setBounds(50,130,270,70);
		this.add(jScrollPaneAddress);
		jlNote.setBounds(20, 250, 50, 200);
		jlNote.setText("<HTML><font size=7>¥<br>��<br>ͨ<br>��</font></html>");
		this.add(jlNote);
		//����ͨ����Ϣ�Ĵ�С��λ��
		jNote.setLineWrap(true);
		jNote.setTabSize(4);
		jNote.setEditable(false);
		jScrollPaneNote = new JScrollPane(jNote);
		jScrollPaneNote.setBounds(70,240,560,260);
		this.add(jScrollPaneNote);
		
		for(int i = 0;i < jLabel.length;i++)
		{
			jLabel[i].setBounds(50,40+60*i,100,30);
			this.add(jLabel[i]);
		}
		jTextField[0].setBounds(50, 70, 270, 30);
		this.add(jTextField[0]);
		jTextField[0].setEditable(false);
		System.out.println(!Power.equals(DButil.Student));
		if(!Power.equals(DButil.Student))
		{
			jbutton[0].setBounds(90, 520, 100, 30);
			jbutton[1].setBounds(510, 520, 100, 30);
			jbutton[2].setBounds(375, 190, 100, 30);
			for(int i = 0;i < jbutton.length;i++)
			{
				jbutton[i].setOpaque(false);
				this.add(jbutton[i]);
			}
			jAddress.setEditable(true);
			jNote.setEditable(true);
		}
		if(Power.equals(DButil.SuperAdmin))
		{
			jTextField[0].setEditable(true);
		}
		this.setOpaque(false);
	}
	
	private void showAparInfo() {
		try {
			rs = DButil.searchAparInfo(aparID);
			System.out.println(rs);
			if(rs.next())
			{	
				for(int i = 0;i < temp.length;i++)
					temp[i] = rs.getString(i+1);
				jTextField[0].setText(temp[0].trim());
				jAddress.setText(temp[1].trim());
				jNote.setText("\t"+temp[2].trim());
			}
			picId = rs.getString(4).trim();//D11
			Image roomPic = DButil.getPic(picId);//�����ݿ�õ����˵�ͼ��
			DButil.setPic(roomPic,jlPhoto,AparInfo.this,150,170);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "��"+this.personID+"��Ϣ");
		}
	}

	
	private void addListener() {
		for(int i = 0;i < jbutton.length;i++)
		{
			jbutton[i].addActionListener(this);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jbutton[0])
		{
			getAparInfo(aparID);
			DButil.updateAparInfo(aparInfo);
		}
		else if(e.getSource() == jbutton[1])
		{
			showAparInfo();
		}
		else if(e.getSource() == jbutton[2])
		{
			Vector temp = DButil.choosePic(this);
			if(!temp.isEmpty())
			{
				Image icon1 = (Image) temp.get(0);
				String FileName = (String) temp.get(1);
				if(icon1 != null)
				{
					DButil.setPic(icon1,jlPhoto,AparInfo.this,150,170);
					DButil.insertPic(picId,FileName);
					System.out.println("JFileChooser---->"+FileName);
				}
			}
		}
	}
	
	private void getAparInfo(String aparID) {
		aparInfo.clear();
		ResultSet rs1 = DButil.searchAparInfo(aparID);
		System.out.println(rs1);
		try {
			if(rs1.next())
			{
				aparInfo.add(aparID);
				for(int i = 0;i < jTextField.length;i++)
					aparInfo.add(jTextField[i].getText().trim());
				aparInfo.add(jAddress.getText().trim());
				aparInfo.add(jNote.getText().trim());
				System.out.println(aparInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void setFocus()
	{
		jTextField[0].requestFocus(true);
	}
	
	public static void main(String[] args)
	{
		AparInfo room=new AparInfo("D11");
		JFrame jframe=new JFrame();
		jframe.add(room);
		jframe.setBounds(70,20,700,650);
		jframe.setVisible(true);
	}

	
}