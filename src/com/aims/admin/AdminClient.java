package com.aims.admin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.aims.DButil.DButil;
import com.aims.apartment.AparInfo;
import com.aims.changepasw.ChangePasw;
import com.aims.room.RoomManagement;
import com.aims.room.SearchRoomInfo;
import com.aims.student.SearchStuInfo;
import com.aims.welcome.Welcome;

public class AdminClient extends JFrame{
	private static final long serialVersionUID = 3181020108880845660L;
	private String admin_id;
	private String aparID;
	private Welcome welcome;
	private ChangePasw changePasw;
	private SearchStuInfo searchStuInfo;
	private SearchRoomInfo searchRoomInfo;
	private AparInfo aparInfo;
	private AdminInfo adminInfo;
	private RoomManagement roomManagement;
	class MyNode
	{
		private String values;
		private String id;
		public MyNode(String values,String id)
		{
			this.values=values;
			this.id=id;
		}
		public String toString()
		{
			return this.values;
		}
		public String getId()
		{
			return this.id;
		}
	}
	static {
		try {
			UIManager.setLookAndFeel(
			         UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}
	private DefaultMutableTreeNode dmtnRoot=
	        new DefaultMutableTreeNode(new MyNode("系统操作","0"));
	private DefaultMutableTreeNode dmtn1=
	        new DefaultMutableTreeNode(new MyNode("系统选项","1"));
	private DefaultMutableTreeNode dmtn2=
	        new DefaultMutableTreeNode(new MyNode("信息维护","2"));
	private DefaultMutableTreeNode dmtn3=
			new DefaultMutableTreeNode(new MyNode("入|退住办理","3"));
	private DefaultMutableTreeNode dmtn11=
	        new DefaultMutableTreeNode(new MyNode("退出","11"));
	private DefaultMutableTreeNode dmtn12=
	        new DefaultMutableTreeNode(new MyNode("密码修改","12"));
	private DefaultMutableTreeNode dmtn21=
	        new DefaultMutableTreeNode(new MyNode("学生信息维护","21"));
	private DefaultMutableTreeNode dmtn22=
	        new DefaultMutableTreeNode(new MyNode("宿舍信息维护","22"));
	private DefaultMutableTreeNode dmtn23=
	        new DefaultMutableTreeNode(new MyNode("楼栋信息维护","23"));
	private DefaultMutableTreeNode dmtn24=
	        new DefaultMutableTreeNode(new MyNode("管理员信息维护","24"));
	
	//创建跟节点
	private DefaultTreeModel dtm=new DefaultTreeModel(dmtnRoot);
	//创建树状列表控件
	private JTree jt=new JTree(dtm);
	//创建滚动窗口
	private JScrollPane jspz=new JScrollPane(jt);
	//创建面板
	private JPanel jpy = new JPanel();
	//创建分割窗格
	private JSplitPane jsp1=new JSplitPane(
		                    JSplitPane.HORIZONTAL_SPLIT,jspz,jpy)
	{
		 public void paintComponent(Graphics g) {
			 String path = "./images/background.jpg";
			 super.paintComponent(g);
			 ImageIcon img = new ImageIcon(path);
			 g.drawImage(img.getImage(), 0, 0, null);
			 }
	};
	//声明卡片布局引用
	CardLayout cl;
	
	public AdminClient(String admin_id)
	{
		DButil.Power = DButil.Admin;
		this.admin_id = admin_id;
		aparID = DButil.getAdminManaApar(admin_id);
		//初始化树状列表控件
		this.initialTree();
		//创建各功能模块对象
		this.initialPanel();
		//为节点注册监听器
		this.addListener();
		//初始化面板
		this.initialJpy();
		//初始化主窗体
		this.initialFrame();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){ 
		    public void windowClosing(WindowEvent e){ 
		    	quitSystem();
		    } 
		    
		});
		
	}
	
	
	
	public void initialTree()
	{
		DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
		cellRenderer.setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
		cellRenderer.setBackgroundSelectionColor(Color.LIGHT_GRAY);
		jt.setCellRenderer(cellRenderer);
		dmtnRoot.add(dmtn1);
		dmtnRoot.add(dmtn2);
		dmtnRoot.add(dmtn3);
		dmtn1.add(dmtn11);
		dmtn1.add(dmtn12);
		dmtn2.add(dmtn21);
		dmtn2.add(dmtn22);
		dmtn2.add(dmtn23);
		dmtn2.add(dmtn24);
		jpy.setOpaque(false);
	}
	
