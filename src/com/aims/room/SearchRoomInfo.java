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
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.aims.DButil.DButil;
import com.aims.student.GetStuInfo;
import com.aims.student.SearchStuInfo;
import com.aims.student.StuInfo;

public class SearchRoomInfo extends JLabel implements ActionListener{
	private String aparID ;
	private JLabel jlRoomID = new JLabel("宿舍号");
	private JTextField jtfRoomID = new JTextField();
	private JButton jbSearch = new JButton("模糊查询");
	private JTable jt;
	private JFrame jf;
	private JScrollPane jsp;
	//创建用于存放表格数据的Vector
	private Vector<String> v_head=new Vector<String>();
	private Vector<String> v_data=new Vector<String>();
	
	private GetRoomInfo gri;
	public SearchRoomInfo(String aparID)
	{
		DButil.Power = DButil.Admin;
		this.aparID = aparID;
		gri = new GetRoomInfo(aparID);
		//初始化数据
		this.initialData();
		this.addListener();
		//初始化界面
		this.initialFrame();
	}


	private void addListener() {
		jbSearch.addActionListener(this);
	}


	private void initialData() {
		v_head.add("宿舍号");
		v_head.add("居住成员1");
		v_head.add("居住成员2");
		v_head.add("居住成员3");
		v_head.add("居住成员4");
	}
	private void initialFrame() {
		this.setLayout(null);
		jlRoomID.setBounds(60, 20, 60, 30);
		this.add(jlRoomID);
		jtfRoomID.setBounds(130, 20, 150, 30);
		this.add(jtfRoomID);
		jbSearch.setBounds(290, 20, 100, 30);
		jbSearch.setOpaque(false);
		this.add(jbSearch);
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
				int col = jt.columnAtPoint(p);
				if(event.getClickCount() >= 2 && event.getButton() == MouseEvent.BUTTON1){
					System.out.println("hahhaah");
					System.out.println(index);
					String ID = "";
					ID = jt.getValueAt(index, col).toString().trim();
					System.out.println(ID);
					if(!ID.equals(""))
					{
						JDialog jDialog;
						if(col == 0){
							RoomInfo room_info = new RoomInfo(aparID+" "+ID){
								public void paintComponent(Graphics g) {
									String path = "./images/background1.jpg";
									super.paintComponent(g);
									ImageIcon img = new ImageIcon(path);
									g.drawImage(img.getImage(), 0, 0, null);
								}
							};
							jDialog = new JDialog(jf,ID+"宿舍信息",true);
							jDialog.add(room_info);
						}
						else
						{
							StuInfo stu_info = new StuInfo(ID){
								public void paintComponent(Graphics g) {
									String path = "./images/background1.jpg";
									super.paintComponent(g);
									ImageIcon img = new ImageIcon(path);
									g.drawImage(img.getImage(), 0, 0, null);
								}
							};
							jDialog = new JDialog(jf,ID+"学生信息",true);
							jDialog.add(stu_info);
						}
						Dimension screenSize = Toolkit.
					             getDefaultToolkit().getScreenSize();
						int centerX = screenSize.width/2;
						int centerY = screenSize.height/2;
						jDialog.setBounds(centerX-350,centerY-350,700,650);
						jDialog.setVisible(true);
					}
				}
				else if(event.getClickCount() == 1 && SwingUtilities.isRightMouseButton(event)){
//					mainFrame.clientTable.changeSelection(index, 1, false, false);
//					mainFrame.clientPopupMenu.show(mainFrame.clientTable, p.x, p.y);
				}
			}
		});
		jsp=new JScrollPane(jt);
		jsp.setBounds(60,60,560,500);
		jsp.setBackground(new Color(228,236,197));
		jsp.getViewport().setOpaque(false);
		this.add(jsp);
		showRoomList();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==jbSearch)
		{//按下查询按钮的处理代码
		//获得输入的学号 
			showRoomList();
		}
	}


	private void showRoomList() {
		String keyWord = jtfRoomID.getText().trim();			
		//根据学号调用GetScore的方法获得该学生的成绩信息
		v_data = gri.getAllRoomInfo(keyWord);
		//更新表格模型
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		//更新显示
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();
	}
	
	public static void main(String[] args)
	{
		SearchRoomInfo ss = new SearchRoomInfo("D11");
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(ss);
		jf.setVisible(true);
	}
}
