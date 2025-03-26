package log4me.core.config;

import java.util.ArrayList;
import java.util.List;

import log4me.api.LogLevel;
import log4me.core.appenders.api.Appender;

public class LoggerConfig {
	
	/**
	 * THis class contains all config from the config file.
	 * and houses the core computation of logging.
	 */
	
	private String name;
	private LoggerConfig parent;
	private LogLevel level;
	private List<Appender> appenders;
	private List<String> appenderRefs;
	
	
	private  LoggerConfig() {
		appenders = new ArrayList<>();
	}

	public LoggerConfig(String name, LogLevel level, List<String> ars) {
		super();
		this.name = name;
		this.level = level;
		this.appenderRefs = ars;
		//appenders = new ArrayList<>();
	}



	public String getName() {
		return name;
	}
	
	
	
	public LogLevel getLevel() {
		return level;
	}



	public List<String> getAppenderRefs() {
		return appenderRefs;
	}
	
	public void addAppender(Appender ap) {
		appenders.add(ap);
	}
	
	public void setParent(LoggerConfig cfg) {
		this.parent = cfg;
	}



	/**
	 * it does the following -
	 * 
	 * 1. converts msg to logEvent by adding time
	 * 2. does filter of log based on the configured logic
	 * 3. call registered self appender
	 * 4. call parent for logging
	 * 
	 * @param lvl
	 * @param msg
	 */
	
	public void log( LogLevel lvl , String msg) {
		log(this.level,lvl,msg);
	}
	
	protected void log(LogLevel selfLevel , LogLevel lvl , String msg) {
		if(this.level != null && !(appenders == null ||  appenders.isEmpty())) {
			if(lvl.ordinal() <= (selfLevel!=null?selfLevel.ordinal(): this.level.ordinal())) {
				for(Appender currApp:appenders) {
					currApp.append(msg);
				}
			}
		}else {
			// assuming isAdditive==false
			if(this.parent != null) {
				this.parent.log(this.level , lvl , msg);
			}
		}
		
		
	}
	
	
	
	

}
