package my.game.util;

import my.game.entity.*;

public class DamageSource {

	public double dmg;
	public Entity source;
	
	public DamageSource(double dmg, Entity source) {
		this.dmg = dmg;
		this.source = source;
	}
	
}
