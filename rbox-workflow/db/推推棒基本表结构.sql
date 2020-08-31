create table work_day
(
    id bigint auto_increment comment '主键'
        primary key,
    description varchar(100) null comment '描述',
    year int null comment '年',
    date_of_year date null,
    day_of_year int null comment '第几天',
    is_working_day int null comment '是否是工作日 0休息,1上班',
    start_time time null comment '开始时间',
    end_time time null comment '结束时间',
    reason varchar(255) null comment '1 正常 2 交换'
);

create table work_mode
(
    id int auto_increment comment '主键'
        primary key,
    name varchar(100) null comment '名称',
    day int null comment '每周的第几天',
    start_time time null comment '开始时间',
    end_time time null comment '结束时间',
    code varchar(50) null
);

create table work_order
(
    id int not null comment '主键'
        primary key,
    code varchar(64) not null comment '工单编号',
    type int not null comment '工单类型 1,咨询,2投诉,3 建议',
    title varchar(255) not null comment '工单标题',
    detail longtext null comment '工单详情',
    custom_id varchar(0) null comment '客户ID',
    custom_name varchar(200) null comment '客户姓名',
    custom_phone varchar(20) null comment '客户电话',
    sku_code varchar(64) null comment '产品编号',
    order_code varchar(64) null comment '订单编号',
    stage int default 0 not null comment '处理状态,0待处理 1处理中 3处理完成',
    is_solved int default 0 not null comment '是否解决 0未解决 1已解决',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by varchar(255) collate utf8_unicode_ci null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null comment '最后修改日期',
    last_updated_by int null comment '最后修改者',
    status tinyint(2) default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
)
comment '工单表';

create table workflow_category
(
    id bigint auto_increment comment '主键'
        primary key,
    name varchar(200) not null comment '名称',
    definition_key varchar(128) null comment '所绑定的流程定义code',
    parent_id bigint unsigned default 0 null comment '父分类Id',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status tinyint(2) default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
);

create table workflow_definition
(
    id varchar(64) not null comment '主键'
        primary key,
    name varchar(200) not null comment '流程名称',
    description varchar(255) null comment '流程描述',
    initial_code varchar(128) not null comment '流程初始编码,不随版本变化而改变',
    version int not null comment '版本号',
    is_released int default 0 not null comment '是否已发布,1 是 0 否 默认为0',
    deployment_id varchar(64) null comment '部署ID;启动流程实例需要此参数',
    business_url text null comment '任务详情页面',
    pc_business_url text null comment '业务详情页面(PC版)',
    mobile_business_url text null comment '业务详情页面(动端版)',
    initial_url text null comment '提交详情页面',
    mobile_initial_url text null comment '提交详情页面(移动端)',
    pc_initial_url text null comment '提交详情页面(PC版)',
    outline text null comment '概要(业务参数展示）',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status int(2) default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
)
comment '流程定义表';
create table node
(
    id varchar(128) not null comment '主键'
        primary key,
    model_id varchar(64) null comment '所属流程定义的ID',
    graph_id varchar(64) null comment '流程图中的节点id',
    name varchar(50) null comment '名称',
    description varchar(255) null comment '描述',
    type int null comment '节点类型,1 用户任务',
    due_time varchar(50) null comment '工作时间,单位是分钟',
    approval_node int null comment '是否是审批节点 1 是 0 否',
    candidate_users text null comment '候选用户,逗号分隔的id列表',
    candidate_groups text null comment '候选用户组,逗号分隔的id列表',
    form_location varchar(255) null comment '表单位置',
    form_content longtext null comment '表单内容',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status tinyint(2) default 0 not null comment '状态：-1废弃，0 禁用,1 启用',
    remarks text null,
    notice_type int(2) null comment '通知类型',
    notice_content text null comment '通知内容（卡片通知是按钮样式）',
    business_url text null,
    summary text null comment '概要信息模板'
);


create table workflow_form
(
    id bigint auto_increment comment '主键'
        primary key,
    definition_id varchar(64) null comment '所属流程定义ID',
    form_content longtext null comment '表单内容',
    select_form_content longtext null comment '筛选表单',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status tinyint(2) default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
)
comment '流程启动时的表单';

