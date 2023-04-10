insert into member(email, password, member_type, member_status, sign_up_type)
values('test@gmail.com', '$2a$10$ImhqIF7271ug7sbMM32tqOgydi6xED0K/MXQnpbOSWaUYFODmMwY.', 'CUSTOMER', 'IN_USE','EMAIL')
on duplicate key update email = 'test@gmail.com';

insert into member(email, password, member_type, member_status, sign_up_type)
values('test2@gmail.com', '$2a$10$ImhqIF7271ug7sbMM32tqOgydi6xED0K/MXQnpbOSWaUYFODmMwY.', 'CUSTOMER','IN_USE', 'EMAIL')
on duplicate key update email = 'test2@gmail.com';

insert into member(email, password, member_type, member_status, sign_up_type)
values('test3@gmail.com', '$2a$10$ImhqIF7271ug7sbMM32tqOgydi6xED0K/MXQnpbOSWaUYFODmMwY.', 'CUSTOMER','IN_USE', 'EMAIL')
on duplicate key update email = 'test3@gmail.com';

/*insert into member(email, password, member_type, member_status, sign_up_type)
values('simdev1234@gmail.com', '$2a$10$ImhqIF7271ug7sbMM32tqOgydi6xED0K/MXQnpbOSWaUYFODmMwY.', 'CUSTOMER','IN_USE', 'EMAIL')
    on duplicate key update email = 'simdev1234@gmail.com';*/