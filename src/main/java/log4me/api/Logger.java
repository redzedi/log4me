package log4me.api;

/**
 *  the client facing interface of the logging system.
 *  It does the string --> Message ( with formatting conversion)
 *  it does check if the proposed msg is enabled 
 */
public interface Logger {
	
	
	
	String getName();
	void log(LogLevel level , String msg);

}
