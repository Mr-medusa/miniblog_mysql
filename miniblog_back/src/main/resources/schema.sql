create schema if not exists miniblog collate utf8mb4_0900_ai_ci;

create table if not exists blog
(
	id char(36) not null
		primary key,
	title varchar(50) null,
	author char(16) null,
	blogTags varchar(50) null,
	previewContent varchar(500) null,
	htmlContent mediumtext null,
	updateTime date null,
	isShowBanner tinyint(1) default 1 null
);

create table if not exists blog_tag
(
	id char(10) not null
		primary key,
	tags blob null
);

create table if not exists card
(
	id char(16) not null
		primary key,
	content text null,
	updateTime date null,
	isChecked tinyint(1) null,
	editable tinyint(1) null
);

create table if not exists pad
(
	id char(36) null,
	parentId char(36) null,
	name varchar(100) null,
	type varchar(10) null,
	isCard tinyint(1) default 0 null,
	isShow tinyint(1) default 1 null,
	isChecked tinyint(1) default 0 null,
	isStretch tinyint(1) default 1 null,
	isDefault tinyint(1) default 0 null,
	createTime date null,
	updateTime date null,
	`order` int default 0 null
);

create table if not exists test
(
	id int auto_increment
		primary key,
	name varchar(50) null
);

create table if not exists test_tow
(
	id int null,
	name varchar(33) null
);

