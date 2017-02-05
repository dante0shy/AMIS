package com.aims.superadmin;

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
import com.aims.admin.AdminManagement;
import com.aims.apartment.AparManagement;
import com.aims.bcakup.Backup;
import com.aims.changepasw.ChangePasw;
import com.aims.classi.ClassManagement;
import com.aims.college.CollegeManagement;
import com.aims.dept.DeptManagement;
import com.aims.room.RoomManagement;
import com.aims.student.StudentManagement;
import com.aims.welcome.Welcome;

public class SuperAdminClient extends JFrame{
	
	private static final long serialVersionUID = -7932305514723441076L;
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
	private String sadmin_id;
	private String aparID;
	private String Power;
	private Welcome welcome;
	private ChangePasw changePasw;
	private AparManagement aparManagement;
	private RoomManagement roomManagement;
	private StudentManagement studentManagement;
	private AdminManagement adminManagement;
	private CollegeManagement collegeManagement;
	private DeptManagement deptManagement;
	private ClassManagement classi;
	private Backup backup;
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
	
	private DefaultMutableTreeNode dmtnRoot=
	        new DefaultMutableTreeNode(new MyNode("ϵͳ����","0"));
	private DefaultMutableTreeNode dmtn1=
	        new DefaultMutableTreeNode(new MyNode("ϵͳѡ��","1"));
	private DefaultMutableTreeNode dmtn2=
	        new DefaultMutableTreeNode(new MyNode("¥��������Ϣά��","2"));
	private DefaultMutableTreeNode dmtn3=
			new DefaultMutableTreeNode(new MyNode("ϵ����Ϣά��","3"));
	private DefaultMutableTreeNode dmtn4=
			new DefaultMutableTreeNode(new MyNode("���ݵĻ�ԭ|�ָ�","4"));
	private DefaultMutableTreeNode dmtn11=
	        new DefaultMutableTreeNode(new MyNode("�˳�","11"));
	private DefaultMutableTreeNode dmtn12=
	        new DefaultMutableTreeNode(new MyNode("�����޸�","12"));
	private DefaultMutableTreeNode dmtn21=
	        new DefaultMutableTreeNode(new MyNode("¥����Ϣά��","21"));
	private DefaultMutableTreeNode dmtn22=
	        new DefaultMutableTreeNode(new MyNode("������Ϣά��","22"));
	private DefaultMutableTreeNode dmtn23=
	        new DefaultMutableTreeNode(new MyNode("ѧ����Ϣά��","23"));
	private DefaultMutableTreeNode dmtn24=
	        new DefaultMutableTreeNode(new MyNode("����Ա��Ϣά��","24"));
	private DefaultMutableTreeNode dmtn31=
			new DefaultMutableTreeNode(new MyNode("ϵ����Ϣά��","31"));
	private DefaultMutableTreeNode dmtn32=
			new DefaultMutableTreeNode(new MyNode("רҵ��Ϣά��","32"));
	private DefaultMutableTreeNode dmtn33=
			new DefaultMutableTreeNode(new MyNode("�༶��Ϣά��","33"));
	//�������ڵ�
	private DefaultTreeModel dtm=new DefaultTreeModel(dmtnRoot);
	//������״�б�ؼ�
	private JTree jt=new JTree(dtm);
	//������������
	private JScrollPane jspz=new JScrollPane(jt);
	//�������
	private JPanel jpy = new JPanel();
	//�����ָ��
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
	//������Ƭ��������
	CardLayout cl;
	
	public SuperAdminClient(String sadmin_id)
	{
		DButil.Power = DButil.SuperAdmin;
		this.sadmin_id = sadmin_id;
		//��ʼ����״�б�ؼ�
		this.initialTree();
		//����������ģ�����
		this.initialPanel();
		//Ϊ�ڵ�ע�������
		this.addListener();
		//��ʼ�����
		this.initialJpy();
		//��ʼ��������
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
		jt.expandRow(0);
		dmtnRoot.add(dmtn1);
		dmtnRoot.add(dmtn2);
		dmtnRoot.add(dmtn3);
		dmtnRoot.add(dmtn4);
		dmtn1.add(dmtn11);
		dmtn1.add(dmtn12);
		dmtn2.add(dmtn21);
		dmtn2.add(dmtn22);
		dmtn2.add(dmtn23);
		dmtn2.add(dmtn24);
		dmtn3.add(dmtn31);
		dmtn3.add(dmtn32);
		dmtn3.add(dmtn33);
		
		//jt.setOpaque(false);
	}
	
	public void initialPanel()
	{//��ʼ��������ģ��
	     //��ʼ�������ں����ģ��Ŀ�����������һ���
	     welcome 		= new Welcome("");
	     changePasw 	= new ChangePasw(sadmin_id);
	     aparManagement = new AparManagement();
	     roomManagement = new RoomManagement("");
	     studentManagement = new StudentManagement();
	     adminManagement = new AdminManagement();
	     collegeManagement = new CollegeManagement();
	     deptManagement = new DeptManagement();
	     classi 		= new ClassManagement();
	     backup			= new Backup();
	}
	
