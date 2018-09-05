package com.codeunits.betterdoctor;

/**
 * This interface to verify the zipcode
 */
public interface BetterDoctorZipCodeValidator {
	
	/**
	 * This method to verify for the given zipcode for U.S region
	 * 
	 * @param zip
	 * @return Boolean
	 *
	 */
	
	public boolean isValidZipCode(final String  zip);
}