	public void initialPanel()
	{//初始化各功能模块
	     //初始化代码在后面各模块的开发过程中逐一添加
	     welcome = new Welcome(admin_id);
	     changePasw = new ChangePasw(admin_id);
	     searchStuInfo = new SearchStuInfo();
	     searchRoomInfo = new SearchRoomInfo(aparID);
	     aparInfo = new AparInfo(aparID);
	     adminInfo = new AdminInfo(admin_id);
	     roomManagement = new RoomManagement(aparID);
	}
	
	public void addListener()
	{//为树状列表控件注册鼠标事件监听器
		jt.addMouseListener(
	           new MouseAdapter()
	           {
	           	  public void mouseClicked(MouseEvent e)
	           	  { 
	           	      DefaultMutableTreeNode dmtntemp=(DefaultMutableTreeNode)jt.
	           	    		  getLastSelectedPathComponent();
					  MyNode mynode=(MyNode)dmtntemp.getUserObject();
					  String id=mynode.getId();
					  //根据id值显示不同的卡片
					  if(id.equals("0"))
					  {//欢迎页面
					        cl.show(jpy,"welcome");
					  }
					  else if(id.equals("3"))
					  {
						  cl.show(jpy, "roomManagement");
					  }
	           	      else if(id.equals("11"))
	           	      {//退出系统
	           	            quitSystem();
	           	      }
	           	      else if(id.equals("12"))
	           	      {//更改密码页面
	           	      	 cl.show(jpy,"changePasw");
	           	      	 changePasw.setFocus();
	           	      }
	           	      else if(id.equals("21"))
	           	      {//添加学生页面
	           	      	 cl.show(jpy,"searchStuInfo");
	           	      }
	           	      else if(id.equals("22"))
	           	      {//学生信息查询页面
	           	    	 cl.show(jpy,"searchRoomInfo");
	           	      }
	           	      else if(id.equals("23"))
	           	      {//成绩查询页面
	           	      	 cl.show(jpy,"aparInfo");
	           	      }
	           	      else if(id.equals("24"))
	           	      {//选课管理页面
	           	    	cl.show(jpy,"adminInfo");
	           	      }
	           	     
	              }
	           }
		                       );
		//将展开节点的鼠标点击次数设为1
		jt.setToggleClickCount(1);
	}
	public void initialJpy()
	{//将各功能模块添加到面板中
	    //将面板设置为卡片布局
		jpy.setLayout(new CardLayout());
		cl=(CardLayout)jpy.getLayout();
		jpy.setBackground(null);
		jpy.setOpaque(false);
		//各功能模块将在后面各模块的开发过程中逐一添加
		jpy.add(welcome,"welcome");
		jpy.add(changePasw,"changePasw");
		jpy.add(searchStuInfo,"searchStuInfo");
		jpy.add(aparInfo,"aparInfo");
		jpy.add(searchRoomInfo,"searchRoomInfo");
		jpy.add(adminInfo,"adminInfo");
		jpy.add(roomManagement,"roomManagement");
		
	}
	
	public void initialFrame()
	{   //设置窗体的标题、大小及其可见性
		this.add(jsp1);
		jsp1.setDividerLocation(200);
		jsp1.setDividerSize(2);
		jsp1.setEnabled(false);
		jspz.getViewport().setOpaque(false);
		jspz.setOpaque(false);
		jt.setOpaque(false);
		Image image=new ImageIcon("./images/AIMS.png").getImage();  
		this.setIconImage(image);
		this.setTitle("管理员登陆");
		Dimension screenSize = Toolkit.
		             getDefaultToolkit().getScreenSize();
		int centerX = screenSize.width/2;
		int centerY = screenSize.height/2;
		int w=900;//本窗体宽度
		int h=650;//本窗体高度
		//设置窗体出现在屏幕中央
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);

		this.setVisible(true);
		this.setResizable(false);
		
		//窗体全屏
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public static void main(String[] args) {
		new AdminClient("M201215073");
	}
	public void quitSystem()
	{
		int i = JOptionPane.showConfirmDialog(jsp1,
	                 "您确认要退出系统吗？","退出",
	                  JOptionPane.YES_NO_OPTION,
	                   JOptionPane.QUESTION_MESSAGE);
		System.out.println("iiiiii"+i);
  		if(i == JOptionPane.YES_OPTION)
  		{
  			if(DButil.exitSys(admin_id))
  				System.exit(0);
  			else{
  				JOptionPane.showConfirmDialog(null,"退出系统异常");
  			}
  		}
  		else
  			return;
	}
	public void quitSystem(int i)
	{
		System.exit(0);
	}
}