	public void addListener()
	{//Ϊ��״�б�ؼ�ע������¼�������
		jt.addMouseListener(
	           new MouseAdapter()
	           {
	           	  public void mouseClicked(MouseEvent e)
	           	  { 
	           	      DefaultMutableTreeNode dmtntemp=(DefaultMutableTreeNode)jt.
	           	    		  getLastSelectedPathComponent();
					  MyNode mynode=(MyNode)dmtntemp.getUserObject();
					  String id=mynode.getId();
					  //����idֵ��ʾ��ͬ�Ŀ�Ƭ
					  if(id.equals("0"))
					  {//��ӭҳ��
						  cl.show(jpy,"welcome");
					  }
	           	      else if(id.equals("11"))
	           	      {//�˳�ϵͳ
	           	          quitSystem();
	           	      }
	           	      else if(id.equals("12"))
	           	      {//��������ҳ��
	           	    	  cl.show(jpy,"changePasw");
	           	    	  changePasw.setFocus();
	           	      }
	           	      else if(id.equals("21"))
	           	      {//���ѧ��ҳ��
	           	    	  cl.show(jpy,"aparManagement");
	           	      }
	           	      else if(id.equals("22"))
	           	      {//ѧ����Ϣ��ѯҳ��
	           	    	  cl.show(jpy,"roomManagement");
	           	      }
	           	      else if(id.equals("23"))
	           	      {//�ɼ���ѯҳ��
	           	    	  cl.show(jpy,"studentManagement");
	           	      }
	           	      else if(id.equals("24"))
	           	      {//ѡ�ι���ҳ��
	           	    	  cl.show(jpy,"adminManagement");
	           	      }
	           	      else if(id.equals("31"))
	           	      {//ѡ�ι���ҳ��
	           	    	  cl.show(jpy,"collegeManagement");
	           	      }
	           	      else if(id.equals("32"))
	           	      {//ѡ�ι���ҳ��
	           	    	  cl.show(jpy,"deptManagement");
	           	      }
	           	      else if(id.equals("33"))
	           	      {//ѡ�ι���ҳ��
	           	    	  cl.show(jpy,"classi");
	           	      }
	           	      else if(id.equals("4"))
	           	      {
	           	    	  cl.show(jpy,"backup");
	           	      }
					  
	           	     
	              }
	           }
		                       );
		//��չ���ڵ�������������Ϊ1
		jt.setToggleClickCount(1);
	}
	public void initialJpy()
	{//��������ģ����ӵ������
	    //���������Ϊ��Ƭ����
		jpy.setLayout(new CardLayout());
		cl=(CardLayout)jpy.getLayout();
		jpy.setBackground(null);
		jpy.setOpaque(false);
		//������ģ�齫�ں����ģ��Ŀ�����������һ���
		jpy.add(welcome,"welcome");
		jpy.add(changePasw,"changePasw");
		jpy.add(aparManagement,"aparManagement");
		jpy.add(roomManagement,"roomManagement");
		jpy.add(studentManagement,"studentManagement");
		jpy.add(adminManagement,"adminManagement");
		jpy.add(collegeManagement,"collegeManagement");
		jpy.add(deptManagement,"deptManagement");
		jpy.add(classi,"classi");
		jpy.add(backup,"backup");
	}
	
	public void initialFrame()
	{   //���ô���ı��⡢��С����ɼ���
		this.add(jsp1);
		jsp1.setDividerLocation(200);
		jsp1.setDividerSize(2);
		jsp1.setEnabled(false);
		jspz.getViewport().setOpaque(false);
		jspz.setOpaque(false);
		jt.setOpaque(false);
		Image image=new ImageIcon("./images/AIMS.png").getImage();  
		this.setIconImage(image);
		this.setTitle("��������Ա��½");
		Dimension screenSize = Toolkit.
		             getDefaultToolkit().getScreenSize();
		int centerX = screenSize.width/2;
		int centerY = screenSize.height/2;
		int w=900;//��������
		int h=650;//������߶�
		//���ô����������Ļ����
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);
		this.setVisible(true);
		this.setResizable(false);
		
		//����ȫ��
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	public static void main(String[] args) {
		new SuperAdminClient("S201215072");
	}
	public void quitSystem()
	{
		int i=JOptionPane.showConfirmDialog(jsp1,
	                 "��ȷ��Ҫ�˳���ϵͳ��","�˳�",
	                  JOptionPane.YES_NO_OPTION,
	                   JOptionPane.QUESTION_MESSAGE);
	      		if(i==0)
	      		{
	      			if(DButil.exitSys(sadmin_id))
	      				System.exit(0);
	      			else{
	      				JOptionPane.showConfirmDialog(null,"�˳�ϵͳ�쳣");
	      			}
	      		}
	}
}
