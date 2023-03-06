insert into member(email, password, member_type, sign_up_type)
values('test@gmail.com', '$2a$10$CMFp18OacHV73jlBIkc3RexsGc4fHOXSKdTVy8T041uTmTO1t3Xv.', 'CUSTOMER', 'EMAIL')
on duplicate key update email = 'test@gmail.com';