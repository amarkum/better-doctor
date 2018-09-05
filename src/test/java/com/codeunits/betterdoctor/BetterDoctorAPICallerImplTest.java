package com.codeunits.betterdoctor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.codeunits.betterdoctor.constants.Resources;
import com.codeunits.betterdoctor.datamodel.Doctor;
import com.codeunits.betterdoctor.datamodel.Practice;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ IOUtils.class, BetterDoctorAPICallerImpl.class })

/**
 * Test class for BetterDoctorCallerAPIImpl
 * 
 * @author AK054561 - AMAR KUMAR
 *
 */

public class BetterDoctorAPICallerImplTest {

	@Mock
	URL url;

	@Mock
	HttpURLConnection connection;

	@Mock
	InputStream inputStrm;

	final static String jsonRESPONSE = "JSON_RESPONSE";
	final static BetterDoctorAPICallerImpl betterDoctorAPICaller = new BetterDoctorAPICallerImpl();

	@Before
	public void setUp() throws Exception {

		mockStatic(IOUtils.class);
		whenNew(URL.class).withArguments(anyString()).thenReturn(url);
		when(url.openConnection()).thenReturn(connection);
		when(connection.getInputStream()).thenReturn(inputStrm);
	}

	@After
	public void tearDown() {
		url = null;
		connection = null;
		inputStrm = null;
	}

	/**
	 * This method tests for getDoctors method with All values received from parsing
	 * JSON Response
	 * 
	 */
	@Test
	public void testgetDoctorsWithIdealValues() throws Exception {

		final String doctorsRESPONSE = "{\"meta\":{\"data_type\":\"array\",\"item_type\":\"Doctor\",\"total\":991,\"count\":1,\"skip\":0,\"limit\":1,\"fields_requested\":\"profile,practices(visit_address)\",\"ignored_query_parameters\":[\"speciality_uid\"]},\"data\":[{\"practices\":[{\"visit_address\":{\"city\":\"Fremont\",\"lat\":37.54081,\"lon\":-121.97231,\"state\":\"CA\",\"state_long\":\"California\",\"street\":\"40000 Fremont Blvd\",\"street2\":\"Ste A\",\"zip\":\"94538\"}}],\"profile\":{\"first_name\":\"Brendan\",\"middle_name\":\"John\",\"last_name\":\"Selway\",\"slug\":\"brendan-selway\",\"title\":\"DDS\",\"image_url\":\"image.jpg\",\"gender\":\"male\",\"languages\":[{\"name\":\"English\",\"code\":\"en\"}],\"bio\":\"Background\"}}]}";

		final List<Doctor> actualdDoctors = new ArrayList<Doctor>();
		final Practice practice = new Practice("Fremont", "40000 Fremont Blvd", 94538);
		final List<Practice> practices = new ArrayList<Practice>();
		practices.add(practice);
		final Doctor doctor = new Doctor("Brendan", "Selway", "male", "Background", practices);
		actualdDoctors.add(doctor);
		when(IOUtils.toString(inputStrm, Resources.ENCODING)).thenReturn(doctorsRESPONSE);
		final List<Doctor> expectedDoctors = betterDoctorAPICaller.getDoctors(94538, "cardiologist");

		assertEquals(actualdDoctors.get(0).getFirstName(), expectedDoctors.get(0).getFirstName());
		assertEquals(actualdDoctors.get(0).getLastName(), expectedDoctors.get(0).getLastName());
		assertEquals(actualdDoctors.get(0).getGender(), expectedDoctors.get(0).getGender());
		assertEquals(actualdDoctors.get(0).getPractices().get(0).getCity(),
				expectedDoctors.get(0).getPractices().get(0).getCity());
		assertEquals(actualdDoctors.get(0).getPractices().get(0).getStreet(),
				expectedDoctors.get(0).getPractices().get(0).getStreet());
		assertEquals(actualdDoctors.get(0).getPractices().get(0).getZip(),
				expectedDoctors.get(0).getPractices().get(0).getZip());
	}

