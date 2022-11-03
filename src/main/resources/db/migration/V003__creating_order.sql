create type payment_type as enum ('cash', 'debit');

create table if not exists palem."order"
(
    id           serial unique,
    user_id      text not null,
    address      text,
    payment      payment_type,
    notes        text,
    total        text,
    order_status text                        default 'IN_PROGRESS',
    created_time timestamp without time zone default now()::timestamp(0),
    constraint pk_palem_orders primary key (id)
);
create index if not exists idx_palem_orders_address on palem.order (address);
create index if not exists idx_palem_orders_notes on palem.order (notes);
create index if not exists idx_palem_orders_total on palem.order (total);
create index if not exists idx_palem_orders_created_time on palem.order (created_time);

create table if not exists palem.order_detail
(
    id           serial unique,
    "order"      bigint not null,
    sku          text,
    quantity     text,
    created_time timestamp without time zone default now()::timestamp(0),
    constraint pk_palem_order_detail primary key (id),
    constraint fk_palem_order_detail_order foreign key ("order") references palem.order (id)
);
create index if not exists idx_palem_orders_detail_order on palem.order_detail ("order");
create index if not exists idx_palem_orders_detail_sku on palem.order_detail (sku);
create index if not exists idx_palem_orders_detail_quantity on palem.order_detail (quantity);
create index if not exists idx_palem_orders_detail_created_time on palem.order_detail (created_time);
