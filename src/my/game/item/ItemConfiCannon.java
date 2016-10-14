package my.game.item;

import my.game.entity.mob.*;
import my.game.entity.projectile.*;
import my.game.world.*;

public class ItemConfiCannon extends ItemShooter {

	@Override
	public int getFireRate() {
		return 15;
	}
	
	@Override
	public EntityProjectile getProjectile(Level level, int x, int y, double angle, EntityMob owner) {
		return new EntityConfiBall(level, x, y, angle, owner);
	}
	
}
