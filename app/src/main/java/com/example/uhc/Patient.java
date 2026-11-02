
package com.example.uhc;

public class Patient {
    private final String id;
    private final String name;
    private final int age;
    private final String gender;
    private final String bloodGroup;
    private final String medicalHistory;
    private final String allergies;

    // New fields for image optimization assignment
    private final String bedNumber;
    private final String profileImageUrl;

    public Patient(String id, String name, int age, String gender,
                   String bloodGroup, String medicalHistory, String allergies,
                   String bedNumber, String profileImageUrl) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;

        // Store the patient's bed number and profile image URL
        this.bedNumber = bedNumber;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getBloodGroup() { return bloodGroup; }
    public String getMedicalHistory() { return medicalHistory; }
    public String getAllergies() { return allergies; }

    // New getters
    public String getBedNumber() { return bedNumber; }
    public String getProfileImageUrl() { return profileImageUrl; }
}
