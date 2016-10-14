package my.game.core;

import java.net.*;

import util.serialization.types.*;

public class ConnectionManager {

	private Thread listenerThread;
	private DatagramSocket socket;
	private boolean listening;
	private byte[] buffer = new byte[2048];
	private InetAddress serverIP;
	private String serverAddress;
	private int serverPort;
	private ConnectionState currentState = ConnectionState.NOT_CONNECTED;
	public static final ConnectionManager INSTANCE = new ConnectionManager();	
	
	public enum ConnectionState {
		NOT_CONNECTED,
		CONNECTING,
		CONNECTED,
		ERRORED;
	}
	
	private ConnectionManager() {}
	
	public void connect(String address, int port) {
		try {
			//Checks
			serverAddress = address;
			serverPort = port;
			
			serverIP = InetAddress.getByName(address);
			
			socket = new DatagramSocket();
			
			listening = true;
			listenerThread = new Thread(this::listen, "FlyTrap");
			listenerThread.start();
			
			send(new byte[] {8, 3, 3});
			currentState = ConnectionState.CONNECTING;
		} catch (Exception e) {
			listening = false;
			e.printStackTrace();
			currentState = ConnectionState.ERRORED;
		}
	}

	private void listen() {
		while(listening) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(packet);	
				}catch(Exception e) {
					e.printStackTrace();
				}
				processPacket(packet);
		}
	}
	
	public void send(SEDatabase db) {
		byte[] data = new byte[db.getSize()];
		db.getBytes(data, 0);
		send(data);
	}
	
	public void send(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, serverPort);
		try {
			socket.send(packet);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void processPacket(DatagramPacket packet) {
		byte[] data = packet.getData();
		if(data.length < 3) return;
		if(new String(data, 0, 4).getBytes().equals(SEDatabase.HEADER_META)) {
			//Process data
		} else {
			//Minimal data
			if(data[0] != 8 || data[1] != 3) return;
			switch(data[2]) {
			case 1:
				//Ping
				// TODO Handle Ping
				break;
			case 2:
				//Keep alive
				send(new byte[] {8, 3, 2});
				break;
			case 3:
				//Connection Success
				System.out.println("Successfully connected to "+serverAddress+":"+serverPort);
				currentState = ConnectionState.CONNECTED;
			}
		}
	}
	
	public ConnectionState getConnectionState() {
		return currentState;
	}
	
}
