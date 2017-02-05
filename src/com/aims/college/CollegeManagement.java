package com.aims.college;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.aims.DButil.DButil;
import com.aims.room.GetRoomInfo;
import com.aims.room.RoomInfo;
import com.aims.room.SearchRoomInfo;
import com.aims.student.StuInfo;

public class CollegeManagement  extends JPanel implements ActionListener{
	private GetCollegeInfo gci;
	private String coll_id;
	private String coll_name;
	private JTable jt;
	private JFrame jf;
	private JScrollPane jsp;
	private JLabel jnotice = new JLabel("<html>��Ҫ�����޸ġ�ɾ��������������߱���е���ѡ��Ҫ���������</html>");
	private JLabel[] jLabel = {
			new JLabel("Ժϵ����")
	};
	private JButton[] jButton ={
			new JButton("����"),new JButton("�޸�"),new JButton("ɾ��")
	};
	private JTextField[] jTextField = {
			new JTextField()
	};
	//�������ڴ�ű�����ݵ�Vector
	private Vector<String> v_head=new Vector<String>();
	private Vector<String> v_data=new Vector<String>();
	
	public CollegeManagement()
	{
		DButil.Power = DButil.SuperAdmin;
		gci = new GetCollegeInfo();
		//��ʼ������
		this.initialData();
		this.addListener();
		//��ʼ������
		this.initialFrame();
	}
	
	private void initialData() {
		v_head.add("Ժϵ���");
		v_head.add("Ժϵ����");
	}
	
	private void addListener() {
		for(int i = 0; i < jButton.length;i++ )
		{
			jButton[i].addActionListener(this);
		}
	}
	
	private void initialFrame() {
		this.setLayout(null);
		jnotice.setBounds(150, 0, 400, 30);
		this.add(jnotice);
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
			 * �������¼�
			 * ����ʱʹѡ����
			 * ˫��ʱ������ļ�������ʾ��Ŀ¼�µ��ļ�
			 */
			public void mouseClicked(MouseEvent event){
				Point p = event.getPoint();
				int index = jt.rowAtPoint(p);
				if(event.getClickCount() >= 2 && event.getButton() == MouseEvent.BUTTON1){
					System.out.println("College double click");
					System.out.println(index);
					coll_id   = jt.getValueAt(index, 0).toString().trim();
					coll_name = jt.getValueAt(index, 1).toString().trim();
					if(!coll_id.equals(""))
					{
						jTextField[0].setText(coll_name);
					}
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					coll_id   = jt.getValueAt(index, 0).toString().trim();
					coll_name = jt.getValueAt(index, 1).toString().trim();
				}
			}
		});
		jsp=new JScrollPane(jt);
		jsp.setBounds(60,90,300,500);
		jsp.setBackground(new Color(228,236,197));
		jsp.getViewport().setOpaque(false);
		this.add(jsp);
		this.setOpaque(false);
		showCollegeInfo();
	}

	private void showCollegeInfo()
	{
		v_data = gci.getCollegeInfo();
		//���±��ģ��
		DefaultTableModel dtm=new DefaultTableModel(v_data,v_head);
		jt.setModel(dtm);
		//������ʾ
		((DefaultTableModel)jt.getModel()).fireTableStructureChanged();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == jButton[2])
		{
			DButil.deleteCollege(coll_id);
		}
		else
		{
			if(e.getSource() == jButton[0]){
				String name = jTextField[0].getText();
				System.out.println(name);
				String id = DButil.createCollegeID();
				DButil.addCollege(id,name);
			}
			else if(e.getSource() == jButton[1])
			{
				String name = jTextField[0].getText();
				DButil.updateCollege(coll_id,name);
			}
		}
		showCollegeInfo();
	}
	
	public static void main(String[] args)
	{
		CollegeManagement c= new CollegeManagement();
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(c);
		jf.setVisible(true);
	}
}
