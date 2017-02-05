package com.aims.room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.aims.DButil.DButil;
import com.aims.student.StuInfo;

public class RoomManagement  extends JPanel implements ActionListener{
	private GetRoomLiveInfo grli;
	private GetLiveOrNot glon;
	private String room_id;
	private String aparID;
	private JFrame jf;
	private JButton refresh = new JButton(new ImageIcon("./images/refresh.png"));
	private Map<String, String> apar_info = new HashMap<String,String>();
	private JTable[] jt = {
			new JTable(),new JTable()
	};
	private JScrollPane[] jsp = {
			new JScrollPane(),new JScrollPane()
	};
	private JLabel jnotice = new JLabel("<html>��Ҫ�����޸ġ�ɾ��������������߱���е���ѡ��Ҫ��������� ¥�㷿����Ϊ30</html>");
	private JLabel[] jLabel1 = {
			new JLabel("��ס¥��"),new JLabel("��ס����"),new JLabel("��סѧ��"),new JLabel("��סѧ��")
	};
	private JLabel[] jLabel2 = {
			new JLabel("¥������"),new JLabel("��סѡ��")
	};
	private JButton[] jButton ={
			new JButton("������ס"),new JButton("��������"),new JButton("���"),new JButton("ɾ������"),new JButton("������ס")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField(),new JTextField(),new JTextField()
	};
	private String[] liveOrNot = {"��ס��ѧ��","δס��ѧ��"};
	private JComboBox[] jcb = {
			new JComboBox<>(),new JComboBox<String>(liveOrNot)
	};
	//�������ڴ�ű�����ݵ�Vector
	private Vector[] v_head = {
			new Vector<String>(),new Vector<String>()
	};
	private Vector[] v_data = {
			new Vector<String>(),new Vector<String>()
	};
	
	public RoomManagement(String aparID)
	{
		this.aparID = aparID; 
//		DButil.Power = DButil.Admin;
		grli = new GetRoomLiveInfo();
		glon = new GetLiveOrNot();
		//��ʼ������
		this.initialData();
		this.addListener();
		//��ʼ������
		this.initialFrame();
	}
	
	private void initialData() {
		v_head[0].add("�����");
		v_head[0].add("��ס����");
		v_head[1].add("ѧ��");
		v_head[1].add("����");
		addCombox1Info();
	}

