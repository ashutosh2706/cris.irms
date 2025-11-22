schema=trains

create sequence trains.t_train_master_seq start with 1 increment by 5;

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

