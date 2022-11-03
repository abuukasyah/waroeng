create table if not exists palem.provinces
(
    provinces bigserial,
    name      text not null,
    constraint pk_palem_provinces primary key (provinces)
);
create index if not exists idx_palem_provinces_name on palem.provinces (name);

create table if not exists palem.regencies
(
    regencies bigserial,
    province  bigint not null,
    name      text   not null,
    constraint pk_palem_regencies primary key (regencies),
    constraint fk_palem_provinces_regencies foreign key (province) references palem.provinces (provinces) on delete cascade on update cascade
);
create index if not exists idx_palem_regencies_provinces on palem.regencies (province);
create index if not exists idx_palem_regencies_name on palem.regencies (name);

create table if not exists palem.districts
(
    districts bigserial,
    regency   bigint not null,
    name      text   not null,
    constraint pk_palem_districts primary key (districts),
    constraint fk_palem_provinces_districts foreign key (regency) references palem.regencies (regencies) on delete cascade on update cascade
);

create index if not exists idx_palem_districts_regency on palem.districts (regency);
create index if not exists idx_palem_districts_name on palem.districts (name);

create table if not exists palem.villages
(
    villages bigserial,
    district bigint not null,
    name     text   not null,
    constraint pk_palem_villages primary key (villages),
    constraint fk_palem_districts_villages foreign key (district) references palem.districts (districts)
);

create index if not exists idx_palem_villages_district on palem.villages (district);
create index if not exists idx_palem_villages_name on palem.villages (name);

create table if not exists palem.address
(
    id            bigserial,
    user_id       text,
    province      text,
    city          text,
    sub_district  text,
    urban_village text,
    constraint pk_palem_address primary key (id)
);
create index if not exists idx_address_user_id on palem.address (user_id);
