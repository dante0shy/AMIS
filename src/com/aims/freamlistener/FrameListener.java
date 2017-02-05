package com.aims.freamlistener;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.aims.login.Login;
import com.aims.login.NewLogin;

public class FrameListener extends MouseAdapter {
	private Point lastPoint = null;
	private NewLogin window = null;
	
	public FrameListener(NewLogin window){
		this.window = window;
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		lastPoint = e.getLocationOnScreen();
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		Point point = e.getLocationOnScreen();
		int offsetX = point.x - lastPoint.x;
		int offsetY = point.y - lastPoint.y;
		
		Rectangle bounds = window.getBounds();
		bounds.x += offsetX;
		bounds.y += offsetY;
		window.setBounds(bounds);
		lastPoint = point;
	}
}