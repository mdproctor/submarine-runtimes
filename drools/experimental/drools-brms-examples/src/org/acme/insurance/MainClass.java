package org.acme.insurance;


public class MainClass {
	public static void main(String[] args) {

		InsuranceBusiness launcher = new InsuranceBusiness();
		try {
			launcher.executeExample();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
