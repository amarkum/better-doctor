package com.codeunits.betterdoctor;

import java.util.List;

import com.codeunits.betterdoctor.datamodel.Doctor;

/**
 * This interface is for using the public BetterDoctorAPI
 */
public interface BetterDoctorAPICaller {

    public String getResponse(String uri);

    /**
     * This method takes zip & specialty as input and returns List of Doctors
     *
     * @param zip
     * @param speciality
     * @return List<Doctor>
     */

    public List<Doctor> getDoctors(int zip, String speciality);

    /**
     * This method accepts JSON & zip and returns List of Doctors
     *
     * @param response,
     * @param zip
     * @return List<Doctor>
     */

    public List<Doctor> parseDoctors(String response, int zip);

    /**
     * This method fetches JSON for total specialties and returns them as List.
     * BetterDoctor
     *
     * @return List<String>
     */

    public List<String> getSpecialities();

}
