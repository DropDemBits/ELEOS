package my.game.world;

import my.game.util.Vector2i;

public class Node {

	public Vector2i pos;
	/**
	 * Used for backtracing
	 */
	public Node parent;
	
	/**
	 * <p>fCost: gCost + hCost</p>
	 * <p>gCost: Distance from starting Node</p>
	 * <p>hCost: Distance from ending Node</p>
	 */
	public double fCost, gCost, hCost;
	
	public Node(Vector2i pos, Node parent, double gCost, double hCost) {
		this.pos = pos;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		
		//Calculate fCost
		fCost = this.gCost + this.hCost;
	}
	
	@Override
	public String toString() {
		return "X: " + pos.getX() + " Y: " + pos.getY();
	}

}
