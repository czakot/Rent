insert into MENUNODES (CONTROLLER_URI, REFERENCE) VALUES ('/noticeboard', 'noticeboard');

insert into MENUNODES (CONTROLLER_URI, REFERENCE) VALUES ('/dashboard', 'dashboard');
    insert into MATCHERS VALUES ('dashboard', 'ADMIN');

insert into MENUNODES (CONTROLLER_URI, REFERENCE) VALUES (null, 'realestate');
    insert into MATCHERS VALUES ('realestate', 'OWNER');
insert into MENUNODES (CONTROLLER_URI, REFERENCE) VALUES ('/realestatelist', 'realestatelist');
    insert into MATCHERS VALUES ('realestatelist', 'OWNER');
    insert into MENUNODEHIERARCHY (CHILD_REFERENCE, PARENT_REFERENCE) VALUES ('realestatelist', 'realestate');
insert into MENUNODES (CONTROLLER_URI, REFERENCE) VALUES ('/realestateimport', 'realestateimport');
    insert into MATCHERS VALUES ('realestateimport', 'OWNER');
    insert into MENUNODEHIERARCHY (CHILD_REFERENCE, PARENT_REFERENCE) VALUES ('realestateimport', 'realestate');
insert into MENUNODES (CONTROLLER_URI, REFERENCE) VALUES ('/realestatemanualadd', 'realestatemanualadd');
    insert into MATCHERS VALUES ('realestatemanualadd', 'OWNER');
    insert into MENUNODEHIERARCHY (CHILD_REFERENCE, PARENT_REFERENCE) VALUES ('realestatemanualadd', 'realestate');

insert into MENUNODES (CONTROLLER_URI, REFERENCE) VALUES ('/userprofile', 'userprofile');
    insert into MATCHERS VALUES ('userprofile', 'ADMIN');
    insert into MATCHERS VALUES ('userprofile', 'OWNER');


-- ADMIN, OWNER, TENANT, TRUSTEE, OBSERVER;
