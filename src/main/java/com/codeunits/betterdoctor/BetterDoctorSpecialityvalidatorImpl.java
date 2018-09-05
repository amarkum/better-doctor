package com.codeunits.betterdoctor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codeunits.betterdoctor.constants.Resources;

/**
 * This class implements the BetterDoctorSpecialityValidator to verify the
 * specialty entered by user
 */
public class BetterDoctorSpecialityvalidatorImpl implements BetterDoctorSpecialityValidator {

    final static Logger log = Logger.getLogger(BetterDoctorSpecialityvalidatorImpl.class);

    Properties properties = new Properties();

    BufferedReader bufferedReader = null;

    List<String> specialities = new ArrayList<String>();

    BetterDoctorAPICaller betterDoctorAPICaller = new BetterDoctorAPICallerImpl();

    /**
     * This method validates the speciality
     *
     * @param Speciality
     * @return Boolean true if valid ,false if invalid
     */

    public Boolean isValidSpecialist(final String speciality) {

        InputStream input = null;

        long currentTime;

        long lastTime;

        try {
            input = new FileInputStream(Resources.PROPERTIES_FILE_PATH);
            properties.load(input);
            lastTime = Long.parseLong(properties.getProperty(Resources.LAST_TIME_PROPERTY_NAME));

            currentTime = System.currentTimeMillis();
            if (currentTime - lastTime > Resources.DAY_IN_SECONDS) {
                updateTimeStamp(currentTime);
                specialities = betterDoctorAPICaller.getSpecialities();
                createSpecilistFile(specialities);
            } else {
                specialities = readJSONFile(Resources.SPECIALIST_FILE_PATH);
            }
            if (specialities != null) {
                final Iterator<String> specialityIterator = specialities.iterator();
                while (specialityIterator.hasNext()) {
                    if (specialityIterator.next().equals(speciality.toLowerCase())) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } catch (final IOException e) {
            log.warn("Exception occured loading in properties file" + Resources.PROPERTIES_FILE_PATH);
            specialities = betterDoctorAPICaller.getSpecialities();
            if (specialities != null) {
                final Iterator<String> specialityIterator = specialities.iterator();
                while (specialityIterator.hasNext()) {
                    if (specialityIterator.next().equals(speciality.toLowerCase())) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (final IOException e) {
                }
            }
        }
        return false;
    }

    /**
     * This method read the JSON file.
     *
     * @param specialistfilepath
     * @return List<String>
     */
    public List<String> readJSONFile(final String specialistfilepath) {
        String jsonData = null;
        try {
            bufferedReader = new BufferedReader
                    (new InputStreamReader(new FileInputStream(specialistfilepath), Resources.ENCODING));
            final StringBuilder sb = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
            jsonData = sb.toString();
            final JSONObject jsonObject = new JSONObject(jsonData);
            final JSONArray arr = new JSONArray(jsonObject.getJSONArray("Specialist").toString());
            for (int i = 0; i < arr.length(); i++) {
                final String speciality = arr.getJSONObject(i).getString("uid");
                specialities.add(speciality);
            }
        } catch (final IOException e) {
            specialities = betterDoctorAPICaller.getSpecialities();
            return specialities;
        } catch (final JSONException e) {
            specialities = betterDoctorAPICaller.getSpecialities();
            return specialities;
        }

        return specialities;
    }

    /**
     * This method stores the response given from the BetterDoctorAPI to a file.
     *
     * @param specialities
     */
    public void createSpecilistFile(final List<String> specialities) {
        Writer writer = null;
        try {

            final File file = new File(Resources.SPECIALIST_FILE_PATH);
            if (file.exists()) {
                if (file.delete()) {
                    log.debug("File deleted successfully" + Resources.SPECIALIST_FILE_PATH);
                }
            }
            if (file.createNewFile()) {
                final JSONObject specialistDetails = new JSONObject();
                final JSONArray jsonArray = new JSONArray();
                final Iterator<String> specialityIterator = specialities.iterator();
                while (specialityIterator.hasNext()) {
                    final JSONObject specialistJSON = new JSONObject();
                    specialistJSON.put("uid", specialityIterator.next());
                    jsonArray.put(specialistJSON);
                }
                specialistDetails.put("Specialist", jsonArray);
                writer = new FileWriter(file);
                writer.write(specialistDetails.toString());
                writer.flush();
            } else {
                log.debug("File not created" + Resources.SPECIALIST_FILE_PATH);
            }
        } catch (final IOException e) {
            log.warn("Cannot write to the file:" + Resources.SPECIALIST_FILE_PATH);
        } catch (final JSONException e) {
            log.warn("Json object cannot be parsed for writing to the specialist file");
        } catch (final NullPointerException e) {
            log.warn("List is null");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {

                }
            }
        }

    }

    /**
     * This method captures the last accessed time in milliseconds to the properties
     * file.
     *
     * @param specialitiesList
     */

    public void updateTimeStamp(final long currentTime) {
        OutputStream output = null;
        try {
            final File file = new File(Resources.PROPERTIES_FILE_PATH);
            output = new FileOutputStream(file);
            properties.setProperty(Resources.LAST_TIME_PROPERTY_NAME, String.valueOf(currentTime));
            properties.store(output, null);
        } catch (final FileNotFoundException e) {
            log.warn(Resources.PROPERTIES_FILE_PATH + ":File Not found");
        } catch (final IOException e) {
            log.warn("OutputStream closed writing to the properties file");
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (final IOException e) {

                }
            }
        }

    }

}
