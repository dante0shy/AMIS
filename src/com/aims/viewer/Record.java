package com.aims.viewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.aims.DButil.DButil;
import com.aims.datechooser.DateChooser;
import com.aims.student.GetStuInfo;

public class Record extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = -8815476503318269944L;
	private ResultSet rs;
	private Map<String, String> apar_info = new HashMap<String,String>();
	private JLabel jTitle = new JLabel();
	private JLabel[] jLabel = {
			new JLabel("访问楼栋"),new JLabel("<=访问日期<=")
	};
	private DateChooser[] dateChooser = {
			DateChooser.getInstance("yyyy-MM-dd"),DateChooser.getInstance("yyyy-MM-dd")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField()
	};
	private JButton jButton = new JButton("查询");
	private JComboBox<String> jcb = new JComboBox<String>();	
	
	private GetRecordInfo gri;
	//声明表格引用
	private JTable jt;
	private JScrollPane jsp;
	//创建用于存放表格数据的Vector
	private Vector<String> v_head=new Vector<String>();
	private Vector<String> v_data=new Vector<String>();
	//创建GetScore对象
	private GetStuInfo gsi;
	private JFrame jf;
	//构造器
	private String[] select = {"访问编号","访问学生ID","自己姓名","访问原因","访问时间"};
		
	public Record()
	{ 
		gri = new GetRecordInfo();
		this.initialData();
	   //初始化页面
		this.initialFrame();
		//为按钮注册监听器
		this.addListener();
	}
	
	private void initialData() {
		for(int i = 0;i < this.select.length;i++){
			v_head.add(select[i]);
		}
		rs = DButil.getAparInfo();
		try {
			while(rs.next())
			{
				String apar_name = rs.getString(1).trim();
				String apar_id = rs.getString(2).trim();
				apar_info.put(apar_name, apar_id);
			}
			Set<String> keyset = apar_info.keySet();
			Iterator<String> i = keyset.iterator();
			while(i.hasNext())
			{
				String apar_name = (String)i.next();
				jcb.addItem(apar_name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//初始化页面的方法
	public void initialFrame()
	{   
		initialJPanel2();
		this.setVisible(true);
	}
	
	private void initialJPanel2() {
		this.setLayout(null);
		jTitle.setText("<HTML><font size=7>登记记录</font></html>");
		jTitle.setBounds(40, 20, 200, 50);
		this.add(jTitle);
		
		for(int i = 0;i < jLabel.length;i++)
		{
			jLabel[i].setBounds(220+i*290, 35, 100, 20);
			this.add(jLabel[i]);
		}
		jcb.setBounds(280, 30, 100, 30);
		jcb.setFocusable(false);
		this.add(jcb);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		for(int i=0;i<jTextField.length;i++){
			dateChooser[i].register(this.jTextField[i]);
			jTextField[i].setBounds(410+i*175, 30, 100, 30);
			jTextField[i].setText(date);
			jTextField[i].setEditable(false);
			this.add(jTextField[i]);
		}
		jButton.setBounds(750, 30, 100, 30);
		jButton.setFocusPainted(false);
		jButton.setOpaque(false);
		this.add(jButton);
		
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt=new JTable(dtm){
			public boolean isCellEditable(int row, int column) { 
				    return false;
			 }
		};
		jsp=new JScrollPane(jt);
		jsp.setBounds(50,80,800,500);
		this.add(jsp);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		this.setOpaque(false);
		refreshTable();
	}

	public void addListener()
	{   
		jButton.addActionListener(this);
		jcb.addActionListener(this);
	}
	//实现ActionListener接口中的方法
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == jButton || e.getSource() == jcb)
		{
			showViewInfo();
		}	
	}

	private void showViewInfo() {
		String apar_name = (String)jcb.getSelectedItem();
		String apar_id = apar_info.get(apar_name);
		System.out.println(apar_id+apar_name);
		//按下查询按钮的处理代码
		//获得输入的学号 
		String[] dateKeyWord = {
				jTextField[0].getText().trim(),jTextField[1].getText().trim()
		};
		int p1 = jcb.getSelectedIndex();
		//根据学号调用GetScore的方法获得该学生的成绩信息
		v_data = gri.getAllRecordInfo(apar_id,dateKeyWord);
		//更新表格模型
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		refreshTable();
	}

	private void refreshTable() {
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();
		jt.getColumn(select[0]).setMinWidth(80);
		jt.getColumn(select[0]).setMaxWidth(80);
		jt.getColumn(select[1]).setMinWidth(80);
		jt.getColumn(select[1]).setMaxWidth(80);
		jt.getColumn(select[2]).setMinWidth(130);
		jt.getColumn(select[2]).setMaxWidth(130);
		jt.getColumn(select[4]).setMinWidth(120);		
		jt.getColumn(select[4]).setMaxWidth(120);		
	}
	
	public static void main(String[] args)
	{		
		Record cpt=new Record();
		JFrame jframe=new JFrame();
		jframe.add(cpt);
		jframe.setBounds(70,20,900,650);
		jframe.setVisible(true);	
	}

}
