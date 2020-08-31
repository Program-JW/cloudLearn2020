-- 2020-04-25 18:26 alan.zhao
-- 之前这个表没有使用,可以先删除再重建
drop table lightning_issue_category;
create table lightning_issue_category
(
    id              int auto_increment comment '主键' primary key,
    name            varchar(50)                         not null comment '分类名称',
    user_id         int                                 null comment '处理人ID',
    user_name       varchar(50)                         null comment '处理人名称',
    sort            int                                 null comment '排序',
    definition_key  varchar(128)                        null comment '流程定义key',
    created_by      int                                 null comment '创建人ID',
    created_on      timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int                                 null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改日期',
    status          int(2)    default 1                 not null comment '状态： -1 作废 0 禁用 1 启用'
) comment '闪电链问题单分类表';
-- 初始数据
INSERT INTO `lightning_issue_category`(`id`, `name`, `user_id`, `user_name`, `sort`, `definition_key`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `status`) VALUES (1, '查询商品到货情况', 1304, '马雨薇', 10, 'lightning-chain', 1115, '2020-04-29 12:00:29', 1115, '2020-04-29 12:00:29', 1);
INSERT INTO `lightning_issue_category`(`id`, `name`, `user_id`, `user_name`, `sort`, `definition_key`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `status`) VALUES (2, '取消订单', 1385, '程毅', 9, 'lightning-chain', 1115, '2020-04-29 12:00:29', 1115, '2020-04-29 12:00:29', 1);
INSERT INTO `lightning_issue_category`(`id`, `name`, `user_id`, `user_name`, `sort`, `definition_key`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `status`) VALUES (3, '售后进度查询', 669, '金佳杰', 8, 'lightning-chain', 1115, '2020-04-29 12:00:29', 1115, '2020-04-29 12:00:29', 1);

-- 增加 version 字段
ALTER TABLE task ADD COLUMN version int(11) DEFAULT 0 COMMENT '版本号';
-- 修改历史数据
UPDATE task SET version = 0;
-- 更新通知模板
UPDATE notice_template SET content = '问题描述：#{[description]}...#{[br]}当前受理人：#{[receiverName]}#{[br]}发送时间：#{[unreadChatSendTime]}#{[br]}发送内容：#{[unreadChatMessage]}' WHERE id = 11;

