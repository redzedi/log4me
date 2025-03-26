package log4me.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class LogManager {
	
 public static	Logger getLogger(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
	  
	  //LoggerContext creation itself could be complicated with contextFactory and spi involved .
	  // contextFactory is needed because --  LoggerContext creation and wiring it with config can be 2 different process
	  // LoggerManager is needed to hide all the contextfactory and other details from the user.
	   return LoggerContext.getInstance().getLogger(name);
	  
	}

}
