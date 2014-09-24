DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS Team;
DROP TABLE IF EXISTS EmpAssignedInTeam;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS CustomerSurveySite;
DROP TABLE IF EXISTS TransactionHistoryLog;
DROP TABLE IF EXISTS DeviceInfo;
DROP TABLE IF EXISTS InspectType;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS ProductAmountUnit;
DROP TABLE IF EXISTS ProductInJobRequest;
DROP TABLE IF EXISTS JobRequest;
DROP TABLE IF EXISTS Task;
DROP TABLE IF EXISTS InspectDataItem;
DROP TABLE IF EXISTS InspectDataGroupType;
DROP TABLE IF EXISTS TaskControlTemplate;
DROP TABLE IF EXISTS TaskFormTemplate;
DROP TABLE IF EXISTS InspectDataObjectPhotoSaved;
DROP TABLE IF EXISTS InspectDataObjectSaved;
DROP TABLE IF EXISTS ExpenseType;
DROP TABLE IF EXISTS Expense;
DROP TABLE IF EXISTS ReasonSentence;
DROP TABLE IF EXISTS MarketPrice;
DROP TABLE IF EXISTS productGroup;
DROP TABLE IF EXISTS PhotoTeamHistory;
DROP TABLE IF EXISTS MemberInTeamHistory;
DROP TABLE IF EXISTS TeamCheckInHistory;
DROP TABLE IF EXISTS TaskFormDataSaved;
DROP TABLE IF EXISTS InspectHistory;
DROP TABLE IF EXISTS News;
DROP TABLE IF EXISTS LayoutItemScaleHistory;
DROP TABLE IF EXISTS TaskResend;
DROP TABLE IF EXISTS TaskComplete;
DROP TABLE IF EXISTS InspectDataSVGResult;
DROP TABLE IF EXISTS JobRequestProduct;
DROP TABLE IF EXISTS SubExpenseType;
DROP TABLE IF EXISTS LicensePlate;
DROP TABLE IF EXISTS JobRequestProductGroup;

CREATE TABLE JobRequestProductGroup
(
 jobRequestID INTEGER,
 productGroupID INTEGER
);

CREATE TABLE LicensePlate
(
	licensePlateId INTEGER,
	licensePlate TEXT,
	licensePlateProvince TEXT,
	vehicleFuelType TEXT
);
CREATE TABLE SubExpenseType
(
	subExpenseTypeID INTEGER,
	subExpenseTypeName TEXT,
	expenseTypeID INTEGER,
	expenseTypeName TEXT,
	expenseMoneyType TEXT,
	expenseFuelName TEXT
);

CREATE TABLE JobRequestProduct
(
	jobNo TEXT,
	resultProductRowId INTEGER,
	productId INTEGER,
	productQty REAL,
	productUnit TEXT,
	productPrice REAL,
	productValue REAL,
	foundFlag TEXT,
	productInfo TEXT,
	clusterControlFlag TEXT,	
	jobRequestID INTEGER,
	productRowId INTEGER,
	itemNo TEXT,
	productGroup TEXT,
	productType TEXT,
	productValueText TEXT,
	cFranchise TEXT,
	cInvoiceDate TEXT,
	cRegisterNumber TEXT,
	cMid TEXT,
	cVin TEXT,
	cEngine TEXT,
	cDescription TEXT,
	cColor TEXT,
	cModel TEXT,
	cYear TEXT,
	cType TEXT,
	tNo TEXT,
	tCode TEXT,
	tSize TEXT,
	tWeight INTEGER,
	mContractNo TEXT,
	mFixedAssetNumber TEXT,
	mAssetName TEXT,
	mBrand TEXT,
	mModel TEXT,
	mSerialNo TEXT,
	mChassissNo TEXT,
	mEngineNo TEXT,
	crHire TEXT,
	crOwnerName TEXT,
	crLicense TEXT,
	crBrand TEXT,
	crRenewDate TEXT,
	crContractNo TEXT,
	crContractDate TEXT,
	crPrincipal TEXT,
	crDueNumber TEXT,
	crDueAmount TEXT,
	crLastPayment TEXT
);

