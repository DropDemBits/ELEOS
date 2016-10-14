package my.game.entity.particle;

import my.game.entity.Entity;
import my.game.render.Screen;
import my.game.render.Sprite;
import my.game.world.Level;

public class EntityParticle extends Entity {

	int life;
	Sprite sprite;
	double xDir, yDir, z0ne;
	double x, y, z;
	int animTime = 0, intervals = 16;
	
	public EntityParticle(Level level, int x, int y, int life, Sprite sprite) {
		super(level);
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.life = life;
		
		xDir = rand.nextGaussian();
		yDir = rand.nextGaussian();
		z0ne = rand.nextFloat() + 2.0;
		intervals += rand.nextInt(32);
	}
	
	@Override
	public void setMotion(double x, double y) {
		xDir = x;
		yDir = y;
	}

	@Override
	public void update() {
		if(animTime >= intervals) animTime = 0;
		z0ne -= 0.1;
		life--;
		animTime++;
		
		if(z < 0) {
			z=0;
			xDir *= 0.4;
			yDir *= 0.4;
			z0ne *= -0.55;
		}
		move(x+xDir, (y+yDir) + (z+z0ne));
		if(life <= 0) remove();
	}
	
	private void move(double x, double y) {
		if(detectCollision(x, y)) {
			xDir *= -0.4;
			yDir *= -0.4;
			z0ne *= -0.4;
		}
		this.x+=xDir;
		this.y+=yDir;
		this.z+=z0ne;
	}
	
	public boolean detectCollision(double x, double y) {
		boolean solid = false;
		for(int c = 0; c < 4; c++) {
			double xT = (x - c % 2 * 16) / 16;
			double yT = (y - c / 2 * 16) / 16;
			int tileX = (int) Math.ceil(xT);
			int tileY = (int) Math.ceil(yT);
			if(c % 2 == 0) tileX = (int) Math.floor(xT);
			if(c / 2 == 0) tileY = (int) Math.floor(yT);
			if(level.getTile(tileX, tileY, 0).isSolid()) solid = true;
		}
		return solid;
	}

	@Override
	public void render(Screen screen) {
		if(sprite != Sprite.fireParticle) {
			screen.renderSprite((int)x, (int)y, sprite, true);
			return;
		}
		if(animTime <= intervals/4){screen.renderSprite((int)x, (int)y - (int) z, Sprite.fireParticle, true);}
		else if(animTime <= intervals/4*2 && animTime > intervals/4){screen.renderSprite((int)x, (int)y - (int) z, Sprite.fireParticle1, true);}
		else if(animTime <= intervals/4*3 && animTime > intervals/4*2){screen.renderSprite((int)x, (int)y - (int) z, Sprite.fireParticle2, true);}
		else if(animTime > intervals/4*3){screen.renderSprite((int)x, (int)y - (int) z, Sprite.fireParticle3, true);}
	}

}
