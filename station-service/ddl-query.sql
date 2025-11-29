create sequence stations.t_station_model_seq start with 1 increment by 1;

create table stations.t_station_model(
	id BIGINT not null default nextval('stations.t_station_model_seq'),
	station_code VARCHAR(50) not null unique,
	station_name TEXT not null,
	division VARCHAR(255),
	state_name VARCHAR(255),
	station_type varchar(255) not null,
	elevation_from_sea INT,
	platform_count INT not null,
	boarding_disabled BOOLEAN,
	primary key(id)
);
