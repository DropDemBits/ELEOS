package my.game.core;

import gio.ddb.serial2.SEBase;
import gio.ddb.serial2.SEBlock;
import gio.ddb.serial2.SEType;
import java.net.*;

import my.game.core.GameCore.*;
import my.game.entity.player.*;

public class ConnectionManager {

	private Thread listenerThread;
	private DatagramSocket socket;
	private boolean listening;
	private byte[] buffer = new byte[2048];
	private InetAddress serverIP;
	private String serverAddress;
	private int serverPort;
	private ConnectionState currentState = ConnectionState.NOT_CONNECTED;
	private long nextKeepAlive = 0;
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
	
	public void send(SEBlock block) {
        if(block.getType() != SEType.ROOT_BLOCK.value()) return;
		byte[] data = new byte[block.getSize()];
		block.getSerialized();
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
		if(new String(data, 0, 4).getBytes().equals(SEBase.DBSIG)) {
			//Process data
			SEBlock block = new SEBlock(data);
			handleDB(block, packet);
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
				nextKeepAlive = System.currentTimeMillis()+5000L;
				break;
			case 3:
				//Connection Success
				System.out.println("Successfully connected to "+serverAddress+":"+serverPort);
				send(new byte[] {8, 3, 5});
				currentState = ConnectionState.CONNECTED;
				break;
			case 4:
				//Disconnect
				currentState = ConnectionState.NOT_CONNECTED;
				GameCore.instance().currentState = GameState.TITLE_SCREEN;
				break;
			}
		}
	}
	
	private void handleDB(SEBlock block, DatagramPacket packet) {
		String pName = block.getBlock("object").getString("name");
		if(pName.equals("S1")) {
			if(GameCore.instance().getClientPlayer() instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP)GameCore.instance().getClientPlayer();
				player.confirmMovement(block);
			}else {
				throw new RuntimeException("Player is not MP type even though we are on server");
			}
		}
	}

	public void sendKeepAlive() {
		if(nextKeepAlive+10000L >= System.currentTimeMillis()) {
			send(new byte[] {8, 3, 4});
			currentState = ConnectionState.NOT_CONNECTED;
			GameCore.instance().currentState = GameState.TITLE_SCREEN;
		}
		if(nextKeepAlive >= System.currentTimeMillis()) {
			send(new byte[] {8, 3, 2});
		}
	}
	
	public ConnectionState getConnectionState() {
		return currentState;
	}
	
}
