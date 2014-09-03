ALTER TABLE JobRequestProduct ADD COLUMN cWareHouse INTEGER DEFAULT(0);
ALTER TABLE JobRequestProduct ADD COLUMN cOrder INTEGER DEFAULT(0);
ALTER TABLE JobRequestProduct ADD COLUMN cSight TEXT DEFAULT('N');
ALTER TABLE JobRequestProduct ADD COLUMN cEquipping TEXT DEFAULT('N');
ALTER TABLE JobRequestProduct ADD COLUMN cPay TEXT DEFAULT('N');
ALTER TABLE JobRequestProduct ADD COLUMN cSold TEXT DEFAULT('N');
ALTER TABLE JobRequestProduct ADD COLUMN cDate datetime default('CURRENT_TIMESTAMP');
ALTER TABLE JobRequestProduct ADD COLUMN cKms TEXT;
ALTER TABLE JobRequestProduct ADD COLUMN cDocument TEXT;
ALTER TABLE JobRequestProduct ADD COLUMN cRemark TEXT;
ALTER TABLE JobRequestProduct ADD COLUMN cImage TEXT;
ALTER TABLE JobRequestProduct ADD COLUMN cReasonId INTEGER;
ALTER TABLE JobRequestProduct ADD COLUMN cReasonCode TEXT;
ALTER TABLE JobRequestProduct ADD COLUMN photoSetID INTEGER;
ALTER TABLE JobRequestProduct ADD COLUMN cErrorType INTEGER DEFAULT(0);
ALTER TABLE JobRequestProduct ADD COLUMN cErrorText TEXT;

ALTER TABLE InspectDataObjectPhotoSaved ADD COLUMN inspectTypeID INTEGER DEFAULT(-1);


DROP TABLE IF EXISTS CarInspectStampLocation;
CREATE TABLE CarInspectStampLocation
(
	taskCode TEXT,
	taskDuplicateNo INTEGER,
	jobRequestID INTEGER,
	customerSurveySiteID INTEGER DEFAULT(0),
	siteAddress TEXT,
	milesNo TEXT,
	timeRecorded TEXT
);






