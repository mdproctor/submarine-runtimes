package org.objenesis;

import java.util.HashMap;
import java.util.Map;
import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

/**
 * Base class to extend if you want to have a class providing your own default strategy. Can also be
 * instantiated directly.
 * 
 * @author Henri Tremblay
 */
public class ObjenesisBase
    implements
    Objenesis {

    /** Strategy used by this Objenesi implementation to create classes */
    protected final InstantiatorStrategy strategy;

    /** Strategy cache. Key = Class, Value = InstantiatorStrategy */
    protected Map                        cache;

    /**
     * Constructor allowing to pick a strategy and using cache
     * 
     * @param strategy Strategy to use
     */
    public ObjenesisBase(final InstantiatorStrategy strategy) {
        this( strategy,
              true );
    }

    /**
     * Flexible constructor allowing to pick the strategy and if caching should be used
     * 
     * @param strategy Strategy to use
     * @param useCache If {@link ObjectInstantiator}s should be cached
     */
    public ObjenesisBase(final InstantiatorStrategy strategy,
                         final boolean useCache) {
        if ( strategy == null ) {
            throw new IllegalArgumentException( "A strategy can't be null" );
        }
        this.strategy = strategy;
        this.cache = useCache ? new HashMap() : null;
    }

    public String toString() {
        return getClass().getName() + " using " + this.strategy.getClass().getName() + (this.cache == null ? " without" : " with") + " caching";
    }

    /**
     * Will create a new object without any constructor being called
     * 
     * @param clazz Class to instantiate
     * @return New instance of clazz
     */
    public Object newInstance(final Class clazz) {
        return getInstantiatorOf( clazz ).newInstance();
    }

    /**
     * Will pick the best instantiator for the provided class. If you need to create a lot of
     * instances from the same class, it is way more efficient to create them from the same
     * ObjectInstantiator than calling {@link #newInstance(Class)}.
     * 
     * @param clazz Class to instantiate
     * @return Instantiator dedicated to the class
     */
    public synchronized ObjectInstantiator getInstantiatorOf(final Class clazz) {
        if ( this.cache == null ) {
            return this.strategy.newInstantiatorOf( clazz );
        }
        ObjectInstantiator instantiator = (ObjectInstantiator) this.cache.get( clazz.getName() );
        if ( instantiator == null ) {
            instantiator = this.strategy.newInstantiatorOf( clazz );
            this.cache.put( clazz.getName(),
                       instantiator );
        }
        return instantiator;
    }

}
