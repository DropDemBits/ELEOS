package my.game.render;

public class Font {

	//i>91?null
	private static SpriteSheet font = new SpriteSheet("/fonts/aaa.png", 8);
	private static Sprite[] characters = Sprite.split(font);
	private static String chars = //
	"ABCDEFGHIJKLM" //
  + "NOPQRSTUVWXYZ" //
  + "abcdefghijklm" //
  + "nopqrstuvwxyz" //
  + "!@#$%^&*()-_+" //
  + "=/0123456789." //
  + ",|[]{}\\;:\"'<>" //
  + "? ";
	
	public Font() {
		
	}
	
	public void drawString(Screen screen, int x, int y, String text, boolean fixed) {
		drawString(screen, x, y, text, 0xFFFFFF, fixed);
	}
	
	public void drawString(Screen screen, int x, int y, String text, int clr, boolean fixed) {
		int line = 0;
		int xPos = x;
		for(int i = 0; i < text.length(); i++) {
			int yOffset = 0;
			int indice = chars.indexOf(text.charAt(i));
			switch(text.charAt(i)) {
			case 'p':
			case 'g':
			case 'j':
			case 'y':
			case 'q':
			case ',':
				yOffset = 2;
				break;
			}
			if(text.charAt(i) == '\n' || xPos >= 37*8/*GameCore.getWindowWidth()*/) { 
				line++;
				xPos = 0;
			}
			if (indice == -1) continue;
			screen.renderSprite(xPos, y + yOffset + (line*8), characters[indice], clr, fixed, false);
			xPos+=8;
		}
	}
	
	public void render(Screen screen) {
		/*drawString(screen, 0, 0, "\"Hello! poop/\"", false);
		drawString(screen, 0, 8, "Problems: A|\npoop,\nqueen,\ngood,\nyou,\njet", false);*/
		drawString(screen, 0, 0, "1000", false);
		//drawString(screen, 0, 16, Integer.toString(new Random().nextInt(), 10), false);
	}

}
