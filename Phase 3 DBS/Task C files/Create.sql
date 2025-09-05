CREATE DATABASE Vitals;

use Vitals;

CREATE TABLE Insurance (
    Insurance_ID INT PRIMARY KEY,
    Policy_Number VARCHAR(50) UNIQUE,
    Provider VARCHAR(255),
    Plan_Type TEXT,
    Coverage_End_Date DATE,
    Policy_Holder VARCHAR(255)
);

CREATE TABLE Staff (
    Staff_ID INT PRIMARY KEY,
    Name VARCHAR(255),
    Position ENUM('Doctor', 'Nurse', 'Receptionist', 'Other'),
    Specialization VARCHAR(255),
    Phone_Number VARCHAR(15),
    Staff_Pass INT
);

CREATE TABLE Patient(
    Patient_ID INT PRIMARY KEY, -- New patients are assigned the next avilable int, if this behaivor is undesirable, remove auto increment
    SSN CHAR(11) UNIQUE, -- Format (XXX-XX-XXXXX) -> why is this a char, not a varchar?
    DOB DATE,
    Gender ENUM('Male', 'Female', 'Other'),
    Phone_Number VARCHAR(15), -- (Format: +Contry Code - Number)
    Emergency_Contact VARCHAR(15), -- Same as above
    Address TEXT,
    Email VARCHAR(255), -- -> no format specification? No validity check?
    Medical_History TEXT,
    Billing_Info TEXT,
    Full_Name VARCHAR(255),
    Insurance_ID INT, -- Domain, [0, INT_MAX] -> We could use an absolute value or bounds check to reject negative values. NULL value indicates error with insurance. remember to enforce not-null when creating records. 
    Patient_Pass INT,
    FOREIGN KEY (Insurance_ID) REFERENCES Insurance(Insurance_ID)
);

CREATE TABLE Appointments (
    Apnt_ID INT PRIMARY KEY,
    Patient_ID INT,
    Date_Time DATETIME,
    Apnt_Type ENUM('Consultation', 'Follow-up', 'Surgery', 'New Patient', 'Emergency', 'Check-up', 'Other'),
    Status ENUM('Scheduled', 'Completed', 'Cancelled', 'Rescheduled', 'No-show'),
    Duration INT, -- int var, represents minutes. User inout should be restricted to 30 minute intervals, evenly dividing hours
    Insurance_ID INT,
    Note TEXT,
    Staff_ID INT UNIQUE,
    FOREIGN KEY (Patient_ID) REFERENCES Patient(Patient_ID),
    FOREIGN KEY (Insurance_ID) REFERENCES Insurance(Insurance_ID),
    FOREIGN KEY (Staff_ID) REFERENCES Staff(Staff_ID)
);

