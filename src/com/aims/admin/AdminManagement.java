package com.aims.admin;

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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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

public class AdminManagement  extends JPanel implements ActionListener{
	private String admin_id;
	private ResultSet rs = null;
	private GetAdminInfo gai;
	private String[] sex ={"男","女"};
	private String[] manOrNot = {"已分配管理员","未分配管理员"};
	private Map<String, String> apar_info = new TreeMap<String,String>();
	private JTable jt;
	private JFrame jf;
	private JScrollPane jsp;
	private JComboBox[] jcb ={
			new JComboBox<>(manOrNot),new JComboBox<>(sex),new JComboBox<>()
	};
	private JLabel jnotice = new JLabel("<html>若要进行修改、删除操作，需在左边表格中单击选中要处理的数据</html>");
	private JLabel[] jlabel ={
			new JLabel("管理分配")
	};
	private JLabel[] jLabel = {
			new JLabel("新增学工号"),new JLabel("管理员姓名"),new JLabel("管理员性别"),
			new JLabel("删除 | 离职"),new JLabel("管理楼栋"),new JLabel("入职学工号")
	};
	private JButton[] jButton ={
			new JButton("新增管理员"),new JButton("清空"),new JButton("删除管理员")
	};
	private JButton[] jButton1 = {
			new JButton("入职办理"),new JButton("离职办理")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField(),new JTextField(),
			new JTextField()
	};
	//创建用于存放表格数据的Vector
	private Vector<String> v_head=new Vector<String>();
	private Vector<String> v_data=new Vector<String>();
	
	public AdminManagement()
	{
		DButil.Power = DButil.SuperAdmin;
		gai = new GetAdminInfo();
		//初始化数据
		this.initialData();
		this.addListener();
		//初始化界面
		this.initialFrame();
	}
	
	private void initialData() {
		v_head.add("工号");
		v_head.add("姓名");
		v_head.add("性别");
		v_head.add("管理楼栋");
		addComboxInfo();
	}
	
	private void addComboxInfo() {
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
			while(i.hasNext())
			{
				String apar_name = (String)i.next();
				jcb[2].addItem(apar_name);
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
		for(int i = 0; i < jButton1.length;i++ )
		{
			jButton1[i].addActionListener(this);
		}
	}
	
	private void initialFrame() {
		this.setLayout(null);
		jnotice.setBounds(150, 0, 400, 30);
		this.add(jnotice);
		for(int i=0;i<jlabel.length;i++){
			jlabel[i].setBounds(70, 65+25*i, 80, 20);
			this.add(jlabel[i]);
		}
		jcb[0].setBounds(150, 65 , 180, 20);	
		jcb[1].setBounds(460, 185, 100, 20);	
		jcb[2].setBounds(460, 415, 200, 20);	
		for(int i =0 ;i<jcb.length;i++){
			this.add(jcb[i]);
		}
		for(int i = 0; i < jLabel.length;i++ )
		{
			if(i == 3)
			{
				jLabel[i].setBounds(380, 310, 200, 30);
			}
			else if(i == 4)
			{
				jLabel[i].setBounds(380, 410, 200, 30);			
			}
			else if(i == 5)
			{
				jLabel[i].setBounds(380, 450, 200, 30);			
			}
			else
				jLabel[i].setBounds(380, 110+i*35, 200, 30);
			this.add(jLabel[i]);
		}
		for(int i=0;i<jTextField.length;i++){
			if(i == 2)
			{
				jTextField[i].setBounds(460, 310, 200, 30);				
				jTextField[i].setEditable(false);				
			}
			else if(i == 3)
			{
				jTextField[i].setBounds(460, 450, 200, 30);				
				jTextField[i].setEditable(false);				
			}
			else
				jTextField[i].setBounds(460, 110+i*35, 200, 30);								
			this.add(jTextField[i]);
		}
		for(int i = 0; i < jButton.length;i++ )
		{
			if(i == 1)
				jButton[i].setBounds(490, 250, 60, 25);
			else
				jButton[i].setBounds(380+i*90, 250, 100, 25);
			this.add(jButton[i]);
		}
		for(int i = 0;i < jButton1.length;i++)
		{
			jButton1[i].setBounds(380+i*180, 370, 100, 25);
			this.add(jButton1[i]);
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
					admin_id   = jt.getValueAt(index, 0).toString().trim();
					AdminInfo admin_info = new AdminInfo(admin_id){
						public void paintComponent(Graphics g) {
							String path = "./images/background1.jpg";
							super.paintComponent(g);
							ImageIcon img = new ImageIcon(path);
							g.drawImage(img.getImage(), 0, 0, null);
						}
					};
					JDialog jDialog = new JDialog(jf,admin_id+"信息",true);
					jDialog.add(admin_info);
					jDialog.setLocationRelativeTo(null);
					Dimension screenSize = Toolkit.
				             getDefaultToolkit().getScreenSize();
					int centerX = screenSize.width/2;
					int centerY = screenSize.height/2;
					jDialog.setBounds(centerX-350,centerY-350,700,650);
					jDialog.setVisible(true);
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					admin_id   = jt.getValueAt(index, 0).toString().trim();
					if(jcb[0].getSelectedIndex() == 0)
						jTextField[2].setText(admin_id);
					else
						jTextField[3].setText(admin_id);
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
		int index = jcb[0].getSelectedIndex();
		v_data = gai.getAdminInfo(index);
		//更新表格模型
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();	
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		String selectedAdminId = jTextField[2].getText();
		if(e.getSource() == jcb[0])
		{
		}
		else if(e.getSource() == jButton[1])
		{
			resetTextfileds();
		}
		else if(e.getSource() == jButton[2])
		{
			DButil.deleteAdmin(selectedAdminId);
		}
		else if(e.getSource() == jButton[0]){
			Vector<String> adminInfo = new Vector<>();
			String admin_id   = jTextField[0].getText().trim();
			String admin_name = jTextField[1].getText().trim();
			String sex = "";
			String regex = "[A-Z][0-9]{9}";
			if(!Pattern.matches(regex,admin_id))
			{
				JOptionPane.showMessageDialog(null, "工号格式为UXXXXXXXXX,例如U201215072");
				return;
			}
			if(admin_name.equals(""))
			{
				JOptionPane.showMessageDialog(null, "姓名不可为空");
				return;
			}
			if(jcb[1].getSelectedItem().equals("男"))
				sex = "1";
			else
				sex = "0";
			adminInfo.add(admin_id);
			adminInfo.add(admin_name);
			adminInfo.add("管理员");
			adminInfo.add(sex);
			if(DButil.addAdmin(adminInfo))
				resetTextfileds();
		}
		else if(e.getSource() == jButton1[0])
		{
			String apar_name = (String)jcb[2].getSelectedItem();
			String apar_id = apar_info.get(apar_name);
			String adminID = jTextField[3].getText();
			if(DButil.addManagement(apar_id,adminID))
				resetTextfileds();
		}
		else if(e.getSource() == jButton1[1])
		{
			DButil.deleteFromManagement(selectedAdminId);
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
		AdminManagement c= new AdminManagement();
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(c);
		jf.setVisible(true);
	}
}
