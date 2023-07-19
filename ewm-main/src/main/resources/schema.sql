CREATE TABLE IF NOT EXISTS categories (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(250) NOT NULL UNIQUE,
	email varchar(320) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS locations (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	lat NUMERIC NOT NULL,
	lon NUMERIC NOT NULL
);
CREATE TABLE IF NOT EXISTS events (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	annotation VARCHAR(2000),
	category_id BIGINT NOT NULL,
	created_on TIMESTAMP NOT NULL,
	description VARCHAR(7000),
	event_date TIMESTAMP NOT NULL,
	initiator_id BIGINT NOT NULL,
	location_id BIGINT,
	paid BOOLEAN,
	participant_limit BIGINT,
	published_on TIMESTAMP,
	request_moderation BOOLEAN,
	title VARCHAR,
	views BIGINT NOT NULL,
	state VARCHAR NOT NULL,
	    CONSTRAINT fk_events_category FOREIGN KEY(category_id)
	        REFERENCES categories(id),
	    CONSTRAINT fk_events_initiator FOREIGN KEY(initiator_id)
        	REFERENCES users(id),
   	    CONSTRAINT fk_events_location FOREIGN KEY(location_id)
           	REFERENCES locations(id)
);

CREATE TABLE IF NOT EXISTS event_requests (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	created TIMESTAMP NOT NULL,
	event_id BIGINT NOT NULL,
	requester_id BIGINT NOT NULL,
	status VARCHAR(20) NOT NULL,
	    CONSTRAINT fk_event_requests_event FOREIGN KEY(event_id)
	        REFERENCES events(id),
	    CONSTRAINT fk_event_requests_requester FOREIGN KEY(requester_id)
        	REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS compilations (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	pinned BOOLEAN,
	title VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS event_vs_compilations (
    event_id BIGINT,
    compilation_id BIGINT,
        CONSTRAINT fk_event_vs_compilations_event FOREIGN KEY(event_id)
    		  REFERENCES events(id) ON DELETE CASCADE,
    	CONSTRAINT fk_event_vs_compilations_compilation FOREIGN KEY(compilation_id)
    		  REFERENCES compilations(id) ON DELETE CASCADE,
    	PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS likes (
    user_id BIGINT,
    event_id BIGINT,
    status INTEGER,
    CONSTRAINT fk_user FOREIGN KEY(user_id)
		  REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT fk_event FOREIGN KEY(event_id)
		  REFERENCES events(id) ON DELETE CASCADE,
	PRIMARY KEY (user_id, event_id)
);







