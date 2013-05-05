package net.core;

import gui.listeners.FromUIListener;
import gui.listeners.ToUIListener;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import main.Items;
import main.Protocol;
import main.entities.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connection implements FromUIListener, Runnable {
	private ArrayList<ToUIListener> uiListeners;
	private final Logger log = LoggerFactory.getLogger(Connection.class);
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private Player player;
	
	
	// El constructor conecta directamente
	public Connection() throws IOException {
		player = new Player();
		uiListeners = new ArrayList<ToUIListener>();
		Thread connectionThread = new Thread(this);
		connectionThread.start();
	}
	
	
	public void addToUIListener(ToUIListener toUI) {
		uiListeners.add(toUI);
	}
	
	
	// Conecta al servidor
	public void connect() throws IOException {
		socket = new Socket(Items.LOCALHOST, Items.PORT);			
	}
	
	
	// Manda el jugador al servidor
	private void sendPlayer() {
		boolean conf = sendRequest(Protocol.PLAYER_REQUEST);
		
		if(conf) {
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(player);
				log.info("Player sent : "+player.toString());
				
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

			if(ois == null)
				ois = new ObjectInputStream(socket.getInputStream());
			
			int confirmation = (Integer) ois.readObject();
			conf = (confirmation == Protocol.ACCEPT) ? true : false;
			log.info("Confirmation : "+conf);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return conf;
	}
	
	
	// Espera a que el servidor le mande posiciones del objeto
	private void waitServer() {
		try {
			while(true) {
			int x = (Integer) ois.readObject();
			int y = (Integer) ois.readObject();
			log.info("Point received : x : "+x+" y : "+y);
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


	@Override
	public void run() {
		try {
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
