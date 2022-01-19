create sequence card_card_id_seq
    as integer;

alter sequence card_card_id_seq owner to swe1user;

create table card
(
    card_id      text not null
        constraint card_pk
            primary key,
    card_typ     text not null,
    card_name    text not null,
    card_element text,
    card_power   text not null
);

alter table card
    owner to swe1user;

alter sequence card_card_id_seq owned by card.card_id;

create unique index card_card_id_uindex
    on card (card_id);

create table player
(
    user_id     text not null
        constraint player_pk
            primary key,
    username    text not null,
    password    text not null,
    user_coins  text not null,
    user_elo    text not null,
    user_online text not null
);

alter table player
    owner to swe1user;

create unique index player_userid_uindex
    on player (user_id);

create unique index player_username_uindex
    on player (username);

create table "cardHolder"
(
    "cardHolder_id" text not null
        constraint cardholder_pk
            primary key,
    card_id         text not null,
    holder_id       text not null,
    number          text not null,
    locked          text not null
);

comment on column "cardHolder".holder_id is '- user ID 
- package ID';

comment on column "cardHolder".number is '- anzahl der Karten';

alter table "cardHolder"
    owner to swe1user;

create unique index cardholder_cardholder_id_uindex
    on "cardHolder" ("cardHolder_id");

create table store
(
    transaction_id text not null
        constraint store_pk
            primary key,
    seller_id      text not null,
    item_id        text not null,
    price          text not null,
    date           text
);

alter table store
    owner to swe1user;

create unique index store_transaction_id_uindex
    on store (transaction_id);

create table deck
(
    user_id    text not null
        constraint deck_pk
            primary key
        constraint deck_player_user_id_fk
            references player,
    card_id_1  text not null
        constraint deck_card_card_id_fk
            references card,
    card_id_2  text not null,
    card_id_3  text not null,
    card_id_4  text not null,
    new_column integer
);

alter table deck
    owner to swe1user;

create unique index deck_deck_id_uindex
    on deck (user_id);

create table package
(
    package_id text not null
        constraint package_pk
            primary key,
    date       text not null
);

alter table package
    owner to swe1user;

create unique index package_package_id_uindex
    on package (package_id);

create table session
(
    session_id text not null
        constraint session_pk
            primary key,
    user_id    text not null
        constraint session_player_user_id_fk
            references player,
    date       text not null
);

alter table session
    owner to swe1user;

create table profil
(
    user_id text not null
        constraint profil_pk
            primary key
        constraint profil_player_user_id_fk
            references player,
    name    text,
    bio     text,
    image   text
);

alter table profil
    owner to swe1user;

create table trade
(
    trade_id       text not null
        constraint trading_pk
            primary key,
    user_id        text not null,
    card_id        text not null,
    card_min_power text not null,
    card_typ       text not null
);

alter table trade
    owner to swe1user;

create unique index trading_trading_id_uindex
    on trade (trade_id);


