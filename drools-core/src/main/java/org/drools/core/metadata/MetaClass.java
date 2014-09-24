package org.drools.core.metadata;

public interface MetaClass<T> extends Identifiable {

    public MetaProperty<T,?>[] getProperties();

    public int getPropertyIndex( MetaProperty propertyLiteral );
}
