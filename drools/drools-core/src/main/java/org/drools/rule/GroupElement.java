package org.drools.rule;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drools.RuntimeDroolsException;

public class GroupElement extends ConditionalElement {

    private static final long serialVersionUID = 125354210439500614L;

    public static final Type  AND              = new AndType();
    public static final Type  OR               = new OrType();
    public static final Type  EXISTS           = new ExistsType();
    public static final Type  NOT              = new NotType();

    private Type              type             = null;
    private final List        children         = new ArrayList();

    public GroupElement() {
        this( AND );
    }

    public GroupElement(Type type) {
        this.type = type;
    }

    /**
     * Adds a child to the current GroupElement.
     * 
     * Restrictions are:
     * NOT/EXISTS: can have only one child, either a single Pattern or another CE
     * 
     * @param child
     */
    public void addChild(final Object child) {
        if ( (this.isNot() || this.isExists()) && (this.children.size() > 0) ) {
            throw new RuntimeDroolsException( this.type.toString() + " can have only a single child element. Either a single Pattern or another CE." );
        }
        this.children.add( child );
    }

    public List getChildren() {
        return this.children;
    }

    /**
     * Optimize the group element subtree by removing redundancies
     * like an AND inside another AND, OR inside OR, single branches
     * AND/OR, etc.
     * 
     * LogicTransformer does further, more complicated, transformations
     */
    public void pack() {
        Object[] clone = this.children.toArray();
        for ( int i = 0; i < clone.length; i++ ) {
            // if child is also a group element, there may be 
            // some possible clean up / optimizations to be done
            if ( clone[i] instanceof GroupElement ) {
                GroupElement childGroup = (GroupElement) clone[i];
                childGroup.pack( this );
            }
        }

        // if after packing, this is an AND or OR GE with a single
        // child GE, then clone child into current node eliminating child
        if ( (this.isAnd() || this.isOr()) && (this.children.size() == 1) ) {
            Object child = this.getChildren().get( 0 );
            if( child instanceof GroupElement ) {
                GroupElement group = (GroupElement) child;
                this.type = group.getType();
                this.children.clear();
                this.children.addAll( group.getChildren() );
            }
        } 
    }

    /**
     * @param parent
     */
    private void pack(GroupElement parent) {
        if( this.children.size() == 0 ) {
            // if there is no child, just remove this node
            parent.children.remove( this );
            return;
        }
        
        // If this is  an AND or OR or EXISTS, there are some possible merges
        if ( this.isAnd() || this.isOr() || this.isExists()) {

            // if parent is of the same type as current node,
            // then merge this childs with parent childs
            if ( parent.getType() == this.getType() ) {

                parent.getChildren().remove( this );
                // for each child, pack it and add it to parent
                for ( Iterator childIt = this.children.iterator(); childIt.hasNext(); ) {
                    Object child = childIt.next();
                    parent.addChild( child );
                    if ( child instanceof GroupElement ) {
                        ((GroupElement) child).pack( parent );
                    }
                }

                // if current node has a single child, then move it to parent and pack it
            } else if ( ( ! this.isExists() ) && ( this.children.size() == 1 ) ) {
                Object child = this.children.get( 0 );
                parent.addChild( child );
                parent.getChildren().remove( this );
                if ( child instanceof GroupElement ) {
                    ((GroupElement) child).pack( parent );
                }

                // otherwise pack itself
            } else {
                this.pack();
            }

        // also pack itself if it is a NOT 
        } else {
            this.pack();
        }
    }

    /**
     * Traverses two trees and checks that they are structurally equal at all
     * levels
     * 
     * @param e1
     * @param e2
     * @return
     */
    public boolean equals(final Object object) {
        // Return false if its null or not an instance of ConditionalElement
        if ( object == null || !(object instanceof GroupElement) ) {
            return false;
        }

        // Return true if they are the same reference
        if ( this == object ) {
            return true;
        }

        // Now try a recurse manual check
        final GroupElement e2 = (GroupElement) object;
        if ( ! this.type.equals( e2.type ) ) {
            return false;
        }

        final List e1Children = this.getChildren();
        final List e2Children = e2.getChildren();
        if ( e1Children.size() != e2Children.size() ) {
            return false;
        }

        for ( int i = 0; i < e1Children.size(); i++ ) {
            final Object e1Object1 = e1Children.get( i );
            final Object e2Object1 = e2Children.get( i );
            if ( e1Object1 instanceof GroupElement ) {
                if ( e1Object1.getClass().isInstance( e2Object1 ) ) {
                    if ( !e1Object1.equals( e2Object1 ) ) {
                        //System.out.println( e1Object1.getClass().getName() + " did not have identical children" );
                        return false;
                    }
                } else {
                    //System.out.println( "Should be the equal Conditionalelements but instead was '" + e1Object1.getClass().getName() + "', '" + e2Object1.getClass().getName() + "'" );
                    return false;
                }
            } else if ( e1Object1 instanceof String ) {
                if ( !e1Object1.equals( e2Object1 ) ) {
                    //System.out.println( "Should be the equal Strings but instead was '" + e1Object1 + "', '" + e2Object1 + "'" );
                    return false;
                }
            } else {
                //System.out.println( "Objects are neither instances of ConditionalElement or String" );
                return false;
            }
        }

        return true;
    }

