USE mmo_db
GO

-- Default accounts
INSERT INTO Accounts VALUES (3,'admin','pass123',NULL,NULL);
INSERT INTO Accounts VALUES (0,'test','test',NULL,NULL);
GO

-- Default characters
INSERT INTO Characters VALUES(0, 'Gavin', 0, 0, 0, 0);
INSERT INTO Characters VALUES(1, 'TestCharacter', 0, 0, 0, 0);
GO

SELECT * FROM Accounts;
SELECT * FROM Characters;