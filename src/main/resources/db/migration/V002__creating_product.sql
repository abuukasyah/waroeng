create table if not exists palem.category
(
    id           serial unique,
    name         text unique not null,
    created_time timestamp without time zone default now()::timestamp(0),
    constraint pk_palem_category primary key (id)
);
create index if not exists idx_palem_category_name on palem.category (name);

create table if not exists palem.product
(
    id           bigserial,
    category     int         not null,
    sku          text unique not null,
    name         text        not null,
    price        text        not null,
    description  text,
    weight       text,
    is_available boolean,
    image        bytea,
    created_time timestamp without time zone default now()::timestamp(0),
    constraint pk_palem_product primary key (id),
    constraint fk_palem_category_product foreign key (category) references palem.category (id) on delete cascade
);
create index if not exists idx_palem_product_category on palem.product (category);
create index if not exists idx_palem_product_sku on palem.product (sku);
create index if not exists idx_palem_product_name on palem.product (name);
create index if not exists idx_palem_product_price on palem.product (price);
create index if not exists idx_palem_product_description on palem.product (description);
create index if not exists idx_palem_product_weight on palem.product (weight);
create index if not exists idx_palem_product_is_available on palem.product (is_available);
