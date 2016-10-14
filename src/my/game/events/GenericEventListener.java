package my.game.events;

import java.lang.reflect.Method;

import my.game.events.EventHandler.Priority;

public class GenericEventListener implements IEventListener {

	Method leanTo;
	Object declared;
	Priority p;
	
	public GenericEventListener(Method method, Object declared) {
		leanTo = method;
		this.declared = declared;
	}

	@Override
	public void invoke(Event e) {
		
		try {
			if(e.isCancelled) return;
			leanTo.setAccessible(true);
			leanTo.invoke(declared, e);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void setPriority(Priority p) {
		this.p = p;
	}
	
	@Override
	public Priority getPriority() {
		return p;
	}

}
