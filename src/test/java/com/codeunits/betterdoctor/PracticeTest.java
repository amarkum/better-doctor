package com.codeunits.betterdoctor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeunits.betterdoctor.datamodel.Practice;

/**
 * Test class for Practice
 * 
 * @author AK054561 - AMAR KUMAR
 *
 */
public class PracticeTest {

	private String city;
	private String street;
	private int zip;

	@Before
	public void setup() {
		city = "city";
		street = "street";
		zip = 111;
	}

	@After
	public void tearDown() {
		city = null;
		street = null;
		zip = 0;

	}

	/**
	 * This method tests Practice with All Ideal fields passed to constructor
	 * 
	 */

	@Test
	public void testPracticesFieldIdeal() {

		final Practice pratice = new Practice(city, street, zip);

		assertEquals(city, pratice.getCity());
		assertEquals(street, pratice.getStreet());
		assertEquals(zip, pratice.getZip());

	}

	/**
	 * This method tests Practice with All fields being NULL passed to constructor
	 * 
	 */
	@Test
	public void testPracticesFieldWithNullAllFields() {

		city = null;
		street = null;
		zip = 0;
		final Practice pratices = new Practice(null, null, 0);

		assertNull(pratices.getCity());
		assertNull(pratices.getStreet());
		assertEquals(0, pratices.getZip());

	}

}
