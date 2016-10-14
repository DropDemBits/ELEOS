package my.game.item;

import my.game.entity.mob.*;
import my.game.entity.projectile.*;
import my.game.render.*;
import my.game.world.*;

public class ItemShooter extends Item {

	protected int fireRate;
	public String useSound;
	private Sprite projectileSprite;
	
	public ItemShooter() {
		stackSize = 1;
	}
	
	public int getFireRate() {
		return fireRate;
	}

	public ItemShooter setFireRate(int rate) {
		fireRate = rate;
		return this;
	}
	
	public ItemShooter setUseSound(String soundFile) {
		useSound = soundFile;
		return this;
	}
	
	public ItemShooter setProjectileSprite(Sprite sprite) {
		projectileSprite = sprite;
		return this;
	}
	
	private Sprite getProjectileSprite() {
		return projectileSprite;
	}
	
	public EntityProjectile getProjectile(Level level, int x, int y, double angle, EntityMob owner) {
		return new EntityFireballSmall(level, x, y, angle, owner, getProjectileSprite());
	}

	
}
