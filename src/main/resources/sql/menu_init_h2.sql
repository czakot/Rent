
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

-- todo make realestates singular
insert into MENUNODES VALUES ('realestate', null, null);
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestate', 'OWNER');
insert into MENUNODES VALUES ('realestate/list', null, 'realestate');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestate/list', null);
insert into MENUNODES VALUES ('realestate/import', null, 'realestate');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestate/import', null);
insert into MENUNODES VALUES ('realestate/manualadd', null, 'realestate');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('realestate/manualadd', null);

insert into MENUNODES VALUES ('userprofile', null, null);
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('userprofile', null);
insert into MENUNODES VALUES ('userprofile/basics', null, 'userprofile');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('userprofile/basics', null);
insert into MENUNODES VALUES ('userprofile/preferences', null, 'userprofile');
    insert into MATCHERS (MENU_NODE_REFERENCE, ROLE) VALUES ('userprofile/preferences', null);

-- ADMIN, OWNER, TENANT, TRUSTEE, OBSERVER;
