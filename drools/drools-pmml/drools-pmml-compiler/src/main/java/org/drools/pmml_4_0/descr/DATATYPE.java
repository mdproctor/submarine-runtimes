//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.10 at 02:27:43 AM CET 
//


package org.drools.pmml_4_0.descr;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DATATYPE.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DATATYPE">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="string"/>
 *     &lt;enumeration value="integer"/>
 *     &lt;enumeration value="float"/>
 *     &lt;enumeration value="double"/>
 *     &lt;enumeration value="boolean"/>
 *     &lt;enumeration value="date"/>
 *     &lt;enumeration value="time"/>
 *     &lt;enumeration value="dateTime"/>
 *     &lt;enumeration value="dateDaysSince[0]"/>
 *     &lt;enumeration value="dateDaysSince[1960]"/>
 *     &lt;enumeration value="dateDaysSince[1970]"/>
 *     &lt;enumeration value="dateDaysSince[1980]"/>
 *     &lt;enumeration value="timeSeconds"/>
 *     &lt;enumeration value="dateTimeSecondsSince[0]"/>
 *     &lt;enumeration value="dateTimeSecondsSince[1960]"/>
 *     &lt;enumeration value="dateTimeSecondsSince[1970]"/>
 *     &lt;enumeration value="dateTimeSecondsSince[1980]"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DATATYPE")
@XmlEnum
public enum DATATYPE {

    @XmlEnumValue("string")
    STRING("string"),
    @XmlEnumValue("integer")
    INTEGER("integer"),
    @XmlEnumValue("float")
    FLOAT("float"),
    @XmlEnumValue("double")
    DOUBLE("double"),
    @XmlEnumValue("boolean")
    BOOLEAN("boolean"),
    @XmlEnumValue("date")
    DATE("date"),
    @XmlEnumValue("time")
    TIME("time"),
    @XmlEnumValue("dateTime")
    DATE_TIME("dateTime"),
    @XmlEnumValue("dateDaysSince[0]")
    DATE_DAYS_SINCE_0("dateDaysSince[0]"),
    @XmlEnumValue("dateDaysSince[1960]")
    DATE_DAYS_SINCE_1960("dateDaysSince[1960]"),
    @XmlEnumValue("dateDaysSince[1970]")
    DATE_DAYS_SINCE_1970("dateDaysSince[1970]"),
    @XmlEnumValue("dateDaysSince[1980]")
    DATE_DAYS_SINCE_1980("dateDaysSince[1980]"),
    @XmlEnumValue("timeSeconds")
    TIME_SECONDS("timeSeconds"),
    @XmlEnumValue("dateTimeSecondsSince[0]")
    DATE_TIME_SECONDS_SINCE_0("dateTimeSecondsSince[0]"),
    @XmlEnumValue("dateTimeSecondsSince[1960]")
    DATE_TIME_SECONDS_SINCE_1960("dateTimeSecondsSince[1960]"),
    @XmlEnumValue("dateTimeSecondsSince[1970]")
    DATE_TIME_SECONDS_SINCE_1970("dateTimeSecondsSince[1970]"),
    @XmlEnumValue("dateTimeSecondsSince[1980]")
    DATE_TIME_SECONDS_SINCE_1980("dateTimeSecondsSince[1980]");
    private final String value;

    DATATYPE(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DATATYPE fromValue(String v) {
        for (DATATYPE c: DATATYPE.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
