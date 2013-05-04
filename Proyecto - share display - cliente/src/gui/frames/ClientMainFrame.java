package gui.frames;

import gui.listeners.FromUIListener;
import gui.listeners.ToUIListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import main.Items;

public class ClientMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final Dimension FRAME_DIMENSION = new Dimension(500, 500);
	
	private ArrayList<FromUIListener> fromUIListeners;
	private CoolPane pane;
	private String name;
	
	// Testing the repo
	public ClientMainFrame(FromUIListener from) {
		initProperties();
		initComponents();
		
		addFromUIListener(from);
		start();
	}
	
	
	public void addFromUIListener(FromUIListener fromUI) {
		fromUIListeners.add(fromUI);
		System.out.println("ClientMainFrame.addFromUIListener() added... "+fromUI);
	}


	private void initProperties() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(FRAME_DIMENSION);
	}

	
	private void initComponents() {
		int random = new Random().nextInt(Items.names.length);
		name = Items.names[random];
		
		pane = new CoolPane(name);
		fromUIListeners = new ArrayList<FromUIListener>();
		this.getContentPane().add(pane, BorderLayout.CENTER);
	}
	
	
	private void start() {
		this.setVisible(true);
		notifyStart();

	}
	
	
	public CoolPane getPane() {
		return pane;
	}
	
	
	// Notifica a la conexion que la ventana esta lista y manda datos del jugador
	public void notifyStart() {
		System.out.println("ClientMainFrame.notifyReady() notifing : "+name+" to "+fromUIListeners.size());
		for(FromUIListener fromUI : fromUIListeners)
			fromUI.onReady(this.getSize(), name, pane);
	}

}
