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
 * <p>Java class for Exists.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Exists.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/1.0/xsd}Exists.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/1.0/xsd}Exists.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Exists.type", propOrder = {
    "oid",
    "declareOrVar",
    "formula",
    "atom",
    "and",
    "or",
    "exists",
    "equal",
    "neg"
})
public class ExistsType {

    protected OidType oid;
    @XmlElements({
        @XmlElement(name = "declare", type = DeclareType.class),
        @XmlElement(name = "Var", type = VarType.class)
    })
    protected List<Object> declareOrVar;
    protected FormulaExistsType formula;
    @XmlElement(name = "Atom")
    protected AtomType atom;
    @XmlElement(name = "And")
    protected AndInnerType and;
    @XmlElement(name = "Or")
    protected OrInnerType or;
    @XmlElement(name = "Exists")
    protected ExistsType exists;
    @XmlElement(name = "Equal")
    protected EqualType equal;
    @XmlElement(name = "Neg")
    protected NegType neg;

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
     * Gets the value of the declareOrVar property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the declareOrVar property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeclareOrVar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeclareType }
     * {@link VarType }
     * 
     * 
     */
    public List<Object> getDeclareOrVar() {
        if (declareOrVar == null) {
            declareOrVar = new ArrayList<Object>();
        }
        return this.declareOrVar;
    }

    /**
     * Gets the value of the formula property.
     * 
     * @return
     *     possible object is
     *     {@link FormulaExistsType }
     *     
     */
    public FormulaExistsType getFormula() {
        return formula;
    }

    /**
     * Sets the value of the formula property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormulaExistsType }
     *     
     */
    public void setFormula(FormulaExistsType value) {
        this.formula = value;
    }

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
     * Gets the value of the and property.
     * 
     * @return
     *     possible object is
     *     {@link AndInnerType }
     *     
     */
    public AndInnerType getAnd() {
        return and;
    }

    /**
     * Sets the value of the and property.
     * 
     * @param value
     *     allowed object is
     *     {@link AndInnerType }
     *     
     */
    public void setAnd(AndInnerType value) {
        this.and = value;
    }

    /**
     * Gets the value of the or property.
     * 
     * @return
     *     possible object is
     *     {@link OrInnerType }
     *     
     */
    public OrInnerType getOr() {
        return or;
    }

    /**
     * Sets the value of the or property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrInnerType }
     *     
     */
    public void setOr(OrInnerType value) {
        this.or = value;
    }

    /**
     * Gets the value of the exists property.
     * 
     * @return
     *     possible object is
     *     {@link ExistsType }
     *     
     */
    public ExistsType getExists() {
        return exists;
    }

    /**
     * Sets the value of the exists property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExistsType }
     *     
     */
    public void setExists(ExistsType value) {
        this.exists = value;
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

}
