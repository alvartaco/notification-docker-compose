CREATE SCHEMA notifications;
USE notifications;

drop TABLE IF EXISTS notification;
drop TABLE IF EXISTS message;
drop TABLE IF EXISTS category;

create table if not exists category (
   category_id smallint AUTO_INCREMENT not null primary key,
   category_name varchar(30) not null
);

insert into category values(1,'Finance');
insert into category values(2,'Movies');
insert into category values(3,'Sports');

create table if not exists message (
   message_id int AUTO_INCREMENT not null primary key,
   category_id smallint not null,
   message_body text not null,
   message_created_on timestamp not null,
   message_creator_id varchar(30) not null,
   constraint fk_category
      foreign key(category_id)
        references category(category_id)
);

--
-- Tables indexes creation.
-- They depends on DB Engine used
--
-- I relly on PK indexes for H2
-- Needed for Postgres/mysql
CREATE INDEX idx_category_category_id ON category(category_id);
CREATE INDEX idx_message_message_id ON message(message_id);
--
-- I trust also in FK Index for H2
-- Needed for Postgres/mysql
CREATE INDEX idx_message_category_id ON message(category_id);
--
-- Because queries look at this columns for
-- retrieval and sorting
--
create index idx_category_category_name on category(category_name);
create index idx_message_message_created_on on message(message_created_on);

create table if not exists notification (
   notification_id int AUTO_INCREMENT not null primary key,
   message_id int not null,
   message_category_id int not null,
   user_id int not null,
   notification_channel_type varchar(30) not null,
   notification_status varchar(30) not null,
   notification_created_on timestamp not null,
   notification_updated_on timestamp ,
   notification_retry_number smallint,
   constraint fk_message
      foreign key(message_id)
        references message(message_id)
);
create index idx_notification_message_id on notification(message_id);
create index idx_notification_message_category_id on notification(message_category_id);
create index idx_notification_user_id on notification(user_id);
create index idx_notification_channel_type on notification(notification_channel_type);
create index idx_notification_status on notification(notification_status);
create index idx_notification_notification_created_on on notification(notification_created_on);
create index idx_notification_notification_updated_on on notification(notification_updated_on);