package my.game.util;

import java.util.*;

public class ObjectRegistry<T> {

	private LinkedHashMap<String, Integer> stringToId = new LinkedHashMap<String, Integer>();
	private LinkedHashMap<Integer, T> objects  = new LinkedHashMap<Integer, T>();
	
	public ObjectRegistry() {
		objects = new LinkedHashMap<Integer, T>();
		stringToId = new LinkedHashMap<String, Integer>();
	}
	
	public ObjectRegistry(int offset) {
		objects = new LinkedHashMap<Integer, T>();
		stringToId = new LinkedHashMap<String, Integer>();
	}
	
	public void registerObject(String name, T object) {
		registerObject(objects.size(), name, object);
	}
	
	public void registerObject(int position, String name, T object) {
		if(name == null) throw new IllegalArgumentException("Someone is registering a name that is null!!!");
		else if(object == null) throw new IllegalArgumentException("Someone is registering a null object!!!");
		
		if(stringToId.containsKey(name)) throw new IllegalArgumentException("Someone is re-registering a used name!!!");
		stringToId.put(name, position);
		int id = stringToId.get(name);
				
		if(objects.containsKey(id)) throw new RuntimeException("Someone is re-registering a used name!!!");
		else if(objects.containsValue(object)) throw new RuntimeException("Someone is re-registering a used name!!!");
		
		else objects.put(id, object);
	}
	
	public T getObjectByName(String name) {
		return name != null ? getObjectByID(stringToId.get(name)) : null;
	}
	
	public T getObjectByID(int id) {
		return id >= 0 ? objects.get(id) : null;
	}
	
	public void removeObject(String name) {
		removeObject(stringToId.get(name));
	}
	
	public void removeObject(int id) {
		objects.remove(id);
	}
	
}
