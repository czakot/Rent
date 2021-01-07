-- todo load up initial data

set schema PUBLIC;
drop table if exists USERPROFILES;
drop table if exists USER_ROLES;
drop table if exists USERS;

create table USERS
(
    ID         BIGINT       not null
        primary key,
    ACTIVATION VARCHAR(255),
    EMAIL      VARCHAR(255) not null
        constraint UK_6DOTKOTT2KJSP8VW4D0M25FB7
            unique,
    ENABLED    BOOLEAN,
    FULL_NAME  VARCHAR(255),
    PASSWORD   VARCHAR(255) not null
);

create table USERS_ROLES
(
    USER_ID BIGINT       not null,
    ID      VARCHAR(255) not null,
    primary key (USER_ID, ID),
    constraint FK2O0JVGH89LEMVVO17CBQVDXAA
        foreign key (USER_ID) references USERS (ID)
);

create table USERPROFILES
(
    PREFERRED_INITIAL_ROLE INTEGER null_to_default,
    USER_ID                BIGINT not null
        primary key,
    constraint FKTQBFJ7B11M8K2CPI7YV4T3RAV
        foreign key (USER_ID) references USERS (ID)
);
