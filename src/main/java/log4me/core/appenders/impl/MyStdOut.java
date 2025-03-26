package log4me.core.appenders.impl;

import log4me.core.appenders.api.Appender;

public class MyStdOut implements Appender {
	
	private String name;
	
	

	public MyStdOut(String name) {
		super();
		this.name = name;
	}



	@Override
	public void append(String msg) {
		System.out.println(String.format("[ Appender -%s] -- %s ", name, msg));
		
	}

}
