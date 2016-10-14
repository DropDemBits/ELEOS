package my.game.entity.mob;

import java.util.List;
import java.util.Stack;

import my.game.render.AnimatedSprite;
import my.game.render.Screen;
import my.game.render.Sprite;
import my.game.render.SpriteSheet;
import my.game.util.Vector2i;
import my.game.world.Level;
import my.game.world.Node;

public class EntityStarboard extends EntityMob {

	AnimatedSprite current = null;
	
	AnimatedSprite dumbo = new AnimatedSprite(SpriteSheet.chase, 8, 8, 4);
	AnimatedSprite dumbo2 = new AnimatedSprite(SpriteSheet.dumbo, 16, 16, 2);
	int time = 0;
	Stack<Node> path = null;
	
	public EntityStarboard(Level level, int x, int y) {
		super(level, x, y);
		current = dumbo2;
		current.setIntervals(6);
		movementSpeed = 1;
	}

	@Override
	public void update() {
		super.update();
		time++;		
		List<EntityMob> entities = level.getEntitiesInRange(this, 400);
		if(!entities.isEmpty()) {
			if(!noClip) {
				//A Star Search
				int playerX = (int) entities.get(0).x;
				int playerY = (int) entities.get(0).y;

				Vector2i start = new Vector2i(Math.floorDiv((int) x, 16), Math.floorDiv((int) y, 16));
				Vector2i goal = new Vector2i(playerX >> 4, playerY >> 4);
				if (time % 60 == 0) path = level.findPath(start, goal);
				if (path != null) {
					if (path.size() > 0) {
						Vector2i vec = path.pop().pos;

						if (x < vec.getX() * 16) xDir += movementSpeed;
						if (y < vec.getY() * 16) yDir += movementSpeed;
						if (x > vec.getX() * 16) xDir -= movementSpeed;
						if (y > vec.getY() * 16) yDir -= movementSpeed;
	
					}
				}else {
					//Obey laws and chase 
					xDir = (entities.get(0).x-x>0 ? movementSpeed : entities.get(0).x-x<0 ? -movementSpeed : 0);
					yDir = (entities.get(0).y-y>0 ? movementSpeed : entities.get(0).y-y<0 ? -movementSpeed : 0);
				}
			}else{
				//Phase Through Everything & Chase an entity
				xDir = (entities.get(0).x-x>0 ? movementSpeed : entities.get(0).x-x<0 ? -movementSpeed : 0);
				yDir = (entities.get(0).y-y>0 ? movementSpeed : entities.get(0).y-y<0 ? -movementSpeed : 0);
			}
		}else{
				if(time % 30 == 0) {
				xDir = 1-rand.nextInt(3);
				yDir = 1-rand.nextInt(3);
				if(rand.nextInt(8) == 0) {
					xDir = 0;
					yDir = 0;
				}
			}
		}
		
		move(xDir, yDir);
		xDir = yDir = 0;
		
		current.update();
		sprite = current.getSprite();
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		if(path != null)
			for(Node n : path) {
				screen.renderSprite(n.pos.getX()*16, n.pos.getY()*16, new Sprite(16, 16, 0xFF0000), true);
			}
		screen.renderMob((int)x, (int)y, this);
		moving = false;
	}

}
