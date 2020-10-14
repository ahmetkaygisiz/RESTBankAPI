INSERT INTO ROLES (role_id, name) values(1, 'ADMIN');
INSERT INTO ROLES (role_id, name) values(2, 'USER');
INSERT INTO ROLES (role_id, name) values(3, 'NOBODY');

INSERT INTO USERS (user_id, first_name, last_name, email, password, phone_number, active)
        values(1,'test','test','test@mail.com','$2a$10$Zc4Vk/kaVMOBAX0XzC.P3O20fYTpHePwbKefMCdTAyFmt9mkiRZAa','1231231232',1);

INSERT INTO USERS_ROLES (user_id, role_id) VALUES (1, 1);
INSERT INTO USERS_ROLES (user_id, role_id) VALUES (1, 2);

