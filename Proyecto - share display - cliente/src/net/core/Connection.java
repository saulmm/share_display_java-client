package net.core;

import gui.listeners.FromUIListener;
import gui.listeners.ToUIListener;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import main.Items;
import main.Protocol;
import main.entities.Player;

public class Connection implements FromUIListener {
	private ArrayList<ToUIListener> uiListeners;
	private DataInputStream dis;
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
		System.out.println("Connection.addToUIListener() added : "+toUI);
	}
	
	
	// Conecta al servidor
	public void connect() throws IOException {
		socket = new Socket(Items.LOCALHOST, Items.PORT);			
	}
	
	
	// Manda el jugador al servidor
	private void sendPlayer() {
		boolean conf = sendRequest(Protocol.PLAYER_REQUEST);
		ObjectOutputStream oos = null;
		
		if(conf) {
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(player);
				
				// Debug
				System.out.println("Client.sendPlayer() player sended "+player);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		waitServer();
	}
	
	
	// Hace una peticion al servidor y recibe la confirmacion
	private boolean sendRequest(int request) {
		DataOutputStream dos = null;
		dis = null;
		boolean conf = false;
		
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			
			dos.writeInt(request);

			int confirmation = dis.readInt();
			conf = (confirmation == Protocol.ACCEPT) ? true : false;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return conf;
	}
	
	
	// Espera a que el servidor le mande posiciones del objeto
	private void waitServer() {
		int currentElement = 0;
		try {
			System.out.println("Client.waitServer() waiting for server");
			while(currentElement != 999999999) {
				int x = dis.readInt();
				int y = dis.readInt();
				sendMoveToUI(x, y);
				System.out.println("Client.waitServer() "+x+" y "+y);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// Manda el movimiento recibido del servido al cliente grafico
	public void sendMoveToUI(int x, int y) {
		System.out.println("Connection.sendMoveToUI() sending : "+uiListeners.size());
		
		for(ToUIListener toUI : uiListeners)
			toUI.move(x, y);
	}
	

	@Override
	public void onReady(Dimension dim, String name, ToUIListener toUI) {
		System.out.println("Connection.onReady() notified ready");
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
