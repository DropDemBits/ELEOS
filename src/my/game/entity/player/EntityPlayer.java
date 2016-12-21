package my.game.entity.player;

import java.awt.Font;
import java.awt.event.*;
import java.awt.image.*;
import java.util.List;

import my.game.core.*;
import my.game.core.GameCore.*;
import my.game.entity.*;
import my.game.entity.mob.*;
import my.game.entity.particle.EntityParticle;
import my.game.entity.projectile.*;
import my.game.events.*;
import my.game.events.MouseEvent;
import my.game.inventory.*;
import my.game.item.*;
import my.game.render.*;
import my.game.render.ui.*;
import my.game.tile.*;
import my.game.util.*;
import my.game.world.*;

public class EntityPlayer extends EntityMob {
	
	KeyHandler input;
	private int animTime = 0;
	int intervals = 6;
	private int fireRate, maxRate;
	AnimatedSprite up = new AnimatedSprite(SpriteSheet.player2Sheet_N, 16, 16, 3);
	AnimatedSprite down = new AnimatedSprite(SpriteSheet.player2Sheet_S, 16, 16, 3);
	AnimatedSprite left = new AnimatedSprite(SpriteSheet.player2Sheet_W, 16, 16, 3);
	AnimatedSprite right = new AnimatedSprite(SpriteSheet.player2Sheet_E, 16, 16, 3);
	UIManager uiMgr;
	UIProgressBar healthMeter = new UIProgressBar(new Vector2i(10, 224), new Vector2i(200, 25));
	UIProgressBar expMeter = new UIProgressBar(new Vector2i(10, 259), new Vector2i(200, 25));
	UILabel expLabel;
	UIInventory uiInv;
	UIButton exampleButton;
	BufferedImage image, imageHover;
	
	String name;
	private boolean shooting = false;
	private AnimatedSprite current = null;
	int selectionX, selectionY;
	
	public Inventory playerInventory;
	public ItemStack currentItem;
	public int currentSlot = 0;
	
