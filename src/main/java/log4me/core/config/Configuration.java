package log4me.core.config;

import java.util.List;
import java.util.Map;

import log4me.core.appenders.api.Appender;

public interface Configuration {
	
	LoggerConfig getRootLogger();
	
	Map<String,LoggerConfig> getLoggers();
	
	List<Appender> getAppenders();
	
	void start();

}
