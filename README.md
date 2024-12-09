# University Management System

## Overview

This project is a **Java-based University Management System** that facilitates administrative and student-related operations.
It includes functionalities like managing courses, departments, students, and GPA calculations.

### Features

#### 1. **Admin Dashboard**

- Enables administrators to add, update, and delete student records efficiently.
- Allows management of departments and courses, including adding new entries and updating existing ones.
- Offers secure authentication to ensure only authorized personnel access administrative functionalities.

#### 2. **Student Dashboard**

- Provides a GPA calculator for students to track their academic progress accurately.
- Displays detailed academic information, including personal data, current courses, and completed courses.
- Features an intuitive design for easy navigation and user interaction.

#### 3. **Course Suggestion System**

- Dynamically suggests courses for students based on their GPA, completed terms, and grades.
- Utilizes external APIs to recommend relevant courses tailored to the student’s academic progress.
- Aims to assist students in planning their academic journey and improving performance.

#### 4. **Splash Screen and Login System**

- Includes a visually appealing splash screen to enhance the user experience during application startup.
- Implements a secure login system with role-based access for admins and students.
- Ensures data security by validating credentials through an integrated authentication mechanism.

#### 5. **Database Integration with `MySQL`**

- Stores and manages all data, such as student records, course details, and department information, in a structured manner.
- Supports dynamic updates and real-time data synchronization across the system.
- Enhances data security and prevents vulnerabilities with parameterized SQL queries.

---

## Requirements

1. **Java**: Install JDK 11 or higher.
2. **MySQL Server**: Ensure MySQL is installed and running on your system or "PhpMyAdmin" with Xampp.
3. **Maven** for dependency management.
4. Any modern IDE for Java (e.g., IntelliJ IDEA, Eclipse).

---

## How to Set Up and Run the Project

### 1. Clone or Download the Project

- If you downloaded the zip, extract it to your desired location.

### 2. Import the Project in Your IDE

- Open your Java IDE (e.g., IntelliJ IDEA).
- Import the project as a Maven project.

### 3. Set Up the Database

- Install MySQL Server if not already installed.
- Create a new database called `universitysystem.sql` in PhpMyAdmin or HeidiSql.

- Create TABLES below :

- **authentication TABLE:**

  ```sql
  CREATE TABLE authentication (
      id INT(11) NOT NULL,
      password VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
      role VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
      PRIMARY KEY (id) USING BTREE
  )
  COLLATE='utf8mb4_general_ci'
  ENGINE=InnoDB;
  CREATE TABLE departments (
      departmentId INT(11) NOT NULL,
      departmentName VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
      PRIMARY KEY (departmentId) USING BTREE
  )
  COLLATE='utf8mb4_general_ci'
  ENGINE=InnoDB;

  CREATE TABLE courses (
    courseId INT(11) NOT NULL,
    courseName VARCHAR(50) NOT NULL DEFAULT '' COLLATE 'utf8mb4_general_ci',
    courseDepartment INT(11) NOT NULL DEFAULT '0',
    grade INT(11) NOT NULL DEFAULT 0,  -- Added grade column
    PRIMARY KEY (courseId) USING BTREE,
    INDEX courseDepartment (courseDepartment) USING BTREE,
    CONSTRAINT courseDepartment FOREIGN KEY (courseDepartment) REFERENCES departments (departmentId) ON UPDATE CASCADE ON DELETE NO ACTION
  )
  COLLATE='utf8mb4_general_ci'
  ENGINE=InnoDB;


  CREATE TABLE studentcourses (
      studentId INT(11) NOT NULL,
      courseId INT(11) NOT NULL,
      examDate DATE NOT NULL,
      PRIMARY KEY (studentId, courseId) USING BTREE,
      INDEX courseId (courseId) USING BTREE,
      CONSTRAINT courseId FOREIGN KEY (courseId) REFERENCES courses (courseId) ON UPDATE CASCADE ON DELETE CASCADE,
      CONSTRAINT studentId FOREIGN KEY (studentId) REFERENCES students (studentId) ON UPDATE CASCADE ON DELETE CASCADE
  )
  COLLATE='utf8mb4_general_ci'
  ENGINE=InnoDB;


  CREATE TABLE students (
      studentId INT(11) NOT NULL,
      studentName VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
      departmentId INT(11) NULL DEFAULT NULL,
      level INT(11) NULL DEFAULT NULL,
      gpa FLOAT NULL DEFAULT NULL,
      nationalId VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
      national VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
      birthDate DATE NULL DEFAULT NULL,
      gender VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
      PRIMARY KEY (studentId) USING BTREE,
      INDEX department (departmentId) USING BTREE,
      CONSTRAINT Id FOREIGN KEY (studentId) REFERENCES authentication (id) ON UPDATE CASCADE ON DELETE CASCADE,
      CONSTRAINT department FOREIGN KEY (departmentId) REFERENCES departments (departmentId) ON UPDATE CASCADE ON DELETE SET NULL
  )
  COLLATE='utf8mb4_general_ci'
  ENGINE=InnoDB;
  ```

### 4. Update Database Configuration

- Locate the `DatabaseHelper.java` file in `src/main/java/com/example/project1`.
- Update the database credentials (username, password, and database URL) to match your local setup.

### 5. Run the Application

- In your IDE, locate the main class (e.g., `SplashScreen.java`) and run it.
- The application should now launch, starting with the Splash Screen then go to LoginPage.

---

### 6. **Course Suggestion System**

- Dynamically suggests courses for students based on their GPA, completed terms, and grades.
- Utilizes a **hosted API** implemented as a JSON file to fetch course suggestions.  
  The API is accessible at:
  ```plaintext
  https://adhamkhaled74.github.io/host_api/temp.json
  ```

## Key Files and Directories

- `src/main/java/com/example/project1/`: Contains all Java source files.
- `src/main/resources/`: Contains application resources (images, FXML files, and stylesheets).
