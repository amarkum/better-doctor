package com.codeunits.betterdoctor.datamodel;

import java.util.List;

/**
 * This Class stores Doctor Information
 *
 * @author AK054561- AMAR KUMAR
 *
 */

public class Doctor {

	private final String firstName;
	private final String lastName;
	private final String gender;
	private final String background;
	private final List<Practice> practices;

	public Doctor(final String firstName, final String lastName, final String gender, final String background,
			final List<Practice> practices) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.background = background;
		this.practices = practices;
	}

	/**
	 * This method returns all practice addresses for a doctor
	 *
	 * @return List<Practice>
	 */
	public final List<Practice> getPractices() {
		return practices;
	}

	/**
	 * This method returns first name of a doctor
	 *
	 * @return firstName
	 */
	public final String getFirstName() {
		return firstName;
	}

	/**
	 * This method returns last name of a doctor
	 *
	 * @return firstName
	 */
	public final String getLastName() {
		return lastName;
	}

	/**
	 * This method returns gender of a doctor
	 *
	 * @return gender
	 */
	public final String getGender() {
		return gender;
	}

	/**
	 * This method returns some background information of a doctor
	 *
	 * @return background
	 */
	public final String getBackground() {
		return background;
	}

}
