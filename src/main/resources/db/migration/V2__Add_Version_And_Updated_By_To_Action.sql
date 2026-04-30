-- V2__Add_Version_And_Updated_By_To_Action.sql

ALTER TABLE action ADD COLUMN version BIGINT DEFAULT 0;
ALTER TABLE action ADD COLUMN updated_by VARCHAR(100);
