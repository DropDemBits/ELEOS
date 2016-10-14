package my.game.events;

public class Event {

	boolean isCancelled = false;
	
	//private List<IEventListener> listeners = new ArrayList<IEventListener>();

	public Event() {boop();}
	
	private void boop() {
		
	}

	/*public List<IEventListener> getListeners() {
		return listeners;
	}*/
	
	public void setCancelled() {
		this.isCancelled = true;
	}
	
	public boolean isCancelled() {
		return isCancelled;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Event;
	}
	
}