create table workflow_history
(
    id varchar(64) not null comment '主键'
        primary key,
    name varchar(255) not null comment '流程实例的名称',
    business_key varchar(255) null comment '业务数据的唯一标识符',
    business_url text null comment '业务详情页面',
    mobile_business_url text null comment '业务详情页面(动端版)',
    pc_business_url text null comment '业务详情页面(PC版)',
    definition_id varchar(64) null comment '流程定义的ID',
    definition_version int null comment '流程定义的版本号',
    definition_code varchar(128) not null comment '流程定义的code',
    delete_reason varchar(255) null comment '作废原因',
    owner_id bigint null comment '所属人ID',
    start_time timestamp null comment '启动时间',
    end_time datetime null comment '停止时间',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status tinyint(2) default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
)
comment '流程实例表';

create table workflow_instance
(
    id varchar(64) not null comment '主键'
        primary key,
    name varchar(255) not null comment '流程实例的名称',
    business_key varchar(255) null comment '业务数据的唯一标识符',
    business_url text null comment '业务详情页面',
    mobile_business_url text null comment '业务详情页面(动端版)',
    pc_business_url text null comment '业务详情页面(PC版)',
    definition_id varchar(64) null comment '流程定义的ID',
    definition_version int null comment '流程定义的版本号',
    definition_code varchar(128) not null comment '流程定义的code',
    delete_reason varchar(255) null comment '作废原因',
    owner_id bigint null comment '所属人ID',
    start_time timestamp null comment '启动时间',
    end_time datetime null comment '停止时间',
    source_platform varchar(50) null comment '来源平台，如MP',
    source_platform_user_id varchar(30) null comment '源平台的用户ID',
    source_platform_user_name varchar(50) null comment '源平台的用户名',
    created_on timestamp null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status int(2) default 0 not null comment '状态：-1废弃，0 待启动,1运行中,2暂停'
)
comment '流程实例表';

create table task
(
    id varchar(64) not null comment '主键'
        primary key,
    node_id varchar(128) null comment '所属任务定义的ID',
    model_id varchar(64) null comment '所属流程定义的ID',
    instance_id varchar(64) null comment '流程实例ID',
    graph_id varchar(64) null comment '流程图中的节点id',
    name varchar(50) null comment '名称',
    description varchar(255) null comment '描述',
    type int null comment '节点类型,1 用户任务',
    approval_node int null comment '是否是审批节点 1 是 0 否',
    due_time varchar(50) null comment '工作时间数,单位是分钟',
    candidate_users text null comment '候选用户,逗号分隔的id列表',
    candidate_groups text null comment '候选用户组,逗号分隔的id列表',
    form_location varchar(255) null comment '表单位置',
    form_content longtext null comment '表单内容',
    data longtext null comment '任务数据',
    submit_by bigint null comment '提交人ID',
    submit_time timestamp null comment '提交时间',
    begin_time timestamp null comment '任务开始时间',
    business_url text null,
    notice_type int(2) null comment '通知类型',
    notice_content text null,
    remarks text null comment '备注',
    created_by int null comment '创建者',
    created_on timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int null comment '最后修改者',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改日期',
    status int(2) not null comment '状态：0未处理，1 处理中, 2 处理完成',
    summary text null comment '概要信息模板'
)
    comment '任务扩展表';

create table task_comment
(
    id int auto_increment comment '评论id'
        primary key,
    content text null comment '评论内容',
    parent_id int null comment '父评论id 0为一级评论',
    created_by int null comment '创建人',
    created_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    last_updated_by int null comment '最后修改人',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改时间',
    status int(1) default 1 null comment '是否有效 0无效 1有效',
    task_id varchar(64) null comment '任务id'
)
    comment '任务评论表';

create table task_submit_batch_log
(
    id int auto_increment
        primary key,
    total int null,
    fail int null,
    success int null,
    fail_id text null,
    success_id text null,
    created_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    created_by int null
);
create table business_param
(
    id int auto_increment comment 'id'
        primary key,
    definition_code varchar(64) null,
    instance_id varchar(64) null,
    param_key varchar(255) null,
    param_value varchar(255) null,
    find_way int(2) null comment '数据库查找方式'
);
create table user_group
(
    id int auto_increment comment 'id'
        primary key,
    name varchar(255) null,
    remark varchar(255) null,
    created_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    created_by int null,
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    last_updated_by int null comment '最后修改人',
    status int(1) default 1 null comment '状态：0停用 1启用'
);