CREATE TABLE InspectDataSVGResult
(
	taskCode TEXT,
	taskDuplicateNo INTEGER,
	customerSurveySiteID INTEGER,
	svgResultFullLayout TEXT,
	svgResultLayoutOnly TEXT,
	locationLatitude REAL,
	locationLongitude REAL
);

CREATE TABLE TaskResend
(
   teamID INTEGER,
   taskCode TEXT,
   taskCodeOld TEXT
);

CREATE TABLE TaskComplete
(
   teamID INTEGER,
   taskCode TEXT
);
	
CREATE TABLE LayoutItemScaleHistory
(
	scaleHistoryId INTEGER PRIMARY KEY  AUTOINCREMENT,
	taskCode TEXT,
	taskDuplicateNo INTEGER,
	customerSurveySiteID INTEGER,
	customerSurveySiteRowID INTEGER,	
	siteWidth REAL,
	siteLong REAL
);

CREATE TABLE News
(
newsID INTEGER PRIMARY KEY  AUTOINCREMENT,
newsTitle TEXT,
newsContent TEXT,
publishDate DATE,
publishBy TEXT
);


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
userNameLogin TEXT NOT NULL UNIQUE,
passWordLogin TEXT NOT NULL,
profileImg BLOB,
updatedDateTime DATETIME
);

CREATE TABLE Team
(
teamID INTEGER,
teamCode TEXT,
teamName TEXT,
deviceID TEXT,
teamAddress TEXT,
teamMobileNo TEXT,
isQCTeam BOOLEAN,
licensePlateId INTEGER
);

CREATE TABLE EmpAssignedInTeam
(
teamID INTEGER ,
employeeCode TEXT,
isTeamLeader BOOLEAN DEFAULT '0' NOT NULL
);


CREATE TABLE Customer
(
customerID INTEGER PRIMARY KEY  ,
customerCode TEXT NOT NULL UNIQUE,
customerName TEXT NOT NULL UNIQUE,
customerAddress TEXT NOT NULL,
customerTels TEXT NOT NULL,
customerFaxs TEXT,
customerEmails TEXT,
officeLocLat REAL,
officeLocLon REAL,
officeMapImg BLOB,
areaID INTEGER
);

CREATE TABLE CustomerSurveySite
(
customerSurveySiteID INTEGER,
customerCode TEXT,
customerName TEXT,
jobRequestID INTEGER,
siteAddressKey TEXT,
customerSurveySiteRowID INTEGER,
taskID INTEGER,
customerID INTEGER,
siteAddress TEXT,
siteTels TEXT,
siteEmails TEXT,
siteLocLat REAL,
siteLocLon REAL,
areaID INTEGER,
provincialID INTEGER,
customerNameAssist TEXT,
layoutSvgPath TEXT,
resultPdfPath TEXT
);


CREATE TABLE TransactionHistoryLog
(
dataVersion TEXT NOT NULL PRIMARY KEY  UNIQUE,
lastUpdated DATETIME
);

CREATE TABLE DeviceInfo
(
deviceInfoID INTEGER PRIMARY KEY  AUTOINCREMENT,
deviceID TEXT NOT NULL UNIQUE,
deviceBrand TEXT,
deviceModel TEXT
);

CREATE TABLE InspectType
(
inspectTypeID INTEGER PRIMARY KEY  AUTOINCREMENT,
inspectTypeName VARCHAR(100) NOT NULL,
inpsectProductType INTEGER
);


CREATE TABLE ProductGroup
(
productGroupID INTEGER,
productGroupName TEXT
);


CREATE TABLE Product
(
productGroupID INTEGER,
productID INTEGER,
productName TEXT NOT NULL,
densityValue REAL,
productAmountUnitID INTEGER,
convertRatio INTEGER,
PRIMARY KEY (productGroupID,productID)
);


CREATE TABLE ProductAmountUnit
(
productAmountUnitID INTEGER ,
unitName TEXT
);

CREATE TABLE ProductInJobRequest
(
jobRequestID INTEGER REFERENCES JobRequest (jobRequestID),
productID INTEGER REFERENCES Product (productID),
amount INTEGER,
productAmountUnitID INTEGER REFERENCES ProductAmountUnit (productAmountUnitID),
PRIMARY KEY (jobRequestID,productID)
);

