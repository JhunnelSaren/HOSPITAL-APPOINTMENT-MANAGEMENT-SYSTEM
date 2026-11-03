package com.example.hospitalappointmentapp;

public class Appointment {
    private String doctorName;
    private String specialty;
    private String date;
    private String time;
    private String patientName;

    public Appointment(String doctorName, String specialty, String date, String time) {
        this.doctorName = doctorName;
        this.specialty = specialty;
        this.date = date;
        this.time = time;
    }

    public String getDoctorName() { return doctorName; }
    public String getSpecialty() { return specialty; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
}