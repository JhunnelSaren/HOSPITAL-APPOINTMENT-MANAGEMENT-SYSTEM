# Client-Side Module Mapping

**Project Name:** Hospital Appointment Management System (MedConnect)  
**Document:** Client-Side Architecture and Patient Module Specification  
**Client Lead:** SAREN, JHUNNEL LUNA (Member 1 - Project Leader)  

---

## 1. Introduction to the Client Side

### What the Client Side Means
In our Hospital Appointment Management System, the **Client Side** refers to the native Android mobile application that patients, doctors, and nurses interact with on their Android smartphones. The client application provides an intuitive graphical user interface (GUI) designed to allow users to perform actions such as registering an account, logging in, searching for medical departments and doctors, viewing available schedules, booking appointments, viewing appointment history, and managing profiles.

### How the Android Java Application Works
The Android application is built using **Android Studio** and programmed in **Java**. Its core operational principles include:
1. **User Interface (UI Layouts):** Built using XML layout files (`activity_main.xml`, `activity_register.xml`, `activity_patient_dashboard.xml`, etc.) using Android UI components such as `TextView`, `EditText`, `Button`, `RecyclerView`, `CardView`, and `Toast`.
2. **Activity & Event Handling:** Java Activity classes (`MainActivity`, `RegisterActivity`, `VerificationActivity`, `PatientDashboardActivity`, etc.) handle user interactions, form validation, and button click listeners.
3. **ViewBinding:** The app utilizes Android's ViewBinding feature to directly reference XML views safely without calling `findViewById()`.
4. **Asynchronous HTTP Communication:** The app relies on the **Volley** library to send HTTP GET and POST requests asynchronously to the Node.js server.
5. **Response Processing:** Server responses are returned in **JSON** format. The Android client parses these JSON objects/arrays to update the user interface dynamically or notify the user of success or failure via `Toast` alerts.

---

## 2. Client-Side Patient Modules & Screen Functionality

Our group project divides responsibilities across three members. Member 1 (SAREN, JHUNNEL LUNA) handles the Android Client Side for all Patient-facing screens and workflows. Below are the 10 required patient modules:

### Module 1: Patient Registration
* **Screen Name:** `RegisterActivity`
* **Description:** Allows new patients to create an account by providing their full name, email address, password, and role selection ("Patient").
* **Screen Actions:**
  * User inputs registration details into input fields.
  * Validates that no fields are left empty.
  * Sends a `POST` request to `/api/register`.
  * On success, navigates to `VerificationActivity` passing the user's email.

### Module 2: Patient Login
* **Screen Name:** `MainActivity`
* **Description:** Entry point of the application where returning users enter their registered email and password to log into the system.
* **Screen Actions:**
  * User enters email and password.
  * Validates non-empty fields.
  * Sends a `POST` request to `/api/login`.
  * On success, parses the user object and role (`Patient`, `Doctor`, or `Nurse`), saves session details, and redirects the patient to `PatientDashboardActivity`.

### Module 3: Patient Dashboard
* **Screen Name:** `PatientDashboardActivity`
* **Description:** The central hub for authenticated patients after logging in.
* **Screen Actions:**
  * Displays a personalized welcome message.
  * Shows quick summary cards (Upcoming Appointments, Quick Booking floating action button, Navigation Menu to Doctor Listing, Departments, History, Profile).
  * Fetches the patient's active and upcoming appointments from `/api/appointments/patient/:patient_id`.

### Module 4: Doctor Listing
* **Screen Name:** `DoctorListingActivity` / View Component
* **Description:** Displays a searchable list of doctors practicing at the hospital.
* **Screen Actions:**
  * Sends a `GET` request to `/api/doctors`.
  * Displays doctor names, specialties, qualifications, and department affiliations inside a `RecyclerView`.
  * Allows filtering or searching doctors by name or medical specialty.
  * Clicking on a doctor redirects the patient to view that doctor's schedule.

### Module 5: Department Viewing
* **Screen Name:** `DepartmentViewingActivity` / View Component
* **Description:** Displays all medical departments available in the hospital (e.g., Cardiology, Pediatrics, Orthopedics, General Medicine).
* **Screen Actions:**
  * Sends a `GET` request to `/api/departments`.
  * Renders department cards with descriptions and available specialists.
  * Selecting a department filters the doctor listing to show doctors belonging to that specific department.

### Module 6: Available Doctor Schedules
* **Screen Name:** `DoctorScheduleActivity` / View Component
* **Description:** Displays available consultation days, dates, and time slots for a selected doctor.
* **Screen Actions:**
  * Sends a `GET` request to `/api/schedules/doctor/:doctor_id`.
  * Parses available dates and open time slots (e.g., "Monday, 9:00 AM - 10:00 AM").
  * Displays slots clearly, highlighting booked vs. available slots.

