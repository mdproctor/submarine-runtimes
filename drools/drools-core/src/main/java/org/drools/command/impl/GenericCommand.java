package org.drools.command.impl;

import org.drools.command.Context;

public interface GenericCommand<T> extends org.drools.command.Command {
	
	T execute(Context context);
	
}
