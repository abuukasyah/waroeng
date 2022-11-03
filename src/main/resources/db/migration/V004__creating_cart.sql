create table if not exists palem.cart
(
    id           bigserial,
    user_id      text not null,
    total        text default '0',
    created_time timestamp without time zone default now()::timestamp(0),
    constraint pk_palem_carts primary key (id)
);
create index if not exists idx_palem_carts_user_account on palem.cart (user_id);
create unique index if not exists idx_palem_carts_unique on palem.cart (user_id);
create index if not exists idx_palem_carts_user_total on palem.cart (total);

create table if not exists palem.cart_items
(
    id           bigserial,
    cart         bigint not null,
    sku          text   not null,
    quantity     text   not null,
    created_time timestamp without time zone default now()::timestamp(0),
    constraint pk_palem_cart_items primary key (id),
    constraint fk_palem_cart_cart_item foreign key (cart) references palem.cart (id) on update cascade on delete cascade,
    constraint fk_palem_product_cart_items foreign key (sku) references palem.product (sku) on delete cascade
);
create index if not exists idx_palem_cart_items_cart on palem.cart_items (cart);
create index if not exists idx_palem_cart_items_product on palem.cart_items (sku);
create unique index if not exists idx_palem_cart_items_unique on palem.cart_items (cart, sku, quantity)
