package log4me.core.impl;

import log4me.api.LogLevel;
import log4me.api.Logger;
import log4me.core.config.Configuration;
import log4me.core.config.LoggerConfig;

public class LoggerImpl implements Logger {
	
	private String name;
	private LoggerConfig cfg;
	
	

	public LoggerImpl(String name, Configuration cfg) {
		super();
		this.name = name;
		this.cfg = cfg.getLoggers().get(name)!=null?cfg.getLoggers().get(name):cfg.getRootLogger();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void log(LogLevel level, String msg) {
		cfg.log(level, msg);
		
	}

}
