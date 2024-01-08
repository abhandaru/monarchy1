-- Add support for Postgres UUID generators.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- This keeps track of when entities are updated.
CREATE OR REPLACE FUNCTION update_timestamps() RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  NEW.created_at = OLD.created_at;
  RETURN NEW;
END
$$ LANGUAGE plpgsql;

-- Simple users table.
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
  secret TEXT NOT NULL,
  username TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  rating INTEGER DEFAULT 1000 NOT NULL,
  membership SMALLINT DEFAULT 1 NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  UNIQUE(username),
  UNIQUE(phone_number)
);

CREATE TRIGGER updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE PROCEDURE update_timestamps();
