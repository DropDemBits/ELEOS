package my.game.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class AssetLoader {

	public Map<Resource, BufferedImage> imageAssets;
	
	public AssetLoader() {
		imageAssets = new LinkedHashMap<Resource, BufferedImage>();
	}
	
	public void loadAssets(String pathToSearch) {
		List<String> imagesToLoad = getFiles(pathToSearch, "png");
		for(String path : imagesToLoad) {
			try {
				String higherPath = "eleos";//path.substring(0, path.indexOf('/'));
				String lowerPath  = path;//path.substring(path.indexOf('/'), path.length());
				
				System.out.println(path);
				Resource resLocation = new Resource(higherPath, lowerPath);
				BufferedImage image = ImageIO.read(AssetLoader.class.getResourceAsStream(path));
				imageAssets.put(resLocation, image);
			} catch (IOException e) {
				System.err.print("Failed to load image: " + path);
				e.printStackTrace();
				continue;
			}
		}
	}
	
	private List<String> getFiles(String path, String extenstion) {
		System.out.println("Searching " + path);
		String[] list = null;
		List<String> files = new ArrayList<String>();
		//Get Path
		InputStream stream = AssetLoader.class.getResourceAsStream(path);
		try{
			if(stream != null) {
				byte[] data = new byte[stream.available()];
				stream.read(data);
				list = new String(data, 0, data.length).split("\n");
			}
			stream.close();
		}catch(Exception e) {}
		
		if(list == null) return null;
		//Explore paths
		for(String item : list) {
			if(item.endsWith("."+extenstion)) {
				files.add(path+item);
			}else if(item.indexOf('.') == -1) {
				List<String> append = getFiles(path+item+"/", extenstion);
				if(append != null) files.addAll(append);
			}
		}
		return files;
	}
	
	public BufferedImage get(Resource location) {
		try {
			return ImageIO.read(AssetLoader.class.getResourceAsStream(location.resourcePath));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		/*if(imageAssets.containsKey(location)) {
			return imageAssets.get(location);
		}
		return null;*/
	}
	
}
