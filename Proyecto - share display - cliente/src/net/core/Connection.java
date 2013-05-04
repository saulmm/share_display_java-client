package net.core;

import gui.listeners.FromUIListener;
import gui.listeners.ToUIListener;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import main.Items;
import main.Protocol;
import main.entities.Player;

public class Connection implements FromUIListener {
	private ArrayList<ToUIListener> uiListeners;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private Player player;
	
	
	// El constructor conecta directamente
	public Connection() throws IOException {
		player = new Player();
		uiListeners = new ArrayList<ToUIListener>();
		connect();
	}
	
	
	public void addToUIListener(ToUIListener toUI) {
		uiListeners.add(toUI);
	}
	
	
	// Conecta al servidor
	public void connect() throws IOException {
		socket = new Socket(Items.LOCALHOST, Items.PORT);			
		System.out.println("Connection.connect() connected to : "+socket.getPort());
	}
	
	
	// Manda el jugador al servidor
	private void sendPlayer() {
		boolean conf = sendRequest(Protocol.PLAYER_REQUEST);
		ObjectOutputStream oos = null;
		
		if(conf) {
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(player);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		waitServer();
	}
	
	
	// Hace una peticion al servidor y recibe la confirmacion
	private boolean sendRequest(int request) {
		boolean conf = false;
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(request);

			ois = new ObjectInputStream(socket.getInputStream());
			int confirmation = (int) ois.readObject();
			System.out.println("Connection.sendRequest() conf code : "+confirmation);
			conf = (confirmation == Protocol.ACCEPT) ? true : false;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return conf;
	}
	
	
	// Espera a que el servidor le mande posiciones del objeto
	private void waitServer() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			while(true) {
			int x = (int) ois.readObject();
			int y = (int) ois.readObject();
			System.out.println("Connection.waitServer() "+x+" "+y);
			sendMoveToUI(x, y);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	// Manda el movimiento recibido del servido al cliente grafico
	public void sendMoveToUI(int x, int y) {
		
		for(ToUIListener toUI : uiListeners)
			toUI.move(x, y);
	}
	

	@Override
	public void onReady(Dimension dim, String name, ToUIListener toUI) {
		addToUIListener(toUI);
		start(dim, name);
	}


	// Comienza la comunicacion con el servidor
	private void start(Dimension dim, String name) {
		player.setDimension(dim);
		player.setName(name);
		sendPlayer();
	}
	
}
