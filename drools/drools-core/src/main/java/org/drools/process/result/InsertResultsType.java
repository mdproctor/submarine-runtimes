//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.16 at 08:23:06 AM CEST 
//


package org.drools.process.result;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InsertResultsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InsertResultsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fact-handles" type="{http://drools.org/process/result}FactHandleListType"/>
 *         &lt;element name="fact-objects" type="{http://drools.org/process/result}FactObjectListType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InsertResultsType", propOrder = {
    "factHandles",
    "factObjects"
})
public class InsertResultsType {

    @XmlElement(name = "fact-handles", required = true)
    protected FactHandleListType factHandles;
    @XmlElement(name = "fact-objects")
    protected FactObjectListType factObjects;
    @XmlAttribute
    protected String identifier;

    /**
     * Gets the value of the factHandles property.
     * 
     * @return
     *     possible object is
     *     {@link FactHandleListType }
     *     
     */
    public FactHandleListType getFactHandles() {
        return factHandles;
    }

    /**
     * Sets the value of the factHandles property.
     * 
     * @param value
     *     allowed object is
     *     {@link FactHandleListType }
     *     
     */
    public void setFactHandles(FactHandleListType value) {
        this.factHandles = value;
    }

    /**
     * Gets the value of the factObjects property.
     * 
     * @return
     *     possible object is
     *     {@link FactObjectListType }
     *     
     */
    public FactObjectListType getFactObjects() {
        return factObjects;
    }

    /**
     * Sets the value of the factObjects property.
     * 
     * @param value
     *     allowed object is
     *     {@link FactObjectListType }
     *     
     */
    public void setFactObjects(FactObjectListType value) {
        this.factObjects = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }
}
