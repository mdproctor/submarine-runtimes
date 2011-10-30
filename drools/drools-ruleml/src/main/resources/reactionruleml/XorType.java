//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.02 at 03:31:08 PM MEZ 
//


package reactionruleml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Xor.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Xor.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/1.0/xsd}Xor.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/1.0/xsd}Xor.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Xor.type", propOrder = {
    "oid",
    "eventContent"
})
public class XorType {

    protected OidType oid;
    @XmlElements({
        @XmlElement(name = "Expr", type = ExprType.class),
        @XmlElement(name = "Any", type = AnyType.class),
        @XmlElement(name = "Ind", type = IndType.class),
        @XmlElement(name = "Aperiodic", type = AperiodicType.class),
        @XmlElement(name = "Concurrent", type = ConcurrentType.class),
        @XmlElement(name = "Var", type = VarType.class),
        @XmlElement(name = "Message", type = MessageType.class),
        @XmlElement(name = "Sequence", type = SequenceType.class),
        @XmlElement(name = "Not", type = NotType.class),
        @XmlElement(name = "Naf", type = NafType.class),
        @XmlElement(name = "Periodic", type = PeriodicType.class),
        @XmlElement(name = "Disjunction", type = DisjunctionType.class),
        @XmlElement(name = "Conjunction", type = ConjunctionType.class),
        @XmlElement(name = "on", type = OnType.class),
        @XmlElement(name = "Xor", type = XorType.class),
        @XmlElement(name = "Atom", type = AtomType.class),
        @XmlElement(name = "Neg", type = NegType.class),
        @XmlElement(name = "Rule", type = RuleType.class)
    })
    protected List<Object> eventContent;

    /**
     * Gets the value of the oid property.
     * 
     * @return
     *     possible object is
     *     {@link OidType }
     *     
     */
    public OidType getOid() {
        return oid;
    }

    /**
     * Sets the value of the oid property.
     * 
     * @param value
     *     allowed object is
     *     {@link OidType }
     *     
     */
    public void setOid(OidType value) {
        this.oid = value;
    }

    /**
     * Gets the value of the eventContent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventContent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExprType }
     * {@link AnyType }
     * {@link IndType }
     * {@link AperiodicType }
     * {@link ConcurrentType }
     * {@link VarType }
     * {@link MessageType }
     * {@link SequenceType }
     * {@link NotType }
     * {@link NafType }
     * {@link PeriodicType }
     * {@link DisjunctionType }
     * {@link ConjunctionType }
     * {@link OnType }
     * {@link XorType }
     * {@link AtomType }
     * {@link NegType }
     * {@link RuleType }
     * 
     * 
     */
    public List<Object> getEventContent() {
        if (eventContent == null) {
            eventContent = new ArrayList<Object>();
        }
        return this.eventContent;
    }

}
