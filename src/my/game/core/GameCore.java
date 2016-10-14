package my.game.core;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

import my.game.core.ConnectionManager.*;
import my.game.entity.*;
import my.game.entity.particle.*;
import my.game.entity.player.*;
import my.game.events.*;
import my.game.gui.*;
import my.game.item.*;
import my.game.network.*;
import my.game.render.*;
import my.game.render.ui.UIManager;
import my.game.world.*;

//We want to paint on it...
public class GameCore extends Canvas implements Runnable {

	private static final long serialVersionUID = -1L;
	
	/**Window Dimensions */
	private static int width = 300 - 80, height = 168, scale = 3;
	/**Window Object*/
	private JFrame window;
	private String version = "1.0b1";
	public static boolean DEV = false;
	
	//Threads (Include others later)
	private Thread clientThread;
	private static GameCore instance;
	private double updateCap = 60.0;
	private int fpsCap = 0;
	private int ups;
	private int fps;
	public static Random rand;
	
	//Rendering
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] imageData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private Screen screen;
	private Level currentLevel;
	
	private EntityPlayer thePlayer;
	private Entity cameraPos;
	boolean init = true;
	private static UIManager uiMgr;
	private static GuiManager guiMgr;
	
	private KeyHandler keys = new KeyHandler();
	public static final EventBusser CORE_BUSSER = new EventBusser();
	public GameState currentState;
	private Gui activeMenuGui;
	
	//Connection Related
	private ConnectionManager connectionMgr;
	
	/**
	 * Says if the game is running for a smooth exit free of bugs
	 */
	private boolean running = false;
	
	//Constructor (No destructors in Java)
	public GameCore(String[] args) throws Exception {
		//Do pre-runtime things here
		instance = this;
		
		UpdateChecker.checkForUpdates(version);
		
		screen = new Screen(width, height);
		
		window = new JFrame("ELEOS, a 100%* java game");
		Dimension size = new Dimension(width*scale+225, height*scale);
		window.setPreferredSize(size);
		
		window.setResizable(false);
		window.add(instance);
		window.pack();
		
		uiMgr = new UIManager();
		guiMgr = new GuiManager();
		currentLevel = Level.spawn;
		
		Item.registerAllItems();
		
		TilePos pos = new TilePos(60, 60);
		thePlayer = new EntityPlayer(currentLevel, args[0], pos.getX(), pos.getY(),keys);
		cameraPos = thePlayer;
		
		rand = new Random();
		MouseHandler mouse = new MouseHandler();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		window.addKeyListener(keys);
		
		activeMenuGui = new GuiMenuMain();
		currentState = GameState.TITLE_SCREEN;
		
		connectionMgr = ConnectionManager.INSTANCE;
	}
	
	public static int getWindowWidth() {
		return width*scale;
	}
	
	public static int getWindowHeight() {
		return height*scale;
	}
	
	public static UIManager getUiManager() {
		return uiMgr;
	}
	
	//Don't desynchronize kids, it's bad for you
	public synchronized void startGame() {
		//We need to run the game, it's pointless to not set this to true (unless we want to do something extremely special)
		running = true;
		clientThread = new Thread(this, "Client Side");
		clientThread.start();
	}
	
	//In a browser, stop the applet so it doesn't do bad stuff (like continue running the game/continue to consume resources) 
	public synchronized void close() {
		running = false;
		try {
			clientThread.join();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static GameCore instance() {
		return instance;
	}
		
	@Override
	public void run() {
		//If we want to init things, then do it here
		
		//Timer Variables
		long lastTime = System.nanoTime();
		long frameTimer = System.currentTimeMillis();
		final double nanoS = 1000000000.0 / updateCap;
		double delta = 0.0;
		
		//Loop the game because nothing happens in one cycle
		while(running) {
			long now = System.nanoTime();
			delta += (now-lastTime) / nanoS;
			lastTime = now;
			
			//Update Logic in 60 cycles per second (We're not Illogical)
			while(delta >= 1) {
				update();
				ups++;
				delta--;
			}
			
			//Render at will
			if(fpsCap > 0) {
				if (fps <= fpsCap) {
					render();
					fps++;
				}
			}
			else {
				render();
				fps++;
			}
			
			//Update Frame Counter
			if(System.currentTimeMillis() - frameTimer > 1000) {
				frameTimer+=1000;
				window.setTitle("ELEOS, a 100%* java game | FPS: "+fps+" UPS: "+ups);
				fps = 0;
				ups = 0;
				if(window.isActive())window.requestFocus();
			}
			
			//Stop the loop once a condition has been met...
		}
	}
	
	private void update() {
		/*
		 * if(currentState == GameState.MULTIPLAYER_SELECTION && activeMenuGui instanceof GuiMenuMain) {
			
		}
		 */
		if(currentState == GameState.TITLE_SCREEN) {
			activeMenuGui.setActive();
			activeMenuGui.update();
		}else if(currentState == GameState.MULTIPLAYER_SELECTION && DEV) {
			if(!init) return;
			if(connectionMgr.getConnectionState() == ConnectionState.NOT_CONNECTED) {
				String serverIp = "localhost";
				int serverPort = 8080;
				connectionMgr.connect(serverIp, serverPort);
				if(connectionMgr.getConnectionState() == ConnectionState.ERRORED) {
					currentState = GameState.TITLE_SCREEN;
					return;
				}
			}else if(connectionMgr.getConnectionState() == ConnectionState.CONNECTED) {
				try {
					currentLevel.removeEntity(thePlayer);
					thePlayer = new EntityPlayerMP(thePlayer);
					cameraPos = thePlayer;
					init = true;
					currentState = GameState.GAMEPLAY;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(currentState == GameState.GAMEPLAY) {
			if(init) {
				currentLevel.spawnPlayer(thePlayer);
				currentLevel.spawnPlayer(new NetPlayer(currentLevel, 60*16, 60*16));
				init = false;
			}
			activeMenuGui.setInactive();
			keys.update();
			if (keys.killAll) {
				for (Entity e : currentLevel.entities) {
					e.remove();
				}
				for (EntityParticle e : currentLevel.particles) {
					e.remove();
				}
				guiMgr.closeAllGuis();
			}
			currentLevel.update();
			uiMgr.update();
			guiMgr.update();
		}
	}
	
	private void render() {
		//Make our buffer Strategy
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			//Triple buffered
			createBufferStrategy(3); 
			//Do rendering the next cycle as the buffer has just been created
			return;
		}
		
		if(currentState == GameState.GAMEPLAY) {
			//Our glClear(int);
			screen.clear();
			int xScroll = (int)cameraPos.x - screen.width/2, yScroll = (int)cameraPos.y - screen.height/2;
			//Render Our things
			currentLevel.setScroll(xScroll, yScroll);
			if(!cameraPos.isRemoved())
				currentLevel.render(screen);
			else
				currentLevel.render(screen);
			
			//int blendFactor = ((currentLevel.getTime()/60)&0xff) < 0x7f ? (currentLevel.getTime()/60)&0xff : (-(currentLevel.getTime()/60)&0xff);
			
			for(int i = 0; i < imageData.length; i++) {
				//imageData[i] = Screen.blend(screen.pixels[i], 0, blendFactor);
				imageData[i] = screen.pixels[i];
			}
		}
		
		//Display our things
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.blue);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if(currentState == GameState.TITLE_SCREEN) {
			activeMenuGui.render(g);
		}else if(currentState == GameState.MULTIPLAYER_SELECTION) {
			
		}
		else if(currentState == GameState.GAMEPLAY) {
			g.drawImage(image, 0, 0, width * scale, height * scale, null);
			guiMgr.render(g);
			uiMgr.render(g);
		}
		
		g.dispose();
		
		//Show the buffer
		bs.show();
		//
	}
	
	public EntityPlayer getClientPlayer() {
		return thePlayer;
	}

	public Screen getScreen() {
		return screen;
	}
	
	public Level getCurrentLevel() {
		return currentLevel;
	}
	
	public static void main(String[] args) throws Exception {
		String uname = "";
		if(args.length > 0) {
			if(args[0].split("=")[0].contains("--") && args[0].contains("--usrName")) {
				//Show login
				uname = args[0].split("=")[1];
			}else {
				GameLogin login = new GameLogin();
				uname = login.getVars()[0];
			}
		}else {
			GameLogin login = new GameLogin();
			uname = login.getVars()[0];
		}
		GameCore game = new GameCore(args.length > 0 ? args : new String[] {uname});
		
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.window.setIconImage(ImageIO.read(GameCore.class.getResourceAsStream("/icon16.png")));
		game.window.setLocationRelativeTo(null);
		game.window.setVisible(true);
		
		game.startGame();
	}

	public GuiManager getGuiMgr() {
		return guiMgr;
	}
	
	public static enum GameState {
		TITLE_SCREEN,
		MULTIPLAYER_SELECTION,
		GAMEPLAY,
		;
	}

}
