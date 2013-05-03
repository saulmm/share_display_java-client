package gui.frames;

import gui.listeners.ToUIListener;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CoolPane extends JPanel implements ToUIListener {
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int diameter;
	private String name;
	
	public CoolPane(String name) {
		initProperties();
		initComponents();
		this.name = name;
	}

	
	private void initProperties() {
		this.setBackground(Color.DARK_GRAY);
	}
	
	
	private void initComponents() {
		x = 100;
		y = 100;
		diameter = 40;
	};
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawOval(g);
	}
	
	
	private void drawOval(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(x, y, diameter, diameter);
	}
	

	
	
	private void sleepThread(long millis) {
		try {
			Thread.sleep(millis);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.repaint();
		System.out.println("CoolPane.move() NAME -> "+name+" : "+x+" y: "+y+" d : "+diameter);
	}


	
}
