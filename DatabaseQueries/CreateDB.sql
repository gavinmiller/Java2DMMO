CREATE DATABASE mmo_db;
GO
USE mmo_db
GO
CREATE USER mainserver WITH PASSWORD = 'aZhEjj11zk23455';
GO

/*
exec sp_configure 'contained database authentication', 1
go
reconfigure
go

alter database mmo_db
set containment = partial
go
*/