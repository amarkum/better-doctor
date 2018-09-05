package com.codeunits.betterdoctor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.codeunits.betterdoctor.constants.Resources;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BetterDoctorSpecialityvalidatorImpl.class})
public class BetterDoctorSpecialityvalidatorImplTest {

	
	
	@Mock
	BetterDoctorAPICallerImpl betterDoctorAPICaller;
	
	@Mock
	Properties prop;
	
	@Mock
	File file;
	
	@Mock
	FileOutputStream output;
	
	@Mock
	FileInputStream input;
	
	@Mock
	FileWriter writer;
	
	@Mock
	JSONObject jsonObject;
	
	@Mock
	JSONArray jsonArray;
	
	@Mock
	BufferedReader br;

	@Mock
	StringBuilder sb;
	
	@Mock
	System system;
	
	@Mock
	BetterDoctorSpecialityvalidatorImpl mockSpecialityClass;
	
	

	BetterDoctorSpecialityvalidatorImpl betterDoctorSpecialityvalidatorImpl;
	
	final static List<String> SPECIALIST = new ArrayList<String>(Arrays.asList("dummy"));
	
	final static List<String> NULL_LIST=null;
	
	final static String SPECIALITY_RESPONSE = "{\"Specialist\":[{\"uid\": \"dummy\"}]}";
	
	final static Long CURRENT_TIME=99999999l;
	
	@Before
	public void setUp() throws Exception {
		mockStatic(System.class);
		whenNew(Properties.class).withNoArguments().thenReturn(prop);
		whenNew(FileInputStream.class).withParameterTypes(String.class).withArguments(anyString())
		.thenReturn(input);
		whenNew(FileOutputStream.class).withParameterTypes(File.class).withArguments(file)
		.thenReturn(output);
		betterDoctorAPICaller = mock(BetterDoctorAPICallerImpl.class);	
		mockSpecialityClass=mock(BetterDoctorSpecialityvalidatorImpl.class);
		whenNew(BetterDoctorAPICallerImpl.class).withNoArguments().thenReturn(betterDoctorAPICaller);
		when(betterDoctorAPICaller.getSpecialities()).thenReturn(SPECIALIST);
		when(System.currentTimeMillis()).thenReturn(CURRENT_TIME);
	}

