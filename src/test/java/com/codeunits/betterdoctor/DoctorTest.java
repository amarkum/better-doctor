package com.codeunits.betterdoctor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeunits.betterdoctor.datamodel.Doctor;
import com.codeunits.betterdoctor.datamodel.Practice;

/**
 * 
 * Test Class for Doctor
 * 
 * @author AK054561 - AMAR KUMAR
 * 
 */

public class DoctorTest {

	private List<Practice> testPractices;
	private String firstName;
	private String lastName;
	private String gender;
	private String background;

	@Before
	public void setup() {
		firstName = "fname";
		lastName = "lname";
		gender = "male";
		background = "background";
		final Practice practice = new Practice("city", "street", 111);
		testPractices = new ArrayList<Practice>();
		testPractices.add(practice);
	}

	@After
	public void tearDown() {
		firstName = null;
		lastName = null;
		gender = null;
		background = null;
		testPractices = null;
	}

	/**
	 * This method tests Doctor with All Ideal fields Passed
	 * 
	 */

	@Test
	public void testDoctorFieldIdeal() {
		final Doctor doctor = new Doctor(firstName, lastName, gender, background, testPractices);

		assertEquals("fname", doctor.getFirstName());
		assertEquals("lname", doctor.getLastName());
		assertEquals("male", doctor.getGender());
		assertEquals("background", doctor.getBackground());
		assertEquals(testPractices, doctor.getPractices());
	}

	/**
	 * This method tests Doctor with All fields being NULL passed to constructor
	 * 
	 */

	@Test
	public void testDoctorFieldIWithNullValuetestAllFields() {

		final Doctor doctor = new Doctor(null, null, null, null, null);

		assertNull(doctor.getFirstName());
		assertNull(doctor.getLastName());
		assertNull(doctor.getGender());
		assertNull(doctor.getBackground());
		assertNull(doctor.getPractices());
	}

}
