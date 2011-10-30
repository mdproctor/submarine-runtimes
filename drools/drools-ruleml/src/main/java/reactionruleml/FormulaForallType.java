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
 * <p>Java class for formula-forall.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="formula-forall.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/1.0/xsd}formula-forall.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/1.0/xsd}formula.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formula-forall.type", propOrder = {
    "atom",
    "implies",
    "equivalent",
    "forall",
    "equal",
    "neg",
    "rule"
})
public class FormulaForallType {

    @XmlElement(name = "Atom")
    protected AtomType atom;
    @XmlElement(name = "Implies")
    protected ImpliesType implies;
    @XmlElement(name = "Equivalent")
    protected EquivalentType equivalent;
    @XmlElement(name = "Forall")
    protected ForallType forall;
    @XmlElement(name = "Equal")
    protected EqualType equal;
    @XmlElement(name = "Neg")
    protected NegType neg;
    @XmlElement(name = "Rule")
    protected RuleType rule;

    /**
     * Gets the value of the atom property.
     * 
     * @return
     *     possible object is
     *     {@link AtomType }
     *     
     */
    public AtomType getAtom() {
        return atom;
    }

    /**
     * Sets the value of the atom property.
     * 
     * @param value
     *     allowed object is
     *     {@link AtomType }
     *     
     */
    public void setAtom(AtomType value) {
        this.atom = value;
    }

    /**
     * Gets the value of the implies property.
     * 
     * @return
     *     possible object is
     *     {@link ImpliesType }
     *     
     */
    public ImpliesType getImplies() {
        return implies;
    }

    /**
     * Sets the value of the implies property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImpliesType }
     *     
     */
    public void setImplies(ImpliesType value) {
        this.implies = value;
    }

    /**
     * Gets the value of the equivalent property.
     * 
     * @return
     *     possible object is
     *     {@link EquivalentType }
     *     
     */
    public EquivalentType getEquivalent() {
        return equivalent;
    }

    /**
     * Sets the value of the equivalent property.
     * 
     * @param value
     *     allowed object is
     *     {@link EquivalentType }
     *     
     */
    public void setEquivalent(EquivalentType value) {
        this.equivalent = value;
    }

    /**
     * Gets the value of the forall property.
     * 
     * @return
     *     possible object is
     *     {@link ForallType }
     *     
     */
    public ForallType getForall() {
        return forall;
    }

    /**
     * Sets the value of the forall property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForallType }
     *     
     */
    public void setForall(ForallType value) {
        this.forall = value;
    }

    /**
     * Gets the value of the equal property.
     * 
     * @return
     *     possible object is
     *     {@link EqualType }
     *     
     */
    public EqualType getEqual() {
        return equal;
    }

    /**
     * Sets the value of the equal property.
     * 
     * @param value
     *     allowed object is
     *     {@link EqualType }
     *     
     */
    public void setEqual(EqualType value) {
        this.equal = value;
    }

    /**
     * Gets the value of the neg property.
     * 
     * @return
     *     possible object is
     *     {@link NegType }
     *     
     */
    public NegType getNeg() {
        return neg;
    }

    /**
     * Sets the value of the neg property.
     * 
     * @param value
     *     allowed object is
     *     {@link NegType }
     *     
     */
    public void setNeg(NegType value) {
        this.neg = value;
    }

    /**
     * Gets the value of the rule property.
     * 
     * @return
     *     possible object is
     *     {@link RuleType }
     *     
     */
    public RuleType getRule() {
        return rule;
    }

    /**
     * Sets the value of the rule property.
     * 
     * @param value
     *     allowed object is
     *     {@link RuleType }
     *     
     */
    public void setRule(RuleType value) {
        this.rule = value;
    }

}
