package com.aims.student;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

import com.aims.DButil.DButil;
import com.aims.apartment.AparInfo;
import com.aims.changepasw.ChangePasw;
import com.aims.room.RoomInfo;
import com.aims.welcome.Welcome;
public class StudentClient extends JFrame
{
	private static final long serialVersionUID = -4840255799109555258L;

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
	//声明标志学生学号的变量
	private String stu_id;
	private String room_id;
	private String apar_id;
	//创建树的各个节点
	private DefaultMutableTreeNode dmtnRoot = 
			new DefaultMutableTreeNode(new MyNode("操作选项","0"));
	private DefaultMutableTreeNode dmtn1 =
			new DefaultMutableTreeNode(new MyNode("系统选项","1"));
	private DefaultMutableTreeNode dmtn2 =
			new DefaultMutableTreeNode(new MyNode("基本信息","2"));
	private DefaultMutableTreeNode dmtn11 =
			new DefaultMutableTreeNode(new MyNode("退出","11"));
	private DefaultMutableTreeNode dmtn12 =
			new DefaultMutableTreeNode(new MyNode("密码修改","12"));
	private DefaultMutableTreeNode dmtn21 =
			new DefaultMutableTreeNode(new MyNode("个人基本信息","21"));
	private DefaultMutableTreeNode dmtn22 =
			new DefaultMutableTreeNode(new MyNode("宿舍基本信息","22"));
	private DefaultMutableTreeNode dmtn23 =
			new DefaultMutableTreeNode(new MyNode("楼栋基本信息","23"));
	
	//创建根节点
	private DefaultTreeModel dtm=new DefaultTreeModel(dmtnRoot);
	//创建树状列表控件
	private JTree jtree = new JTree(dtm);
	private JScrollPane jspz=new JScrollPane(jtree);
	//创建存放个功能模块面板
	private JPanel jpy=new JPanel();
	private JSplitPane jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jspz,jpy){
		 public void paintComponent(Graphics g) {
			 String path = "./images/background.jpg";
			 super.paintComponent(g);
			 ImageIcon img = new ImageIcon(path);
			 g.drawImage(img.getImage(), 0, 0, null);
			 }
	};
	//声明卡片布局的引用
	private CardLayout cl;
	//声明欢迎页面
	private Welcome welcome;
	//密码更改页面
	private ChangePasw changepasw;
	//个人信息查询页面
	private StuInfo stuinfo;
	//宿舍信息显示
	private AparInfo aparinfo;
	//楼栋信息显示
	private RoomInfo roominfo;
	//构造器
	public StudentClient(String stu_id)
	{
		this.stu_id=stu_id;
		room_id = DButil.getStuLiveRoom(stu_id);
		apar_id = room_id.split(" ")[0];
		System.out.println(apar_id);
		DButil.Power = DButil.Student;
		//初始化树状列表控件
		this.initialTree();
		//初始化个功能模块面板
		this.initialPane();
		//初始化主功能面板，其他面板都一卡
		//片布局的形式存在与该面板
		this.initialJpy();
		//为控件注册监听器
		this.addListener();
		//初始化窗体
		this.initialFrame();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){ 
		    public void windowClosing(WindowEvent e){ 
		    	quitSystem();
		    } 
		    
		});
	}
	//主面板的初始化方法
	public void initialJpy()
	{//将各功能模块以卡片布局的形式存入主面板
		jpy.setLayout(new CardLayout());
		cl=(CardLayout)jpy.getLayout();
		jpy.setBackground(null);
		jpy.setOpaque(false);
		jpy.add(welcome,"welcome");
		jpy.add(changepasw,"changepasw");
		jpy.add(stuinfo,"stuinfo");
		jpy.add(roominfo,"roominfo");
		jpy.add(aparinfo,"aparinfo");
	}
	//初始化各功能模块的方法
	public void initialPane()
	{
		welcome=new Welcome("学生成绩管理系统");
		changepasw = new ChangePasw(stu_id);
		stuinfo = new StuInfo(stu_id);
		roominfo = new RoomInfo(stu_id);
		aparinfo = new AparInfo(apar_id);
	}
	//初始化树状列表控件的方法
	public void initialTree()
	{
		DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
		cellRenderer.setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
		cellRenderer.setBackgroundSelectionColor(Color.LIGHT_GRAY);
		jtree.setCellRenderer(cellRenderer);
		dmtnRoot.add(dmtn1);
		dmtnRoot.add(dmtn2);
		
		dmtn1.add(dmtn11);
		dmtn1.add(dmtn12);
		dmtn2.add(dmtn21);
		dmtn2.add(dmtn22);
		dmtn2.add(dmtn23);
		jtree.setToggleClickCount(1);
	}
	//为树状列表控件注册鼠标事件监听器的方法
	public void addListener()
	{
		jtree.addMouseListener(
               new MouseAdapter()
               {
               	  public void mouseClicked(MouseEvent e)
               	  { 
               	      DefaultMutableTreeNode dmtntemp=(DefaultMutableTreeNode)jtree.getLastSelectedPathComponent();
					  MyNode mynode=(MyNode)dmtntemp.getUserObject();
					  String id=mynode.getId();
					  //根据id值显示不同的卡片
					  if(id.equals("0"))
					  {
					  	    cl.show(jpy,"note");
					  }
               	      else if(id.equals("11"))
               	      {
               	      	    int i=JOptionPane.showConfirmDialog(jpy,"您确认要退出出系统吗？","询问",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
               	      		if(i==0)
               	      		{
               	      			System.exit(0);
               	      		}
               	      		
               	      }
               	      else if(id.equals("12"))
               	      {
               	      		cl.show(jpy,"changepasw");
               	      		changepasw.setFocus();
               	      }
               	      else if(id.equals("21"))
               	      {
               	      		cl.show(jpy,"stuinfo");
               	      		//stuinfo.setFocus();
               	      }
               	      else if(id.equals("22"))
               	      {
               	      		cl.show(jpy,"roominfo");
               	      }
               	      else if(id.equals("23"))
               	      {
               	      	    //在显示之后立即更新数据
               	      		cl.show(jpy,"aparinfo");
               	      }
	              }
	           }
		);
	}	
	//初始化窗体的方法
	public void initialFrame()
	{
		this.add(jsp1);
		jsp1.setDividerLocation(200);
		jsp1.setDividerSize(4);
		jsp1.setEnabled(false);
		jspz.getViewport().setOpaque(false);
		jspz.setOpaque(false);
		jtree.setOpaque(false);
		//设置窗体的标题、大小及其可见性
		this.setTitle("学生客户端");
		Image image=new ImageIcon("ico.gif").getImage();  
 		this.setIconImage(image);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=900;//本窗体宽度
		int h=650;//本窗体高度
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);//设置窗体出现在屏幕中央
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter(){ 
		    public void windowClosing(WindowEvent e){ 
		    	quitSystem();
		    }     
		});
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
  			if(DButil.exitSys(stu_id))
  				System.exit(0);
  			else{
  				JOptionPane.showConfirmDialog(null,"退出系统异常");
  			}
  		}
  		else
  			return;
	}
	//自定义的初始化树节点的数据对象的类
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
	
	public static void main(String[] args)
	{
		new StudentClient("U201215072");
	}
}