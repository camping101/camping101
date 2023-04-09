insert into member(email, password, member_type, member_status, sign_up_type)
values('test@gmail.com', '$2a$12$hWck1.7NqB0/ns6xOUtweecrOIp1XDqTpaPMR76WiNjhy7BsmGOTa', 'CUSTOMER', 'IN_USE','EMAIL')
on duplicate key update email = 'test@gmail.com';

insert into member(email, password, member_type, member_status, sign_up_type)
values('test2@gmail.com', '$2a$12$hWck1.7NqB0/ns6xOUtweecrOIp1XDqTpaPMR76WiNjhy7BsmGOTa', 'CUSTOMER','IN_USE', 'EMAIL')
on duplicate key update email = 'test@gmail.com';

insert into member(email, password, member_type, member_status, sign_up_type)
values('test3@gmail.com', '$2a$12$hWck1.7NqB0/ns6xOUtweecrOIp1XDqTpaPMR76WiNjhy7BsmGOTa', 'CUSTOMER','IN_USE', 'EMAIL')
on duplicate key update email = 'test@gmail.com';