	/**
	 * This method tests for getDoctors method with some of Value e.g. lastName &
	 * gender being not available while parsing JSON Response
	 * 
	 */
	@Test
	public void testgetDoctorsWithMissingValues() throws Exception {

		final String doctorsRESPONSE = "{\"meta\":{\"data_type\":\"array\",\"item_type\":\"Doctor\",\"total\":991,\"count\":1,\"skip\":0,\"limit\":1,\"fields_requested\":\"profile,practices(visit_address)\",\"ignored_query_parameters\":[\"speciality_uid\"]},\"data\":[{\"practices\":[{\"visit_address\":{\"city\":\"Fremont\",\"lat\":37.54081,\"lon\":-121.97231,\"state\":\"CA\",\"state_long\":\"California\",\"street\":\"40000 Fremont Blvd\",\"street2\":\"Ste A\",\"zip\":\"94538\"}}],\"profile\":{\"first_name\":\"Brendan\",\"middle_name\":\"John\",\"slug\":\"brendan-selway\",\"title\":\"DDS\",\"image_url\":\"image.jpg\",\"languages\":[{\"name\":\"English\",\"code\":\"en\"}],\"bio\":\"Background\"}}]}";
		final List<Doctor> actualdDoctors = new ArrayList<Doctor>();
		final Practice practice = new Practice("Fremont", "40000 Fremont Blvd", 94538);
		final List<Practice> practices = new ArrayList<Practice>();
		practices.add(practice);
		final Doctor doctor = new Doctor("Brendan", null, null, "Background", practices);
		actualdDoctors.add(doctor);
		when(IOUtils.toString(inputStrm, Resources.ENCODING)).thenReturn(doctorsRESPONSE);

		final List<Doctor> expectedDoctors = betterDoctorAPICaller.getDoctors(94538, "cardiologist");
		assertEquals("", expectedDoctors.get(0).getLastName());
		assertEquals("Not Available", expectedDoctors.get(0).getGender());
	}

	/**
	 * This method test parseDoctors method.
	 */
	@Test
	public void parseDoctors() throws Exception {
		final int zip = 95401;
		final String doctorsRESPONSE = "{\"meta\":{\"data_type\":\"array\",\"item_type\":\"Doctor\",\"total\":539,\"count\":1,\"skip\":0,\"limit\":1,\"fields_requested\":\"profile,practices(visit_address)\",\"ignored_query_parameters\":[\"speciality_uid\"]},\"data\":[{\"practices\":[{\"visit_address\":{\"city\":\"Los Angeles\",\"lat\":33.959665,\"lon\":-118.30212,\"state\":\"CA\",\"state_long\":\"California\",\"street\":\"1440 W Manchester Ave\",\"zip\":\"90047\"}},{\"visit_address\":{\"city\":\"San Ramon\",\"lat\":37.72856,\"lon\":-121.93097,\"state\":\"CA\",\"state_long\":\"California\",\"street\":\"9130 Alcosta Blvd\",\"street2\":\"Ste A\",\"zip\":\"94583\"}},{\"visit_address\":{\"city\":\"Concord\",\"lat\":37.98144,\"lon\":-122.03253,\"state\":\"CA\",\"state_long\":\"California\",\"street\":\"2222 East St\",\"street2\":\"Ste 355\",\"zip\":\"94520\"}},{\"visit_address\":{\"city\":\"Santa Rosa\",\"lat\":38.44136,\"lon\":-122.74467,\"state\":\"CA\",\"state_long\":\"California\",\"street\":\"140 Stony Point Rd\",\"street2\":\"Ste A\",\"zip\":\"95401\"}}],\"profile\":{\"first_name\":\"Ali\",\"last_name\":\"Iranmanesh\",\"slug\":\"ali-iranmanesh\",\"title\":\"DMD\",\"image_url\":\"image.jpg\",\"gender\":\"male\",\"languages\":[{\"name\":\"English\",\"code\":\"en\"}],\"bio\":\"BackgroundOfDoctor\"}}]}";
		final List<Doctor> expectedDoctors = betterDoctorAPICaller.parseDoctors(doctorsRESPONSE, zip);
		assertEquals(95401, expectedDoctors.get(0).getPractices().get(0).getZip());
	}

