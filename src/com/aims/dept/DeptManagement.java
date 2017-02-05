package com.aims.dept;

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
import java.util.TreeMap;
import java.util.Vector;

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

public class DeptManagement  extends JPanel implements ActionListener{
	private String dept_id;
	private String dept_name;
	private GetDeptInfo gdi;
	private ResultSet rs = null;
	private JButton refresh = new JButton(new ImageIcon("./images/refresh.png"));
	private Map<String, String> coll_info = new HashMap<String,String>();
	private JTable jt;
	private JFrame jf;
	private JScrollPane jsp;
	private JComboBox jcb = new JComboBox<>();
	private JLabel jnotice = new JLabel("<html>若要进行修改、删除操作，需在左边表格中单击选中要处理的数据</html>");
	private JLabel jlabel = new JLabel("选择院系");
	private JLabel[] jLabel = {
			new JLabel("专业名称")
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
	
	public DeptManagement()
	{
		DButil.Power = DButil.SuperAdmin;
		
		gdi = new GetDeptInfo();
		//初始化数据
		this.initialData();
		this.addListener();
		//初始化界面
		this.initialFrame();
	}
	
	private void initialData() {
		v_head.add("专业编号");
		v_head.add("专业名称");
		addConboxInfo();
	}

	private void addConboxInfo() {
		jcb.removeAllItems();
		coll_info.clear();
		rs = DButil.getCollegeInfo();
		try {
			while(rs.next())
			{
				String dept_name = rs.getString(1).trim();
				String dept_id = rs.getString(2).trim();
				coll_info.put(dept_name, dept_id);
			}
			Set<String> keyset = coll_info.keySet();
			Iterator<String> i = keyset.iterator();
			while(i.hasNext())
			{
				String dept_name = (String)i.next();
				jcb.addItem(dept_name);
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
		jcb.addActionListener(this);
		refresh.addActionListener(this);
	}
	
	private void initialFrame() {
		this.setLayout(null);
		jnotice.setBounds(150, 0, 400, 30);
		this.add(jnotice);
		jlabel.setBounds(70, 40, 80, 20);
		this.add(jlabel);
		jcb.setBounds(150, 40, 180, 20);	
		this.add(jcb);
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
					System.out.println("College double click");
					System.out.println(index);
					dept_id   = jt.getValueAt(index, 0).toString().trim();
					dept_name = jt.getValueAt(index, 1).toString().trim();
					if(!dept_id.equals(""))
					{
						jTextField[0].setText(dept_name);
					}
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					dept_id   = jt.getValueAt(index, 0).toString().trim();
					dept_name = jt.getValueAt(index, 1).toString().trim();
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
		String coll_name = (String)jcb.getSelectedItem();
		String coll_id = coll_info.get(coll_name);
		System.out.println(coll_id+coll_name);
		v_data = gdi.getDeptInfo(coll_id);
		//更新表格模型
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == refresh)
		{
			addConboxInfo();
		}
		else if(e.getSource() == jcb)
		{
			showDeptInfo();
		}
		else if(e.getSource() == jButton[2])
		{
			DButil.deleteDept(dept_id);
		}
		else
		{
			if(e.getSource() == jButton[0]){
				String coll_name = (String)jcb.getSelectedItem();
				String coll_id = coll_info.get(coll_name);
				String name = jTextField[0].getText().trim();
				System.out.println(name);
				if(name.equals(""))
				{
					JOptionPane.showMessageDialog(null, "系别名称不可为空");
					return;
				}
				String id = DButil.createDeptID();
				DButil.addDept(coll_id,id,name);
			}
			else if(e.getSource() == jButton[1])
			{
				String name = jTextField[0].getText();
				DButil.updateDept(dept_id,name);
			}
		}
		showDeptInfo();
	}
	
	public static void main(String[] args)
	{
		DeptManagement c= new DeptManagement();
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(c);
		jf.setVisible(true);
	}
}
