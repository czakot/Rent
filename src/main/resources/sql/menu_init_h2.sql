
set schema PUBLIC;
drop table if exists MATCHERS;
drop table if exists MENUNODES;

create table MENUNODES
(
    REFERENCE        VARCHAR(255) not null primary key,
    CONTROLLER_URI   VARCHAR(255) unique,
    PARENT_REFERENCE VARCHAR(255),
        foreign key (PARENT_REFERENCE) references MENUNODES (REFERENCE)
);

create table MATCHERS
(
    KEY                 BIGINT primary key AUTO_INCREMENT,
    MENU_NODE_REFERENCE VARCHAR(255) not null,
    ROLE                VARCHAR(255) null,
        foreign key (MENU_NODE_REFERENCE) references MENUNODES (REFERENCE)
);


insert into MENUNODES VALUES ('noticeboard', null, null);
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('noticeboard', null);

insert into MENUNODES VALUES ('dashboard', null, null);
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('dashboard', 'ADMIN');

insert into MENUNODES VALUES ('realestates', null, null);
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestates', 'OWNER');
insert into MENUNODES VALUES ('realestatelist', null, 'realestates');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestatelist', null);
insert into MENUNODES VALUES ('realestateimport', null, 'realestates');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestateimport', null);
insert into MENUNODES VALUES ('realestatemanualadd', null, 'realestates');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestatemanualadd', null);

insert into MENUNODES VALUES ('userprofile', null, null);
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('userprofile', 'ADMIN');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('userprofile', 'OWNER');


-- ADMIN, OWNER, TENANT, TRUSTEE, OBSERVER;
