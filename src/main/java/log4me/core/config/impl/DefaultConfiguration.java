package log4me.core.config.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;

import log4me.core.appenders.api.Appender;
import log4me.core.config.Configuration;
import log4me.core.config.LoggerConfig;

public class DefaultConfiguration implements Configuration {
	
	public static record ConfigDto(List<AppenderConfigDto> appenders, List<LoggerConfig> loggers , LoggerConfig rootLogger) {
		
	}
	
	public static record AppenderConfigDto(String fqcn , String name ) {
		
	}
	
	private  LoggerConfig rootLogger;
	private Map<String,LoggerConfig> loggers = new HashMap<>();
	private List<Appender> appenders = new ArrayList<>();
	
	private HashMap<String, Appender> appenderMap = new HashMap<>();
	
	
	private static Gson gson = new Gson();
	
	private static volatile DefaultConfiguration instance;
	
	private static ReentrantLock clsLock = new ReentrantLock();
	
	
	
	public static DefaultConfiguration getInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
			if (instance == null) {
				try {
				clsLock.lock();
				
				 if(instance == null) {
					 instance  = new DefaultConfiguration();
				 }
				} finally {
					clsLock.unlock();
				}
				
			} 
			
		return instance;
		
		
	}

	@Override
	public LoggerConfig getRootLogger() {
		return rootLogger;
	}

	@Override
	public Map<String,LoggerConfig> getLoggers() {
		return loggers;
	}

	@Override
	public List<Appender> getAppenders() {
		return appenders;
	}
	
	
	
	
	private DefaultConfiguration() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        
		try(InputStreamReader rdr = new InputStreamReader( this.getClass().getClassLoader().getResourceAsStream("log4me.json") )){
			ConfigDto cfgDto =  gson.fromJson( rdr, ConfigDto.class);
			
			for(AppenderConfigDto adto:cfgDto.appenders()) {
				Class<Appender> apClass = (Class<Appender>)Class.forName(adto.fqcn());
				Appender currApp = (Appender)apClass.getConstructor(String.class).newInstance(adto.name());
				appenders.add(currApp);
				appenderMap.put(adto.name(), currApp);
			}
			
			this.rootLogger = cfgDto.rootLogger;
			
			setAppender(rootLogger);
			
			for(LoggerConfig lc:cfgDto.loggers()) {
				lc.setParent(rootLogger);
				
				setAppender(lc);
				
				
				loggers.put(lc.getName(), lc);
			}
			
			
			
		}
		
		
	}

	private void setAppender(LoggerConfig lc) {
		if(lc.getAppenderRefs() != null) {
			for(String currApRef:lc.getAppenderRefs())
				lc.addAppender(appenderMap.get(currApRef));
		}
	}
	
	/**
	 *  start is needed if we want separate initialization from construction .
	 *  
	 *  In log4j the config constructors, do the parsing of file and map it to the general config node structure
	 *  
	 *   thereafter the start is called for appender and loggerConfig initialization
	 */

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}





}


