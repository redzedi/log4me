package log4me;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.Test;

import com.google.gson.Gson;

import log4me.api.LogLevel;
import log4me.api.LogManager;
import log4me.api.Logger;
import log4me.core.config.impl.DefaultConfiguration.ConfigDto;

public class Log4meTest {
	
	private static Gson gson = new Gson();
	
	@Test
	public void testConfigRead() throws FileNotFoundException, IOException {
		
		//try(FileReader rdr = new FileReader("src/test/resources/log4me.json")){
			try(InputStreamReader rdr = new InputStreamReader( this.getClass().getClassLoader().getResourceAsStream("log4me.json") )){
			ConfigDto cfg =  gson.fromJson( rdr, ConfigDto.class);
			
			System.out.println(cfg);
			System.out.println(cfg.loggers().get(0).getName());
			System.out.println(cfg.loggers().get(0));
			
			
			assertEquals(LogLevel.INFO, cfg.rootLogger().getLevel());
			assertEquals(Arrays.asList("my-stdout"), cfg.rootLogger().getAppenderRefs());
			assertNotNull(cfg);
		}
	}
	
	@Test
	public void testLoggingForDeclaredLogger() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		Logger lgr = LogManager.getLogger("a.b.c");
		assertNotNull(lgr);
		lgr.log(LogLevel.ERROR, "this is an error msg !!");
		lgr.log(LogLevel.INFO, "this is an info msg !!"); // should not be printed
		
	}
	
	@Test
	public void testLoggingForUnDeclaredLogger() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {
		
		Logger lgr = LogManager.getLogger("x.y.z");
		assertNotNull(lgr);
		lgr.log(LogLevel.INFO, "this is an info msg !!"); // should  be printed
		lgr.log(LogLevel.DEBUG, "this is an debug msg !!"); //should not be printed
		
	}

}
