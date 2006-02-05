package org.drools.util.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * This implements the stuff used by the dynamic proxies that are wrapped around facts.
 * 
 * @author Michael Neale
 */
public class FactInterceptor
    implements
    MethodInterceptor {

    protected final Object target;
    
    protected final Method[] targetFields;
    
    public FactInterceptor(Object target, Method[] fieldMethods) {
        this.target = target;
        this.targetFields = fieldMethods;
    }    
    
    public Object intercept(Object obj,
                            Method method,
                            Object[] args,
                            MethodProxy proxy) throws Throwable {
        if (method.getDeclaringClass() == FieldIndexAccessor.class ) {
            Integer arg = (Integer) args[0];
            return targetFields[arg.intValue() - 1].invoke(target, null);
        } else if (method.getDeclaringClass() == TargetAccessor.class ) {
            return target;
        }
//        } else if (method.getDeclaringClass() == SerializableProxy.class) {
//            
//        }
        return method.invoke(target, args);
        
    }
    


    
    
}
