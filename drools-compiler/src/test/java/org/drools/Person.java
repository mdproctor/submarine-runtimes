package org.drools;

import java.io.Serializable;
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
import java.math.BigDecimal;
import java.math.BigInteger;



public class Person implements Serializable {
    private final String name;
    private final String likes;
    private final int age;
    private BigDecimal bigDecimal;
    private BigInteger bigInteger;
    
    private char sex;
    
    private boolean alive;
    
    private String status;
    
    public Person( String name  ) {
        this( name, "", 0 );
    }
    
    
    public Person( String name, String likes ) {
        this( name, likes, 0 );
    }
    
    public Person( String name, String likes, int age ) {
        this.name = name;
        this.likes = likes;
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLikes() {
        return likes;
    }

    public String getName() {
        return name;
    }
        
    public int getAge() {
        return this.age;
    }
    
    
    
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String toString() {
        return "[Person name='" + this.name + "']";
    }


    /**
     * @inheritDoc
     */
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + age;
        result = PRIME * result + (alive ? 1231 : 1237);
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + sex;
        return result;
    }


    /**
     * @inheritDoc
     */
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        final Person other = (Person) obj;
        if ( age != other.age ) return false;
        if ( alive != other.alive ) return false;
        if ( name == null ) {
            if ( other.name != null ) return false;
        } else if ( !name.equals( other.name ) ) return false;
        if ( sex != other.sex ) return false;
        return true;
    }


    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }


    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }


    public BigInteger getBigInteger() {
        return bigInteger;
    }


    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }
}