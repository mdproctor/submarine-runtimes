//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.10 at 02:27:43 AM CET 
//


package org.drools.pmml_4_0.descr;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="dataName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SSE" type="{http://www.dmg.org/PMML-4_0}NUMBER" />
 *       &lt;attribute name="SSB" type="{http://www.dmg.org/PMML-4_0}NUMBER" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ClusteringModelQuality")
public class ClusteringModelQuality
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute
    protected String dataName;
    @XmlAttribute(name = "SSE")
    protected Double sse;
    @XmlAttribute(name = "SSB")
    protected Double ssb;

    /**
     * Gets the value of the dataName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataName() {
        return dataName;
    }

    /**
     * Sets the value of the dataName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataName(String value) {
        this.dataName = value;
    }

    /**
     * Gets the value of the sse property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSSE() {
        return sse;
    }

    /**
     * Sets the value of the sse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSSE(Double value) {
        this.sse = value;
    }

    /**
     * Gets the value of the ssb property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSSB() {
        return ssb;
    }

    /**
     * Sets the value of the ssb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSSB(Double value) {
        this.ssb = value;
    }

}
