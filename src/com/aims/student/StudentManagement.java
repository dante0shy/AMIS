package com.aims.student;

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
import java.util.Vector;
import java.util.regex.Pattern;

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

public class StudentManagement  extends JPanel implements ActionListener{
	private String stu_id;
	private ResultSet rs = null;
	private GetStudentInfo gsi;
	private String[] sex ={"��","Ů"};
	private JButton refresh = new JButton(new ImageIcon("./images/refresh.png"));
	private Map<String, String> coll_info  = new HashMap<String,String>();
	private Map<String, String> dept_info  = new HashMap<String,String>();
	private Map<String, String> class_info = new HashMap<String,String>();
	private JTable jt;
	private JFrame jf;
	private JScrollPane jsp;
	private JComboBox[] jcb ={
			new JComboBox<>(),new JComboBox<>(),new JComboBox<>(),new JComboBox<>(sex)
	};
	private JLabel jnotice = new JLabel("<html>��Ҫ�����޸ġ�ɾ��������������߱���е���ѡ��Ҫ���������</html>");
	private JLabel[] jlabel ={
			new JLabel("ѡ��Ժϵ"),new JLabel("ѡ��רҵ"),new JLabel("ѡ��༶")
	};
	private JLabel[] jLabel = {
			new JLabel("����ѧ��"),new JLabel("ѧ������"),new JLabel("ѧ���Ա�"),new JLabel("ɾ��ѧ��")
	};
	private JButton[] jButton ={
			new JButton("����ѧ��"),new JButton("���"),new JButton("ɾ��ѧ��")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField(),new JTextField()
	};
	//�������ڴ�ű�����ݵ�Vector
	private Vector<String> v_head=new Vector<String>();
	private Vector<String> v_data=new Vector<String>();
	
	public StudentManagement()
	{
		DButil.Power = DButil.SuperAdmin;
		gsi = new GetStudentInfo();
		//��ʼ������
		this.initialData();
		this.addListener();
		//��ʼ������
		this.initialFrame();
	}
	
	private void initialData() {
		v_head.add("ѧ��");
		v_head.add("����");
		v_head.add("�Ա�");
		addCombox1Info();
		addCombox2Info();
		addCombox3Info();
	}

	
	private void addCombox1Info() {
		jcb[0].removeAllItems();
		coll_info.clear();
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
		for(int i = 0; i < jButton.length;i++ )
		{
			jButton[i].addActionListener(this);
		}
		
		for(int i = 0; i < jcb.length-1;i++ )
		{
			jcb[i].addActionListener(this);
		}
		refresh.addActionListener(this);
	}
	
	private void initialFrame() {
		this.setLayout(null);
		jnotice.setBounds(150, 0, 400, 30);
		this.add(jnotice);
		for(int i=0;i<jlabel.length;i++){
			jlabel[i].setBounds(70, 40+25*i, 80, 20);
			this.add(jlabel[i]);
		}
		for(int i =0 ;i<jcb.length-1;i++){
			jcb[i].setBounds(150, 40+i*25, 180, 20);	
			this.add(jcb[i]);
		}
		jcb[3].setBounds(440, 185, 100, 20);	
		this.add(jcb[3]);
		refresh.setBounds(340, 40, 20, 20);
		refresh.setFocusPainted(false);
		this.add(refresh);
		for(int i = 0; i < jLabel.length;i++ )
		{
			if(i == 3)
			{
				jLabel[i].setBounds(380, 310, 200, 30);
			}
			else
				jLabel[i].setBounds(380, 110+i*35, 200, 30);
			this.add(jLabel[i]);
		}
		for(int i=0;i<jTextField.length;i++){
			if(i == 2)
			{
				jTextField[i].setBounds(440, 310, 200, 30);				
				jTextField[i].setEditable(false);				
			}
			else
				jTextField[i].setBounds(440, 110+i*35, 200, 30);								
			this.add(jTextField[i]);
		}
		for(int i = 0; i < jButton.length;i++ )
		{
			jButton[i].setBounds(380+i*100, 250, 90, 25);
			this.add(jButton[i]);
		}
		DefaultTableModel dtm = new DefaultTableModel(v_data,v_head);
		jt = new JTable(dtm){
			public boolean isCellEditable(int row, int column) { 
			    return false;
			}	
		};
		jt.addMouseListener(new MouseAdapter(){
			/**
			 * �������¼�
			 * ����ʱʹѡ����
			 * ˫��ʱ������ļ�������ʾ��Ŀ¼�µ��ļ�
			 */
			public void mouseClicked(MouseEvent event){
				Point p = event.getPoint();
				int index = jt.rowAtPoint(p);
				if(event.getClickCount() >= 2 && event.getButton() == MouseEvent.BUTTON1){
					stu_id   = jt.getValueAt(index, 0).toString().trim();
					StuInfo stu_info = new StuInfo(stu_id){
						public void paintComponent(Graphics g) {
							String path = "./images/background1.jpg";
							super.paintComponent(g);
							ImageIcon img = new ImageIcon(path);
							g.drawImage(img.getImage(), 0, 0, null);
						}
					};
					JDialog jDialog = new JDialog(jf,stu_id+"��Ϣ",true);
					jDialog.add(stu_info);
					jDialog.setLocationRelativeTo(null);
					Dimension screenSize = Toolkit.
				             getDefaultToolkit().getScreenSize();
					int centerX = screenSize.width/2;
					int centerY = screenSize.height/2;
					jDialog.setBounds(centerX-350,centerY-350,700,650);
					jDialog.setVisible(true);
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					stu_id   = jt.getValueAt(index, 0).toString().trim();
					jTextField[2].setText(stu_id);
				}
			}
		});
		jsp=new JScrollPane(jt);
		jsp.setBounds(60,110,300,470);
		jsp.setBackground(new Color(228,236,197));
		jsp.getViewport().setOpaque(false);
		this.add(jsp);
		this.setOpaque(false);
		showDeptInfo();
	}

