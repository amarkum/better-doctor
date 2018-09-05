package com.codeunits.betterdoctor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codeunits.betterdoctor.constants.Resources;
import com.codeunits.betterdoctor.datamodel.Doctor;
import com.codeunits.betterdoctor.datamodel.Practice;

/**
 * This class returns information of doctor for queried zip & speciality and
 * specialty list
 */

class BetterDoctorAPICallerImpl implements BetterDoctorAPICaller {

	public final static Logger log = Logger.getLogger(DisplayDoctors.class);
	public final static String VISIT_ADDRESS = "visit_address";
	public final static String GENDER = "gender";
	public final static String LASTNAME = "last_name";

	/**
	 * This method takes zip and specialty as input, gets the JSON response , calls
	 * parseDoctors() methhod and returns list of doctor information.
	 * 
	 * @param zip
	 * @param speciality
	 * @return List<Doctor>
	 * 
	 */
	public List<Doctor> getDoctors(int zip, String speciality) {
		String response = null;

		final StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(Resources.BETTER_DOCTOR_BASE_URL).append("doctors?query=").append(zip)
				.append("&speciality_uid=").append(speciality)
				.append("&fields=profile%2Cpractices(visit_address)&sort=rating-desc&skip=0&limit=10&")
				.append(Resources.KEY);
		final String uri = uriBuilder.toString();
		response = getResponse(uri);
		return parseDoctors(response, zip);
	}

	/**
	 * This method accepts JSON & zip and returns List of Doctors
	 * 
	 * @param response,
	 * @param zip
	 * @return List<Doctor>
	 */

	public List<Doctor> parseDoctors(String response, int zip) {

		final List<Doctor> doctors = new ArrayList<Doctor>();
		List<Practice> practices = null;

		try {
			final JSONObject parentJsonObject = new JSONObject(response);
			final JSONArray dataArray = parentJsonObject.getJSONArray("data");

			for (int i = 0; i < dataArray.length(); i++) {
				final JSONObject profileJsonObject = new JSONObject(dataArray.getJSONObject(i).getString("profile"));
				final String firstName = profileJsonObject.getString("first_name");
				final String lastName = profileJsonObject.has(LASTNAME) ? profileJsonObject.getString(LASTNAME) : "";
				final String gender = profileJsonObject.has(GENDER) ? profileJsonObject.getString(GENDER)
						: "Not Available";
				final String background = profileJsonObject.getString("bio");
				final JSONArray practicesArray = parentJsonObject.getJSONArray("data").getJSONObject(i)
						.getJSONArray("practices");

				for (int j = 0; j < practicesArray.length(); j++) {
					final int zipcode = Integer
							.parseInt(practicesArray.getJSONObject(j).getJSONObject(VISIT_ADDRESS).getString("zip"));
					final String city = practicesArray.getJSONObject(j).getJSONObject(VISIT_ADDRESS).getString("city");
					final String street = practicesArray.getJSONObject(j).getJSONObject(VISIT_ADDRESS)
							.getString("street");

					if (zipcode == zip) {
						final Practice practice = new Practice(city, street, zip);
						practices = new ArrayList<Practice>();
						practices.add(practice);
					}
				}

				final Doctor doctor = new Doctor(firstName, lastName, gender, background, practices);
				doctors.add(doctor);
			}
		}

		catch (final JSONException jsonException) {
			log.error("Error in Processing Information.");
		}

		return doctors;

	}

	/**
	 * This method returns list of all Specialty UID from BetterDoctor API by
	 * parsing JSON Object
	 */

	public List<String> getSpecialities() {

		final StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(Resources.BETTER_DOCTOR_BASE_URL).append("specialties?").append(Resources.KEY);
		final String uri = uriBuilder.toString();
		final List<String> specialities = new ArrayList<String>();

		try {

			final JSONObject specialityJsonObject = new JSONObject(getResponse(uri));
			final JSONArray array = specialityJsonObject.getJSONArray("data");

			for (int i = 0; i < array.length(); i++) {
				final String speciality = array.getJSONObject(i).getString("uid");
				specialities.add(speciality);
			}
		}

		catch (final JSONException jsonException) {
			log.error("Error in Processing Information.");
		}

		return specialities;
	}

	/**
	 * This method takes uri as input and returns JSON as string
	 * 
	 * @param uri
	 * @return response
	 */
	public String getResponse(final String uri) {
		String response = null;
		try {
			final URL url = new URL(uri);
			response = IOUtils.toString(url.openConnection().getInputStream(), Resources.ENCODING);
		} catch (final MalformedURLException e) {
			log.error("Not a proper URL");

		} catch (final IOException e) {
			log.error("Failed to connect to Internet");
		}

		return response;
	}

}
