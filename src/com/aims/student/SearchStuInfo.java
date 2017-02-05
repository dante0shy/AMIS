package com.aims.student;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.*;
public class SearchStuInfo extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 6564448842780086787L;
	//创建提示标签
	private JLabel jl = new JLabel("查询方式");
	private JLabel j2 = new JLabel("排序方式");
			
	//创建用于输入学号的文本框
	private JTextField jtfSearch = new JTextField();
	//创建动作按钮
	private JButton jbSearch = new JButton("模糊查询");
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
	private String[] select = {"学号","姓名","院系","专业","班级"};
	private String[] sResult = {"stu_id","stu_name","coll_name","dept_name","class_name"};
	private JComboBox[] jcbSort = {new JComboBox(select),new JComboBox(select)};
	public SearchStuInfo()
	{
		gsi = new GetStuInfo();
		//初始化数据
		this.initialData();
		this.addListener();
		//初始化界面
		this.initialFrame();
	}
	private void addListener() {
		jbSearch.addActionListener(this);
		jcbSort[0].addActionListener(this);
		jcbSort[1].addActionListener(this);
	}
	//初始化数据的方法
	public void initialData()
	{
		//初始化表头
		for(int i = 0;i < this.select.length;i++){
			v_head.add(select[i]);
		}
	}
	//初始化界面的方法
	public void initialFrame()
	{ //创建表格并添加到容器
		this.setLayout(null);
		jl.setBounds(60,20,60,30);
		this.add(jl);
		for(int i = 0;i <jcbSort.length;i++)
		{
			jcbSort[i].setBounds(120+i*310, 20, 80, 30);
			jcbSort[i].setFocusable(false);
			this.add(jcbSort[i]);
		}
		jtfSearch.setBounds(210,20,150,30);
		this.add(jtfSearch);
		j2.setBounds(370,20,60,30);
		this.add(j2);
		jbSearch.setBounds(520,20,100,30);
		jbSearch.setOpaque(false);
		this.add(jbSearch);
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt=new JTable(dtm){
			private static final long serialVersionUID = -6036145391917188462L;

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
					System.out.println("hahhaah");
					System.out.println(index);
					String stu_id = jt.getValueAt(index, 0).toString().trim();
					System.out.println(stu_id);
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
						JDialog jDialog = new JDialog(jf,stu_id+"信息",true);
						jDialog.add(stu_info);
						jDialog.setLocationRelativeTo(null);
						Dimension screenSize = Toolkit.
					             getDefaultToolkit().getScreenSize();
						int centerX = screenSize.width/2;
						int centerY = screenSize.height/2;
						jDialog.setBounds(centerX-350,centerY-350,700,650);
						jDialog.setVisible(true);
					}
				}
			}
		});
		jsp=new JScrollPane(jt);
		jsp.setBounds(60,60,560,500);
		jsp.setBackground(new Color(228,236,197));
		jsp.getViewport().setOpaque(false);
		this.add(jsp);
		this.setOpaque(false);
		refreshTable();
	}
	
	//鼠标事件监听器的方法
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==jbSearch||e.getSource()==jcbSort[0]||e.getSource()==jcbSort[1])
		{
			showStuInfo();
			refreshTable();
		}
	}
	
	private void refreshTable() {
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();
		jt.getColumn(select[0]).setMinWidth(80);
		jt.getColumn(select[0]).setMaxWidth(80);
		jt.getColumn(select[1]).setMinWidth(80);
		jt.getColumn(select[1]).setMaxWidth(200);
		jt.getColumn(select[2]).setMinWidth(150);
		jt.getColumn(select[2]).setMaxWidth(150);
		jt.getColumn(select[4]).setMinWidth(50);	
		jt.getColumn(select[4]).setMaxWidth(50);		
	}
	private void showStuInfo() {
		//按下查询按钮的处理代码
		//获得输入的学号 
		String keyWord = jtfSearch.getText().trim();
		int p1 = jcbSort[1].getSelectedIndex();
		int p2 = jcbSort[0].getSelectedIndex();
		System.out.println(sResult);
		//根据学号调用GetScore的方法获得该学生的成绩信息
		v_data = gsi.getAllStuInfo(keyWord,sResult[p2],sResult[p1]);
		//更新表格模型
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		//更新显示
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();
	}
	
	public static void main(String[] args)
	{
		SearchStuInfo ss = new SearchStuInfo();
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(ss);
		jf.setVisible(true);
	}
}