	private void showDeptInfo()
	{
		jTextField[0].setText("");
		String dept_name = (String)jcb[1].getSelectedItem();
		String dept_id = dept_info.get(dept_name);
		String class_name = (String)jcb[2].getSelectedItem();
		String class_id = class_info.get(class_name);
		v_data = gsi.getStudentInfo(class_id);
		//���±��ģ��
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String coll_name = (String)jcb[0].getSelectedItem();
		String coll_id = coll_info.get(coll_name);
		String dept_name = (String)jcb[1].getSelectedItem();
		String dept_id = dept_info.get(dept_name);
		String class_name = (String)jcb[2].getSelectedItem();
		String class_id = class_info.get(class_name);
		String deleteStuId = jTextField[2].getText();
		if(e.getSource() == refresh)
		{
			addCombox1Info();
			addCombox2Info();
			addCombox3Info();
		}
		else if(e.getSource() == jcb[0])
		{
			addCombox2Info();
			addCombox3Info();
		}
		else if(e.getSource() == jcb[1])
		{
			addCombox3Info();
		}
		else if(e.getSource() == jcb[2])
		{
			
			showDeptInfo();
		}
		else if(e.getSource() == jButton[1])
		{
			resetTextfileds();
		}
		else if(e.getSource() == jButton[2])
		{
			DButil.deleteStudent(deleteStuId);
		}
		else if(e.getSource() == jButton[0]){
			Vector<String> stuinfo = new Vector<>();
			String stu_id   = jTextField[0].getText().trim();
			String stu_name = jTextField[1].getText().trim();
			String sex = "";
			String regex = "[A-Z][0-9]{9}";
			Pattern p = Pattern.compile(regex);
			if(!p.matches(regex,stu_id))
			{
				JOptionPane.showMessageDialog(null, "ѧ�Ÿ�ʽΪUXXXXXXXXX,����U201215072");
				return;
			}
			if(stu_name.equals(""))
			{
				JOptionPane.showMessageDialog(null, "��������Ϊ��");
				return;
			}
			if(jcb[3].getSelectedItem().equals("��"))
				sex = "1";
			else
				sex = "0";
			stuinfo.add(stu_id);
			stuinfo.add(stu_name);
			stuinfo.add(coll_id);
			stuinfo.add(dept_id);
			stuinfo.add(class_id);
			stuinfo.add(sex);
			if(DButil.addStudent(stuinfo))
				resetTextfileds();
		}
		showDeptInfo();
	}
	
	private void resetTextfileds() {
		for(int i = 0;i < jTextField.length;i++)
		{
			jTextField[i].setText("");
		}
	}
	
	public static void main(String[] args)
	{
		StudentManagement c= new StudentManagement();
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(c);
		jf.setVisible(true);
	}
}
