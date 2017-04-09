package my.game.entity.mob;

import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import my.game.render.AnimatedSprite;
import my.game.render.Screen;
import my.game.render.Sprite;
import my.game.render.SpriteSheet;
import my.game.util.Vector2i;
import my.game.world.Level;
import my.game.world.Node;

public class EntityStarboard extends EntityMob {

	private AnimatedSprite current = null;
	
	private AnimatedSprite dumbo = new AnimatedSprite(SpriteSheet.chase, 8, 8, 4);
	private AnimatedSprite dumbo2 = new AnimatedSprite(SpriteSheet.dumbo, 16, 16, 2);
	private int time = 0;
    private final Object vectorKey = new Object();
    private final Thread aStarThread;
    private Vector2i start, goal;
    protected volatile boolean timedOut;
    private Stack<Node> path = null;
	
	public EntityStarboard(Level level, int x, int y) {
		super(level, x, y);
		current = dumbo2;
		current.setIntervals(6);
		movementSpeed = 100;
        aStarThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean shouldCalc = true;
                while(shouldCalc) {
                    try {
                        Vector2i oldStart = getStartVec(), oldGoal = getGoalVec();
                        if(getStartVec() == null || getGoalVec() == null) Thread.sleep(200);
                        if(path == null || (oldStart != getStartVec() && oldGoal != getGoalVec())) {
                            path = level.findPath(getStartVec(), getGoalVec());
                            //If fail, then sleep
                            if(path == null) {
                                timedOut = true;
                                Thread.sleep(5000);
                                timedOut = false;
                            }
                        }
                        else Thread.sleep(500);
                    } catch (Exception ex) {
                        Logger.getLogger(EntityStarboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
            }
        }, "A*Calc");
	}

	@Override
	public void update() {
		super.update();
		time++;		
		List<EntityMob> entities = level.getEntitiesInRange(this, 400);
		boolean movingX = false, movingY = false;
        
        if (path != null) {
            setVecs(null, null);
			if (path.size() > 0) {
                for(int i = 0; i < 2 && !path.isEmpty(); i++) {
                    Vector2i vec = path.pop().pos;
                    if (x < vec.getX() * 16) {
                        xDir += movementSpeed / 4;
                        movingX= true;
                    }
                    if (y < vec.getY() * 16) {
                        yDir += movementSpeed / 4;
                        movingY= true;
                    }
                    if (x > vec.getX() * 16) {
                        xDir += -movementSpeed / 4;
                        movingX= true;
                    }
                    if (y > vec.getY() * 16) {
                        yDir += -movementSpeed / 4;
                        movingY= true;
                    }
                }
			} else path = null;
		} else if(!entities.isEmpty() && !timedOut) {
			if(!noClip) {
				//A Star Search
				int playerX = (int) entities.get(0).x;
				int playerY = (int) entities.get(0).y;

				Vector2i start = new Vector2i(Math.floorDiv((int) x, 16), Math.floorDiv((int) y, 16));
				Vector2i goal = new Vector2i(playerX >> 4, playerY >> 4);
				
                if(path == null) {
                    setVecs(start, goal);
					if(aStarThread.getState() == Thread.State.TERMINATED || aStarThread.getState() == Thread.State.NEW) {
                        aStarThread.start();
                    }
				}
			}else{
				//Phase Through Everything & Chase an entity
				xDir = (entities.get(0).x-x>0 ? movementSpeed : entities.get(0).x-x<0 ? -movementSpeed : 0);
				yDir = (entities.get(0).y-y>0 ? movementSpeed : entities.get(0).y-y<0 ? -movementSpeed : 0);
			}
		} else {
			if(time % 30 == 0 && timedOut) {
                xDir = (1-rand.nextInt(3)) * movementSpeed;
                yDir = (1-rand.nextInt(3)) * movementSpeed;
                if(rand.nextInt(8) == 0) {
                    xDir = 0;
                    yDir = 0;
                }
            }
		}
		
		move(xDir, yDir);
		if(!timedOut && time % 20 == 0) {
            if(xDir > 0 && !movingX) xDir += -movementSpeed / 8d;
            if(xDir < 0 && !movingX) xDir += movementSpeed / 8d;
            
            if(yDir > 0 && !movingY) yDir += -movementSpeed / 8d;
            if(yDir < 0 && !movingY) yDir += movementSpeed / 8d;
        }
		
		current.update();
		sprite = current.getSprite();
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.renderMob((int)x, (int)y, this);
		moving = false;
	}
    
    private void setVecs(Vector2i start, Vector2i goal) {
        synchronized(vectorKey) {
            this.start = start;
            this.goal = goal;
        }
    }
    
    private synchronized Vector2i getStartVec() {
        return start;
    }
    
    private synchronized Vector2i getGoalVec() {
        return goal;
    }

}
