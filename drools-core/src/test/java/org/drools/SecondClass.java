package org.drools;

public class SecondClass {
	private String item1;
	private String item2;
	private String item3;
	private String item4;
	private String item5;
	
	public String getItem1() {
		return item1;
	}
	public void setItem1(String item1) {
		this.item1 = item1;
	}
	public String getItem2() {
		return item2;
	}
	public void setItem2(String item2) {
		this.item2 = item2;
	}
	public String getItem3() {
		return item3;
	}
	public void setItem3(String item3) {
		this.item3 = item3;
	}
	public String getItem4() {
		return item4;
	}
	public void setItem4(String item4) {
		this.item4 = item4;
	}
	public String getItem5() {
		return item5;
	}
	public void setItem5(String item5) {
		this.item5 = item5;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("SecondClass[");
		buf.append("item1=" + item1 + ", ");
		buf.append("item2=" + item2 + ", ");
		buf.append("item3=" + item3 + ", ");
		buf.append("item4=" + item4 + ", ");
		buf.append("item5=" + item5 + "]");
		return buf.toString();
	}
	
	public static class AlternativeKey {
		
	}

}
