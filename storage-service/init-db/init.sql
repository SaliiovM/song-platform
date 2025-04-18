create table if not exists storage (
    id bigserial primary key,
    type varchar(255),
    bucket varchar(255),
    path varchar(255)
);