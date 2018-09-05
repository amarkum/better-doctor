package com.codeunits.betterdoctor;

import java.util.List;
import java.util.Scanner;

import com.codeunits.betterdoctor.constants.Resources;
import com.codeunits.betterdoctor.datamodel.Doctor;
import org.apache.log4j.Logger;

/**
 * This class is used to get the input data from the user and display the list
 * of doctors
 */
public class DisplayDoctors {
    final static Logger log = Logger.getLogger(DisplayDoctors.class);

    public static void main(String[] args) {
        final Scanner inputs = new Scanner(System.in, Resources.ENCODING);

        try {

            final BetterDoctorZipCodeValidator betterDoctorZipCodeValidator = new BetterDoctorZipCodeValidatorImpl();
            final BetterDoctorSpecialityValidator betterDoctorSpecialityValidator = new BetterDoctorSpecialityvalidatorImpl();
            final BetterDoctorAPICaller betterDoctorAPICaller = new BetterDoctorAPICallerImpl();
            log.info("Enter the zipcode to search for the doctor:");
            final String zipcode = inputs.next();
            log.info("Enter the speciality of the doctor:");
            final String speciality = inputs.next();
            final Boolean isValidSpecialist = betterDoctorSpecialityValidator.isValidSpecialist(speciality);
            final Boolean isValidZipcode = betterDoctorZipCodeValidator.isValidZipCode(zipcode);

            if (isValidSpecialist && isValidZipcode) {
                String doctorStreet = null;

                String doctorCity = null;

                int zip = 0;

                final List<Doctor> doctors = betterDoctorAPICaller.getDoctors(Integer.parseInt(zipcode), speciality);
                if (!(doctors.isEmpty())) {
                    for (int i = 0; i < doctors.size(); i++) {
                        final String doctorfirstName = doctors.get(i).getFirstName();
                        final String doctorlastName = doctors.get(i).getLastName();
                        final String doctorGender = doctors.get(i).getGender().toUpperCase();
                        final String doctorBackground = doctors.get(i).getBackground();

                        if (doctors.get(i).getPractices() != null) {
                            doctorStreet = doctors.get(i).getPractices().get(0).getStreet();
                            doctorCity = doctors.get(i).getPractices().get(0).getCity();
                            zip = doctors.get(i).getPractices().get(0).getZip();
                        } else {
                            log.info(speciality.toUpperCase() + " doctors not available in the specified zipcode - "
                                    + zipcode);
                            System.exit(1);
                        }
                        log.info(i + 1 + ".) Dr. " + doctorfirstName + " " + doctorlastName + "\n" + "Gender:"
                                + doctorGender + "\nSpecialist:" + speciality.toUpperCase() + "\n" + "Information: "
                                + doctorBackground.replaceAll("\n", "") + "\n" + "Address:" + doctorStreet + ","
                                + doctorCity + ",Zip:" + zip);
                        log.info("\n");
                    }
                } else {
                    log.info("Information Not Avaliable");
                    System.exit(1);
                }
            } else if (!(isValidZipcode) && !(isValidSpecialist)) {
                log.error("Zipcode and Specialist entered are not valid");
                System.exit(1);
            } else if (isValidSpecialist) {
                log.error("Zipcode entered is not valid");
                System.exit(1);
            } else if (isValidZipcode) {
                log.error("Specialist entered is not valid");
                System.exit(1);
            }
        } finally {
            inputs.close();
        }
    }
}
