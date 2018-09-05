package com.codeunits.betterdoctor.datamodel;

/**
 *
 * This class stores practice information
 *
 * @author AK054561 - AMAR KUMAR
 *
 */
public class Practice {

	private final String city;
	private final String street;
	private final int zip;

	public Practice(final String city, final String street, final int zip) {
		this.street = street;
		this.zip = zip;
		this.city = city;
	}

	/**
	 * This method returns city
	 *
	 * @return city
	 */
	public final String getCity() {
		return city;
	}

	/**
	 * This method returns street
	 *
	 * @return street
	 */
	public final String getStreet() {
		return street;
	}

	/**
	 * This method returns zip
	 *
	 * @return zip
	 */
	public final int getZip() {
		return zip;
	}

}