-- ----------------------------------------------------------------------------------
-- 2020-05-15 18:00 alan.zhao
ALTER TABLE lightning_issue_category ADD COLUMN rule_id int(11) COMMENT '绑定的规则ID' after user_name;
CREATE TABLE duty_rule
(
    id              int auto_increment comment '主键' primary key,
    name            varchar(50)                         null comment '策略名称',
    scope_type      int                                 null comment '使用范围标记 1. 产品技术专用',
    is_pre_defined  int       default 0                 null comment '是否预定义的 1 是 0 否',
    type            int                                 not null comment '值班策略 1 按天轮流分配 2 按问题轮流分配',
    department_id   int                                 not null comment '部门ID',
    user_ids        text                                null comment '值班人配置. 对类型2有效',
    created_by      int                                 null comment '创建人ID',
    created_on      timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int                                 null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改日期',
    status          int(2)                              not null comment '状态：0 禁用 1 启用'
) COMMENT '值班策略';
CREATE TABLE duty_plan
(
    id              int auto_increment comment '主键' primary key,
    rule_id         int                                 not null comment '所属规则ID',
    person_id       int                                 not null comment '值班人',
    person_name     varchar(100)                        null comment '值班人姓名',
    duty_date       timestamp                           not null comment '值班日期',
    created_by      int                                 null comment '创建人ID',
    created_on      timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int                                 null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改日期'
) COMMENT '按天轮流的排班表';
-- 策略名唯一索引
ALTER TABLE `duty_rule` ADD UNIQUE (`name`);
-- 分类名唯一索引
ALTER TABLE `lightning_issue_category` ADD UNIQUE (`name`);
-- 值班日期唯一索引
ALTER TABLE `duty_plan` ADD UNIQUE (`rule_id`,`duty_date`);
-- 初始数据
-- 增加系统问题分类
INSERT INTO `lightning_issue_category`(`name`, `user_id`, `user_name`, `rule_id`, `sort`, `definition_key`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `status`) VALUES ('系统问题', NULL, NULL, 1, 7, 'lightning-chain', 1115, '2020-05-15 13:14:00', 1115, '2020-05-15 13:14:00', 1);
-- 插入产品技术部的默认值班策略
INSERT INTO `duty_rule`(`id`, `name`, `scope_type`, `is_pre_defined`, `type`, `department_id`, `user_ids`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`, `status`) VALUES (1, '产品技术值班策略(按天)', 1, 1, 1, 18, NULL, 1115, '2020-05-14 04:57:23', 1115, '2020-05-14 05:12:59', 1);
-- 插入产品技术部的默认值班策略的排班数据
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (1, 1, 885, '袁伟玲', '2020-01-16 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (2, 1, 883, '王懿浈', '2020-01-17 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (3, 1, 1125, '邓子其', '2020-01-18 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (4, 1, 885, '袁伟玲', '2020-01-19 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (5, 1, 1305, '郭倩君', '2020-01-20 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (6, 1, 717, '肖璐', '2020-01-21 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (7, 1, 717, '肖璐', '2020-01-22 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (8, 1, 717, '肖璐', '2020-01-31 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (9, 1, 717, '肖璐', '2020-02-01 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (10, 1, 1669, '叶凯', '2020-02-02 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (11, 1, 1325, '樊笑然', '2020-02-03 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (12, 1, 1125, '邓子其', '2020-02-04 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (13, 1, 717, '肖璐', '2020-02-05 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (14, 1, 1305, '郭倩君', '2020-02-06 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (15, 1, 891, '陈历', '2020-02-07 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (16, 1, 1392, '操景红', '2020-02-08 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (17, 1, 1338, '王争艳', '2020-02-09 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (18, 1, 885, '袁伟玲', '2020-02-10 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (19, 1, 717, '肖璐', '2020-02-11 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (20, 1, 717, '肖璐', '2020-02-12 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (21, 1, 883, '王懿浈', '2020-02-13 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (22, 1, 1305, '郭倩君', '2020-02-14 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (23, 1, 1388, '柏仁杰', '2020-02-15 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (24, 1, 1383, '陈振亚', '2020-02-16 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (25, 1, 1325, '肖璐换成樊笑然', '2020-02-17 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (26, 1, 717, '肖璐', '2020-02-18 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (27, 1, 717, '樊笑然换成肖璐', '2020-02-19 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (28, 1, 891, '陈历', '2020-02-20 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (29, 1, 1125, '邓子其', '2020-02-21 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (30, 1, 1491, '楚森', '2020-02-22 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (31, 1, 1364, '孙静静', '2020-02-23 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (32, 1, 885, '袁伟玲', '2020-02-24 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (33, 1, 883, '王懿浈', '2020-02-25 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (34, 1, 1305, '郭倩君', '2020-02-26 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (35, 1, 717, '肖璐', '2020-02-27 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (36, 1, 717, '肖璐', '2020-02-28 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (37, 1, 898, '邓团', '2020-02-29 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (38, 1, 1334, '宫新程', '2020-03-01 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (39, 1, 1325, '许凯1403换成樊笑然', '2020-03-02 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (40, 1, 717, '肖璐', '2020-03-03 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (41, 1, 717, '肖璐', '2020-03-04 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (42, 1, 891, '陈历', '2020-03-05 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (43, 1, 1125, '邓子其', '2020-03-06 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (44, 1, 1403, '许凯', '2020-03-07 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (45, 1, 1325, '樊笑然', '2020-03-08 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (46, 1, 717, '肖璐', '2020-03-09 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (47, 1, 717, '肖璐', '2020-03-10 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (48, 1, 885, '袁伟玲', '2020-03-11 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (49, 1, 883, '王懿浈', '2020-03-12 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (50, 1, 1305, '郭倩君', '2020-03-13 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (51, 1, 1553, '何毅', '2020-03-14 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (52, 1, 1538, '王冲', '2020-03-15 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (53, 1, 1325, '樊笑然', '2020-03-16 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (54, 1, 891, '陈历', '2020-03-17 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (55, 1, 1125, '邓子其', '2020-03-18 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (56, 1, 717, '肖璐', '2020-03-19 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (57, 1, 717, '肖璐', '2020-03-20 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (58, 1, 1339, '徐静', '2020-03-21 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (59, 1, 2036, '杜明宏', '2020-03-22 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (60, 1, 885, '袁伟玲', '2020-03-23 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (61, 1, 883, '王懿浈', '2020-03-24 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (62, 1, 717, '肖璐', '2020-03-25 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (63, 1, 717, '肖璐', '2020-03-26 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (64, 1, 883, '郭倩君1305--换成王懿浈883', '2020-03-27 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (65, 1, 2033, '俞霞菲', '2020-03-28 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (66, 1, 1338, '王争艳', '2020-03-29 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (67, 1, 717, '肖璐', '2020-03-30 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (68, 1, 1325, '肖璐717--换成樊笑然1325', '2020-03-31 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (69, 1, 1325, '樊笑然', '2020-04-01 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (70, 1, 891, '陈历', '2020-04-02 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (71, 1, 1305, '邓子其1125--换成郭倩君1305', '2020-04-03 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (72, 1, 2022, '潘卓平', '2020-04-04 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (73, 1, 897, '白冰907--换成张亚中897', '2020-04-05 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (74, 1, 1388, '柏仁杰', '2020-04-06 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (75, 1, 885, '袁伟玲', '2020-04-07 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (76, 1, 883, '王懿浈', '2020-04-08 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (77, 1, 1305, '郭倩君', '2020-04-09 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (78, 1, 1325, '樊笑然', '2020-04-10 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (79, 1, 1392, '操景红', '2020-04-11 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (80, 1, 888, '曹金柱', '2020-04-12 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (81, 1, 891, '陈历', '2020-04-13 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (82, 1, 1125, '邓子其', '2020-04-14 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (83, 1, 885, '袁伟玲', '2020-04-15 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (84, 1, 883, '王懿浈', '2020-04-16 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (85, 1, 1305, '郭倩君', '2020-04-17 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (86, 1, 958, '陈钢', '2020-04-18 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (87, 1, 1315, '陈钢1', '2020-04-19 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (88, 1, 1325, '樊笑然', '2020-04-20 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (89, 1, 891, '陈历', '2020-04-21 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (90, 1, 1125, '邓子其', '2020-04-22 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (91, 1, 885, '袁伟玲', '2020-04-23 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (92, 1, 883, '王懿浈', '2020-04-24 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (93, 1, 1383, '陈振亚', '2020-04-25 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (94, 1, 1305, '郭倩君', '2020-04-26 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (95, 1, 1325, '樊笑然', '2020-04-27 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (96, 1, 891, '陈历', '2020-04-28 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (97, 1, 1125, '邓子其', '2020-04-29 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (98, 1, 885, '袁伟玲', '2020-04-30 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (99, 1, 1491, '楚森', '2020-05-01 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (100, 1, 2036, '杜明宏', '2020-05-02 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (101, 1, 909, '邓小明', '2020-05-03 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (102, 1, 1096, '董净茜', '2020-05-04 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (103, 1, 905, '董松松', '2020-05-05 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (104, 1, 1305, '王懿浈--换成郭倩君', '2020-05-06 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (105, 1, 883, '郭倩君--换成王懿浈', '2020-05-07 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (106, 1, 1325, '樊笑然', '2020-05-08 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (107, 1, 891, '陈历', '2020-05-09 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (108, 1, 898, '邓团', '2020-05-10 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (109, 1, 1125, '邓子其', '2020-05-11 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (110, 1, 1325, '袁伟铃--换成樊笑然', '2020-05-12 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (111, 1, 883, '王懿浈', '2020-05-13 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (112, 1, 1305, '郭倩君', '2020-05-14 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (113, 1, 885, '樊笑然--换成袁伟铃', '2020-05-15 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (114, 1, 893, '冯居原', '2020-05-16 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (115, 1, 1334, '宫新程', '2020-05-17 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (116, 1, 891, '陈历', '2020-05-18 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (117, 1, 1125, '邓子其', '2020-05-19 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (118, 1, 885, '袁伟玲', '2020-05-20 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (119, 1, 883, '王懿浈', '2020-05-21 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (120, 1, 1305, '郭倩君', '2020-05-22 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (121, 1, 1553, '何毅', '2020-05-23 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (122, 1, 1355, '侯玉婷', '2020-05-24 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (123, 1, 1325, '樊笑然', '2020-05-25 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (124, 1, 891, '陈历', '2020-05-26 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (125, 1, 1125, '邓子其', '2020-05-27 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (126, 1, 885, '袁伟玲', '2020-05-28 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (127, 1, 883, '王懿浈', '2020-05-29 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (128, 1, 904, '侯占义', '2020-05-30 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');
INSERT INTO `duty_plan`(`id`, `rule_id`, `person_id`, `person_name`, `duty_date`, `created_by`, `created_on`, `last_updated_by`, `last_updated_on`) VALUES (129, 1, 957, '黄承晟', '2020-05-31 00:00:00', NULL, '2020-05-19 20:39:47', NULL, '2020-05-19 20:39:47');

-- 2020-07-23 alan.zhao 可靠消息发送日志
create table reliable_mq_log
(
    id                  bigint unsigned auto_increment primary key,
    current_retry_times int             default 0                 not null comment '当前重试次数',
    max_retry_times     int             default 5                 not null comment '最大重试次数',
    exchange_name       varchar(255)                              not null comment '交换器名',
    exchange_type       varchar(8)                                not null comment '交换类型',
    routing_key         varchar(255)                              null comment '路由键',
    content             text                                      null comment '消息内容',
    business_module     varchar(32)                               not null comment '业务模块',
    business_key        varchar(255)                              not null comment '业务键',
    next_schedule_time  datetime                                  not null comment '下一次调度时间',
    message_status      tinyint         default 0                 not null comment '消息状态',
    init_backoff        bigint unsigned default 10                not null comment '退避初始化值,单位为秒',
    backoff_factor      tinyint         default 2                 not null comment '退避因子(也就是指数)',
    created_by          int             default -1                not null comment '创建人',
    created_at          datetime        default CURRENT_TIMESTAMP not null comment '创建时间',
    last_updated_by     int             default -1                not null comment '更新人',
    last_updated_at     datetime        default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
) comment '保证成功的mq消息表';
create index idx_business_key on reliable_mq_log (business_key);
create index idx_created_at on reliable_mq_log (created_at);
create index idx_next_schedule_time on reliable_mq_log (next_schedule_time);
