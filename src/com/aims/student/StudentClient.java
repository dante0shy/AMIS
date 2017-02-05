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
	//������־ѧ��ѧ�ŵı���
	private String stu_id;
	private String room_id;
	private String apar_id;
	//�������ĸ����ڵ�
	private DefaultMutableTreeNode dmtnRoot = 
			new DefaultMutableTreeNode(new MyNode("����ѡ��","0"));
	private DefaultMutableTreeNode dmtn1 =
			new DefaultMutableTreeNode(new MyNode("ϵͳѡ��","1"));
	private DefaultMutableTreeNode dmtn2 =
			new DefaultMutableTreeNode(new MyNode("������Ϣ","2"));
	private DefaultMutableTreeNode dmtn11 =
			new DefaultMutableTreeNode(new MyNode("�˳�","11"));
	private DefaultMutableTreeNode dmtn12 =
			new DefaultMutableTreeNode(new MyNode("�����޸�","12"));
	private DefaultMutableTreeNode dmtn21 =
			new DefaultMutableTreeNode(new MyNode("���˻�����Ϣ","21"));
	private DefaultMutableTreeNode dmtn22 =
			new DefaultMutableTreeNode(new MyNode("���������Ϣ","22"));
	private DefaultMutableTreeNode dmtn23 =
			new DefaultMutableTreeNode(new MyNode("¥��������Ϣ","23"));
	
	//�������ڵ�
	private DefaultTreeModel dtm=new DefaultTreeModel(dmtnRoot);
	//������״�б�ؼ�
	private JTree jtree = new JTree(dtm);
	private JScrollPane jspz=new JScrollPane(jtree);
	//������Ÿ�����ģ�����
	private JPanel jpy=new JPanel();
	private JSplitPane jsp1=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jspz,jpy){
		 public void paintComponent(Graphics g) {
			 String path = "./images/background.jpg";
			 super.paintComponent(g);
			 ImageIcon img = new ImageIcon(path);
			 g.drawImage(img.getImage(), 0, 0, null);
			 }
	};
	//������Ƭ���ֵ�����
	private CardLayout cl;
	//������ӭҳ��
	private Welcome welcome;
	//�������ҳ��
	private ChangePasw changepasw;
	//������Ϣ��ѯҳ��
	private StuInfo stuinfo;
	//������Ϣ��ʾ
	private AparInfo aparinfo;
	//¥����Ϣ��ʾ
	private RoomInfo roominfo;
	//������
	public StudentClient(String stu_id)
	{
		this.stu_id=stu_id;
		room_id = DButil.getStuLiveRoom(stu_id);
		apar_id = room_id.split(" ")[0];
		System.out.println(apar_id);
		DButil.Power = DButil.Student;
		//��ʼ����״�б�ؼ�
		this.initialTree();
		//��ʼ��������ģ�����
		this.initialPane();
		//��ʼ����������壬������嶼һ��
		//Ƭ���ֵ���ʽ����������
		this.initialJpy();
		//Ϊ�ؼ�ע�������
		this.addListener();
		//��ʼ������
		this.initialFrame();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){ 
		    public void windowClosing(WindowEvent e){ 
		    	quitSystem();
		    } 
		    
		});
	}
	//�����ĳ�ʼ������
	public void initialJpy()
	{//��������ģ���Կ�Ƭ���ֵ���ʽ���������
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
	//��ʼ��������ģ��ķ���
	public void initialPane()
	{
		welcome=new Welcome("ѧ���ɼ�����ϵͳ");
		changepasw = new ChangePasw(stu_id);
		stuinfo = new StuInfo(stu_id);
		roominfo = new RoomInfo(stu_id);
		aparinfo = new AparInfo(apar_id);
	}
	//��ʼ����״�б�ؼ��ķ���
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
	//Ϊ��״�б�ؼ�ע������¼��������ķ���
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
					  //����idֵ��ʾ��ͬ�Ŀ�Ƭ
					  if(id.equals("0"))
					  {
					  	    cl.show(jpy,"note");
					  }
               	      else if(id.equals("11"))
               	      {
               	      	    int i=JOptionPane.showConfirmDialog(jpy,"��ȷ��Ҫ�˳���ϵͳ��","ѯ��",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
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
               	      	    //����ʾ֮��������������
               	      		cl.show(jpy,"aparinfo");
               	      }
	              }
	           }
		);
	}	
	//��ʼ������ķ���
	public void initialFrame()
	{
		this.add(jsp1);
		jsp1.setDividerLocation(200);
		jsp1.setDividerSize(4);
		jsp1.setEnabled(false);
		jspz.getViewport().setOpaque(false);
		jspz.setOpaque(false);
		jtree.setOpaque(false);
		//���ô���ı��⡢��С����ɼ���
		this.setTitle("ѧ���ͻ���");
		Image image=new ImageIcon("ico.gif").getImage();  
 		this.setIconImage(image);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=900;//��������
		int h=650;//������߶�
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);//���ô����������Ļ����
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
	                 "��ȷ��Ҫ�˳�ϵͳ��","�˳�",
	                  JOptionPane.YES_NO_OPTION,
	                   JOptionPane.QUESTION_MESSAGE);
		System.out.println("iiiiii"+i);
  		if(i == JOptionPane.YES_OPTION)
  		{
  			if(DButil.exitSys(stu_id))
  				System.exit(0);
  			else{
  				JOptionPane.showConfirmDialog(null,"�˳�ϵͳ�쳣");
  			}
  		}
  		else
  			return;
	}
	//�Զ���ĳ�ʼ�����ڵ�����ݶ������
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