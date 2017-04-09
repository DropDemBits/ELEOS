package my.game.entity.mob;

import my.game.core.*;
import my.game.entity.Entity;
import my.game.entity.particle.EntityParticle;
import my.game.entity.projectile.EntityProjectile;
import my.game.render.Screen;
import my.game.render.Sprite;
import my.game.tile.BaseTiles;
import my.game.util.*;
import my.game.world.Level;

public abstract class EntityMob extends Entity {

	public double movementSpeed = 1;
	protected double movementSpeed2 = movementSpeed/2;
	protected double oldMovementSpeed = movementSpeed;
	protected Sprite sprite;
	public double health = 100, maxHealth;
	/**0=N, 1=E, 2=S, 3=W*/
	public int dir = 0;
	protected boolean moving = false;
	public boolean noClip = false;
	public double expGiven = 0;
	public double currentExp = 0;
	public int expLevel = 1;
	protected EntityProjectile pj;
    protected int damageTime;
	public int cycle = 0;
	
	protected EntityMob(Level level, int x, int y) {
		super(level);
		this.x = x;
		this.y = y;
		maxHealth = health;
	}
	
	public void render(Screen screen) {
		super.render(screen);
	}
	
	public void update() {
		cycle++;
		cycle%=60;
        damageTime--;
		if(Math.floor(health) <= 0) {
			if(deathSound() != null) Utilities.playSound(deathSound());
			remove();
		}
		if(level.getTile((int)x/16, (int)y/16, 0) == BaseTiles.lava && rand.nextInt(5) == 0) {
			/*for(int i = 0; i < 5; i++)*/ level.spawnParticle(new EntityParticle(level, (int)x+8, (int)y+8, 120, Sprite.fireParticle), 1, 1);
			damage(new DamageSource(0.5, this));
		}
		if(level.getTile((int)x/16, (int)y/16, 0) == BaseTiles.water) movementSpeed = movementSpeed2;
		else movementSpeed = oldMovementSpeed;
	}
	
	public void move(double xDir, double yDir) {
		if(xDir > 0) dir = 3;
		if(xDir < 0) dir = 1;	
		if(yDir > 0) dir = 2;
		if(yDir < 0) dir = 0;
		if(xDir != 0 || yDir != 0) moving = true;
		box.move((int)xDir, (int)yDir);
		
		/*if(level.getTile((int)(x/16+xDir), (int)(y/16+yDir)) == BaseTiles.air) {
			xDir *= -1;
			yDir *= -1;
		}*/
		if(!noClip) {
			while(xDir != 0) {
				if(Math.abs(xDir) > 1) {
					if(!collision(abs(xDir), yDir)) {
						this.x += abs(xDir);
					}
					xDir-=abs(xDir);
				} else {
					if(!collision(abs(xDir), yDir)) {
						this.x += xDir;
					}
					xDir=0;
				}
			}
			
			while(yDir != 0) {
				if(Math.abs(yDir) > 1) {
					if(!collision(xDir, abs(yDir))) {
						this.y += abs(yDir);
					}
					yDir-=abs(yDir);
				} else {
					if(!collision(xDir, abs(yDir))) {
						this.y += yDir;
					}
					yDir=0;
				}
			}
		}else {
			x += xDir;
			y += yDir;
		}
		box.setLocation((int)x-1, (int)y);
		if(moving && cycle%30 == 0 && walkSound() != null) Utilities.playSound(walkSound());
	}
	
	protected int abs(double value) {
		if(value < 0) return -1;
		return 1;
	}

	protected void shoot(EntityProjectile e) {
		pj = e;
		level.spawnEntity(pj);
	}
	
	private boolean collision(double xDir, double yDir) {
		boolean solid = false;
		for(int c = 0; c < 4; c++) {
			double xT = ((x+xDir) - c % 2 * 16) / 16;
			double yT = ((y+yDir) - c / 2 * 16-1) / 16;
			int xI = (int) Math.ceil(xT);
			int yI = (int) Math.ceil(yT);
			if(c % 2 == 0) xI = (int) Math.ceil(xT);
			if(c / 2 == 0) yI = (int) Math.ceil(yT);
			GameCore.instance().getScreen().drawRect((int)x+xI, (int)y+yI, 1, 1, 0xff00ff, true);
			if(level.getTile(xI, yI, 0).isSolid() || (xT < 0 && yT < 0) || (xT >= level.width && yT >= level.height)) solid = true;
		}
		return solid;
	}
	
	public void setMovementSpeed(double movementSpeed) {
		this.movementSpeed = movementSpeed;
		this.movementSpeed2 = movementSpeed/2;
		this.oldMovementSpeed = movementSpeed;
	}
	
	public Sprite getSprite() {
		return sprite != null ? sprite : Sprite.air;
	}
	
	public String hitSound() {
		return null;
	}
	
	public String walkSound() {
		return null;
	}
	
	public String randomSound() {
		return null;
	}
	
	public String deathSound() {
		return null;
	}

	public void damage(DamageSource d) {
		if(damageTime <= 0 && hitSound() != null)Utilities.playSound(hitSound());
		if(d.dmg < 0) return;
		health -= d.dmg;
        damageTime = 500;
	}
	

}
