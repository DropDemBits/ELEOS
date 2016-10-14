package my.game.events;

import my.game.events.EventHandler.Priority;

public interface IEventListener {

	void invoke(Event e);
	/**
	 * The priority of this EventListener
	 */
	public Priority getPriority();
	
}
