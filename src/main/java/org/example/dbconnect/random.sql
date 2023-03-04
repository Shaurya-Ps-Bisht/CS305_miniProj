


CREATE TABLE IF NOT EXISTS courseCatalog(
  courseID VARCHAR NOT NULL,
  courseName VARCHAR NOT NULL,
  ltps varchar NOT NULL,
  creds numeric not null,
  csb varchar,
  mcb varchar,
  eeb varchar,
  chb varchar,
  mmb varchar,
  ceb varchar,
  meb varchar,
  PRIMARY KEY(courseID)
);



CREATE TABLE IF NOT EXISTS students (
  studentID VARCHAR NOT NULL,
  studentName VARCHAR NOT NULL,
  contactNo VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  PRIMARY KEY(studentID)
);

CREATE TABLE IF NOT EXISTS instructors (
  instructorID VARCHAR NOT NULL,
  instructorName VARCHAR NOT NULL,
  contactNo VARCHAR NOT NULL,
  password varchar NOT NULL,
  PRIMARY KEY(instructorID)
);

CREATE TABLE IF NOT EXISTS coursesOffered (
  courseID VARCHAR NOT NULL,
  instructorID VARCHAR NOT NULL,
  minCGPA numeric,
  periodOffered VARCHAR NOT NULL,
  FOREIGN KEY (courseID) REFERENCES courseCatalog(courseID),
  FOREIGN KEY (instructorID) REFERENCES instructors(instructorID),
  PRIMARY KEY(courseID, instructorID,periodOffered)

);

CREATE TABLE IF NOT EXISTS coursesTaken (
  courseID VARCHAR NOT NULL,
  studentID VARCHAR NOT NULL,
  periodTaken VARCHAR NOT NULL,
  grade varchar,
  type varchar NOT NULL,
  FOREIGN KEY (courseID) REFERENCES courseCatalog(courseID),
  FOREIGN KEY (studentID) REFERENCES students(studentID),
  PRIMARY KEY(courseID, studentID, periodTaken)
);

CREATE TABLE IF NOT EXISTS coursesPrereq (
  courseID VARCHAR NOT NULL,
  prereqcourseID VARCHAR NOT NULL,
  FOREIGN KEY (prereqcourseID) REFERENCES courseCatalog(courseID),
  FOREIGN KEY (courseID) REFERENCES courseCatalog(courseID),
  PRIMARY KEY(courseID,prereqcourseID)
);

CREATE TABLE IF NOT EXISTS coursesApproval (
  courseID VARCHAR NOT NULL,
  instructorID VARCHAR NOT NULL,
  studentID VARCHAR NOT NULL,
  FOREIGN KEY (instructorID) REFERENCES instructors(instructorID),
  FOREIGN KEY (courseID) REFERENCES courseCatalog(courseID),
  FOREIGN KEY (studentID) REFERENCES students(studentID),
  PRIMARY KEY(courseID,instructorID,studentID)
);

CREATE TABLE IF NOT EXISTS curriculumInfo (
  year int NOT NULL,
  branch varchar NOT NULL,
  gr numeric NOT NULL,
  pc numeric NOT NULL,
  pe numeric NOT NULL,
  cp numeric NOT NULL,
  PRIMARY KEY (year, branch)
);

CREATE TABLE IF NOT EXISTS autoEnrollment (
  courseID VARCHAR NOT NULL,
  branchAndYear VARCHAR NOT NULL,
  periodToenroll VARCHAR NOT NULL,
  FOREIGN KEY (courseID) REFERENCES courseCatalog(courseID),
  PRIMARY KEY(courseID,branchAndYear,periodToenroll)
);

CREATE TABLE IF NOT EXISTS currentPeriod (
  year int NOT NULL,
  term VARCHAR NOT NULL,
  sub_period VARCHAR NOT NULL,
  PRIMARY KEY (year, term, sub_period)
);
insert into currentPeriod(year,term,sub_period) values(2020,'W', 'stud_reg_catalogUpdate');


