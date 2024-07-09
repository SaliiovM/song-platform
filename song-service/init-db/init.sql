CREATE TABLE IF NOT EXISTS song (
	id bigserial PRIMARY KEY NOT NULL,
	album varchar(255),
	artist varchar(255),
	length varchar(255),
	"name" varchar(255),
	resource_id BIGINT,
	song_year varchar(255)
);