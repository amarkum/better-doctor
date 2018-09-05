package com.codeunits.betterdoctor;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.codeunits.betterdoctor.constants.Resources;

/**
 * This class validates the zip code entered by user
 */
public class BetterDoctorZipCodeValidatorImpl implements BetterDoctorZipCodeValidator {


    final static Logger log = Logger.getLogger(BetterDoctorZipCodeValidatorImpl.class);

    /**
     * This method validates whether the zip entered is valid by hitting the ZIPCODEAPI
     *
     * @param zip
     * @return Boolean true if valid ,false if invalid
     */
    public boolean isValidZipCode(final String zip) {

        final StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(Resources.ZIPCODE_URL).append(zip);
        final String uri = uriBuilder.toString();
        try {
            final URL url = new URL(uri);
            final String response = IOUtils.toString(url.openConnection().getInputStream(), Resources.ENCODING);
            final JSONObject zipJsonObject = new JSONObject(response);
            log.info("Valid City Found : " + zipJsonObject.getString("city"));

        } catch (final JSONException e) {
            return false;
        } catch (final IOException e) {
            return false;

        }
        return true;

    }
}
