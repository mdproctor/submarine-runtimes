package org.drools.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.drools.rule.Column;
import org.drools.rule.Declaration;
import org.drools.rule.GroupElement;
import org.drools.rule.RuleConditionElement;

/**
 * A class capable of resolving a declaration in the current build context
 * 
 * @author etirelli
 */
public class DeclarationScopeResolver {
    private static final Stack EMPTY_STACK = new Stack(  );
    private Map[] maps;
    private Stack buildStack; 

    public DeclarationScopeResolver(final Map[] maps ) {
        this( maps, EMPTY_STACK );
    }

    public DeclarationScopeResolver(final Map[] maps, final Stack buildStack ) {
        this.maps = maps;
        if( buildStack == null ) {
            this.buildStack = EMPTY_STACK;
        } else {
            this.buildStack = buildStack;
        }
    }

    public Class getType(final String name) {
        for( int i = this.buildStack.size()-1; i >= 0; i-- ) {
            Declaration declaration = ( Declaration ) (( RuleConditionElement ) this.buildStack.get( i )).getInnerDeclarations().get( name );
            if( declaration != null ) {
                return declaration.getExtractor().getExtractToClass();
            }
        }
        for ( int i = 0, length = this.maps.length; i < length; i++ ) {
            final Object object = this.maps[i].get( name );
            if ( object != null ) {
                if ( object instanceof Declaration ) {
                    return ((Declaration) object).getExtractor().getExtractToClass();
                } else {
                    return (Class) object;
                }
            }
        }
        return null;
    }
    
    public Declaration getDeclaration( final String name ) {
        // it may be a local bound variable
        for( int i = this.buildStack.size()-1; i >= 0; i-- ) {
            Declaration declaration = ( Declaration ) (( RuleConditionElement ) this.buildStack.get( i )).getInnerDeclarations().get( name );
            if( declaration != null ) {
                return declaration;
            }
        }
        // it may be a global or something
        for ( int i = 0, length = this.maps.length; i < length; i++ ) {
            if ( this.maps[i].containsKey( (name) ) ) {
                GlobalExtractor global = new GlobalExtractor( this.maps[i].get( name ));
                Column dummy = new Column(0, global.getObjectType());
                Declaration declaration  = new Declaration(name, global, dummy);
                return declaration;
            }
        }
        return null;
    }

    public boolean available(final String name) {
        for( int i = this.buildStack.size()-1; i >= 0; i-- ) {
            Declaration declaration = ( Declaration ) (( RuleConditionElement ) this.buildStack.get( i )).getInnerDeclarations().get( name );
            if( declaration != null ) {
                return true;
            }
        }
        for ( int i = 0, length = this.maps.length; i < length; i++ ) {
            if ( this.maps[i].containsKey( (name) ) ) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isDuplicated( final String name ) {
        for ( int i = 0, length = this.maps.length; i < length; i++ ) {
            if ( this.maps[i].containsKey( (name) ) ) {
                return true;
            }
        }
        for( int i = this.buildStack.size()-1; i >= 0; i-- ) {
            RuleConditionElement rce = ( RuleConditionElement ) this.buildStack.get( i );
            Declaration declaration = ( Declaration ) rce.getInnerDeclarations().get( name );
            if( declaration != null ) {
                if( ( rce instanceof GroupElement ) && ( (GroupElement)rce).isOr() ) {
                    // if it is an OR and it is duplicated, we can stop looking for duplication now
                    // as it is a separate logical branch
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Return all declarations scoped to the current
     * RuleConditionElement in the build stack
     * 
     * @return
     */
    public Map getDeclarations() {
        Map declarations = new HashMap();
        for( int i = 0; i < this.buildStack.size(); i++ ) {
            // this may be optimized in the future to only re-add elements at 
            // scope breaks, like "NOT" and "EXISTS"
            declarations.putAll( ((RuleConditionElement) this.buildStack.get( i )).getInnerDeclarations() );
        }
        return declarations;
    }
}
