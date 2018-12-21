USE master

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'mmo_db')
	DROP DATABASE mmo_db
GO

CREATE DATABASE mmo_db;
GO

USE mmo_db
GO

IF OBJECT_ID(N'dbo.Characters', N'U') IS NOT NULL
	DROP TABLE dbo.Characters;
GO

IF OBJECT_ID(N'dbo.Accounts', N'U') IS NOT NULL
	DROP TABLE dbo.Accounts;
GO

-- Account info
CREATE TABLE Accounts (
	player_id INT IDENTITY(0,1),
	player_rights INT,
	player_username NVARCHAR(50),
	player_password NVARCHAR(50),
	last_login_ip NVARCHAR(50) NULL,
	player_email_address NVARCHAR(100) NULL,
	-- More variables?

	CONSTRAINT pk_Accounts PRIMARY KEY(player_id),
	CONSTRAINT ck_player_email_address CHECK(player_email_address LIKE ('#@#.#'))
);


-- Character info (NOT CURRENTLY USED)
CREATE TABLE Characters (
	character_id INT IDENTITY(0,1),
	player_id INT,
	character_name NVARCHAR(50),
	x INT,
	y INT,
	facing_direction INT,
	is_connected BIT NOT NULL,

	CONSTRAINT pk_Characters PRIMARY KEY(character_id),
	CONSTRAINT fk_Characters_Accounts FOREIGN KEY (player_id) REFERENCES Accounts(player_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT ck_facing_direction CHECK (facing_direction BETWEEN 0 AND 3)
);
