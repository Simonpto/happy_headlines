-- Our own copy of the subscribers, kept up to date from the subscriber queue.
-- The id comes from the subscriber-service, so it isn't generated here.
CREATE TABLE IF NOT EXISTS subscriber (
    id    BIGINT PRIMARY KEY,
    email VARCHAR(255) NOT NULL
);
