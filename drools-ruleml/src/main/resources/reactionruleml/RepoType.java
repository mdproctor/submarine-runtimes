//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.02 at 03:31:08 PM MEZ 
//


package reactionruleml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for repo.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="repo.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/1.0/xsd}repo.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/1.0/xsd}repo.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "repo.type", propOrder = {
    "var",
    "plex"
})
public class RepoType {

    @XmlElement(name = "Var")
    protected VarType var;
    @XmlElement(name = "Plex")
    protected PlexRepoType plex;

    /**
     * Gets the value of the var property.
     * 
     * @return
     *     possible object is
     *     {@link VarType }
     *     
     */
    public VarType getVar() {
        return var;
    }

    /**
     * Sets the value of the var property.
     * 
     * @param value
     *     allowed object is
     *     {@link VarType }
     *     
     */
    public void setVar(VarType value) {
        this.var = value;
    }

    /**
     * Gets the value of the plex property.
     * 
     * @return
     *     possible object is
     *     {@link PlexRepoType }
     *     
     */
    public PlexRepoType getPlex() {
        return plex;
    }

    /**
     * Sets the value of the plex property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlexRepoType }
     *     
     */
    public void setPlex(PlexRepoType value) {
        this.plex = value;
    }

}
