ALTER TABLE JobRequestProduct ADD COLUMN cTeamId INTEGER DEFAULT(0);
ALTER TABLE JobRequestProduct ADD COLUMN cJobRowId INTEGER DEFAULT(0);


ALTER TABLE InspectDataObjectPhotoSaved ADD COLUMN flagGeneralImage TEXT DEFAULT('N');

ALTER TABLE CarInspectStampLocation ADD COLUMN flagIsAddNewCustomerSite TEXT DEFAULT('N');
ALTER TABLE CarInspectStampLocation ADD COLUMN taskID INTEGER DEFAULT(0);
ALTER TABLE CarInspectStampLocation ADD COLUMN siteLat REAL;
ALTER TABLE CarInspectStampLocation ADD COLUMN siteLon REAL;
