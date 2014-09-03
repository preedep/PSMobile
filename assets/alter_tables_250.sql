DROP TABLE IF EXISTS Employee;

CREATE TABLE Employee
(
employeeCode TEXT PRIMARY KEY  ,
roleID INTEGER REFERENCES Role (roleID),
preFix TEXT,
firstName TEXT,
lastName TEXT,
address_1 TEXT,
address_2 TEXT,
telNo TEXT,
mobileNo TEXT,
email TEXT,
userNameLogin TEXT,
passWordLogin TEXT,
profileImg BLOB,
updatedDateTime DATETIME
);


ALTER TABLE CustomerSurveySite ADD COLUMN locationCompleteCheckDate TEXT;
ALTER TABLE CustomerSurveySite ADD COLUMN locationCompleteCheckEndDate TEXT;
ALTER TABLE CustomerSurveySite ADD COLUMN mileNo TEXT;
	