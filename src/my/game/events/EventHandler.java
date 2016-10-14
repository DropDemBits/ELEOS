package my.game.events;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface EventHandler {
	/**
	 * The priority of this EventHandler
	 */
	Priority value() default Priority.NORMAL;
	
	public enum Priority {
		HIGHEST,
		HIGHER,
		NORMAL,
		LOWER,
		LOWEST;	
	}
}