	/* Test case for updateTimeStamp Method*/
	@Test
	public void updateTimeStampTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.PROPERTIES_FILE_PATH).thenReturn(file);
		
		when((prop.setProperty(Resources.LAST_TIME_PROPERTY_NAME, "12345678"))).thenReturn(Boolean.TRUE);
		
		betterDoctorSpecialityvalidatorImpl.updateTimeStamp(12345678);
		
		doNothing().when(prop).store(output, null);
		verify(prop).setProperty(Resources.LAST_TIME_PROPERTY_NAME, "12345678");
		verify(prop).store(output, null);
		
		
	}
	
	/* Test case for IO Exception in updateTimeStamp Method*/
	@Test
	public void updateTimeStampIOExceptionTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.PROPERTIES_FILE_PATH).thenReturn(file);
		whenNew(FileOutputStream.class).withParameterTypes(File.class).withArguments(file)
		.thenReturn(output);
		doThrow(new IOException()).when(prop).store(output, null);
		when((prop.setProperty(Resources.LAST_TIME_PROPERTY_NAME, "12345678"))).thenReturn(Boolean.TRUE);
		
		betterDoctorSpecialityvalidatorImpl.updateTimeStamp(12345678);
		
		verify(prop).setProperty(Resources.LAST_TIME_PROPERTY_NAME, "12345678");
		verify(prop).store(output, null);
		
		
	}
	
	/* Test case for FileNotFound Exception in updateTimeStamp Method*/
	@SuppressWarnings("unchecked")
	@Test
	public void updateTimeStampFileNotFoundExceptionTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.PROPERTIES_FILE_PATH).thenReturn(file);
		whenNew(FileOutputStream.class).withParameterTypes(File.class).withArguments(file).thenThrow(FileNotFoundException.class);
		
		betterDoctorSpecialityvalidatorImpl.updateTimeStamp(12345678);
		verify(prop,never()). setProperty(Resources.LAST_TIME_PROPERTY_NAME, "12345678");
		verify(prop,never()).store(output, null);
		
		
		
	}
	
	/* Test case for  createSpecialistFile  Method*/
	@SuppressWarnings("unchecked")
	@Test
	public void createSpecialistFileTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(file.exists()).thenReturn(true);
		when(file.delete()).thenReturn(true);
		when(file.createNewFile()).thenReturn(true);
		whenNew(JSONObject.class).withAnyArguments().thenReturn(jsonObject);
		whenNew(JSONArray.class).withAnyArguments().thenReturn(jsonArray);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		
		
		betterDoctorSpecialityvalidatorImpl.createSpecilistFile(SPECIALIST);
		
		verify(file).exists();
		verify(file).delete();
		verify(file).createNewFile();
		verify(writer).write( jsonObject.toString());
		verify(writer).flush();
		
		
	}
	
	/* Test case for  createSpecialistFile  Method with a null list*/
	@SuppressWarnings("unchecked")
	@Test
	public void createSpecialistFileNullTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(file.exists()).thenReturn(true);
		when(file.delete()).thenReturn(true);
		when(file.createNewFile()).thenReturn(true);
		whenNew(JSONObject.class).withAnyArguments().thenReturn(jsonObject);
		whenNew(JSONArray.class).withAnyArguments().thenReturn(jsonArray);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		
		
		betterDoctorSpecialityvalidatorImpl.createSpecilistFile(NULL_LIST);
		
		verify(file).exists();
		verify(file).delete();
		verify(file).createNewFile();
		verify(writer,never()).write( jsonObject.toString());
		verify(writer,never()).flush();
		
		
	}
	
	/* Test case for  createSpecialistFile  Method for IO Exception*/
	@SuppressWarnings("unchecked")
	@Test
	public void createSpecialistFileIOExceptionTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(file.exists()).thenReturn(true);
		when(file.delete()).thenReturn(true);
		when(file.createNewFile()).thenThrow(IOException.class);
		whenNew(JSONObject.class).withAnyArguments().thenReturn(jsonObject);
		whenNew(JSONArray.class).withAnyArguments().thenReturn(jsonArray);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		
		
		betterDoctorSpecialityvalidatorImpl.createSpecilistFile(NULL_LIST);
		
		verify(file).exists();
		verify(file).delete();
		verify(file).createNewFile();
		verify(writer,never()).write( jsonObject.toString());
		verify(writer,never()).flush();
		
		
	}
	
	/* Test case for  createSpecialistFile  Method for JSON Exception*/
	@SuppressWarnings("unchecked")
	@Test
	public void createSpecialistFileJSONExceptionTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(file.exists()).thenReturn(true);
		when(file.delete()).thenReturn(true);
		when(file.createNewFile()).thenReturn(true);
		whenNew(JSONObject.class).withAnyArguments().thenReturn(jsonObject);
		whenNew(JSONArray.class).withAnyArguments().thenReturn(jsonArray);
		when(jsonArray.put(jsonObject)).thenThrow(JSONException.class);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		
		
		betterDoctorSpecialityvalidatorImpl.createSpecilistFile(SPECIALIST);
		
		verify(file).exists();
		verify(file).delete();
		verify(file).createNewFile();
		verify(jsonObject,never()).put( "Speacialist",jsonArray);
		verify(writer,never()).write("TEST");
		verify(writer,never()).flush();
		
		
	}
	
	/* Test case for  createSpecialistFile  Method if file.exists() is false*/
	@SuppressWarnings("unchecked")
	@Test
	public void createSpecialistFileExistsFalse() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(file.exists()).thenReturn(false);
		when(file.delete()).thenReturn(true);
		when(file.createNewFile()).thenReturn(true);
		whenNew(JSONObject.class).withAnyArguments().thenReturn(jsonObject);
		whenNew(JSONArray.class).withAnyArguments().thenReturn(jsonArray);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		
		
		betterDoctorSpecialityvalidatorImpl.createSpecilistFile(SPECIALIST);
		

		verify(file,never()).delete();
		verify(file).createNewFile();
		verify(writer).write( jsonObject.toString());
		verify(writer).flush();
		
		
		
	}
	
	/* Test case for  createSpecialistFile  Method if file.delete() is false*/
	@SuppressWarnings("unchecked")
	@Test
	public void createSpecialistFileDeleteFalse() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(file.exists()).thenReturn(true);
		when(file.delete()).thenReturn(false);
		when(file.createNewFile()).thenReturn(true);
		whenNew(JSONObject.class).withAnyArguments().thenReturn(jsonObject);
		whenNew(JSONArray.class).withAnyArguments().thenReturn(jsonArray);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		
		
		betterDoctorSpecialityvalidatorImpl.createSpecilistFile(SPECIALIST);
		
		verify(file).createNewFile();
		verify(writer).write(jsonObject.toString());
		verify(writer).flush();
	
	}
	
	/* Test case for  createSpecialistFile  Method if file.createNewFile() is false*/
	@SuppressWarnings("unchecked")
	@Test
	public void createSpecialistFileCreateFalse() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(file.exists()).thenReturn(true);
		when(file.delete()).thenReturn(true);
		when(file.createNewFile()).thenReturn(false);
		whenNew(JSONObject.class).withAnyArguments().thenReturn(jsonObject);
		whenNew(JSONArray.class).withAnyArguments().thenReturn(jsonArray);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		
		
		betterDoctorSpecialityvalidatorImpl.createSpecilistFile(SPECIALIST);
		
		verify(jsonObject,never()).put( "Speacialist",jsonArray);
		verify(writer,never()).write("TEST");
		verify(writer,never()).flush();
	
	}
	
	/* Test case for  readJsonFile  Method*/
	@SuppressWarnings("unchecked")
	@Test
	public void readJsonFileTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(BufferedReader.class).withAnyArguments().thenReturn(br);
		when(br.readLine()).thenReturn(SPECIALITY_RESPONSE).thenReturn(null);
		whenNew(StringBuilder.class).withNoArguments().thenReturn(sb);
		when(sb.toString()).thenReturn(SPECIALITY_RESPONSE);
		
		final List<String> result = betterDoctorSpecialityvalidatorImpl.readJSONFile(Resources.SPECIALIST_FILE_PATH);
		assertEquals("dummy", result.get(0));
		
	
	}
	
	/* Test case for  readJsonFile  Method with JSON exception*/
	@SuppressWarnings("unchecked")
	@Test
	public void readJsonFileJsonExceptionTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(BufferedReader.class).withAnyArguments().thenReturn(br);
		when(br.readLine()).thenReturn(SPECIALITY_RESPONSE).thenReturn(null);
		whenNew(StringBuilder.class).withNoArguments().thenReturn(sb);
		when(sb.toString()).thenReturn(SPECIALITY_RESPONSE);
		whenNew(JSONObject.class).withParameterTypes(String.class).withArguments(SPECIALITY_RESPONSE)
		.thenReturn(jsonObject);
		when(jsonObject.getJSONArray(anyString().toString())).thenThrow(JSONException.class);
		
		final List<String> result = betterDoctorSpecialityvalidatorImpl.readJSONFile(Resources.SPECIALIST_FILE_PATH);
		assertEquals("dummy", result.get(0));
		
	
	}
	
	/* Test case for  readJsonFile  Method with IO exception*/
	@SuppressWarnings("unchecked")
	@Test
	public void readJsonFileIOExceptionTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		whenNew(BufferedReader.class).withAnyArguments().thenReturn(br);
		when(br.readLine()).thenThrow(IOException.class);
		final List<String> result = betterDoctorSpecialityvalidatorImpl.readJSONFile(Resources.SPECIALIST_FILE_PATH);
		assertEquals("dummy", result.get(0));
		
	
	}
	
	/* Test case for  isValidSpecialist method*/
	@SuppressWarnings("unchecked")
	@Test
	public void isValidSpecialistTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		when((prop.getProperty(Resources.LAST_TIME_PROPERTY_NAME))).thenReturn("9999999999999999");
		when(mockSpecialityClass.readJSONFile(Resources.SPECIALIST_FILE_PATH)).thenReturn(SPECIALIST);
		final Boolean status = betterDoctorSpecialityvalidatorImpl.isValidSpecialist("dummy");
		assertEquals(true, status);
		
	
	}
	
	/* Test case for  isValidSpecialist method with wrong specialist*/
	@SuppressWarnings("unchecked")
	@Test
	public void isValidSpecialistNegativeTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		when((prop.getProperty(Resources.LAST_TIME_PROPERTY_NAME))).thenReturn("999999999999999999");
		when(mockSpecialityClass.readJSONFile(Resources.SPECIALIST_FILE_PATH)).thenReturn(SPECIALIST);
		final Boolean status = betterDoctorSpecialityvalidatorImpl.isValidSpecialist("dummyTest");
		assertEquals(false, status);
		
	
	}
	
	/* Test case for  isValidSpecialist method without hitting the endpoint*/
	@SuppressWarnings("unchecked")
	@Test
	public void isValidSpecialistFileTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		when((prop.getProperty(Resources.LAST_TIME_PROPERTY_NAME))).thenReturn("1");
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.PROPERTIES_FILE_PATH).thenReturn(file);
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(betterDoctorAPICaller.getSpecialities()).thenReturn(NULL_LIST);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		final Boolean status = betterDoctorSpecialityvalidatorImpl.isValidSpecialist("dummyTest");
		assertEquals(false, status);
		
	
	}
	
	/* Test case for  isValidSpecialist method for IO Exception*/
	@SuppressWarnings("unchecked")
	@Test
	public void isValidSpecialistIOExceptionTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		when((prop.getProperty(Resources.LAST_TIME_PROPERTY_NAME))).thenReturn("1");
		doThrow(new IOException()).when(prop).load(input);
		final Boolean status = betterDoctorSpecialityvalidatorImpl.isValidSpecialist("dummy");
		assertEquals(true, status);
		
	
	}
	
	/* Test case for  isValidSpecialist method for IO Exception when a null list is returned from endpoint*/
	@SuppressWarnings("unchecked")
	@Test
	public void isValidSpecialistIOExceptionNullTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		when((prop.getProperty(Resources.LAST_TIME_PROPERTY_NAME))).thenReturn("1");
		doThrow(new IOException()).when(prop).load(input);
		when(betterDoctorAPICaller.getSpecialities()).thenReturn(NULL_LIST);
		final Boolean status = betterDoctorSpecialityvalidatorImpl.isValidSpecialist("dummy");
		assertEquals(false, status);
	}
	
	/* Test case for  isValidSpecialist method for IO Exception with wrong specialist*/
	@SuppressWarnings("unchecked")
	@Test
	public void isValidSpecialistNullResponseTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		when((prop.getProperty(Resources.LAST_TIME_PROPERTY_NAME))).thenReturn("1");
		doThrow(new IOException()).when(prop).load(input);
		final Boolean status = betterDoctorSpecialityvalidatorImpl.isValidSpecialist("dummyTest");
		assertEquals(false, status);
		
	
	}
	
	/*Test case for  isValidSpecialist method  when a null list is returned from endpoint*/
	@SuppressWarnings("unchecked")
	@Test
	public void isValidSpecialistNullTest() throws Exception {
		betterDoctorSpecialityvalidatorImpl=new BetterDoctorSpecialityvalidatorImpl();
		when((prop.getProperty(Resources.LAST_TIME_PROPERTY_NAME))).thenReturn("1");
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.PROPERTIES_FILE_PATH).thenReturn(file);
		whenNew(File.class).withParameterTypes(String.class).withArguments(Resources.SPECIALIST_FILE_PATH).thenReturn(file);
		when(betterDoctorAPICaller.getSpecialities()).thenReturn(NULL_LIST);
		whenNew(FileWriter.class).withParameterTypes(File.class).withArguments(file).thenReturn( writer);
		doNothing().when(writer).write("TEST");
		doNothing().when(writer).flush();
		final Boolean status = betterDoctorSpecialityvalidatorImpl.isValidSpecialist("dummy");
		assertEquals(false, status);
	}

}
