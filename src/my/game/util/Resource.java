package my.game.util;

public class Resource {
	
	public String higherPath;
	public String resourcePath;
	
	public Resource(String path) {
		String[] parts = path.split(":");
		if(parts.length == 0) return; //Constructors are void methods
		else if(parts.length == 2) {
			higherPath = parts[0];
			resourcePath = parts[1];
		} else {
			higherPath = "eleos";
			resourcePath = parts[0];
		}
	}
	
	public Resource(String id, String path) {
		if(id != null && !id.isEmpty()) higherPath = id;
		else higherPath = "eleos";
		resourcePath = path;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != getClass()) return false;
		Resource cmpObj = (Resource) obj;
		return cmpObj.higherPath == higherPath && cmpObj.resourcePath == resourcePath;
	}

}
