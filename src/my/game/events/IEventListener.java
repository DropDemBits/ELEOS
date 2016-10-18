package my.game.events;

import my.game.events.EventHandler.Priority;

public interface IEventListener {

	/**
	 * Called when the specified event has been fired
	 */
	void invoke(Event e);
	
	/**
	 * The priority of this EventListener
	 */
	public Priority getPriority();
	
}