	/**
	 * This method test parseDoctors method to throw JSON Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void parseDoctorsJSONException() throws Exception {
		final int zip = 94538;
		final String doctorsRESPONSE = "{\"meta\":{\"data_type\":\"array\",\"item_type\":\"Doctor\",\"total\":991,\"count\":1,\"skip\":0,\"limit\":1,\"fields_requested\":\"profile,practices(visit_address)\",\"ignored_query_parameters\":[\"speciality_uid\"]},\"data\":[{\"practices\":[{\"visit_address\":{\"city\":\"Fremont\",\"lat\":37.54081,\"lon\":-121.97231,\"state\":\"CA\",\"state_long\":\"California\",\"street\":\"40000 Fremont Blvd\",\"street2\":\"Ste A\",\"zip\":\"94538\"}}],\"profile\":{\"first_name\":\"Brendan\",\"middle_name\":\"John\",\"last_name\":\"Selway\",\"slug\":\"brendan-selway\",\"title\":\"DDS\",\"image_url\":\"https://asset1.betterdoctor.com/images/528259e44214f850700000a0-1_thumbnail.jpg\",\"gender\":\"male\",\"languages\":[{\"name\":\"English\",\"code\":\"en\"}],\"bio\":\"BackgroundOfDoctor\"}}]}";
		whenNew(JSONObject.class).withParameterTypes(String.class).withArguments(doctorsRESPONSE)
				.thenThrow(JSONException.class);
		final List<Doctor> expectedDoctors = betterDoctorAPICaller.parseDoctors(doctorsRESPONSE, zip);
		assertEquals(0, expectedDoctors.size());
	}

	/**
	 * This method test getSpecialities method.
	 */
	@Test
	public void testgetSpecialities() throws Exception {
		final String specialityResponse = "{\"data\":[{\"uid\": \"cardiologist\"}]}";
		when(IOUtils.toString(inputStrm, Resources.ENCODING)).thenReturn(specialityResponse);
		final List<String> expectedSpecialities = betterDoctorAPICaller.getSpecialities();
		assertEquals("cardiologist", expectedSpecialities.get(0));
	}

	/**
	 * This method test getSpecialities method to throw JSON Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testgetSpecialitiesJSONException() throws Exception {
		final String specialityResponse = "NO_VALID_JSON_TO_PARSE";
		when(IOUtils.toString(inputStrm, Resources.ENCODING)).thenReturn(specialityResponse);
		whenNew(JSONObject.class).withParameterTypes(String.class).withArguments(specialityResponse)
				.thenThrow(JSONException.class);
		final List<String> expectedSpecialities = betterDoctorAPICaller.getSpecialities();
		assertEquals(0, expectedSpecialities.size());
	}

	/**
	 * This method test getResponse method.
	 */
	@Test
	public void getResponseTest() throws Exception {
		final String uri = "https://api.betterdoctor.com/2016-03-01/doctors?query=94538&speciality_uid=cardiologist&fields=profile%2Cpractices(visit_address)&sort=rating-desc&skip=0&limit=1&user_key=931d9cc6831b686ac9cb104da3a5ac2d";
		when(IOUtils.toString(url.openConnection().getInputStream(), Resources.ENCODING)).thenReturn(jsonRESPONSE);
		final String expectedJSON = betterDoctorAPICaller.getResponse(uri);
		assertEquals("JSON_RESPONSE", expectedJSON);
	}

	/**
	 * This method test getResponse method to throw MalformedURLException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getResponseMalformedURLException() throws Exception {
		final String malformedURI = "https://api. betterdoctor. ** com";
		whenNew(URL.class).withArguments(malformedURI).thenThrow(MalformedURLException.class);
		final String expectedJSON = betterDoctorAPICaller.getResponse(malformedURI);
		assertEquals(null, expectedJSON);
	}

	/**
	 * This method test getResponse method to throw IOException
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void getResponseIOException() throws Exception {
		final String uri = "https://api.betterdoctor.com/2016-03-01/doctors?query=94538&speciality_uid=cardiologist&fields=profile%2Cpractices(visit_address)&sort=rating-desc&skip=0&limit=1&user_key=931d9cc6831b686ac9cb104da3a5ac2d";
		when(IOUtils.toString(url.openConnection().getInputStream(), Resources.ENCODING)).thenThrow(IOException.class);
		final String expectedJSON = betterDoctorAPICaller.getResponse(uri);
		assertEquals(null, expectedJSON);
	}

}
