drop database if exists `initialize`;
create database `initialize` default character set utf8mb4 collate utf8mb4_general_ci;

drop table if exists `test`;
create table `test` (
	`id` int(11) auto_increment comment '主键',
	`name` varchar(20) comment '名称',
	primary key (`id`) using btree
) engine=InnoDB character set=utf8mb4 collate=utf8mb4_general_ci comment='test' row_format=Dynamic;

INSERT INTO test(`name`) VALUES('a'); 
INSERT INTO test(`name`) VALUES('b'); 