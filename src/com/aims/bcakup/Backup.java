package com.aims.bcakup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.aims.DButil.DButil;
/*参考资料：http://blog.csdn.net/tkd03072010/article/details/6668940*/
public class Backup extends JPanel implements ActionListener {
	private JLabel jLabel = new JLabel("<html><font size = 7>数据的备份与恢复过程中<p><font color = #ff0041>请 勿 操 作  ! ! !</font></html>");
	private JButton[] jButtons = {
			new JButton("Backup"),new JButton("Rescovery")
	};
	private JFileChooser jFileChooser;
	public Backup()
	{
		initialFrame();
	}

	private void initialFrame() {
		this.setLayout(null);
		jLabel.setBounds(100 , 0, 500, 200);
		this.add(jLabel);
		for(int i = 0;i < jButtons.length;i++)
		{
			jButtons[i].setBounds(225+150*i, 300, 100, 30);
			jButtons[i].addActionListener(this);
			this.add(jButtons[i]);
		}
		this.setOpaque(false);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == jButtons[0])
		{
			System.out.println("backup in");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"*.bak", "bak");
			jFileChooser = new JFileChooser("./backup");
			jFileChooser.setFileFilter(filter);
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = jFileChooser.showSaveDialog(this);
			String fileName = "";
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File saveFile = jFileChooser.getSelectedFile();
				fileName = jFileChooser.getSelectedFile().getAbsolutePath();
				if(!saveFile.exists())
					DButil.backup(fileName);
				System.out.println(fileName);
			}
		}
		else if(e.getSource() == jButtons[1])
		{
			System.out.println("restore in");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"*.bak", "bak");
			jFileChooser = new JFileChooser("./backup");
			jFileChooser.setFileFilter(filter);
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = jFileChooser.showOpenDialog(this);
			String fileName = "";
			if(result == JFileChooser.APPROVE_OPTION)
			{
				File saveFile = jFileChooser.getSelectedFile();
				fileName = jFileChooser.getSelectedFile().getAbsolutePath();
				if(saveFile.exists())
					DButil.restore(fileName);
				System.out.println(fileName);
			}
		}
	}
	
	
	public static void main(String[] args) {
		Backup backup = new Backup();
		JFrame jFrame = new JFrame();
		jFrame.add(backup);
		jFrame.setBounds(0,0,650,700);
		jFrame.setVisible(true);
	}

}
