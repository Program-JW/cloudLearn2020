create table lightning_issue_apply
(
    id int auto_increment comment '主键'
        primary key,
    category_id int null comment '问题所属分类ID',
    description varchar(255) null comment '问题描述',
    expected_solver int null comment '期望处理人',
    definition_id varchar(64) null comment '流程定义id',
    instance_id varchar(64) null comment '流程id',
    attachments text null comment '附件清单，逗号分隔的文件列表',
    issue_reason text null comment '问题原因',
    issue_department_id int null comment '问题部门id',
    is_demand tinyint(2) null comment '是否转化为需求：0否 1是',
    current_solver_id int null comment '当前处理人id',
    created_by int null comment '创建人ID',
    creator varchar(100) null comment '创建人名称(冗余字段)',
    created_on timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改日期',
    status int(2) not null comment '状态： -1 作废 0 发起 1 待受理 2 受理中 3 待确认 4 已解决 5 未解决  7 已撤销',
    revoke_reason text null comment '撤销原因'
)
comment '闪电链问题申请单';

create table lightning_issue_category
(
    id int auto_increment comment '主键'
        primary key,
    name varchar(255) null comment '问题分类名称',
    definition_key varchar(128) null comment '流程定义key',
    created_by int null comment '创建人ID',
    creator varchar(100) null comment '创建人名称(冗余字段)',
    created_on timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改日期',
    status int(2) not null comment '状态： -1 作废 0 禁用 1 启用'
)
comment '闪电链问题单分类表';

create table lightning_issue_evaluation
(
    id int auto_increment
        primary key,
    issue_id int not null comment '问题id',
    score int(5) null comment '评价分数',
    best_person_id int null comment '最佳处理人id',
    best_person_name varchar(100) null comment '最佳处理人名称(冗余字段)',
    created_by int null comment '创建人',
    created_at timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int null comment '最后更新人',
    last_updated_at timestamp default CURRENT_TIMESTAMP null comment '最后更新时间'
)
comment '闪电链问题申请评价表';

create table lightning_issue_group
(
    id int auto_increment comment 'id'
        primary key,
    issue_id int null comment '问题id',
    group_id varchar(50) null comment '群组id',
    group_name varchar(255) null comment '群组名',
    created_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间'
);

create table lightning_issue_log
(
    id int auto_increment comment '主键'
        primary key,
    issue_id int not null comment '所属问题ID',
    action int null comment '操作类型 0 发起 1 已受理 2 已交接 3 提交确认 4 确认已解决 5 确认未解决 6 撤销 7-超时4小时 8-超时24小时 9-超时48小时',
    created_by int null comment '创建人ID',
    created_on timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    status int(2) default 1 not null comment '状态： -1 作废 1 启用 0 禁用',
    last_updated_by int null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null comment '最后更新时间'
)
comment '闪电链问题处理日志';

create table lightning_issue_relevant_user
(
    id int auto_increment comment '主键'
        primary key,
    issue_id int not null comment '问题id',
    user_id int null comment '人员id',
    created_by int null comment '创建人ID',
    created_on timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改日期'
)
comment '闪电链问题对应相关成员表';
create table duty_roster
(
    id int auto_increment comment '主键'
        primary key,
    person_id int null comment '值班人',
    person_name varchar(100) null comment '值班人姓名',
    duty_date timestamp default CURRENT_TIMESTAMP not null comment '值班日期'
);
create table rabbitmq_msg
(
    id int auto_increment comment 'id'
        primary key,
    message text null,
    created_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    status int(2) default 1 null comment '0未消费 1已消费',
    task_id_weixin varchar(64) null
);

create table ws_api_log
(
    id int auto_increment comment '主键'
        primary key,
    message_id varchar(128) null comment '请求唯一标识',
    issue_id int null comment '问题ID',
    send_message text null comment '请求参数',
    return_message text null comment '响应结果',
    created_on timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    created_by int null comment '创建人ID',
    constraint index_request_id
        unique (message_id) comment '请求ID的唯一索引'
);

