package org.drools.xml.jaxb.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name="list")
public class JaxbListWrapper<T> extends ArrayList<T> {

	public JaxbListWrapper() {
		super();
	}

	public JaxbListWrapper(Collection<? extends T> c) {
		super(c);
	}

	public JaxbListWrapper(int initialCapacity) {
		super(initialCapacity);
	}

	@XmlElement(name="element")
	public List<T> getElements() {
		return this;
	}
	
	public void setElements(List<T> elems) {
		clear();
		if (elems != null) {
			addAll(elems);
		}
	}
}
