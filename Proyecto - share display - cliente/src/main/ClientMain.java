package main;

import javax.swing.JOptionPane;

import net.core.Connection;

import gui.frames.ClientMainFrame;

public class ClientMain {
	public static void main(String [] args) {
		
		try {
			Connection conn = new Connection();
			ClientMainFrame frame = new ClientMainFrame(conn);
			
			System.out.println("ClientMain.main() sended listener");
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
