package my.game.render.ui;

import java.awt.*;
import java.awt.image.*;

import my.game.entity.*;
import my.game.entity.mob.*;
import my.game.entity.player.*;
import my.game.entity.projectile.*;
import my.game.util.*;
import my.game.world.*;

public class UIMap extends UIComponent {

	Level level;
	BufferedImage bgImage;
	int scale = 1;
	int minX, minY, maxX, maxY;
	
	public UIMap(Vector2i pos, Level lvl) {
		super(pos);
		this.level = lvl;
	}
	
	public UIMap(Vector2i pos, Level lvl, BufferedImage bgImage) {
		this(pos, lvl);
		this.bgImage = bgImage;
	}
	
	@Override
	public void render(Graphics g) {
		scale = 1;
		maxX = level.width;
		maxY = level.height;
		if(bgImage != null) {
			g.drawImage(bgImage, pos.getX()+offset.getX(), pos.getY()+offset.getY(), null);
		}
		BufferedImage minimap = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
		int[] rgb = new int[level.tiles[0].length];
		for(int i = 0; i < rgb.length; i++) {
			rgb[i] = level.tiles[0][i];
			if(level.tiles[1][i] != 0xFFFFFFFF) rgb[i] = level.tiles[1][i];
		}
		minimap.setRGB(0, 0, maxX, maxY, level.tiles[0], 0, maxX);
		g.drawImage(minimap, pos.getX()+offset.getX(), pos.getY()+offset.getY(), 64*3, 64*3, null);
		
		for(EntityMob e : level.players) {
			if(e instanceof EntityPlayer) {
				int x = (int)Math.round(e.x)*3/36+pos.getX()+offset.getX();
				int y = (int)Math.round(e.y)*3/36+pos.getY()+offset.getY();
				g.setColor(Color.red);
				g.fillRect(x, y, 2, 2);
				
				//ArrayList<EntityMob> entities = (ArrayList<EntityMob>)level.getEntitiesInRange(e, 200);
				
				for(Entity e1 : level.entities) {
					int x1 = (int)e1.x*3/36+pos.getX()+offset.getX();
					int y1 = (int)e1.y*3/36+pos.getY()+offset.getY();
					if(e1 instanceof EntityMob) {
						g.setColor(Color.green);
					}else if(e1 instanceof EntityProjectile) {
						g.setColor(Color.magenta);
					}
					if(x != x1 && y != y1 && 
							(x1-offset.getX() < maxX && x1-offset.getX() >= minX && y1-offset.getY() < maxY && y1-offset.getY() >= minY)) g.fillRect(x1, y1, 2, 2);
				}
			}
		}
	}

}
