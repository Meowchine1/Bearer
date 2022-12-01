create table newuserdata(
    user_id integer primary key not null,
    first_name varchar(20) not null,
    password text not null,
    salt text not null
);