### Module 7: Appointment Booking
* **Screen Name:** `BookAppointmentActivity` / Booking Form Dialog
* **Description:** Form where a patient selects an available schedule slot, inputs a reason for visit/chief complaint, and submits a booking request.
* **Screen Actions:**
  * Accepts doctor ID, date, time slot, and reason for visit.
  * Validates selected slot.
  * Sends a `POST` request to `/api/appointments/book`.
  * Receives confirmation JSON and returns user to `PatientDashboardActivity` with a success notification.

### Module 8: Appointment Cancellation
* **Screen Name:** Cancellation Confirmation Dialog in `PatientDashboardActivity` or `AppointmentHistoryActivity`
* **Description:** Allows a patient to cancel an upcoming appointment prior to consultation time.
* **Screen Actions:**
  * Prompts the patient with a confirmation alert dialog ("Are you sure you want to cancel this appointment?").
  * Sends a `PUT` or `POST` request to `/api/appointments/cancel` with `appointment_id`.
  * On success, updates the appointment status tag to "Cancelled" in the UI.

### Module 9: Appointment History
* **Screen Name:** `AppointmentHistoryActivity` / View Component
* **Description:** Displays a full list of past, completed, cancelled, and upcoming appointments.
* **Screen Actions:**
  * Sends a `GET` request to `/api/appointments/history/:patient_id`.
  * Displays appointment date, time, doctor name, department, status (Pending, Completed, Cancelled), and consultation notes if available.

### Module 10: Patient Profile
* **Screen Name:** `PatientProfileActivity` / View Component
* **Description:** Displays and allows updating of patient personal details (Full Name, Contact Number, Address, Emergency Contact).
* **Screen Actions:**
  * Sends a `GET` request to `/api/patients/profile/:patient_id` to populate fields.
  * Allows updating editable fields and submitting a `PUT` request to `/api/patients/profile/update`.
  * Provides a "Logout" button to clear user session and return to `MainActivity`.

---

## 3. Screen Navigation Flow

```
+-------------------+       +-----------------------+       +------------------------+
|   MainActivity    | ----> |   RegisterActivity    | ----> |  VerificationActivity  |
|     (Login)       | <---- |      (Sign Up)        |       |     (OTP/Code)         |
+-------------------+       +-----------------------+       +------------------------+
          |
          | (On Successful Login)
          v
+------------------------------------------------------------------------------------+
|                             PatientDashboardActivity                               |
|  - View Upcoming Appointments                                                      |
|  - Access Quick Navigation Menu                                                    |
+------------------------------------------------------------------------------------+
     |                        |                          |                       |
     v                        v                          v                       v
+------------------+  +-------------------+   +--------------------+  +------------------+
| Department View  |  |  Doctor Listing   |   | Appointment History|  | Patient Profile  |
+------------------+  +-------------------+   +--------------------+  +------------------+
         |                      |                                          |
         +----------+-----------+                                          v
                    |                                                [ Logout ] ---> Login
                    v
         +----------------------+
         | Available Schedules  |
         +----------------------+
                    |
                    v
         +----------------------+
         | Book Appointment Form|
         +----------------------+
                    |
                    v
         [ Server Confirmation ] ---> Back to Patient Dashboard
```

---

## 4. Client-Server Communication & Data Exchange

The Android app communicates with the backend server via HTTP requests formatted as JSON payloads.

| Screen / Feature | HTTP Method | Endpoint | Sent Information (Request Payload) | Received Information (Response Payload) |
| :--- | :--- | :--- | :--- | :--- |
| **Patient Registration** | `POST` | `/api/register` | `{ "full_name": "John Doe", "email": "john@example.com", "password": "pass", "role": "Patient" }` | `{ "success": true, "message": "Registration successful" }` |
| **Email Verification** | `POST` | `/api/verify` | `{ "email": "john@example.com", "code": "123456" }` | `{ "success": true, "message": "Account verified successfully" }` |
| **Patient Login** | `POST` | `/api/login` | `{ "email": "john@example.com", "password": "pass" }` | `{ "success": true, "user": { "id": 1, "name": "John Doe", "email": "john@example.com", "role": "Patient" } }` |
| **Doctor Listing** | `GET` | `/api/doctors` | None (Query parameters optional: `?department_id=2`) | `{ "success": true, "doctors": [ { "id": 5, "name": "Dr. Smith", "specialty": "Cardiology", "department": "Heart Center" } ] }` |
| **Department Viewing** | `GET` | `/api/departments` | None | `{ "success": true, "departments": [ { "id": 2, "name": "Cardiology", "description": "Heart care unit" } ] }` |
| **Available Schedules** | `GET` | `/api/schedules/doctor/:id` | Doctor ID in URL path | `{ "success": true, "schedules": [ { "schedule_id": 10, "date": "2026-11-10", "time_slot": "09:00 AM - 10:00 AM", "status": "Available" } ] }` |
| **Book Appointment** | `POST` | `/api/appointments/book` | `{ "patient_id": 1, "doctor_id": 5, "schedule_id": 10, "reason": "Chest pain routine check" }` | `{ "success": true, "message": "Appointment booked successfully", "appointment_id": 101 }` |
| **Cancel Appointment**| `PUT` | `/api/appointments/cancel` | `{ "appointment_id": 101, "patient_id": 1, "reason": "Personal conflict" }` | `{ "success": true, "message": "Appointment cancelled successfully" }` |
| **Appointment History**| `GET` | `/api/appointments/history/:patient_id` | Patient ID in URL path | `{ "success": true, "appointments": [ { "id": 101, "doctor": "Dr. Smith", "date": "2026-11-10", "status": "Scheduled" } ] }` |
| **Patient Profile** | `GET` / `PUT` | `/api/patients/profile/:patient_id` | Updated fields on PUT `{ "phone": "09123456789", "address": "City" }` | `{ "success": true, "profile": { "name": "John Doe", "email": "john@example.com", "phone": "09123456789" } }` |

