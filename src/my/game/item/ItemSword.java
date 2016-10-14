package my.game.item;

public class ItemSword extends Item {

	private double dmg = 0;
	private boolean repeat = false;
	
	public ItemSword setDamage(double damage) {
		dmg = damage;
		return this;
	}
	
	public double getDamage() {
		return dmg;
	}
	
	public ItemSword setRepeat() {
		repeat = true;
		return this;
	}
	
	public boolean doesRepeat() {
		return repeat;
	}

}