	private void addCombox1Info() {
		jcb[0].removeAllItems();
		apar_info.clear();
		ResultSet rs = DButil.getAparInfo();
		try {
			while(rs.next())
			{
				String apar_name = rs.getString(1).trim();
				String apar_id = rs.getString(2).trim();
				apar_info.put(apar_name, apar_id);
			}
			Set<String> keyset = apar_info.keySet();
			Iterator<String> i = keyset.iterator();
			jcb[0].addItem("");
			while(i.hasNext())
			{
				String apar_name = (String)i.next();
				jcb[0].addItem(apar_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void addListener() {
		for(int i = 0; i < jButton.length;i++ )
		{
			jButton[i].addActionListener(this);
		}
		for(int i = 0; i < jcb.length;i++ )
		{
			jcb[i].addActionListener(this);
		}
		refresh.addActionListener(this);
	}
	
	private void initialFrame() {
		this.setLayout(null);
		jnotice.setBounds(150, 0, 400, 40);
		this.add(jnotice);
		for(int i = 0;i<jcb.length;i++)
		{
			jcb[i].setFocusable(false);
			jcb[i].setBounds(115+400*i, 60, 120, 20);
			jLabel2[i].setBounds(40+400*i,60,100,20);
			this.add(jcb[i]);
			this.add(jLabel2[i]);
		}
		refresh.setBounds(250, 60, 20, 20);
		refresh.setFocusPainted(false);
		this.add(refresh);
		for(int i = 0; i < jLabel1.length;i++ )
		{
			jLabel1[i].setBounds(265, 150+i*60, 150, 30);
			this.add(jLabel1[i]);
			jTextField[i].setBounds(265, 180+i*60, 150, 30);
			jTextField[i].setEditable(false);
			this.add(jTextField[i]);
		}
		for(int i = 0; i < jButton.length;i++ )
		{
			jButton[i].setBounds(40+i*125, 570, 100, 25);
			jButton[i].setFocusPainted(false);
			jButton[i].setOpaque(false);
			this.add(jButton[i]);
		}
		DefaultTableModel[] dtm ={ 
				new DefaultTableModel(v_data[0],v_head[0]),new DefaultTableModel(v_data[1],v_head[1])
		};
		for(int i = 0;i < 2;i++){
			jt[i] = new JTable(dtm[i]){
				public boolean isCellEditable(int row, int column) { 
				    return false;
				}	
			};
			jsp[i] = new JScrollPane(jt[i]);
			jsp[i].setBounds(40+400*i,90,200,470);
			jsp[i].setBackground(new Color(228,236,197));
			jsp[i].getViewport().setOpaque(false);
			this.add(jsp[i]);
		}
		jt[0].addMouseListener(new MouseAdapter(){
			/**
			 * �������¼�
			 * ����ʱʹѡ����
			 * ˫��ʱ������ļ�������ʾ��Ŀ¼�µ��ļ�
			 */
			public void mouseClicked(MouseEvent event){
				Point p = event.getPoint();
				int index = jt[0].rowAtPoint(p);
				if(event.getClickCount() >= 2 && event.getButton() == MouseEvent.BUTTON1){
					System.out.println("aparege double click");
					System.out.println(index);
					room_id   = jt[0].getValueAt(index, 0).toString().trim();
					if(!room_id.equals(""))
					{
						String aparname = jcb[0].getSelectedItem().toString();
						String aparID = apar_info.get(aparname);
						SearchRoomInfo aparRoominfo = new SearchRoomInfo(aparID){
							public void paintComponent(Graphics g) {
								String path = "./images/background1.jpg";
								super.paintComponent(g);
								ImageIcon img = new ImageIcon(path);
								g.drawImage(img.getImage(), 0, 0, null);
							}
						};
						JDialog jDialog = new JDialog(jf,aparID+"¥��������Ϣ",true);
						jDialog.add(aparRoominfo);
						Dimension screenSize = Toolkit.
					             getDefaultToolkit().getScreenSize();
						int centerX = screenSize.width/2;
						int centerY = screenSize.height/2;
						jDialog.setBounds(centerX-350,centerY-350,700,650);
						jDialog.setVisible(true);
					}
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					String apar_name = (String)jcb[0].getSelectedItem();
					room_id   = jt[0].getValueAt(index, 0).toString().trim();
					jTextField[0].setText(apar_name);
					jTextField[1].setText(room_id);
					
				}
			}
		});
		jt[1].addMouseListener(new MouseAdapter(){
			/**
			 * �������¼�
			 * ����ʱʹѡ����
			 * ˫��ʱ������ļ�������ʾ��Ŀ¼�µ��ļ�
			 */
			public void mouseClicked(MouseEvent event){
				Point p = event.getPoint();
				int index = jt[1].rowAtPoint(p);
				if(event.getClickCount() >= 2 && event.getButton() == MouseEvent.BUTTON1){
					System.out.println("aparege double click");
					System.out.println(index);
					String stu_id   = jt[1].getValueAt(index, 0).toString().trim();
					if(!stu_id.equals(""))
					{
						StuInfo stu_info = new StuInfo(stu_id){
							public void paintComponent(Graphics g) {
								String path = "./images/background1.jpg";
								super.paintComponent(g);
								ImageIcon img = new ImageIcon(path);
								g.drawImage(img.getImage(), 0, 0, null);
							}
						};
						JDialog jDialog = new JDialog(jf,stu_id+"������Ϣ",true);
						jDialog.add(stu_info);
						Dimension screenSize = Toolkit.
					             getDefaultToolkit().getScreenSize();
						int centerX = screenSize.width/2;
						int centerY = screenSize.height/2;
						jDialog.setBounds(centerX-350,centerY-350,700,650);
						jDialog.setVisible(true);
					}
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					String stu_id   = jt[1].getValueAt(index, 0).toString().trim();
					int index1 = jcb[1].getSelectedIndex();
					if(index1 == 1)
					{
						jTextField[3].setText("");
						jTextField[2].setText(stu_id);
					}
					else
					{
						jTextField[2].setText("");
						ResultSet rs = DButil.getLiveInfo(stu_id);
						try {
							if(rs.next())
							{
								jTextField[0].setText(rs.getString(1).toString());
								jTextField[1].setText(rs.getString(2).toString());
								jTextField[3].setText(rs.getString(3).toString());
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				}
			}
		});
		this.setOpaque(false);
		if(DButil.Power.equals(DButil.Admin))
		{
			ResultSet rs = DButil.searchAparInfo(aparID);
			String name = "";
			try {
				if(rs.next()){
					name = rs.getString(1);
					System.out.println(name+"name");
					jcb[0].setSelectedItem(name);
				}
				else{
					jcb[1].setSelectedItem(liveOrNot[1]);
					jcb[1].setEnabled(false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			jcb[0].setEnabled(false);
			this.remove(jButton[1]);
			this.remove(jButton[2]);
			this.remove(jButton[3]);
		}
		showAparInfo();
	}

	private void showAparInfo()
	{
		String apar_name = (String)jcb[0].getSelectedItem();
		String apar_id = apar_info.get(apar_name);
		v_data[0] = grli.getRoomLiveInfo(apar_id);
		int index = jcb[1].getSelectedIndex();
		v_data[1] = glon.getRoomLiveInfo(index,aparID);
		
		//���±��ģ��
		DefaultTableModel[] dtm = {
				new DefaultTableModel(v_data[0],v_head[0]),new DefaultTableModel(v_data[1],v_head[1])
		};
		for(int i = 0;i <2;i++)
		{	
			jt[i].setModel(dtm[i]);
			((DefaultTableModel)jt[i].getModel()).fireTableStructureChanged();
		}
		//������ʾ
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String apar_name = (String)jcb[0].getSelectedItem();
		String apar_id = apar_info.get(apar_name);
		String stu_id = jTextField[3].getText();
		if(e.getSource() == jButton[0])
		{
			String ap_name = jTextField[0].getText();
			String stuid = jTextField[2].getText();
			if(ap_name.equals("")||stuid.equals("")){
				JOptionPane.showMessageDialog(null, "��ס��Ϣ�����������������");
				return;
			}
			String ap_id = apar_info.get(ap_name);
			String room_id = jTextField[1].getText();
			if(DButil.addLive(ap_id,room_id,stuid))
				resetTextfileds();
		}
		else if(e.getSource() == jButton[1])
		{
			DButil.addRoom(apar_id);
		}
		else if(e.getSource() == jButton[2])
		{
			resetTextfileds();
		}
		else if(e.getSource() == jButton[3])
		{
			DButil.deleteRoom(apar_id,room_id);
		}
		else if(e.getSource() == jButton[4])
		{
			if(stu_id.equals("")){
				JOptionPane.showMessageDialog(null, "��ѡ��Ҫ��ס��ѧ��");
				return;
			}
			if(DButil.deleteFromLive(stu_id))
				resetTextfileds();
		}
		else if(e.getSource() == refresh)
		{
			addCombox1Info();
		};
		showAparInfo();
	}

	private void resetTextfileds() {
		for(int i = 0;i < jTextField.length;i++)
		{
			jTextField[i].setText("");
		}
	}
	
	public static void main(String[] args)
	{
		RoomManagement c= new RoomManagement("D11");
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(c);
		jf.setVisible(true);
	}
}