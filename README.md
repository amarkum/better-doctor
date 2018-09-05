Display Best Doctors based on User Input using Public API</br></br><center>     ![BetterDoctorAPI](https://www.3scale.net/wp-content/uploads/2015/10/betterdoctorlogo.png) </center>
===================

## This project fetches List of Doctors with a specific Specialty and Zipcode.

 ### Steps to Run the Project:

- Clone the master branch source code to your local from the Repository and get into it.

    	$ git clone https://github.com/amarkum/better-doctor.git


- Run the command: `mvn clean install` to build the project. </br>

    	$ mvn clean install

- After a successful `BUILD`, Run the Application with below command:</br>

    	 $ mvn exec:java -Dexec.mainClass=com.codeunits.betterdoctor.DisplayDoctors
-	User will be prompted to enter Zipcode (5 digit numeric zipcode of the U.S region) after which user will be prompted to enter the Specialist uid of the doctor.
-	After the input validation the List of doctors is displayed on the console.

     
