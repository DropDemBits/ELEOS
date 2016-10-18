package my.game.events;

public class Event {

	private boolean isCancelled = false;
	
	//private List<IEventListener> listeners = new ArrayList<IEventListener>();

	public Event() {boop();}
	
	private void boop() {
		
	}

	/*public List<IEventListener> getListeners() {
		return listeners;
	}*/
	
	/**
	 * Cancels an event (prevents further processing)
	 */
	public void setCancelled() {
		this.isCancelled = true;
	}
	
	/**
	 * Queries if canceled
	 * 
	 * @return true if the event has been canceled
	 */
	public boolean isCancelled() {
		return isCancelled;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Event;
	}
	
}
