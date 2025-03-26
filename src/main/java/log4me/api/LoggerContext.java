package log4me.api;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import log4me.core.config.Configuration;
import log4me.core.config.impl.DefaultConfiguration;
import log4me.core.impl.LoggerImpl;

public class LoggerContext {

	// why separate LoggerContext here ??
	// this is here a singleton holder for Configuration
	
	/**
	 * 
	 *  LogManager.getLogger() --> LoggerCOntextFactory.getContext().getLogger()
	 *  
	 *  Log4jContextFactory.getContext( configLocation) --> CconfigurationFactory.getInstance().getConfiguration(configLocaton) ,  LoggerContext.start( config)
	 *  
	 *   
	 *  
	 *  
	 *  LoggerContext.start() --> LoggerContext.setConfiguration(cfg) --> Configuration.start() --> Configuration.initialize()
	 *  
	 *   Configuration.initialize() --> Configuration.setup() , Configuration.doConfigure()
	 *   
	 *   Configuration.setup() = extension point for subclasses
	 *   
	 *   Configuration.doConfigure() == 
	 *   1. interprets the parsed config file and stores reference of the config items on itself
	 *   2. de-references appenderRef from loggerConfig , it sets actual appender instances on loggerConfig
	 *   3. Logger name strings representing fqcn have a parent-child relationship basis the names . The chain ending in root Logger
	 *   
	 *   
	 *   
	 */

	private static volatile LoggerContext instance;

	private static ReentrantLock clsLock = new ReentrantLock();
	
	//ensures once it is set any thread accessing it gets the latest ref
	private volatile Configuration config;
	
	private ConcurrentHashMap<String, Logger> loggerRegistry = new ConcurrentHashMap<String, Logger>();
	
	private ReentrantReadWriteLock rwLock  = new ReentrantReadWriteLock(); 
	
	private LoggerContext() {

	}

	public static LoggerContext getInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		if (instance == null) {
			try {
				clsLock.lock();
				if (instance == null) {
					LoggerContext tmp = new LoggerContext();

					//initialize
					tmp.setConfig(DefaultConfiguration.getInstance());
					instance = tmp;
				} 
			} finally {
				clsLock.unlock();
			}
			
		}

		return instance;

	}
	
	private void setConfig(Configuration cfg) {
		 rwLock.writeLock().lock();
		   if(this.config != null) {
			   throw new RuntimeException(" config is already set . It can be set once");
		   }
		  this.config = cfg;
		  rwLock.writeLock().unlock();
	}
	
	public Configuration getConfig() {
		return this.config;
	}
	
	public Logger getLogger(String name) {
		
		if(config == null) {
			throw new IllegalStateException("getLogger can't be called before config is set");
		}
		
		return loggerRegistry.computeIfAbsent(name, this::newInstance);
	}
	
	private Logger newInstance(String name) {
		return new LoggerImpl(name, this.config);
	}

}
