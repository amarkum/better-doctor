package com.codeunits.betterdoctor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.codeunits.betterdoctor.constants.Resources;

/**
 * Test class for BetterDoctorZipCodeValidatorImpl
 * 
 * @author SP054800-Sneha Panchannavar
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ IOUtils.class, BetterDoctorZipCodeValidatorImpl.class })
public class BetterDoctorZipCodeValidatorImplTest {

	BetterDoctorZipCodeValidatorImpl zipCodeValidator;


	@Mock
	URL url;

	@Mock
	HttpURLConnection connection;

	@Mock
	InputStream inputStrm;


	@Before
	public void setUp() throws Exception {
		zipCodeValidator = new BetterDoctorZipCodeValidatorImpl();
		mockStatic(IOUtils.class);
		connection = Mockito.mock(HttpURLConnection.class);
		whenNew(URL.class).withArguments(anyString()).thenReturn(url);
		when(url.openConnection()).thenReturn(connection);
		when(connection.getInputStream()).thenReturn(inputStrm);

	}
	
	@After
	public void tearDown() throws Exception {
		url=null;
		connection=null;
		inputStrm=null;
	}

	/* Test case for a valid zipcode*/
	@Test
	public void PositiveTest() throws Exception {
		final String response  = "{\"city\":\"abcd\"}";
		when(IOUtils.toString(url.openConnection().getInputStream(), Resources.ENCODING)).thenReturn(response);
		final boolean status = zipCodeValidator.isValidZipCode("12345");
		assertEquals(true, status);

	}

	/* Test case for a invalid zipcode with JSONException*/
	@Test
	public void NegativeJSONException() throws Exception {
		final String response="{\"data\":\"abcd\"}";
		when(IOUtils.toString(url.openConnection().getInputStream(), Resources.ENCODING)).thenReturn(response);
		final boolean status = zipCodeValidator.isValidZipCode("99999999999999");
		assertEquals(false, status);

	}
	
	/* Test case for a invalid zipcode with IOException*/
	@SuppressWarnings("unchecked")
	@Test
	public void NegativeIOException() throws Exception {
		final String response="{\"data\":\"abcd\"}";
		when(IOUtils.toString(url.openConnection().getInputStream(), Resources.ENCODING)).thenThrow(IOException.class);
		final boolean status = zipCodeValidator.isValidZipCode("99999999999999");
		assertEquals(false, status);

	}

}