### How Communication Works in Code (Volley)
* **Base URL:** `http://localhost:3000/api`
* **Local Development Note:** When testing the app on an Android Emulator or physical device connected via USB, port forwarding is established using:
  ```bash
  adb reverse tcp:3000 tcp:3000
  ```
  This allows the Android app to connect to `http://localhost:3000` directly.
* **Request Handler:** `Volley.newRequestQueue(context)` executes asynchronous `JsonObjectRequest` calls so that network operations do not freeze the UI thread.

---

## 5. Success and Error Message Display

To keep the user interface clean and user-friendly, feedback is handled systematically:

1. **Client-Side Input Validation:**
   * Before sending requests, the app checks if required `EditText` fields are blank or improperly formatted.
   * If validation fails, an immediate Toast message is displayed (e.g., `"Please fill all fields"` or `"Enter a 6-digit code"`).

2. **Server Success Responses:**
   * When the server returns `"success": true`, the app displays a confirmation Toast message (e.g., `Toast.makeText(context, "Appointment booked successfully!", Toast.LENGTH_SHORT).show()`).
   * The app then navigates to the appropriate screen or refreshes the current list.

3. **Server Error Responses:**
   * If the server returns `"success": false` with a custom message (e.g., `"Schedule slot no longer available"` or `"Invalid email or password"`), the app extracts `response.getString("message")` and shows it in a Toast alert.

4. **Network & Server Down Errors:**
   * If the Volley `Response.ErrorListener` triggers (e.g., timeout, connection refused, 500 error), the app handles the error gracefully:
   ```java
   error -> {
       String message = "Server Error. Please check your connection.";
       if (error.networkResponse != null && error.networkResponse.data != null) {
           message = new String(error.networkResponse.data);
       }
       Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
   }
   ```

---

## 6. Text-Based Flowchart: Patient Appointment Booking

```
[ START: Patient opens MedConnect App ]
                   |
                   v
       [ Is Patient Logged In? ]
          /                 \
        NO                   YES
        /                     \
       v                       v
[ Open MainActivity ]    [ Patient Dashboard ]
(Enter Email & Password)       |
       |                       v
[ Send POST /api/login ] ---> [ Select "Book Appointment" ]
       |                       |
       +---> Login Verified?   v
                /      \     [ Select Department ]
              NO        YES    |
              /          \     v
   [ Toast: Failed ]      +->[ Select Doctor ]
                               |
                               v
                     [ View Available Schedules ]
                               |
                               v
                     [ Select Open Date & Time Slot ]
                               |
                               v
                     [ Fill Reason for Visit ]
                               |
                               v
                     [ Click "Submit Booking" ]
                               |
                               v
                     [ Send POST /api/appointments/book ]
                               |
                   +-----------+-----------+
                   |                       |
            [ Slot Available? ]     [ Slot Taken? ]
                   |                       |
                  YES                      NO
                   |                       |
                   v                       v
        [ Save in MySQL DB ]    [ Server Error Response ]
                   |                       |
                   v                       v
        [ Return Success JSON ]  [ Return Error JSON ]
                   |                       |
                   v                       v
        [ Toast: "Booked!" ]    [ Toast: "Slot Taken!" ]
                   |                       |
                   v                       v
        [ Redirect Dashboard ]   [ Choose Different Slot ]
                   |
                   v
         [ END: Booking Done ]
```

---
*Document prepared by Member 1 (SAREN, JHUNNEL LUNA) for Hospital Appointment Management System documentation.*
