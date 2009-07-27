//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.16 at 08:23:06 AM CEST 
//


package org.drools.process.result;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExecutionResultsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExecutionResultsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="query-results" type="{http://drools.org/process/result}QueryResultsType"/>
 *         &lt;element name="insert-results" type="{http://drools.org/process/result}InsertResultsType"/>
 *         &lt;element name="global-value" type="{http://drools.org/process/result}GlobalType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement( namespace = "http://drools.org/process/result", name = "execution-results" )
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExecutionResultsType", propOrder = {
    "queryResultsOrInsertResultsOrGlobalValue"
})
public class ExecutionResultsType {

    @XmlElements({
        @XmlElement(name = "global-value", type = GlobalType.class),
        @XmlElement(name = "insert-results", type = InsertResultsType.class),
        @XmlElement(name = "query-results", type = QueryResultsType.class)
    })
    protected List<Object> queryResultsOrInsertResultsOrGlobalValue;

    /**
     * Gets the value of the queryResultsOrInsertResultsOrGlobalValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the queryResultsOrInsertResultsOrGlobalValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQueryResultsOrInsertResultsOrGlobalValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GlobalType }
     * {@link InsertResultsType }
     * {@link QueryResultsType }
     * 
     * 
     */
    public List<Object> getQueryResultsOrInsertResultsOrGlobalValue() {
        if (queryResultsOrInsertResultsOrGlobalValue == null) {
            queryResultsOrInsertResultsOrGlobalValue = new ArrayList<Object>();
        }
        return this.queryResultsOrInsertResultsOrGlobalValue;
    }

}