CREATE TABLE JobRequest
(
jobRequestID INTEGER PRIMARY KEY  AUTOINCREMENT,
inspectTypeID INTEGER REFERENCES InspectType (inspectTypeID),
customerCode TEXT,
purposeName TEXT,
typeOfSurvey TEXT,
businessType TEXT,
jobRequestNotes TEXT
);


CREATE TABLE Task
(
taskID INTEGER,
taskCode VARCHAR(50),
jobRequestID INTEGER,
taskNo INTEGER,
taskDuplicateNo INTEGER,
teamID INTEGER,
taskFormTemplateID INTEGER,
taskLevel INTEGER,
taskStatus INTEGER,
taskDate DATE,
taskScheduleDate DATE,
taskStartTime TEXT,
taskEndTime TEXT,
reasonSentenceID INTEGER,
reasonSentenceType TEXT,
shiftCause TEXT,
remark TEXT,
isDuplicatedTask BOOLEAN,
duplicatedFromTaskID INTEGER,
duplicatedLocationStart TEXT,
duplicatedSurveyStartSiteID INTEGER,
duplicatedLocationEnd TEXT,
duplicatedSurveyEndSiteID INTEGER,
duplicatedDistance REAL,
forceTime TEXT,
flagOtherTeam TEXT,
isTaskTeamLeader BOOLEAN,
isUploadSynced BOOLEAN,
notUploadFlag TEXT,
isInspectReportGenerated BOOLEAN,
PRIMARY KEY (taskID,taskCode,jobRequestID,taskNo,taskDuplicateNo,teamID)
);



CREATE TABLE InspectDataGroupType
(
inspectDataGroupTypeID INTEGER,
inspectDataGroupTypeName TEXT
);

CREATE TABLE InspectDataItem
(
inspectDataItemID INTEGER,
inspectDataGroupTypeID INTEGER,
inspectDataItemName TEXT,
formular TEXT,
kilogram REAL,
imageFileName TEXT,
isBuildingComponent TEXT,
isCameraObject TEXT,
isGodownComponent TEXT,
isInspectObject TEXT,
multipleType INTEGER,
convertRatioWidth REAL DEFAULT 1.0,
convertRatioDeep  REAL DEFAULT 1.0,
convertRatioHeight REAL DEFAULT 1.0
);


CREATE TABLE TaskControlTemplate
(
taskControlTemplateID INTEGER PRIMARY KEY  AUTOINCREMENT,
taskControlType INTEGER NOT NULL UNIQUE,
taskControlName TEXT NOT NULL UNIQUE
);

CREATE TABLE TaskFormTemplate
(
taskFormTemplateID INTEGER,
taskFormAttributeID INTEGER,
taskControlType INTEGER,
taskControlNo INTEGER,
jobRequestID INTEGER,
textQuestion TEXT,
choiceTextMatrixColumns TEXT,
choiceTexts TEXT,
choiceValues TEXT,
minSlider INTEGER,
maxSlider INTEGER,
stepSlider INTEGER,
reasonSentenceID INTEGER,
reasonSentenceType TEXT,
parentId  TEXT,
path  TEXT,
isQCTemplate BOOLEAN,
PRIMARY KEY (taskFormTemplateID,taskFormAttributeID,taskControlType,taskControlNo,jobRequestID)
);

CREATE TABLE Role
(
roleID INTEGER PRIMARY KEY  AUTOINCREMENT,
roleName TEXT NOT NULL
);

CREATE TABLE InspectDataObjectSaved
(
siteAddressKey TEXT,
jobRequestID INTEGER,
taskCode TEXT,
customerSurveySiteID INTEGER,
layoutDrawingPageNo INTEGER,
inspectDataObjectID INTEGER,
inspectDataItemID INTEGER,
inspectDataGroupTypeID INTEGER,
inspectDataItemStartX REAL,
inspectDataItemStartY REAL,
inspectDataItemZOrder INTEGER,
productID INTEGER,
productGroupID INTEGER,
productAmountUnitID INTEGER,
productAmountUnitText TEXT,
width REAL,
deep REAL,
height REAL,
lost REAL,
overValue REAL,
total REAL,
value REAL,
qty REAL,
angle REAL,
radius REAL,
density REAL,
palateAmount REAL DEFAULT 1,
marketPrice REAL,
marketPriceID INTEGER,
opinion TEXT,
photoID INTEGER,
widthObject INTEGER,
longObject INTEGER,
isProductControlled BOOLEAN,
authorized BOOLEAN,
teamIDObjectOwner INTEGER,
addObjectFlag TEXT,
PRIMARY KEY (jobRequestID,taskCode,customerSurveySiteID,layoutDrawingPageNo,inspectDataObjectID)
);

