package com.multiscreen.siams.project6;

public class Students {

    private String Education;

    private String FirstName;

    private String Gender;

    private String LastName;

    private String MiddleName;

    private String Residence;

    private String SrudentNr;

    public Students(String education, String firstName, String gender, String lastName, String middleName, String residence, String srudentNr) {
        Education = education;
        FirstName = firstName;
        Gender = gender;
        LastName = lastName;
        MiddleName = middleName;
        Residence = residence;
        SrudentNr = srudentNr;
    }

    public String getEducation() {
        return Education;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getGender() {
        return Gender;
    }

    public String getLastName() {
        return LastName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public String getResidence() {
        return Residence;
    }

    public String getSrudentNr() {
        return SrudentNr;
    }
}
