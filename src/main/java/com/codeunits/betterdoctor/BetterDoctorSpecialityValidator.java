package com.codeunits.betterdoctor;

/**
 * This interface to verify the specialty
 *
 */
public interface BetterDoctorSpecialityValidator {

    /**
     * This method to verify the specialty  entered by the user
     *
     * @param specialty
     * @return Boolean
     */
    public Boolean isValidSpecialist(final String speciality);

}
