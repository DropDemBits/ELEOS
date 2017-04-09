package my.game.world;

import java.util.*;
import java.util.concurrent.*;

import my.game.core.*;
import my.game.entity.*;
import my.game.entity.mob.*;
import my.game.entity.particle.*;
import my.game.entity.player.*;
import my.game.render.*;
import my.game.tile.*;
import my.game.util.*;

public class Level {

	public Random rand = new Random();
	public int width, height;
	public int[][] tiles;
	protected Tile[][] tileList;
	protected int[] lightmap;
	protected int[] currTileIDs;
	public int layers = 2;
	protected float time;
	public int brightness;
	private boolean day, night;
	boolean gameOver = false;
	
	public static Level spawn = new CentralLevel("/levels/spawnLevel");
	public static Level collide = new CentralLevel("/levels/collisionTest");
	public static Level level0 = new LoadedLevel("/levels/level0.png");
	
	public List<Entity> entities = new CopyOnWriteArrayList<Entity>();
	public List<EntityMob> players = new CopyOnWriteArrayList<EntityMob>();
	public List<TileEntity> tileEntities = new CopyOnWriteArrayList<TileEntity>();
	
	/**
	 * TODO Optimize & remove (make particles not entities)
	 */
	public List<EntityParticle> particles = new CopyOnWriteArrayList<EntityParticle>();
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new int[layers][width * height];
		lightmap = new int[width * height];
		generateLevel();
	}

	public Level(String path) {
		loadLevel(path);
		generateLevel();
	}

	public void spawnEntity(Entity e) {
		entities.add(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	public void addTileEntity(TileEntity e) {
		tileEntities.add(e);
	}
	
	public void removeTileEntity(TileEntity e) {
		tileEntities.remove(e);
	}
	
	/*
	 * Respawning etc.
	 */
	public void spawnPlayer(EntityMob player/*Inventory inv, boolean forcedRespawn*/) {
		players.add(player);
	}
	
	public void spawnParticle(EntityParticle e, double xDir, double yDir) {
		e.setMotion(xDir, yDir);
		particles.add(e);
	}
	
	public void spawnParticle(EntityParticle e) {
		particles.add(e);
	}
	
	protected void loadLevel(String path) {}
	
	protected void loadLevelFromSave(String file) {}

	protected void generateLevel() {}
	
	public void update() {
		for(Entity entity : entities) {
			entity.update();
			if(entity.isRemoved()) removeEntity(entity);
		}
		for(EntityParticle particle : particles) {
			particle.update();
			if(particle.isRemoved()) particles.remove(particle);
		}
		for(EntityMob player : players) {
			player.update();
			if(player.isRemoved()) players.remove(player);
			if(players.size() <= 0) gameOver = true;
			else gameOver = false;
		}
		for(TileEntity te : tileEntities) {
			te.update();
		}
		
		time++;
		if(time % 1 == 0) time();
		if(time >= 1000000) time = 0;
	}
	
	protected void time() {
		//0 - 839 Day (hrs 0 - 13)
		//840 - 999 Sunset (hr 14)
		//1000 - 1379 Night
		//1380 - 0 Sunrise
		
		//420 Noon
		/*if(time > 1440f) {
			time = 0f;
		}*/
		
		if (brightness < -100) {
			night = true;
			day = false;
		}

		if (brightness > 100) {
			day = true;
			night = false;
		}

		if (night) {
			brightness++;
			return;
		}

		if (day) {
			brightness--;
			return;
		}

		brightness++;
	}
	
	public double getAngle() {
		return Math.sin(time)/Math.cos(time);
	}
	
	int xScroll, yScroll;
	
	public void setScroll(int xScroll, int yScroll) {
		this.xScroll = xScroll;
		this.yScroll = yScroll;
	}
	
	public void render(Screen screen) {
		screen.setOffset(xScroll, yScroll);
		int xInner = xScroll >> 4; //Player xPos
		int yInner = yScroll >> 4; //Player yPos
		int xOuter = (xScroll + screen.width+16) >> 4;
		int yOuter = (yScroll + screen.height+16) >> 4;
		
		for(int y = yInner; y < yOuter; y++) {
			for(int x = xInner; x < xOuter; x++) {
				for(int layer = 0; layer < layers; layer++) {
					//if(x+y*16 < 0 || x+y*16 >= 256) { 
					if(x < 0 || y < 0 || x >= width || y >= height) {
						BaseTiles.air.render(x, y, screen);
						continue;
					}
					//tiles[x+y*16].render(x, y, screen);
					if(layer == 0) getTile(x,y,0).render(x, y, screen);
					else if(getTile(x,y,layer) != BaseTiles.air && !getTile(x,y,layer).isInFront()) getTile(x,y,layer).render(x, y, screen);
				}
			}
		}
		for(Entity entity : entities) {
			entity.render(screen);
		}
		for(EntityParticle particle : particles) {
			particle.render(screen);
		}
		for(EntityMob player : players) {
			player.render(screen);
		}
		
		for(int y = yInner; y < yOuter; y++) {
			for(int x = xInner; x < xOuter; x++) {
				//if(x+y*16 < 0 || x+y*16 >= 256) { 
				if(getTile(x,y,1) != BaseTiles.air && getTile(x,y,1).isInFront())
					getTile(x,y,1).render(x, y, screen);
				//}
			}
		}
		
		if(gameOver) {
			Font font = new Font();
			font.drawString(screen, GameCore.getWindowWidth()/3/2+1, GameCore.getWindowHeight()/3/2+1, "GAME OVER", 0xFFAA0000, false);
			font.drawString(screen, GameCore.getWindowWidth()/3/2, GameCore.getWindowHeight()/3/2, "GAME OVER", 0xFFFF0000, false);
		}
	}
	
	/**
	 * Gets the tile at the specified coordinates
	 * 
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @return Returns the tile at the specified position (or air if this method is not overridden or is out of bounds)
	 */
	public Tile getTile(int x, int y, int layer) {
		if(x < 0 || y < 0 || x >= width || y >= height) return BaseTiles.air;
		return tileList[layer][x+y*width] != null ? tileList[layer][x+y*width] : BaseTiles.air;
	}
	
	/**
	 * Set a tile at the specified position
	 * <P>Will remove or place a tile entity if one currently does or doesn't exist at the position
	 * 
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @param tile The type of tile to place
	 */
	public boolean setTile(int x, int y, int layer, Tile tile) {
		if(x < 0 || y < 0 || x >= width || y >= height || tile == null) return false;
		if(getTile(x, y, 0).hasTileEntity() || getTile(x, y, 1).hasTileEntity())
			removeTileEntity(x, y);
		if(tile.hasTileEntity())
			addTileEntity( tile.getTileEntity().setPos(x, y, layer));
		tileList[layer][x+y*width] = tile;
		return true;
	}
	
	/**
	 * Set a tile at the specified position
	 * <P>Will remove or place a tile entity if one currently does or doesn't exist at the position
	 * 
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @param tile The type of tile to place
	 *//*
	public boolean setTile(int x, int y, Tile tile) {
		if(x < 0 || y < 0 || x >= width || y >= height || tile == null) return false;
		if(getTile(x, y, 0) instanceof TileContainer || getTile(x, y, 1) instanceof TileContainer)
			removeTileEntity(x, y);
		if(tile instanceof TileContainer)
			addTileEntity(((TileContainer) tile).getTileEntity().setPos(x, y, tile.isOverlay() ? 1 : 0));
		tileList[tile.isOverlay() ? 1 : 0][x+y*width] = tile;
		return true;
	}*/
	
	private void removeTileEntity(int x, int y) {
		if(getTileEntity(x, y) != null) {
			TileEntity te = getTileEntity(x, y);
			te.onRemoved();
			tileEntities.remove(te);
		}
	}
	
	public int getLightLevelAt(int x, int y) {
		if(x+y*width < 0 || x+y*width >= lightmap.length) return 0;
		return lightmap[x+y*width];
	}

	/**
	 * Get a certain player from a certain index
	 * <P> Even: Client player
	 * <P> Odd: Server player
	 * @param index The index of the list
	 * @return Returns 
	 */
	public EntityPlayer getPlayersAt(int index) {
		return (EntityPlayer) players.get(index);
	}
	
	
	public boolean detectCollision(int x, int y, int xOff, int yOff, int width, int height) {
		boolean solid = false;
		for(int c = 0; c < 4; c++) {
			int xT = (x - c % 2 * width + xOff) >> 4;
			int yT = (y - c / 2 * height + yOff) >> 4;
			if(getTile(xT, yT, 0).isSolid() || (xT < 0 && yT < 0) || (xT >= this.width && yT >= this.height)) solid = true;
		}
		return solid;
	}
	
	
	public boolean detectCollision(int x, int y, int xOff, int yOff, int size) {
		return detectCollision(x, y ,xOff, yOff, size, size);
	}
	
	/**
	 * Gets every entity within a certain radius from a certain entity
	 * 
	 * @param from The entity where the area is centered
	 * @param radius The radius of the area where the entities are found
	 * @return A list of entities found within the area
	 */
	public List<EntityMob> getEntitiesInRange(Entity from, int radius) {
		List<EntityMob> result = new ArrayList<EntityMob>();
		for(EntityMob e : players) {
			if(!(e.x < xScroll >> 4 || e.x > (xScroll + GameCore.getWindowWidth()/3+16) >> 4 ||
					(e.y < yScroll >> 4 || e.y > (yScroll + GameCore.getWindowHeight()/3+16) >> 4))) continue;
			if(getDistance(from, e) <= radius)
				result.add(e);
		}
		for(Entity en : entities) {
			if(!(en instanceof EntityMob)) continue;
			if(!(en.x < xScroll >> 4 || en.x > (xScroll + GameCore.getWindowWidth()/3+16) >> 4 ||
					(en.y < yScroll >> 4 || en.y > (yScroll + GameCore.getWindowHeight()/3+16) >> 4))) continue;
			EntityMob e = (EntityMob) en;
			if(getDistance(from, e) <= radius)
				result.add(e);
		}
		return result;
	}
	
	/**
	 * Calculates the distance between two entities
	 * 
	 * @param from 
	 * @param to 
	 * @return The distance between the two entities
	 */
	public double getDistance(Entity from, Entity to) {
		double x = (double)to.x;
		double y = (double)to.y;
		double dX = Math.abs((double)from.x-x)*Math.abs((double)from.x-x);
		double dY = Math.abs((double)from.y-y)*Math.abs((double)from.y-y);
		double distance = Math.sqrt(dX+dY);
		return distance;
	}
	
	private Comparator<Node> nSort = new Comparator<Node>() {
		public int compare(Node node0, Node node1) {
			if(node1.fCost < node0.fCost) return +1;
			if(node1.fCost > node0.fCost) return -1;
			return 0;
		}
	};
	
	/**
	 * Finds a path between two vectors using the A* algorithm
	 * 
	 * @param start The starting vector position
	 * @param goal The goal vector position
	 * @return A stack of nodes that represents the path between the two nodes
	 */
	public Stack<Node> findPath(Vector2i start, Vector2i goal) {
		List<Node> openList = new ArrayList<>();
		Set<Node> closedList = new HashSet<>();
		Node current = new Node(start, null, 0, getDistance(start, goal));
		openList.add(current);
        int ops = 0;
		while(openList.size() > 0) {
            if(ops > 5000) break;
			Collections.sort(openList, nSort);
            current = openList.get(0);
			if(current.pos.equals(goal)) {
				Stack<Node> path = new Stack<>();
				while(current.parent != null) {
					path.add(current.parent);
					current = current.parent;
				}
				//openList.clear();
				//closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			
			for(int i = 0; i < 9; i++) {
				if(i == 4) continue;
				int x = current.pos.getX();
				int y = current.pos.getY();
				
				int xAd = (i % 3)-1;
				int yAd = (i / 3)-1;
				
				Tile at = getTile(x+xAd, y+yAd, 0);
				
				if(at == BaseTiles.air || at.isSolid()) continue;
				Vector2i tilePos = new Vector2i(x+xAd, y+yAd);
				double gCost = current.gCost + (getDistance(current.pos, tilePos) == 1 ? 1 : 1);
				double hCost = current.hCost + getDistance(tilePos, goal);
				
				Node node = new Node(tilePos, current, gCost, hCost);
				
				if(vecInSet(closedList, tilePos) && gCost >= node.gCost) continue;
				if(!vecInList(openList, tilePos) || gCost < node.gCost) openList.add(node);
			}
            ops++;
            System.out.println(ops);
		}
		//closedList.clear();
		//closedList.trimToSize();
		return null;
	}
	
	private boolean vecInList(List<Node> list, Vector2i vector) {
		for(Node n : list) {
			if(n.pos == vector) return true;
		}
		return false;
	}
    
    private boolean vecInSet(Set<Node> list, Vector2i vector) {
		for(Node n : list) {
			if(n.pos == vector) return true;
		}
		return false;
	}
	
	/**
	 * Finds the distance between two vectors
	 * 
	 * @param currentPos The vector for the starting position
	 * @param goal The vector for the end goal
	 * @return The distance between the two vectors
	 */
	public double getDistance(Vector2i currentPos, Vector2i goal) {
		double dx = Math.abs(goal.getX() - currentPos.getX());
		double dy = Math.abs(goal.getY() - currentPos.getY());
		double distance = (dx + dy) + (1.414 - 2 * 1) *  Math.min(dx, dy);
		return distance;
	}

	/**
	 * The current time of the world
	 * @return Returns the current time of the world in ticks (60 ticks = 1 second)
	 */
	public float getTime() {
		return time;
	}

	/**
	 * Gets a tile entity at the specified position
	 * 
	 * @param x The positive x position of the tile entity
	 * @param y The positive y position of the tile entity
	 * @return Returns the tile entity found, or returns null if no entity is found
	 */
	public TileEntity getTileEntity(int x, int y) {
		return getTileEntity(x, y, null);
	}
	
	/**
	 * Gets a tile entity at the specified position
	 * 
	 * @param x The positive x position of the tile entity
	 * @param y The positive y position of the tile entity
	 * @param comparative A class used to find a specific type of tile entity
	 * @return Returns the tile entity found, or returns null if no entity is found
	 */
	public TileEntity getTileEntity(int x, int y, Class<? extends TileEntity> comparative) {
		TileEntity res = null;
		if(comparative == null) {
			comparative = TileEntity.class;
		}
		for(TileEntity te : tileEntities) {
			if(!te.getClass().equals(comparative)) continue;
			if(te.xPos == x && te.yPos == y) res = te;
		}
		return res;
	}

	/**
	 * Finds every entity within a collision box
	 * 
	 * @param box The collision box used to search for entities
	 * @param exception The entity to exclude from the returned list (Can be null)
	 * @return A list of entities found within the box
	 */
	public List<Entity> getEntitiesInBox(CollisionBox box, Entity exception) {
		List<Entity> res = new ArrayList<Entity>();
		for(Entity e : entities) {
			if(e.getBox().intersectBoth(box) && e != exception) res.add(e);
		}
		
		for(Entity e : players) {
			if(e.getBox().intersectBoth(box) && e != exception) res.add(e);
		}
		
		return res;
	}
	
}