    public int hashCode() {
        return this.type.hashCode() + this.children.hashCode();
    }

    /**
     * Clones all Conditional Elements but references the non ConditionalElement
     * children
     * 
     * @param e1
     * @param e2
     * @return
     */
    public Object clone() {
        GroupElement cloned = null;

        try {
            cloned = (GroupElement) this.getClass().newInstance();
        } catch ( final InstantiationException e ) {
            throw new RuntimeException( "Could not clone '" + this.getClass().getName() + "'" );
        } catch ( final IllegalAccessException e ) {
            throw new RuntimeException( "Could not clone '" + this.getClass().getName() + "'" );
        }
        
        cloned.setType( this.getType() );

        for ( final Iterator it = this.children.iterator(); it.hasNext(); ) {
            Object object = it.next();
            if ( object instanceof GroupElement ) {
                object = ((GroupElement) object).clone();
            }
            cloned.addChild( object );

        }

        return cloned;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isAnd() {
        return this.type.isAnd();
    }

    public boolean isOr() {
        return this.type.isOr();
    }

    public boolean isNot() {
        return this.type.isNot();
    }

    public boolean isExists() {
        return this.type.isExists();
    }
    
    public String toString() {
        return this.type.toString()+this.children.toString();
    }

    /**
     * A public interface for CE types
     */
    public static interface Type extends Serializable {

        /**
         * Returns true if this CE type is an AND
         */
        public boolean isAnd();

        /**
         * Returns true if this CE type is an OR
         */
        public boolean isOr();

        /**
         * Returns true if this CE type is an NOT
         */
        public boolean isNot();

        /**
         * Returns true if this CE type is an EXISTS
         */
        public boolean isExists();
    }

    /**
     * An AND CE type
     */
    private static class AndType
        implements
        Type {

        private static final long serialVersionUID = -669797012452495460L;

        AndType() {
        }

        public boolean isAnd() {
            return true;
        }

        public boolean isExists() {
            return false;
        }

        public boolean isNot() {
            return false;
        }

        public boolean isOr() {
            return false;
        }

        public boolean equals(Object obj) {
            if ( !(obj instanceof AndType) ) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return 11;
        }

        public String toString() {
            return "AND";
        }
    }

    /**
     * An OR CE type
     */
    private static class OrType
        implements
        Type {

        private static final long serialVersionUID = 8108203371968455372L;

        OrType() {
        }

        public boolean isAnd() {
            return false;
        }

        public boolean isExists() {
            return false;
        }

        public boolean isNot() {
            return false;
        }

        public boolean isOr() {
            return true;
        }

        public boolean equals(Object obj) {
            if ( !(obj instanceof OrType) ) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return 17;
        }

        public String toString() {
            return "OR";
        }
    }

    /**
     * A NOT CE type
     */
    private static class NotType
        implements
        Type {

        private static final long serialVersionUID = -7873159668081968617L;

        NotType() {
        }

        public boolean isAnd() {
            return false;
        }

        public boolean isExists() {
            return false;
        }

        public boolean isNot() {
            return true;
        }

        public boolean isOr() {
            return false;
        }

        public boolean equals(Object obj) {
            if ( !(obj instanceof NotType) ) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return 23;
        }

        public String toString() {
            return "NOT";
        }

    }

    /**
     * An EXISTS CE type
     */
    private static class ExistsType
        implements
        Type {

        private static final long serialVersionUID = -1528071451996382861L;

        ExistsType() {
        }

        public boolean isAnd() {
            return false;
        }

        public boolean isExists() {
            return true;
        }

        public boolean isNot() {
            return false;
        }

        public boolean isOr() {
            return false;
        }

        public boolean equals(Object obj) {
            if ( !(obj instanceof ExistsType) ) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return 31;
        }

        public String toString() {
            return "EXISTS";
        }
    }

}