CREATE TABLE InspectDataObjectPhotoSaved
(
photoID INTEGER ,
photoNo INTEGER ,
fileName TEXT,
comment TEXT,
taskCode TEXT,
customerSurveySiteID INTEGER,
inspectDataObjectID INTEGER,
inspectDataTextSelected TEXT,
angleDetail TEXT,
productID INTEGER,
PRIMARY KEY(photoID,photoNo)
);


CREATE TABLE ExpenseType
(
expenseTypeID INTEGER PRIMARY KEY,
expenseTypeName TEXT
);

CREATE TABLE Expense
(
expenseID INTEGER PRIMARY KEY  AUTOINCREMENT,
expenseGroupID INTEGER,
expenseTypeID INTEGER,
expenseDate DATE,
expenseTime TEXT,
mileNumber INTEGER,
typeFuelAndPaidType TEXT,
otherExpenseType TEXT,
amount REAL,
liter REAL,
remark TEXT,
expenseStatus INTEGER,
expenseCancelCause TEXT,
subExpenseTypeID INTEGER
);

CREATE TABLE ReasonSentence
(
reasonSentenceID INTEGER,
reasonSentenceType TEXT,
reasonSentenceText TEXT,
reasonSentencePath TEXT
);
CREATE TABLE MarketPrice
(
marketPriceID INTEGER,
provincialID INTEGER,
productGroupID INTEGER,
productID INTEGER,
marketPrice REAL,
unit TEXT
);

CREATE TABLE MemberInTeamHistory
(
teamCheckInHistoryID INTEGER,
employeeCode TEXT
);

CREATE TABLE PhotoTeamHistory
(
imgTeamHistoryID INTEGER,
imgTeamHistoryNo INTEGER,
imgFileTeamCheckIn TEXT
);

CREATE TABLE TeamCheckInHistory
(
teamCheckInHistoryID INTEGER PRIMARY KEY  AUTOINCREMENT,
teamID INTEGER,
lastCheckInDateTime TIMESTAMP,
lastCheckOutDateTime TIMESTAMP,
teamStartLatLoc REAL,
teamStartLonLoc REAL,
numberOfMilesAtStartPoint TEXT,
numberOfMilesAtEndPoint TEXT,
carLicenseNumber TEXT,
imgTeamHistoryID INTEGER,
historyType INTEGER,
taskCode TEXT,
taskID INTEGER,
taskDuplicateNo INTEGER,
licensePlateId INTEGER,
customerName TEXT,
customerSurveySites TEXT,
inspectTypeID INTEGER,
inspectTypeName TEXT,
isTaskCompleted BOOLEAN
);

CREATE TABLE TaskFormDataSaved
(
taskFormTemplateID INTEGER,
taskFormAttributeID INTEGER,
taskControlType INTEGER,
taskControlNo INTEGER,
taskID INTEGER,
taskCode TEXT,
jobRequestID INTEGER,
customerSurveySiteID INTEGER DEFAULT 0,
taskDataValues TEXT,
chooseReasonSentenceID INTEGER,
chooseReasonSentenceType TEXT,
chooseReasonSentenceText TEXT,
chooseReasonSentencePath TEXT,
parentID TEXT
);

CREATE TABLE InspectHistory
(
jobRequestID INTEGER,
taskNo INTEGER,
customerSurveySiteID INTEGER,
imageFileLastLayout TEXT,
imageFileLastResult TEXT
);

INSERT INTO "ExpenseType" VALUES(1,'ตรวจสินค้า');
INSERT INTO "ExpenseType" VALUES(2,'อบรม');
INSERT INTO "ExpenseType" VALUES(3,'ย้ายงาน');