	public EntityPlayer(Level currentLevel, String name, int x, int y, KeyHandler input, Inventory inv) {
		super(currentLevel, x, y);
		this.input = input;
		current = down;
		current.setIntervals(intervals);
		setMovementSpeed(1.5);
		if(playerInventory == null) {
			playerInventory = new Inventory(16);
		}
		GameCore.CORE_BUSSER.registerListener(this);
		this.name = name != null ? trimToSize(name, 32) : "The unnamed one...";
		
		UIPanel pan = (UIPanel) new UIPanel(new Vector2i(220*3, 0), new Vector2i(80*3, GameCore.getWindowHeight()));
		pan.setColor(0x363636);
		
		UILabel nameLabel = new UILabel(new Vector2i(40, 216), this.name);
		nameLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, this.name.length() > 19 ? 10 : 16));
		nameLabel.dropShadow = true;
		nameLabel.drop = 3;
		
		
		UILabel hpLabel = new UILabel(new Vector2i(healthMeter.pos).add(new Vector2i(2,19)), "HP");
		hpLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
		
		expLabel = new UILabel(new Vector2i(expMeter.pos).add(new Vector2i(2,19)), "Lvl ");
		expLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
		
		exampleButton = new UIButton(new Vector2i(10, 259+35), new Vector2i(200, 25));
		//exampleButton.setColor(0xfa0bf6);
		exampleButton.setText(new UILabel(new Vector2i(0,20), "Mute").setFont(new Font("Sans", 0, 20)));
		exampleButton.setActionListener(new UIEventListener() {
			@Override
			public void preformAction(UIButton button) {
				Utilities.muted = !Utilities.muted;
				if(Utilities.muted) button.setText(new UILabel(new Vector2i(0,20), "Unmute").setFont(new Font("Sans", 0, 20)));
				else button.setText(new UILabel(new Vector2i(0,20), "Mute").setFont(new Font("Sans", 0, 20)));
			}
		});
		
		healthMeter.foregroundClr = (0xFF0000);
		expMeter.foregroundClr = (0x00A300);
		expMeter.countdown = false;
		
		image = GameCore.instance().getTextureManager().get(new Resource("eleos", "/textures/homeIcon.png"));//ImageIO.read(EntityPlayer.class.getResourceAsStream(""));
		
		byte[] srcPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		imageHover = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		byte[] destPixels = ((DataBufferByte)imageHover.getRaster().getDataBuffer()).getData();
		
		for(int i = 0; i < srcPixels.length; i++) {
			if(srcPixels[i] == 0) continue;
			destPixels[i] = (byte) ColorUtil.blend(srcPixels[i], 0x00FFFFFF, 255);
		}
		
		UIButton spawnButtonIco = new UIButton(new Vector2i(200, 220-16), null);
		spawnButtonIco.setIcon(image);
		spawnButtonIco.setButtonListener(new UIButtonListener() {
			@Override
			public void mouseEntered(UIButton button) {
				button.setIcon(imageHover);
			}
			
			@Override
			public void mouseExited(UIButton button) {
				button.setIcon(image);
			}
		});
		spawnButtonIco.setActionListener(new UIEventListener() {
			@Override
			public void preformAction(UIButton button) {
				GameCore.instance().currentState = GameState.TITLE_SCREEN;
			}
		});
		
		uiInv = new UIInventory(new Vector2i(10-1, 259+35*2), this);
		UIMap uiMap = new UIMap(new Vector2i(10, 10), level);
		
		pan.addComponent(healthMeter);
		pan.addComponent(hpLabel);
		pan.addComponent(expMeter);
		pan.addComponent(expLabel);
		pan.addComponent(nameLabel);
		pan.addComponent(exampleButton);
		pan.addComponent(spawnButtonIco);
		pan.addComponent(uiInv);
		pan.addComponent(uiMap);
		uiMgr = GameCore.getUiManager();
		uiMgr.addPanel(pan);
		this.x = x;
		this.y = y;
		health = 100;
		
		playerInventory.setStack(0, new ItemStack(Items.fireStaff));
		playerInventory.setStack(1, new ItemStack(Items.woodenBow));
		playerInventory.setStack(2, new ItemStack(Items.swordBasic));
		playerInventory.setStack(3, new ItemStack(Items.confiCannon));
	}
	
	public EntityPlayer(Level currentLevel, String name, int x, int y, KeyHandler input) {
		this(currentLevel, name, x, y, input, null);
	}
	
	private String trimToSize(String in, int size) {
		char[] chars = new char[size];
		if(in.length() >= 32){ 
			for(int i = 0; i < 32; i++) {
				chars[i] = in.toCharArray()[i];
			}
			return new StringBuilder().append(chars).toString();
		}else{
			return in;
		}
	}

	@Override
	public void update() {
		boolean movingX = false, movingY = false;
		super.update();
		if(currentItem != null && currentItem.quantity == 0) currentItem = null;
		noClip = true && GameCore.DEV;
		if(rand.nextInt(100) == 0) health += 0.5;
		if(health > 100) health = 100;
		else if(health < 0) health = 0;
		//						current/max
		healthMeter.setProgress(Math.floor(health)/maxHealth);
		
		if(currentExp >= (expLevel*100/2+(expLevel*8))) {
			currentExp = currentExp-(expLevel*100/2+(expLevel*8));
			expLevel+=1;
		}else
			expMeter.setProgress(currentExp/(expLevel*100/2+(expLevel*8)));
		expLabel.setLabel("Lvl " + expLevel);
		
		if(input.isKeyPressed(KeyEvent.VK_1)) {
			currentSlot = 0;
		}else if(input.isKeyPressed(KeyEvent.VK_2)) {
			currentSlot = 1;
		}else if(input.isKeyPressed(KeyEvent.VK_3)) {
			currentSlot = 2;
		}else if(input.isKeyPressed(KeyEvent.VK_4)) {
			currentSlot = 3;
		}
		
		if (input.isKeyPressed(KeyEvent.VK_W) || input.isKeyPressed(KeyEvent.VK_UP)) {
			yDir += -movementSpeed / 4d;
			current = up;
			movingY = true;
		}
		if (input.isKeyPressed(KeyEvent.VK_S) || input.isKeyPressed(KeyEvent.VK_DOWN)) {
			yDir += +movementSpeed / 4d;
			current = down;
			movingY = true;
		}
		if (input.isKeyPressed(KeyEvent.VK_A) || input.isKeyPressed(KeyEvent.VK_LEFT)) {
			xDir += -movementSpeed / 4d;
			current = left;
			movingX = true;
		}
		if (input.isKeyPressed(KeyEvent.VK_D) || input.isKeyPressed(KeyEvent.VK_RIGHT)) {
			xDir += +movementSpeed / 4d;
			current = right;
			movingX = true;
		}
		if(xDir > movementSpeed) xDir = movementSpeed;
		if(yDir > movementSpeed) yDir = movementSpeed;
		if(xDir < -movementSpeed) xDir = -movementSpeed;
		if(yDir < -movementSpeed) yDir = -movementSpeed;
		
		if(input.heal) {
			health = maxHealth;
		}
		
		move(xDir, 0);
		move(0, yDir);
		
		if(playerInventory.getStack(currentSlot) != null && playerInventory.getStack(currentSlot).getItem() instanceof ItemShooter) {
			ItemShooter item = (ItemShooter) playerInventory.getStack(currentSlot).getItem();
			maxRate = item.getFireRate();
			updateProjectiles(item);
		}
		
		if(animTime == intervals) animTime = 0;
		current.setIntervals(intervals);
		if(moving) current.update();
		else current.setFrame(0);
		
		
		if(xDir > 0 && !movingX) xDir += -movementSpeed / 8d;
		//else xDir = 0;
		if(xDir < 0 && !movingX) xDir += movementSpeed / 8d;
		//else xDir = 0;
		
		if(yDir > 0 && !movingY) yDir += -movementSpeed / 8d;
		//else yDir = 0;
		if(yDir < 0 && !movingY) yDir += movementSpeed / 8d;
		//else yDir = 0;
	}
	
	@EventHandler
	public void updateTileSelection(MouseEvent.MouseMoved e) {
		selectionX = ((int)x+e.x/3+4)/16-7;
		selectionY = ((int)y+e.y/3+14)/16-6;
		/*if(e.dragged) {
			level.setTile(selectionX, selectionY, BaseTiles.air);
		}*/
	}

	private void updateProjectiles(ItemShooter item) {
		if(shooting && fireRate <= 0) {
			if(MouseHandler.getMouseX() >= GameCore.getWindowWidth()) return;
			double dX = (MouseHandler.getMouseX()-GameCore.getWindowWidth()/2);
			double dY = (MouseHandler.getMouseY()-GameCore.getWindowHeight()/2);
			double dir = Math.atan2(dY, dX);			
			EntityProjectile e;
			
			/*switch(item.getType()) {
			case FIREBALL:
				e = new EntityFireballSmall(level, (int)x, (int)y, dir, this, Sprite.fireballSmall0);
				Utilities.playSound("firestaff_Shoot.wav");
				break;
			case ARROW:
				e = new EntityFireballSmall(level, (int)x, (int)y, dir, this, Sprite.arrow);
				Utilities.playSound("bow_Shoot.wav");
				break;
			default:
				e = new EntityFireballSmall(level, (int)x, (int)y, dir, this, Sprite.fireballSmall0);
				break;
			}*/
			e = item.getProjectile(level, (int)x, (int)y, dir, (EntityMob)this);
			if(e != null) {
				if(item.useSound != null)
					Utilities.playSound(item.useSound);
			}else {
				e = new EntityFireballSmall(level, (int)x, (int)y, dir, this, Sprite.fireballSmall0);
			}
			
			shoot(e);
			if(fireRate <= 0) fireRate = maxRate;
		}else if(fireRate > 0) fireRate--;
	}
	
	@EventHandler
	public void onMousePressed(MouseEvent.MousePressed e) {
		for(int layer = 0; layer < level.layers; layer++) {
			Tile selectedTile = level.getTile(selectionX, selectionY, layer);
			if(selectedTile.onClick(selectionX, selectionY, level, this, MouseHandler.getMouseButton())) {
				e.setCancelled();
				return;
			}
		}
		if(e.button == 1) {
			shooting = true;
			if(playerInventory.getStack(0) != null && playerInventory.getStack(0).getItem() instanceof ItemSword) {
				ItemSword sword = (ItemSword) playerInventory.getStack(0).getItem();
				if(!sword.doesRepeat()) return;
				double damage = sword.getDamage();
				CollisionBox box = new CollisionBox((int)x-16, (int)y-16, 48, 48);
						//new CollisionBox((int)x+((dir%2)*(dir == 1 ?16: dir == 3 ? -16 : 0)), (int)y+((dir%2+1)*(dir == 0?-16:dir == 2?16:0)), 16, 16);
				List<Entity> eList = level.getEntitiesInBox(box, this);
				
				for(Entity en : eList)
					if(en instanceof EntityMob) {
						EntityMob mob = (EntityMob) en;
						if(damage != 0)
							mob.damage(new DamageSource(damage, this));
					}
			}
		}
	}
	
	@EventHandler
	public void onMouseReleased(MouseEvent.MouseReleased e) {
		if(shooting) shooting = false;
	}
	
	@EventHandler
	public void onMouseClicked(MouseEvent.MouseClicked e) {
		if(playerInventory.getStack(0) != null && playerInventory.getStack(0).getItem() instanceof ItemSword) {
			ItemSword sword = (ItemSword) playerInventory.getStack(0).getItem();
			if(sword.doesRepeat()) return;
			double damage = sword.getDamage();
			CollisionBox box = new CollisionBox((int)x-16, (int)y-16, 48, 48);
					//new CollisionBox((int)x+((dir%2)*(dir == 1 ?16: dir == 3 ? -16 : 0)), (int)y+((dir%2+1)*(dir == 0?-16:dir == 2?16:0)), 16, 16);
			List<Entity> eList = level.getEntitiesInBox(box, this);
			
			for(Entity en : eList)
				if(en instanceof EntityMob) {
					EntityMob mob = (EntityMob) en;
					if(damage != 0)
						mob.damage(new DamageSource(damage, this));
				}
		}
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		int offX = 0, offY = 4;
		if(playerInventory.getStack(currentSlot) != null) {
			if(current.getCurrentFrame() >= 2)
				offY=5;
			else
				offY = 4;
			if (current == right) {
				// E
				if(current.getCurrentFrame() >= 2)
					offX=6;
				else if(current.getCurrentFrame() >= 1 && current.getCurrentFrame() < 2)
					offX=7;
				else
					offX = 8;
				screen.renderSprite((int) x + offX, (int) y + offY, playerInventory.getStack(currentSlot).getItem().getSprite(),
						true);
			}else if (current == up) {
				// N
				offX = 6+playerInventory.getStack(currentSlot).getItem().getSprite().getWidth()/2;
				screen.renderSprite((int) x + offX, (int) y + offY, playerInventory.getStack(currentSlot).getItem().getSprite(),
						true, false);
			}
		}
		screen.renderMob((int)x-1, (int)y, this);
		if (playerInventory.getStack(currentSlot) != null) {
			if (current == down) {
				// N
				offX = -playerInventory.getStack(currentSlot).getItem().getSprite().getWidth() / 2;
				screen.renderSprite((int) x + offX, (int) y + offY, playerInventory.getStack(currentSlot).getItem().getSprite(),
						true, false);
			} else if (current == left) {
				// W
				if (current.getCurrentFrame() >= 2) offX = -1;
				else if (current.getCurrentFrame() >= 1 && current.getCurrentFrame() < 2) offX = -2;
				else offX = -3;
				screen.renderSprite((int) x + offX, (int) y + offY, playerInventory.getStack(currentSlot).getItem().getSprite(),
						true, true);
				offX = -1;
			}
		}
		moving = false;
		if(!GameCore.DEV) return;
		my.game.render.Font fnt = new my.game.render.Font();
		fnt.drawString(screen, 0, 0, "X: " + x + "\nY: " + y, false);
	}
	
	@Override
	public void removeAnim() {
		Utilities.playSound("confi_Spread.wav", -20);
		for(int i = 0; i < 128; i++) {
			Sprite s = new Sprite(2, 2, rand.nextInt());
			level.spawnParticle(new EntityParticle(level, (int)x, (int)y, rand.nextInt(300), s));
		}
	}
	
	public String hitSound() {
		return "hit.wav";
	}
	
	public String walkSound() {
		return "walk.wav";
	}
	
	public String randomSound() {
		return null;
	}
	
	public String deathSound() {
		return null;
	}
	
	@Override
	public Sprite getSprite() {
		return current.getSprite();
	}

}
