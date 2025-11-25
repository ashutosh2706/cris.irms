schema=trains

create sequence trains.t_train_master_seq start with 1 increment by 5;
create sequence trains.t_train_route_seq start with 1 increment by 5;

create table trains.t_train_master(
	id BIGINT not null default nextval('trains.t_train_master_seq'),
	train_number INT not null unique,
	train_name VARCHAR(255) not null,
	train_type VARCHAR(255) not null,
	journey_direction VARCHAR(10) not null,
	journey_total_duration INT,
	journey_total_distance BIGINT,
	primary key(id)
);

create table trains.t_coach_info(
	train_number INT not null unique,
	first_ac INT not null,
	second_ac INT not null,
	third_ac INT not null,
	economy_ac INT not null,
	second_seater INT not null,
	sleeper INT not null,
	general INT not null,
	chair_car_ac INT not null,
	pantry_car BOOLEAN not null,
	parcel_van BOOLEAN not null,
	primary key(train_number),
	foreign key(train_number) references trains.t_train_master(train_number)
);

create table trains.t_route_detail(
	id BIGINT not null default nextval('trains.t_train_route_seq'),
	train_number INT not null,
	station_id BIGINT not null,
	arrival_sequence INT not null,
	arrival_time TIME not null,
	departure_time TIME not null,
	departs_on_week_days INT not null,
	day_offset INT not null,
	distance_covered BIGINT,
	primary key(id)
);