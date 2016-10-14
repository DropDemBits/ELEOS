package my.game.item;

import my.game.entity.mob.*;
import my.game.item.ItemPotion.*;
import my.game.render.*;
import my.game.util.*;
import my.game.world.*;

public class Item {

	public static final ObjectRegistry<Item> itemObjs = new ObjectRegistry<Item>();
	Sprite sprite;
	//TODO: add localizations
	String name;
	public int stackSize = 64;
	
	public static void registerAllItems() {
		itemObjs.registerObject("fireStaff", new ItemShooter().setFireRate(30).setProjectileSprite(Sprite.fireballSmall0).setUseSound("firestaff_Shoot.wav").setSprite(Sprite.fireStaff).setName("fireStaff"));
		itemObjs.registerObject("woodenBow", new ItemShooter().setFireRate(30).setProjectileSprite(Sprite.arrow).setUseSound("bow_Shoot.wav").setSprite(Sprite.woodenBow).setName("woodenBow"));
		itemObjs.registerObject("confiCannon", new ItemConfiCannon().setFireRate(30).setUseSound("firestaff_Shoot.wav").setSprite(Sprite.confiCannon).setName("confiCannon"));
		itemObjs.registerObject("basicSword", new ItemSword().setDamage(80).setSprite(Sprite.swordBasic).setName("basicSword"));
		
		itemObjs.registerObject("healthPotion", new ItemPotion(PotionType.HEAL).setSprite(Sprite.healthPotion).setName("healthPotion"));
		itemObjs.registerObject("expPotion", new ItemPotion(PotionType.EXP).setSprite(Sprite.expPotion).setName("expPotion"));
	}
	
	public Item() {
		sprite = new Sprite(8, 8, 0xF0E50);//Sprite.fireStaff;
	}
	
	public Item(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public int getMaxStackSize() {
		return stackSize;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public Item setSprite(Sprite sprite) {
		this.sprite = sprite;
		return this;
	}
	
	public Item setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Action on "using" an item
	 * 
	 * @param level The current level
	 * @param mob The mob that has the item
	 * @return True to consume the item
	 */
	public boolean useItem(Level level, EntityMob mob) {
		return false;
	}

}
