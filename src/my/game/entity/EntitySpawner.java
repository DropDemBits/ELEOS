package my.game.entity;

import java.util.*;
import java.util.concurrent.*;

import my.game.entity.mob.*;
import my.game.entity.particle.*;
import my.game.render.*;
import my.game.world.*;

public class EntitySpawner extends Entity {

	private List<Entity> entities = new CopyOnWriteArrayList<Entity>();
	
	public enum Type {
		DUMBO, CHASER, PARTICLE, STARBOARD, BLASER;
	}
	
	private Type type;
	private int amount;
	boolean constant;
	
	public EntitySpawner(Level level, int x, int y, Type type, int amount, boolean constant) {
		super(level);
		this.x = x;
		this.y = y;
		this.type = type;
		this.amount = amount;
		this.constant = constant;
		if(!constant) {
			Entity e;
			if (type == Type.PARTICLE) {
				e = new EntityParticle(level, x, y, 300, Sprite.fireParticle);
			} else if (type == Type.CHASER) {
				e = new EntityChase(level, x + (-5 + rand.nextInt(7))*16, y + (-1 + rand.nextInt(7)*16));
			}else if(type == Type.STARBOARD) {
				e = new EntityStarboard(level, x + (-5 + rand.nextInt(7))*16, y + (-1 + rand.nextInt(7)*16));
			}else if(type == Type.BLASER) {
				e = new EntityLaserBlaser(level, x + (-5 + rand.nextInt(7))*16, y + (-1 + rand.nextInt(7)*16));
			}
			else e = new EntityTester(level, x+(-5+rand.nextInt(7)*16), y+(-1+rand.nextInt(7)*16));
			for(int i = 0; i < amount; i++) {
				if(type != Type.PARTICLE) level.spawnEntity(e);
				else if(type == Type.PARTICLE) level.spawnParticle((EntityParticle) e);
			}
		}
	}

	@Override
	public void update() {
		if(constant) {
		if(entities.size() < amount) {
			if(type == Type.PARTICLE) {
				EntityParticle particle = new EntityParticle(level, (int)x, (int)y, 300, Sprite.fireParticle);
				level.spawnParticle(particle);
				entities.add(particle);
			}else if(type == Type.DUMBO) {
				EntityTester t = new EntityTester(level, (int)x+(-5+rand.nextInt(7)*16), (int)y+(-1+rand.nextInt(7)*16));
				level.spawnEntity(t);
				entities.add(t);
			}else if(type == Type.CHASER) {
				EntityChase t = new EntityChase(level, (int)x+(-5+rand.nextInt(7)*16), (int)y+(-1+rand.nextInt(7)*16));
				level.spawnEntity(t);
				entities.add(t);
			}else if(type == Type.STARBOARD) {
				EntityStarboard t = new EntityStarboard(level, (int)x+(-5+rand.nextInt(7)*16), (int)y+(-1+rand.nextInt(7)*16));
				level.spawnEntity(t);
				entities.add(t);
			}else if(type == Type.BLASER) {
				EntityLaserBlaser e = new EntityLaserBlaser(level, (int)x + (-5 + rand.nextInt(7))*16, (int)y + (-1 + rand.nextInt(7)*16));
				level.spawnEntity(e);
				entities.add(e);
			}
		}
		for(Entity e : entities) {
			if (e.isRemoved()) {
				entities.remove(e);
				level.removeEntity(e);
			}
		}
		}
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
	}

}