create table user_group_asso
(
    id int auto_increment comment 'id'
        primary key,
    group_id int null comment '用户组id',
    user_id int null comment '用户id',
    created_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    created_by int null comment '创建人',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改时间',
    last_updated_by int null comment '最后修改人',
    status int(1) default 1 null comment '状态：0停用 1启用'
);
create table node_notice_config
(
    id bigint auto_increment comment '主键'
        primary key,
    node_id varchar(128) null comment '节点ID',
    enabled int null comment '是否启用 1 启用 0 关闭',
    event int null comment '适用事件类型',
    template_id bigint null comment '通知模板的ID',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status int default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
);

create table notice
(
    id bigint auto_increment comment '内容'
        primary key,
    type int(2) null comment '通知类型 0 超时',
    title varchar(255) null comment '标题',
    content text null comment '内容',
    notice_url text null,
    targets varchar(255) null comment '送达目标',
    definition_id varchar(64) null,
    instance_id varchar(64) null,
    task_id varchar(64) null comment '任务ID',
    task_id_weixin varchar(64) null,
    count int default 1 null comment '通知次数',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改时间',
    status int(2) default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
)
    comment '通知';

create table notice_template
(
    id bigint auto_increment
        primary key,
    event int null comment '适用事件类型 1 流程启动 2 任务创建 3 任务完成 4 任务转办 5 任务删除 6 流程完成',
    channel int null comment '渠道类型 1 企业维系 2 邮件',
    type int(255) null comment '子类型',
    title text null comment '标题模板',
    content text null comment '内容模板',
    detail_url text null comment '消息详情URL',
    button_config text null comment '按钮配置（如果通知为卡片通知，该字段不能为空）',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status int default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
);
create table definition_notice_config
(
    id bigint auto_increment comment '主键'
        primary key,
    definition_id varchar(64) null comment '节点ID',
    enabled int null comment '是否启用 1 启用 0 关闭',
    event int null comment '适用事件类型',
    template_id bigint null comment '通知模板的ID',
    created_on timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    created_by bigint null comment '创建者',
    last_updated_on timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '最后修改日期',
    last_updated_by bigint null comment '最后修改者',
    status int default 0 not null comment '状态：-1废弃，0 禁用,1 启用'
);
create table attachment
(
    id bigint not null comment '主键'
        primary key,
    instance_id varchar(64) null comment '所属实例名称',
    task_id varchar(128) null comment '所属任务定义ID',
    name varchar(255) null comment '文件名称',
    ext varchar(50) null comment '扩展名',
    path varchar(255) null comment '文件路径',
    created_on timestamp null comment '创建时间',
    created_by int null comment '创建者',
    status int(2) default 0 not null comment '状态：-1废弃 0 禁用 1 启用'
)
    comment '流程附件表';
create table api_log
(
    id bigint auto_increment
        primary key,
    object_id int null comment '操作对象表id',
    request_data text null comment '请求参数（json）',
    response_data text null comment '返回参数（json）',
    method_name varchar(100) null comment '调用记录日志的方法名',
    request_url varchar(100) null comment '请求的url',
    create_on timestamp null on update CURRENT_TIMESTAMP comment '创建时间',
    status int(2) null comment '日志状态 0无效 1有效'
);
create table operation_log
(
    id int auto_increment comment '日志id'
        primary key,
    content varchar(255) null,
    owner_id int null comment '负责人id',
    owner_ids varchar(255) null comment '用于存储多个负责人使用',
    definition_id varchar(64) null,
    instance_id varchar(64) null comment '实例id',
    task_id varchar(64) null comment '任务id',
    event varchar(50) null comment '事件类型',
    created_by int null comment '创建人(操作人)',
    created_on timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    last_updated_by int null comment '最后修改人',
    last_updated_on timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改时间',
    show_status int(1) null comment '对外展示状态，用于业务扩展',
    status int(1) null comment '日志状态，默认1'
);









