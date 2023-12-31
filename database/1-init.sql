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
  id SERIAL PRIMARY KEY NOT NULL,
  username TEXT NOT NULL,
  phone_number TEXT NOT NULL,
  secret TEXT NOT NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  UNIQUE(username),
  UNIQUE(phone_number)
);

CREATE TRIGGER updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE PROCEDURE update_timestamps();
