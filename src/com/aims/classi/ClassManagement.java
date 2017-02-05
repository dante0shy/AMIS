package com.aims.classi;

import java.awt.Color;
import java.awt.Point;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.aims.DButil.DButil;

public class ClassManagement  extends JPanel implements ActionListener{

	private static final long serialVersionUID = -6009673772901011132L;
	private String class_id;
	private ResultSet rs = null;
	private GetClassInfo gci;
	private JButton refresh = new JButton(new ImageIcon("./images/refresh.png"));
	private Map<String, String> coll_info = new HashMap<String,String>();
	private Map<String, String> dept_info = new HashMap<String,String>();
	private JTable jt;
	private JScrollPane jsp;
	private JComboBox[] jcb ={
			new JComboBox<>(),new JComboBox<>()
	};
	private JLabel jnotice = new JLabel("<html>若要进行修改、删除操作，需在左边表格中单击选中要处理的数据</html>");
	private JLabel[] jlabel ={
			new JLabel("选择院系"),new JLabel("选择专业")
	};
	private JLabel[] jLabel = {
			new JLabel("班级编号")
	};
	private JButton[] jButton ={
			new JButton("新增"),new JButton("修改"),new JButton("删除")
	};
	private JTextField[] jTextField = {
			new JTextField()
	};
	//创建用于存放表格数据的Vector
	private Vector<String> v_head=new Vector<String>();
	private Vector<String> v_data=new Vector<String>();
	
	public ClassManagement()
	{
		DButil.Power = DButil.SuperAdmin;
		gci = new GetClassInfo();
		//初始化数据
		this.initialData();
		this.addListener();
		//初始化界面
		this.initialFrame();
	}
	
	private void initialData() {
		v_head.add("班级编号");
		v_head.add("班级名称");
		addCombox1Info();
		addCombox2Info();
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
				String coll_name = i.next();
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
				String dept_name = i1.next();
				jcb[1].addItem(dept_name);
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
		jcb[0].addActionListener(this);
		jcb[1].addActionListener(this);
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
		for(int i =0 ;i<jcb.length;i++){
			jcb[i].setBounds(150, 40+i*25, 180, 20);	
			this.add(jcb[i]);
		}
		refresh.setBounds(340, 40, 20, 20);
		refresh.setFocusPainted(false);
		this.add(refresh);
		for(int i = 0; i < jLabel.length;i++ )
		{
			jLabel[i].setBounds(400, 60+i*60, 200, 30);
			this.add(jLabel[i]);
			jTextField[i].setBounds(400, 90+i*60, 200, 30);
			this.add(jTextField[i]);
		}
		for(int i = 0; i < jButton.length;i++ )
		{
			jButton[i].setBounds(400+i*70, 140, 60, 30);
			this.add(jButton[i]);
		}
		DefaultTableModel dtm = new DefaultTableModel(v_data,v_head);
		jt = new JTable(dtm){
			/**
			 * 
			 */
			private static final long serialVersionUID = 2773171491447375971L;

			public boolean isCellEditable(int row, int column) { 
			    return false;
			}	
		};
		jt.addMouseListener(new MouseAdapter(){
			/**
			 * 添加鼠标事件
			 * 单击时使选中行
			 * 双击时如果是文件夹则显示该目录下的文件
			 */
			public void mouseClicked(MouseEvent event){
				Point p = event.getPoint();
				int index = jt.rowAtPoint(p);
				if(event.getClickCount() >= 2 && event.getButton() == MouseEvent.BUTTON1){
					System.out.println("Class double click");
					System.out.println(index);
					class_id   = jt.getValueAt(index, 0).toString().trim();
					if(!class_id.equals(""))
					{
						String name = class_id.subSequence(6, 10).toString();
						jTextField[0].setText(name);
					}
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					class_id   = jt.getValueAt(index, 0).toString().trim();
				}
			}
		});
		jsp=new JScrollPane(jt);
		jsp.setBounds(60,90,300,500);
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
		v_data = gci.getClassInfo(dept_id);
		//更新表格模型
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String coll_name = (String)jcb[0].getSelectedItem();
		String coll_id = coll_info.get(coll_name);
		String dept_name = (String)jcb[1].getSelectedItem();
		if(dept_name == null)
			return;
		String dept_id = dept_info.get(dept_name);
		if(e.getSource() == refresh)
		{
			addCombox1Info();
			addCombox2Info();
		}
		else if(e.getSource() == jcb[0])
		{
			addCombox2Info();
		}
		else if(e.getSource() == jcb[1])
		{
			showDeptInfo();
		}
		else if(e.getSource() == jButton[2])
		{
			DButil.deleteClass(this.class_id);
		}
		else
		{
			String class_id = jTextField[0].getText().trim();
			String realClassId = coll_id+dept_id+class_id;
			if(e.getSource() == jButton[0]){
				String regex = "[0-9]{4}";
				if(!Pattern.matches(regex,class_id))
				{
					JOptionPane.showMessageDialog(null, "班级编号格式为XXXX,例如1209");
					return;
				}
				DButil.addClass(coll_id,dept_id,realClassId);
			}
			else if(e.getSource() == jButton[1])
			{
				System.out.println(class_id);
				DButil.updateClass(this.class_id,realClassId);
			}
		}
		showDeptInfo();
	}
	
	public static void main(String[] args)
	{
		ClassManagement c= new ClassManagement();
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(c);
		jf.setVisible(true);
	}
}
