package com.aims.apartment;

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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.aims.DButil.DButil;

public class AparManagement  extends JPanel implements ActionListener{
	private GetAparInfo gai;
	private String apar_id;
	private JTable jt;
	private JFrame jf;
	private JScrollPane jsp;
	private JLabel jnotice = new JLabel("<html>��Ҫ�����޸ġ�ɾ��������������߱���е���ѡ��Ҫ���������</html>");
	private JLabel[] jLabel = {
			new JLabel("¥�����"),new JLabel("¥������"),new JLabel("¥����ַ")
	};
	private JButton[] jButton ={
			new JButton("����"),new JButton("ɾ��"),new JButton("����")
	};
	private JTextField[] jTextField = {
			new JTextField(),new JTextField(),new JTextField()
	};
	//�������ڴ�ű�����ݵ�Vector
	private Vector<String> v_head=new Vector<String>();
	private Vector<String> v_data=new Vector<String>();
	
	public AparManagement()
	{
		DButil.Power = DButil.SuperAdmin;
		gai = new GetAparInfo();
		//��ʼ������
		this.initialData();
		this.addListener();
		//��ʼ������
		this.initialFrame();
	}
	
	private void initialData() {
		v_head.add("¥�����");
		v_head.add("¥������");
		v_head.add("¥����ַ");
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
			jLabel[i].setBounds(420, 100+i*60, 200, 30);
			this.add(jLabel[i]);
		}
		for(int i = 0; i < jTextField.length;i++ )
		{
			jTextField[i].setBounds(420, 130+i*60, 200, 30);
			this.add(jTextField[i]);
		}
		for(int i = 0; i < jButton.length;i++ )
		{
			jButton[i].setBounds(420+i*70, 300, 60, 25);
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
					System.out.println("aparege double click");
					System.out.println(index);
					apar_id   = jt.getValueAt(index, 0).toString().trim();
					if(!apar_id.equals(""))
					{
						AparInfo apar_info = new AparInfo(apar_id){
							public void paintComponent(Graphics g) {
								String path = "./images/background1.jpg";
								super.paintComponent(g);
								ImageIcon img = new ImageIcon(path);
								g.drawImage(img.getImage(), 0, 0, null);
							}
						};
						JDialog jDialog = new JDialog(jf,apar_id+"¥����Ϣ",true);
						jDialog.add(apar_info);
						Dimension screenSize = Toolkit.
					             getDefaultToolkit().getScreenSize();
						int centerX = screenSize.width/2;
						int centerY = screenSize.height/2;
						jDialog.setBounds(centerX-350,centerY-350,700,650);
						jDialog.setVisible(true);
					}
				}
				else if(event.getClickCount() == 1 && event.getButton() == MouseEvent.BUTTON1){
					apar_id   = jt.getValueAt(index, 0).toString().trim();
					jt.getValueAt(index, 1).toString().trim();
					jt.getValueAt(index, 2).toString().trim();
					
				}
			}
		});
		jsp=new JScrollPane(jt);
		jsp.setBounds(40,90,340,500);
		jsp.setBackground(new Color(228,236,197));
		jsp.getViewport().setOpaque(false);
		this.add(jsp);
		this.setOpaque(false);
		showAparInfo();
	}

	private void showAparInfo()
	{
		v_data = gai.getAparInfo();
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
			resetTextfileds();
		}
		else if(e.getSource() == jButton[1])
		{
			DButil.deleteApar(apar_id);
		}
		else if(e.getSource() == jButton[0]){
				Vector<String> apar_info = new Vector<>();
				for(int i = 0;i<jTextField.length;i++)
					apar_info.add(jTextField[i].getText().trim());
				if(DButil.addApar(apar_info))
					resetTextfileds();
		}
		showAparInfo();
	}

	private void resetTextfileds() {
		for(int i = 0;i < jTextField.length;i++)
		{
			jTextField[i].setText("");
		}
	}
	
	public static void main(String[] args)
	{
		AparManagement c= new AparManagement();
		JFrame jf=new JFrame();
		jf.setBounds(10,10,700,650);
		jf.add(c);
		jf.setVisible(true);
	}
}
