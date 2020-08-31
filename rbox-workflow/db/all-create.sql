SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
create table ACT_GE_PROPERTY (
                                 NAME_ varchar(64),
                                 VALUE_ varchar(300),
                                 REV_ integer,
                                 primary key (NAME_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

insert into ACT_GE_PROPERTY
values ('schema.version', '7.0.0.0', 1);

insert into ACT_GE_PROPERTY
values ('schema.history', 'create(7.0.0.0)', 1);

insert into ACT_GE_PROPERTY
values ('next.dbid', '1', 1);

create table ACT_GE_BYTEARRAY (
                                  ID_ varchar(64),
                                  REV_ integer,
                                  NAME_ varchar(255),
                                  DEPLOYMENT_ID_ varchar(64),
                                  BYTES_ LONGBLOB,
                                  GENERATED_ TINYINT,
                                  primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RE_DEPLOYMENT (
                                   ID_ varchar(64),
                                   NAME_ varchar(255),
                                   CATEGORY_ varchar(255),
                                   KEY_ varchar(255),
                                   TENANT_ID_ varchar(255) default '',
                                   DEPLOY_TIME_ timestamp(3) NULL,
                                   ENGINE_VERSION_ varchar(255),
                                   primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RE_MODEL (
                              ID_ varchar(64) not null,
                              REV_ integer,
                              NAME_ varchar(255),
                              KEY_ varchar(255),
                              CATEGORY_ varchar(255),
                              CREATE_TIME_ timestamp(3) null,
                              LAST_UPDATE_TIME_ timestamp(3) null,
                              VERSION_ integer,
                              META_INFO_ varchar(4000),
                              DEPLOYMENT_ID_ varchar(64),
                              EDITOR_SOURCE_VALUE_ID_ varchar(64),
                              EDITOR_SOURCE_EXTRA_VALUE_ID_ varchar(64),
                              TENANT_ID_ varchar(255) default '',
                              primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_EXECUTION (
                                  ID_ varchar(64),
                                  REV_ integer,
                                  PROC_INST_ID_ varchar(64),
                                  BUSINESS_KEY_ varchar(255),
                                  PARENT_ID_ varchar(64),
                                  PROC_DEF_ID_ varchar(64),
                                  SUPER_EXEC_ varchar(64),
                                  ROOT_PROC_INST_ID_ varchar(64),
                                  ACT_ID_ varchar(255),
                                  IS_ACTIVE_ TINYINT,
                                  IS_CONCURRENT_ TINYINT,
                                  IS_SCOPE_ TINYINT,
                                  IS_EVENT_SCOPE_ TINYINT,
                                  IS_MI_ROOT_ TINYINT,
                                  SUSPENSION_STATE_ integer,
                                  CACHED_ENT_STATE_ integer,
                                  TENANT_ID_ varchar(255) default '',
                                  NAME_ varchar(255),
                                  START_TIME_ datetime(3),
                                  START_USER_ID_ varchar(255),
                                  LOCK_TIME_ timestamp(3) NULL,
                                  IS_COUNT_ENABLED_ TINYINT,
                                  EVT_SUBSCR_COUNT_ integer,
                                  TASK_COUNT_ integer,
                                  JOB_COUNT_ integer,
                                  TIMER_JOB_COUNT_ integer,
                                  SUSP_JOB_COUNT_ integer,
                                  DEADLETTER_JOB_COUNT_ integer,
                                  VAR_COUNT_ integer,
                                  ID_LINK_COUNT_ integer,
                                  primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_JOB (
                            ID_ varchar(64) NOT NULL,
                            REV_ integer,
                            TYPE_ varchar(255) NOT NULL,
                            LOCK_EXP_TIME_ timestamp(3) NULL,
                            LOCK_OWNER_ varchar(255),
                            EXCLUSIVE_ boolean,
                            EXECUTION_ID_ varchar(64),
                            PROCESS_INSTANCE_ID_ varchar(64),
                            PROC_DEF_ID_ varchar(64),
                            RETRIES_ integer,
                            EXCEPTION_STACK_ID_ varchar(64),
                            EXCEPTION_MSG_ varchar(4000),
                            DUEDATE_ timestamp(3) NULL,
                            REPEAT_ varchar(255),
                            HANDLER_TYPE_ varchar(255),
                            HANDLER_CFG_ varchar(4000),
                            TENANT_ID_ varchar(255) default '',
                            primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_TIMER_JOB (
                                  ID_ varchar(64) NOT NULL,
                                  REV_ integer,
                                  TYPE_ varchar(255) NOT NULL,
                                  LOCK_EXP_TIME_ timestamp(3) NULL,
                                  LOCK_OWNER_ varchar(255),
                                  EXCLUSIVE_ boolean,
                                  EXECUTION_ID_ varchar(64),
                                  PROCESS_INSTANCE_ID_ varchar(64),
                                  PROC_DEF_ID_ varchar(64),
                                  RETRIES_ integer,
                                  EXCEPTION_STACK_ID_ varchar(64),
                                  EXCEPTION_MSG_ varchar(4000),
                                  DUEDATE_ timestamp(3) NULL,
                                  REPEAT_ varchar(255),
                                  HANDLER_TYPE_ varchar(255),
                                  HANDLER_CFG_ varchar(4000),
                                  TENANT_ID_ varchar(255) default '',
                                  primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_SUSPENDED_JOB (
                                      ID_ varchar(64) NOT NULL,
                                      REV_ integer,
                                      TYPE_ varchar(255) NOT NULL,
                                      EXCLUSIVE_ boolean,
                                      EXECUTION_ID_ varchar(64),
                                      PROCESS_INSTANCE_ID_ varchar(64),
                                      PROC_DEF_ID_ varchar(64),
                                      RETRIES_ integer,
                                      EXCEPTION_STACK_ID_ varchar(64),
                                      EXCEPTION_MSG_ varchar(4000),
                                      DUEDATE_ timestamp(3) NULL,
                                      REPEAT_ varchar(255),
                                      HANDLER_TYPE_ varchar(255),
                                      HANDLER_CFG_ varchar(4000),
                                      TENANT_ID_ varchar(255) default '',
                                      primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_DEADLETTER_JOB (
                                       ID_ varchar(64) NOT NULL,
                                       REV_ integer,
                                       TYPE_ varchar(255) NOT NULL,
                                       EXCLUSIVE_ boolean,
                                       EXECUTION_ID_ varchar(64),
                                       PROCESS_INSTANCE_ID_ varchar(64),
                                       PROC_DEF_ID_ varchar(64),
                                       EXCEPTION_STACK_ID_ varchar(64),
                                       EXCEPTION_MSG_ varchar(4000),
                                       DUEDATE_ timestamp(3) NULL,
                                       REPEAT_ varchar(255),
                                       HANDLER_TYPE_ varchar(255),
                                       HANDLER_CFG_ varchar(4000),
                                       TENANT_ID_ varchar(255) default '',
                                       primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RE_PROCDEF (
                                ID_ varchar(64) not null,
                                REV_ integer,
                                CATEGORY_ varchar(255),
                                NAME_ varchar(255),
                                KEY_ varchar(255) not null,
                                VERSION_ integer not null,
                                DEPLOYMENT_ID_ varchar(64),
                                RESOURCE_NAME_ varchar(4000),
                                DGRM_RESOURCE_NAME_ varchar(4000),
                                DESCRIPTION_ varchar(4000),
                                HAS_START_FORM_KEY_ TINYINT,
                                HAS_GRAPHICAL_NOTATION_ TINYINT,
                                SUSPENSION_STATE_ integer,
                                TENANT_ID_ varchar(255) default '',
                                ENGINE_VERSION_ varchar(255),
                                primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_TASK (
                             ID_ varchar(64),
                             REV_ integer,
                             EXECUTION_ID_ varchar(64),
                             PROC_INST_ID_ varchar(64),
                             PROC_DEF_ID_ varchar(64),
                             NAME_ varchar(255),
                             PARENT_TASK_ID_ varchar(64),
                             DESCRIPTION_ varchar(4000),
                             TASK_DEF_KEY_ varchar(255),
                             OWNER_ varchar(255),
                             ASSIGNEE_ varchar(255),
                             DELEGATION_ varchar(64),
                             PRIORITY_ integer,
                             CREATE_TIME_ timestamp(3) NULL,
                             DUE_DATE_ datetime(3),
                             CATEGORY_ varchar(255),
                             SUSPENSION_STATE_ integer,
                             TENANT_ID_ varchar(255) default '',
                             FORM_KEY_ varchar(255),
                             CLAIM_TIME_ datetime(3),
                             primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_IDENTITYLINK (
                                     ID_ varchar(64),
                                     REV_ integer,
                                     GROUP_ID_ varchar(255),
                                     TYPE_ varchar(255),
                                     USER_ID_ varchar(255),
                                     TASK_ID_ varchar(64),
                                     PROC_INST_ID_ varchar(64),
                                     PROC_DEF_ID_ varchar(64),
                                     primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_VARIABLE (
                                 ID_ varchar(64) not null,
                                 REV_ integer,
                                 TYPE_ varchar(255) not null,
                                 NAME_ varchar(255) not null,
                                 EXECUTION_ID_ varchar(64),
                                 PROC_INST_ID_ varchar(64),
                                 TASK_ID_ varchar(64),
                                 BYTEARRAY_ID_ varchar(64),
                                 DOUBLE_ double,
                                 LONG_ bigint,
                                 TEXT_ varchar(4000),
                                 TEXT2_ varchar(4000),
                                 primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_EVENT_SUBSCR (
                                     ID_ varchar(64) not null,
                                     REV_ integer,
                                     EVENT_TYPE_ varchar(255) not null,
                                     EVENT_NAME_ varchar(255),
                                     EXECUTION_ID_ varchar(64),
                                     PROC_INST_ID_ varchar(64),
                                     ACTIVITY_ID_ varchar(64),
                                     CONFIGURATION_ varchar(255),
                                     CREATED_ timestamp(3) not null DEFAULT CURRENT_TIMESTAMP(3),
                                     PROC_DEF_ID_ varchar(64),
                                     TENANT_ID_ varchar(255) default '',
                                     primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_EVT_LOG (
                             LOG_NR_ bigint auto_increment,
                             TYPE_ varchar(64),
                             PROC_DEF_ID_ varchar(64),
                             PROC_INST_ID_ varchar(64),
                             EXECUTION_ID_ varchar(64),
                             TASK_ID_ varchar(64),
                             TIME_STAMP_ timestamp(3) not null,
                             USER_ID_ varchar(255),
                             DATA_ LONGBLOB,
                             LOCK_OWNER_ varchar(255),
                             LOCK_TIME_ timestamp(3) null,
                             IS_PROCESSED_ tinyint default 0,
                             primary key (LOG_NR_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_PROCDEF_INFO (
                                  ID_ varchar(64) not null,
                                  PROC_DEF_ID_ varchar(64) not null,
                                  REV_ integer,
                                  INFO_JSON_ID_ varchar(64),
                                  primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_RU_INTEGRATION (
                                    ID_ varchar(64) not null,
                                    EXECUTION_ID_ varchar(64),
                                    PROCESS_INSTANCE_ID_ varchar(64),
                                    PROC_DEF_ID_ varchar(64),
                                    FLOW_NODE_ID_ varchar(64),
                                    CREATED_DATE_ timestamp(3),
                                    primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create index ACT_IDX_EXEC_BUSKEY on ACT_RU_EXECUTION(BUSINESS_KEY_);
create index ACT_IDC_EXEC_ROOT on ACT_RU_EXECUTION(ROOT_PROC_INST_ID_);
create index ACT_IDX_TASK_CREATE on ACT_RU_TASK(CREATE_TIME_);
create index ACT_IDX_IDENT_LNK_USER on ACT_RU_IDENTITYLINK(USER_ID_);
create index ACT_IDX_IDENT_LNK_GROUP on ACT_RU_IDENTITYLINK(GROUP_ID_);
create index ACT_IDX_EVENT_SUBSCR_CONFIG_ on ACT_RU_EVENT_SUBSCR(CONFIGURATION_);
create index ACT_IDX_VARIABLE_TASK_ID on ACT_RU_VARIABLE(TASK_ID_);
create index ACT_IDX_ATHRZ_PROCEDEF on ACT_RU_IDENTITYLINK(PROC_DEF_ID_);
create index ACT_IDX_INFO_PROCDEF on ACT_PROCDEF_INFO(PROC_DEF_ID_);

alter table ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
        foreign key (DEPLOYMENT_ID_)
            references ACT_RE_DEPLOYMENT (ID_);

alter table ACT_RE_PROCDEF
    add constraint ACT_UNIQ_PROCDEF
        unique (KEY_,VERSION_, TENANT_ID_);

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_) on delete cascade on update cascade;

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PARENT
        foreign key (PARENT_ID_)
            references ACT_RU_EXECUTION (ID_) on delete cascade;

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_SUPER
        foreign key (SUPER_EXEC_)
            references ACT_RU_EXECUTION (ID_) on delete cascade;

alter table ACT_RU_EXECUTION
    add constraint ACT_FK_EXE_PROCDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_TSKASS_TASK
        foreign key (TASK_ID_)
            references ACT_RU_TASK (ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_ATHRZ_PROCEDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF(ID_);

alter table ACT_RU_IDENTITYLINK
    add constraint ACT_FK_IDL_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_EXE
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TASK
    add constraint ACT_FK_TASK_PROCDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_EXE
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_PROCINST
        foreign key (PROC_INST_ID_)
            references ACT_RU_EXECUTION(ID_);

alter table ACT_RU_VARIABLE
    add constraint ACT_FK_VAR_BYTEARRAY
        foreign key (BYTEARRAY_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_JOB
    add constraint ACT_FK_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TIMER_JOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TIMER_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TIMER_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_TIMER_JOB
    add constraint ACT_FK_TIMER_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SUSPENDED_JOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SUSPENDED_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SUSPENDED_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_SUSPENDED_JOB
    add constraint ACT_FK_SUSPENDED_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DEADLETTER_JOB_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DEADLETTER_JOB_PROCESS_INSTANCE
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DEADLETTER_JOB_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

alter table ACT_RU_DEADLETTER_JOB
    add constraint ACT_FK_DEADLETTER_JOB_EXCEPTION
        foreign key (EXCEPTION_STACK_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RU_EVENT_SUBSCR
    add constraint ACT_FK_EVENT_EXEC
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION(ID_);

alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE
        foreign key (EDITOR_SOURCE_VALUE_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_SOURCE_EXTRA
        foreign key (EDITOR_SOURCE_EXTRA_VALUE_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RE_MODEL
    add constraint ACT_FK_MODEL_DEPLOYMENT
        foreign key (DEPLOYMENT_ID_)
            references ACT_RE_DEPLOYMENT (ID_);

alter table ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_JSON_BA
        foreign key (INFO_JSON_ID_)
            references ACT_GE_BYTEARRAY (ID_);

alter table ACT_PROCDEF_INFO
    add constraint ACT_FK_INFO_PROCDEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);

alter table ACT_PROCDEF_INFO
    add constraint ACT_UNIQ_INFO_PROCDEF
        unique (PROC_DEF_ID_);

alter table ACT_RU_INTEGRATION
    add constraint ACT_FK_INT_EXECUTION
        foreign key (EXECUTION_ID_)
            references ACT_RU_EXECUTION (ID_)
            on delete cascade;

alter table ACT_RU_INTEGRATION
    add constraint ACT_FK_INT_PROC_INST
        foreign key (PROCESS_INSTANCE_ID_)
            references ACT_RU_EXECUTION (ID_);

alter table ACT_RU_INTEGRATION
    add constraint ACT_FK_INT_PROC_DEF
        foreign key (PROC_DEF_ID_)
            references ACT_RE_PROCDEF (ID_);
create table ACT_HI_PROCINST (
                                 ID_ varchar(64) not null,
                                 PROC_INST_ID_ varchar(64) not null,
                                 BUSINESS_KEY_ varchar(255),
                                 PROC_DEF_ID_ varchar(64) not null,
                                 START_TIME_ datetime(3) not null,
                                 END_TIME_ datetime(3),
                                 DURATION_ bigint,
                                 START_USER_ID_ varchar(255),
                                 START_ACT_ID_ varchar(255),
                                 END_ACT_ID_ varchar(255),
                                 SUPER_PROCESS_INSTANCE_ID_ varchar(64),
                                 DELETE_REASON_ varchar(4000),
                                 TENANT_ID_ varchar(255) default '',
                                 NAME_ varchar(255),
                                 primary key (ID_),
                                 unique (PROC_INST_ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_ACTINST (
                                ID_ varchar(64) not null,
                                PROC_DEF_ID_ varchar(64) not null,
                                PROC_INST_ID_ varchar(64) not null,
                                EXECUTION_ID_ varchar(64) not null,
                                ACT_ID_ varchar(255) not null,
                                TASK_ID_ varchar(64),
                                CALL_PROC_INST_ID_ varchar(64),
                                ACT_NAME_ varchar(255),
                                ACT_TYPE_ varchar(255) not null,
                                ASSIGNEE_ varchar(255),
                                START_TIME_ datetime(3) not null,
                                END_TIME_ datetime(3),
                                DURATION_ bigint,
                                DELETE_REASON_ varchar(4000),
                                TENANT_ID_ varchar(255) default '',
                                primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_TASKINST (
                                 ID_ varchar(64) not null,
                                 PROC_DEF_ID_ varchar(64),
                                 TASK_DEF_KEY_ varchar(255),
                                 PROC_INST_ID_ varchar(64),
                                 EXECUTION_ID_ varchar(64),
                                 NAME_ varchar(255),
                                 PARENT_TASK_ID_ varchar(64),
                                 DESCRIPTION_ varchar(4000),
                                 OWNER_ varchar(255),
                                 ASSIGNEE_ varchar(255),
                                 START_TIME_ datetime(3) not null,
                                 CLAIM_TIME_ datetime(3),
                                 END_TIME_ datetime(3),
                                 DURATION_ bigint,
                                 DELETE_REASON_ varchar(4000),
                                 PRIORITY_ integer,
                                 DUE_DATE_ datetime(3),
                                 FORM_KEY_ varchar(255),
                                 CATEGORY_ varchar(255),
                                 TENANT_ID_ varchar(255) default '',
                                 primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_VARINST (
                                ID_ varchar(64) not null,
                                PROC_INST_ID_ varchar(64),
                                EXECUTION_ID_ varchar(64),
                                TASK_ID_ varchar(64),
                                NAME_ varchar(255) not null,
                                VAR_TYPE_ varchar(100),
                                REV_ integer,
                                BYTEARRAY_ID_ varchar(64),
                                DOUBLE_ double,
                                LONG_ bigint,
                                TEXT_ varchar(4000),
                                TEXT2_ varchar(4000),
                                CREATE_TIME_ datetime(3),
                                LAST_UPDATED_TIME_ datetime(3),
                                primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_DETAIL (
                               ID_ varchar(64) not null,
                               TYPE_ varchar(255) not null,
                               PROC_INST_ID_ varchar(64),
                               EXECUTION_ID_ varchar(64),
                               TASK_ID_ varchar(64),
                               ACT_INST_ID_ varchar(64),
                               NAME_ varchar(255) not null,
                               VAR_TYPE_ varchar(255),
                               REV_ integer,
                               TIME_ datetime(3) not null,
                               BYTEARRAY_ID_ varchar(64),
                               DOUBLE_ double,
                               LONG_ bigint,
                               TEXT_ varchar(4000),
                               TEXT2_ varchar(4000),
                               primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_COMMENT (
                                ID_ varchar(64) not null,
                                TYPE_ varchar(255),
                                TIME_ datetime(3) not null,
                                USER_ID_ varchar(255),
                                TASK_ID_ varchar(64),
                                PROC_INST_ID_ varchar(64),
                                ACTION_ varchar(255),
                                MESSAGE_ varchar(4000),
                                FULL_MSG_ LONGBLOB,
                                primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_ATTACHMENT (
                                   ID_ varchar(64) not null,
                                   REV_ integer,
                                   USER_ID_ varchar(255),
                                   NAME_ varchar(255),
                                   DESCRIPTION_ varchar(4000),
                                   TYPE_ varchar(255),
                                   TASK_ID_ varchar(64),
                                   PROC_INST_ID_ varchar(64),
                                   URL_ varchar(4000),
                                   CONTENT_ID_ varchar(64),
                                   TIME_ datetime(3),
                                   primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

create table ACT_HI_IDENTITYLINK (
                                     ID_ varchar(64),
                                     GROUP_ID_ varchar(255),
                                     TYPE_ varchar(255),
                                     USER_ID_ varchar(255),
                                     TASK_ID_ varchar(64),
                                     PROC_INST_ID_ varchar(64),
                                     primary key (ID_)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;


create index ACT_IDX_HI_PRO_INST_END on ACT_HI_PROCINST(END_TIME_);
create index ACT_IDX_HI_PRO_I_BUSKEY on ACT_HI_PROCINST(BUSINESS_KEY_);
create index ACT_IDX_HI_ACT_INST_START on ACT_HI_ACTINST(START_TIME_);
create index ACT_IDX_HI_ACT_INST_END on ACT_HI_ACTINST(END_TIME_);
create index ACT_IDX_HI_DETAIL_PROC_INST on ACT_HI_DETAIL(PROC_INST_ID_);
create index ACT_IDX_HI_DETAIL_ACT_INST on ACT_HI_DETAIL(ACT_INST_ID_);
create index ACT_IDX_HI_DETAIL_TIME on ACT_HI_DETAIL(TIME_);
create index ACT_IDX_HI_DETAIL_NAME on ACT_HI_DETAIL(NAME_);
create index ACT_IDX_HI_DETAIL_TASK_ID on ACT_HI_DETAIL(TASK_ID_);
create index ACT_IDX_HI_PROCVAR_PROC_INST on ACT_HI_VARINST(PROC_INST_ID_);
create index ACT_IDX_HI_PROCVAR_NAME_TYPE on ACT_HI_VARINST(NAME_, VAR_TYPE_);
create index ACT_IDX_HI_PROCVAR_TASK_ID on ACT_HI_VARINST(TASK_ID_);
create index ACT_IDX_HI_ACT_INST_PROCINST on ACT_HI_ACTINST(PROC_INST_ID_, ACT_ID_);
create index ACT_IDX_HI_ACT_INST_EXEC on ACT_HI_ACTINST(EXECUTION_ID_, ACT_ID_);
create index ACT_IDX_HI_IDENT_LNK_USER on ACT_HI_IDENTITYLINK(USER_ID_);
create index ACT_IDX_HI_IDENT_LNK_TASK on ACT_HI_IDENTITYLINK(TASK_ID_);
create index ACT_IDX_HI_IDENT_LNK_PROCINST on ACT_HI_IDENTITYLINK(PROC_INST_ID_);
create index ACT_IDX_HI_TASK_INST_PROCINST on ACT_HI_TASKINST(PROC_INST_ID_);

DROP TABLE IF EXISTS `node`;
CREATE TABLE `node` (
                        `id` varchar(128) NOT NULL COMMENT '主键',
                        `model_id` varchar(64) DEFAULT NULL COMMENT '所属流程定义的ID',
                        `graph_id` varchar(64) DEFAULT NULL COMMENT '流程图中的节点id',
                        `name` varchar(50) DEFAULT NULL COMMENT '名称',
                        `description` varchar(255) DEFAULT NULL COMMENT '描述',
                        `type` int(11) DEFAULT NULL COMMENT '节点类型,1 用户任务',
                        `due_time` varchar(50) DEFAULT NULL COMMENT '工作时间,单位是分钟',
                        `approval_node` int(11) DEFAULT NULL COMMENT '是否是审批节点 1 是 0 否',
                        `candidate_users` text COMMENT '候选用户,逗号分隔的id列表',
                        `candidate_groups` text COMMENT '候选用户组,逗号分隔的id列表',
                        `form_location` varchar(255) DEFAULT NULL COMMENT '表单位置',
                        `form_content` longtext COMMENT '表单内容',
                        `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
                        `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改日期',
                        `last_updated_by` bigint(20) DEFAULT NULL COMMENT '最后修改者',
                        `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 禁用,1 启用',
                        `notice_type` int(2) DEFAULT NULL COMMENT '通知类型',
                        `notice_content` text COMMENT '通知内容（卡片通知是按钮样式）',
                        `business_url` text,
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内容',
                          `task_id` varchar(64) DEFAULT NULL COMMENT '任务ID',
                          `title` varchar(255) DEFAULT NULL COMMENT '标题',
                          `content` varchar(255) DEFAULT NULL COMMENT '内容',
                          `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 禁用,1 启用',
                          `task_id_weixin` varchar(64) DEFAULT NULL,
                          `type` int(2) DEFAULT NULL COMMENT '通知类型 0 超时',
                          `count` int(11) DEFAULT '1' COMMENT '通知次数',
                          `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知';

DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
                                 `content` varchar(255) DEFAULT NULL,
                                 `status` int(1) DEFAULT NULL COMMENT '0 表示动作失败 1 表示动作成功',
                                 `owner_id` int(11) DEFAULT NULL COMMENT '负责人id',
                                 `owner_ids` varchar(255) DEFAULT NULL COMMENT '用于存储多个负责人使用',
                                 `instance_id` varchar(64) DEFAULT NULL COMMENT '实例id',
                                 `task_id` varchar(64) DEFAULT NULL COMMENT '任务id',
                                 `event` varchar(50) DEFAULT NULL COMMENT '事件类型',
                                 `created_by` int(11) DEFAULT NULL COMMENT '创建人(操作人)',
                                 `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `last_updated_by` int(11) DEFAULT NULL COMMENT '最后修改人',
                                 `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `rabbitmq_msg`;
CREATE TABLE `rabbitmq_msg` (
                                `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `message` text,
                                `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                `status` int(2) DEFAULT '1' COMMENT '0未消费 1已消费',
                                `task_id_weixin` varchar(64) DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
                        `id` varchar(64) NOT NULL COMMENT '主键',
                        `node_id` varchar(128) DEFAULT NULL COMMENT '所属任务定义的ID',
                        `model_id` varchar(64) DEFAULT NULL COMMENT '所属流程定义的ID',
                        `instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例ID',
                        `graph_id` varchar(64) DEFAULT NULL COMMENT '流程图中的节点id',
                        `name` varchar(50) DEFAULT NULL COMMENT '名称',
                        `description` varchar(255) DEFAULT NULL COMMENT '描述',
                        `type` int(11) DEFAULT NULL COMMENT '节点类型,1 用户任务',
                        `approval_node` int(11) DEFAULT NULL COMMENT '是否是审批节点 1 是 0 否',
                        `due_time` varchar(50) DEFAULT NULL COMMENT '工作时间数,单位是分钟',
                        `candidate_users` text COMMENT '候选用户,逗号分隔的id列表',
                        `candidate_groups` text COMMENT '候选用户组,逗号分隔的id列表',
                        `form_location` varchar(255) DEFAULT NULL COMMENT '表单位置',
                        `form_content` longtext COMMENT '表单内容',
                        `data` longtext COMMENT '任务数据',
                        `submit_by` bigint(20) DEFAULT NULL COMMENT '提交人ID',
                        `submit_time` timestamp NULL DEFAULT NULL COMMENT '提交时间',
                        `begin_time` timestamp NULL DEFAULT NULL COMMENT '任务开始时间',
                        `business_url` text,
                        `notice_type` int(2) DEFAULT NULL COMMENT '通知类型',
                        `notice_content` text,
                        `created_by` int(11) DEFAULT NULL COMMENT '创建者',
                        `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `last_updated_by` int(11) DEFAULT NULL COMMENT '最后修改者',
                        `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改日期',
                        `status` int(2) NOT NULL COMMENT '状态：0未处理，1 处理中, 2 处理完成',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务扩展表';

DROP TABLE IF EXISTS `task_comment`;
CREATE TABLE `task_comment` (
                                `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
                                `content` text COMMENT '评论内容',
                                `parent_id` int(11) DEFAULT NULL COMMENT '父评论id 0为一级评论',
                                `created_by` int(11) DEFAULT NULL COMMENT '创建人',
                                `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                                `last_updated_by` int(11) DEFAULT NULL COMMENT '最后修改人',
                                `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
                                `status` int(1) DEFAULT '1' COMMENT '是否有效 0无效 1有效',
                                `task_id` varchar(64) DEFAULT NULL COMMENT '任务id',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务评论表';

DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
                              `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                              `name` varchar(255) DEFAULT NULL,
                              `remark` varchar(255) DEFAULT NULL,
                              `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              `created_by` int(11) DEFAULT NULL,
                              `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              `last_updated_by` int(11) DEFAULT NULL COMMENT '最后修改人',
                              `status` int(1) DEFAULT '1' COMMENT '状态：0停用 1启用',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user_group_asso`;
CREATE TABLE `user_group_asso` (
                                   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `group_id` int(11) DEFAULT NULL COMMENT '用户组id',
                                   `user_id` int(11) DEFAULT NULL COMMENT '用户id',
                                   `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `created_by` int(11) DEFAULT NULL COMMENT '创建人',
                                   `last_updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
                                   `last_updated_by` int(11) DEFAULT NULL COMMENT '最后修改人',
                                   `status` int(1) DEFAULT '1' COMMENT '状态：0停用 1启用',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `work_mode` (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `name` varchar(100) DEFAULT NULL COMMENT '名称',
                             `day` int(11) DEFAULT NULL COMMENT '每周的第几天',
                             `start_time` time DEFAULT NULL COMMENT '开始时间',
                             `end_time` time DEFAULT NULL COMMENT '结束时间',
                             `code` varchar(50) DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;
BEGIN;
INSERT INTO `work_mode` VALUES (1, '通用', 1, '09:00:00', '18:00:00', 'other');
INSERT INTO `work_mode` VALUES (2, '通用', 2, '09:00:00', '18:00:00', 'other');
INSERT INTO `work_mode` VALUES (3, '通用', 3, '09:00:00', '18:00:00', 'other');
INSERT INTO `work_mode` VALUES (4, '通用', 4, '09:00:00', '18:00:00', 'other');
INSERT INTO `work_mode` VALUES (5, '通用', 5, '09:00:00', '18:00:00', 'other');
INSERT INTO `work_mode` VALUES (6, '通用', 6, '09:00:00', '18:00:00', 'other');
INSERT INTO `work_mode` VALUES (7, '通用', 7, '09:00:00', '18:00:00', 'other');
INSERT INTO `work_mode` VALUES (8, '产品与技术', 1, '10:00:00', '20:00:00', 'dev');
INSERT INTO `work_mode` VALUES (9, '产品与技术', 2, '10:00:00', '20:00:00', 'dev');
INSERT INTO `work_mode` VALUES (10, '产品与技术', 3, '10:00:00', '20:00:00', 'dev');
INSERT INTO `work_mode` VALUES (11, '产品与技术', 4, '10:00:00', '20:00:00', 'dev');
INSERT INTO `work_mode` VALUES (12, '产品与技术', 5, '10:00:00', '20:00:00', 'dev');
INSERT INTO `work_mode` VALUES (13, '产品与技术', 6, '10:00:00', '20:00:00', 'dev');
INSERT INTO `work_mode` VALUES (14, '产品与技术', 7, '10:00:00', '20:00:00', 'dev');
COMMIT;

CREATE TABLE `work_day` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `description` varchar(100) DEFAULT NULL COMMENT '描述',
                            `year` int(11) DEFAULT NULL COMMENT '年',
                            `date_of_year` date DEFAULT NULL,
                            `day_of_year` int(11) DEFAULT NULL COMMENT '第几天',
                            `is_working_day` int(11) DEFAULT NULL COMMENT '是否是工作日 0休息,1上班',
                            `start_time` time DEFAULT NULL COMMENT '开始时间',
                            `end_time` time DEFAULT NULL COMMENT '结束时间',
                            `reason` varchar(255) DEFAULT NULL COMMENT '1 正常 2 交换',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=366 DEFAULT CHARSET=utf8mb4;
BEGIN;
INSERT INTO `work_day` VALUES (1, '2019年测试工作日历', 2019, '2019-01-01', 1, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (2, '2019年测试工作日历', 2019, '2019-01-02', 2, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (3, '2019年测试工作日历', 2019, '2019-01-03', 3, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (4, '2019年测试工作日历', 2019, '2019-01-04', 4, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (5, '2019年测试工作日历', 2019, '2019-01-05', 5, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (6, '2019年测试工作日历', 2019, '2019-01-06', 6, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (7, '2019年测试工作日历', 2019, '2019-01-07', 7, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (8, '2019年测试工作日历', 2019, '2019-01-08', 8, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (9, '2019年测试工作日历', 2019, '2019-01-09', 9, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (10, '2019年测试工作日历', 2019, '2019-01-10', 10, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (11, '2019年测试工作日历', 2019, '2019-01-11', 11, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (12, '2019年测试工作日历', 2019, '2019-01-12', 12, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (13, '2019年测试工作日历', 2019, '2019-01-13', 13, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (14, '2019年测试工作日历', 2019, '2019-01-14', 14, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (15, '2019年测试工作日历', 2019, '2019-01-15', 15, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (16, '2019年测试工作日历', 2019, '2019-01-16', 16, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (17, '2019年测试工作日历', 2019, '2019-01-17', 17, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (18, '2019年测试工作日历', 2019, '2019-01-18', 18, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (19, '2019年测试工作日历', 2019, '2019-01-19', 19, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (20, '2019年测试工作日历', 2019, '2019-01-20', 20, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (21, '2019年测试工作日历', 2019, '2019-01-21', 21, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (22, '2019年测试工作日历', 2019, '2019-01-22', 22, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (23, '2019年测试工作日历', 2019, '2019-01-23', 23, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (24, '2019年测试工作日历', 2019, '2019-01-24', 24, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (25, '2019年测试工作日历', 2019, '2019-01-25', 25, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (26, '2019年测试工作日历', 2019, '2019-01-26', 26, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (27, '2019年测试工作日历', 2019, '2019-01-27', 27, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (28, '2019年测试工作日历', 2019, '2019-01-28', 28, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (29, '2019年测试工作日历', 2019, '2019-01-29', 29, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (30, '2019年测试工作日历', 2019, '2019-01-30', 30, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (31, '2019年测试工作日历', 2019, '2019-01-31', 31, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (32, '2019年测试工作日历', 2019, '2019-02-01', 32, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (33, '2019年测试工作日历', 2019, '2019-02-02', 33, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (34, '2019年测试工作日历', 2019, '2019-02-03', 34, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (35, '2019年测试工作日历', 2019, '2019-02-04', 35, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (36, '2019年测试工作日历', 2019, '2019-02-05', 36, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (37, '2019年测试工作日历', 2019, '2019-02-06', 37, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (38, '2019年测试工作日历', 2019, '2019-02-07', 38, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (39, '2019年测试工作日历', 2019, '2019-02-08', 39, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (40, '2019年测试工作日历', 2019, '2019-02-09', 40, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (41, '2019年测试工作日历', 2019, '2019-02-10', 41, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (42, '2019年测试工作日历', 2019, '2019-02-11', 42, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (43, '2019年测试工作日历', 2019, '2019-02-12', 43, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (44, '2019年测试工作日历', 2019, '2019-02-13', 44, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (45, '2019年测试工作日历', 2019, '2019-02-14', 45, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (46, '2019年测试工作日历', 2019, '2019-02-15', 46, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (47, '2019年测试工作日历', 2019, '2019-02-16', 47, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (48, '2019年测试工作日历', 2019, '2019-02-17', 48, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (49, '2019年测试工作日历', 2019, '2019-02-18', 49, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (50, '2019年测试工作日历', 2019, '2019-02-19', 50, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (51, '2019年测试工作日历', 2019, '2019-02-20', 51, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (52, '2019年测试工作日历', 2019, '2019-02-21', 52, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (53, '2019年测试工作日历', 2019, '2019-02-22', 53, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (54, '2019年测试工作日历', 2019, '2019-02-23', 54, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (55, '2019年测试工作日历', 2019, '2019-02-24', 55, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (56, '2019年测试工作日历', 2019, '2019-02-25', 56, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (57, '2019年测试工作日历', 2019, '2019-02-26', 57, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (58, '2019年测试工作日历', 2019, '2019-02-27', 58, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (59, '2019年测试工作日历', 2019, '2019-02-28', 59, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (60, '2019年测试工作日历', 2019, '2019-03-01', 60, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (61, '2019年测试工作日历', 2019, '2019-03-02', 61, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (62, '2019年测试工作日历', 2019, '2019-03-03', 62, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (63, '2019年测试工作日历', 2019, '2019-03-04', 63, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (64, '2019年测试工作日历', 2019, '2019-03-05', 64, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (65, '2019年测试工作日历', 2019, '2019-03-06', 65, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (66, '2019年测试工作日历', 2019, '2019-03-07', 66, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (67, '2019年测试工作日历', 2019, '2019-03-08', 67, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (68, '2019年测试工作日历', 2019, '2019-03-09', 68, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (69, '2019年测试工作日历', 2019, '2019-03-10', 69, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (70, '2019年测试工作日历', 2019, '2019-03-11', 70, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (71, '2019年测试工作日历', 2019, '2019-03-12', 71, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (72, '2019年测试工作日历', 2019, '2019-03-13', 72, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (73, '2019年测试工作日历', 2019, '2019-03-14', 73, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (74, '2019年测试工作日历', 2019, '2019-03-15', 74, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (75, '2019年测试工作日历', 2019, '2019-03-16', 75, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (76, '2019年测试工作日历', 2019, '2019-03-17', 76, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (77, '2019年测试工作日历', 2019, '2019-03-18', 77, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (78, '2019年测试工作日历', 2019, '2019-03-19', 78, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (79, '2019年测试工作日历', 2019, '2019-03-20', 79, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (80, '2019年测试工作日历', 2019, '2019-03-21', 80, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (81, '2019年测试工作日历', 2019, '2019-03-22', 81, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (82, '2019年测试工作日历', 2019, '2019-03-23', 82, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (83, '2019年测试工作日历', 2019, '2019-03-24', 83, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (84, '2019年测试工作日历', 2019, '2019-03-25', 84, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (85, '2019年测试工作日历', 2019, '2019-03-26', 85, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (86, '2019年测试工作日历', 2019, '2019-03-27', 86, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (87, '2019年测试工作日历', 2019, '2019-03-28', 87, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (88, '2019年测试工作日历', 2019, '2019-03-29', 88, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (89, '2019年测试工作日历', 2019, '2019-03-30', 89, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (90, '2019年测试工作日历', 2019, '2019-03-31', 90, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (91, '2019年测试工作日历', 2019, '2019-04-01', 91, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (92, '2019年测试工作日历', 2019, '2019-04-02', 92, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (93, '2019年测试工作日历', 2019, '2019-04-03', 93, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (94, '2019年测试工作日历', 2019, '2019-04-04', 94, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (95, '2019年测试工作日历', 2019, '2019-04-05', 95, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (96, '2019年测试工作日历', 2019, '2019-04-06', 96, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (97, '2019年测试工作日历', 2019, '2019-04-07', 97, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (98, '2019年测试工作日历', 2019, '2019-04-08', 98, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (99, '2019年测试工作日历', 2019, '2019-04-09', 99, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (100, '2019年测试工作日历', 2019, '2019-04-10', 100, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (101, '2019年测试工作日历', 2019, '2019-04-11', 101, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (102, '2019年测试工作日历', 2019, '2019-04-12', 102, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (103, '2019年测试工作日历', 2019, '2019-04-13', 103, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (104, '2019年测试工作日历', 2019, '2019-04-14', 104, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (105, '2019年测试工作日历', 2019, '2019-04-15', 105, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (106, '2019年测试工作日历', 2019, '2019-04-16', 106, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (107, '2019年测试工作日历', 2019, '2019-04-17', 107, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (108, '2019年测试工作日历', 2019, '2019-04-18', 108, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (109, '2019年测试工作日历', 2019, '2019-04-19', 109, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (110, '2019年测试工作日历', 2019, '2019-04-20', 110, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (111, '2019年测试工作日历', 2019, '2019-04-21', 111, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (112, '2019年测试工作日历', 2019, '2019-04-22', 112, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (113, '2019年测试工作日历', 2019, '2019-04-23', 113, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (114, '2019年测试工作日历', 2019, '2019-04-24', 114, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (115, '2019年测试工作日历', 2019, '2019-04-25', 115, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (116, '2019年测试工作日历', 2019, '2019-04-26', 116, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (117, '2019年测试工作日历', 2019, '2019-04-27', 117, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (118, '2019年测试工作日历', 2019, '2019-04-28', 118, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (119, '2019年测试工作日历', 2019, '2019-04-29', 119, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (120, '2019年测试工作日历', 2019, '2019-04-30', 120, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (121, '2019年测试工作日历', 2019, '2019-05-01', 121, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (122, '2019年测试工作日历', 2019, '2019-05-02', 122, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (123, '2019年测试工作日历', 2019, '2019-05-03', 123, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (124, '2019年测试工作日历', 2019, '2019-05-04', 124, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (125, '2019年测试工作日历', 2019, '2019-05-05', 125, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (126, '2019年测试工作日历', 2019, '2019-05-06', 126, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (127, '2019年测试工作日历', 2019, '2019-05-07', 127, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (128, '2019年测试工作日历', 2019, '2019-05-08', 128, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (129, '2019年测试工作日历', 2019, '2019-05-09', 129, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (130, '2019年测试工作日历', 2019, '2019-05-10', 130, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (131, '2019年测试工作日历', 2019, '2019-05-11', 131, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (132, '2019年测试工作日历', 2019, '2019-05-12', 132, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (133, '2019年测试工作日历', 2019, '2019-05-13', 133, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (134, '2019年测试工作日历', 2019, '2019-05-14', 134, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (135, '2019年测试工作日历', 2019, '2019-05-15', 135, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (136, '2019年测试工作日历', 2019, '2019-05-16', 136, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (137, '2019年测试工作日历', 2019, '2019-05-17', 137, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (138, '2019年测试工作日历', 2019, '2019-05-18', 138, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (139, '2019年测试工作日历', 2019, '2019-05-19', 139, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (140, '2019年测试工作日历', 2019, '2019-05-20', 140, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (141, '2019年测试工作日历', 2019, '2019-05-21', 141, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (142, '2019年测试工作日历', 2019, '2019-05-22', 142, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (143, '2019年测试工作日历', 2019, '2019-05-23', 143, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (144, '2019年测试工作日历', 2019, '2019-05-24', 144, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (145, '2019年测试工作日历', 2019, '2019-05-25', 145, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (146, '2019年测试工作日历', 2019, '2019-05-26', 146, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (147, '2019年测试工作日历', 2019, '2019-05-27', 147, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (148, '2019年测试工作日历', 2019, '2019-05-28', 148, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (149, '2019年测试工作日历', 2019, '2019-05-29', 149, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (150, '2019年测试工作日历', 2019, '2019-05-30', 150, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (151, '2019年测试工作日历', 2019, '2019-05-31', 151, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (152, '2019年测试工作日历', 2019, '2019-06-01', 152, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (153, '2019年测试工作日历', 2019, '2019-06-02', 153, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (154, '2019年测试工作日历', 2019, '2019-06-03', 154, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (155, '2019年测试工作日历', 2019, '2019-06-04', 155, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (156, '2019年测试工作日历', 2019, '2019-06-05', 156, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (157, '2019年测试工作日历', 2019, '2019-06-06', 157, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (158, '2019年测试工作日历', 2019, '2019-06-07', 158, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (159, '2019年测试工作日历', 2019, '2019-06-08', 159, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (160, '2019年测试工作日历', 2019, '2019-06-09', 160, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (161, '2019年测试工作日历', 2019, '2019-06-10', 161, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (162, '2019年测试工作日历', 2019, '2019-06-11', 162, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (163, '2019年测试工作日历', 2019, '2019-06-12', 163, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (164, '2019年测试工作日历', 2019, '2019-06-13', 164, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (165, '2019年测试工作日历', 2019, '2019-06-14', 165, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (166, '2019年测试工作日历', 2019, '2019-06-15', 166, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (167, '2019年测试工作日历', 2019, '2019-06-16', 167, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (168, '2019年测试工作日历', 2019, '2019-06-17', 168, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (169, '2019年测试工作日历', 2019, '2019-06-18', 169, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (170, '2019年测试工作日历', 2019, '2019-06-19', 170, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (171, '2019年测试工作日历', 2019, '2019-06-20', 171, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (172, '2019年测试工作日历', 2019, '2019-06-21', 172, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (173, '2019年测试工作日历', 2019, '2019-06-22', 173, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (174, '2019年测试工作日历', 2019, '2019-06-23', 174, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (175, '2019年测试工作日历', 2019, '2019-06-24', 175, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (176, '2019年测试工作日历', 2019, '2019-06-25', 176, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (177, '2019年测试工作日历', 2019, '2019-06-26', 177, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (178, '2019年测试工作日历', 2019, '2019-06-27', 178, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (179, '2019年测试工作日历', 2019, '2019-06-28', 179, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (180, '2019年测试工作日历', 2019, '2019-06-29', 180, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (181, '2019年测试工作日历', 2019, '2019-06-30', 181, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (182, '2019年测试工作日历', 2019, '2019-07-01', 182, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (183, '2019年测试工作日历', 2019, '2019-07-02', 183, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (184, '2019年测试工作日历', 2019, '2019-07-03', 184, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (185, '2019年测试工作日历', 2019, '2019-07-04', 185, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (186, '2019年测试工作日历', 2019, '2019-07-05', 186, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (187, '2019年测试工作日历', 2019, '2019-07-06', 187, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (188, '2019年测试工作日历', 2019, '2019-07-07', 188, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (189, '2019年测试工作日历', 2019, '2019-07-08', 189, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (190, '2019年测试工作日历', 2019, '2019-07-09', 190, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (191, '2019年测试工作日历', 2019, '2019-07-10', 191, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (192, '2019年测试工作日历', 2019, '2019-07-11', 192, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (193, '2019年测试工作日历', 2019, '2019-07-12', 193, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (194, '2019年测试工作日历', 2019, '2019-07-13', 194, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (195, '2019年测试工作日历', 2019, '2019-07-14', 195, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (196, '2019年测试工作日历', 2019, '2019-07-15', 196, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (197, '2019年测试工作日历', 2019, '2019-07-16', 197, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (198, '2019年测试工作日历', 2019, '2019-07-17', 198, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (199, '2019年测试工作日历', 2019, '2019-07-18', 199, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (200, '2019年测试工作日历', 2019, '2019-07-19', 200, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (201, '2019年测试工作日历', 2019, '2019-07-20', 201, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (202, '2019年测试工作日历', 2019, '2019-07-21', 202, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (203, '2019年测试工作日历', 2019, '2019-07-22', 203, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (204, '2019年测试工作日历', 2019, '2019-07-23', 204, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (205, '2019年测试工作日历', 2019, '2019-07-24', 205, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (206, '2019年测试工作日历', 2019, '2019-07-25', 206, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (207, '2019年测试工作日历', 2019, '2019-07-26', 207, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (208, '2019年测试工作日历', 2019, '2019-07-27', 208, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (209, '2019年测试工作日历', 2019, '2019-07-28', 209, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (210, '2019年测试工作日历', 2019, '2019-07-29', 210, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (211, '2019年测试工作日历', 2019, '2019-07-30', 211, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (212, '2019年测试工作日历', 2019, '2019-07-31', 212, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (213, '2019年测试工作日历', 2019, '2019-08-01', 213, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (214, '2019年测试工作日历', 2019, '2019-08-02', 214, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (215, '2019年测试工作日历', 2019, '2019-08-03', 215, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (216, '2019年测试工作日历', 2019, '2019-08-04', 216, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (217, '2019年测试工作日历', 2019, '2019-08-05', 217, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (218, '2019年测试工作日历', 2019, '2019-08-06', 218, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (219, '2019年测试工作日历', 2019, '2019-08-07', 219, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (220, '2019年测试工作日历', 2019, '2019-08-08', 220, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (221, '2019年测试工作日历', 2019, '2019-08-09', 221, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (222, '2019年测试工作日历', 2019, '2019-08-10', 222, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (223, '2019年测试工作日历', 2019, '2019-08-11', 223, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (224, '2019年测试工作日历', 2019, '2019-08-12', 224, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (225, '2019年测试工作日历', 2019, '2019-08-13', 225, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (226, '2019年测试工作日历', 2019, '2019-08-14', 226, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (227, '2019年测试工作日历', 2019, '2019-08-15', 227, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (228, '2019年测试工作日历', 2019, '2019-08-16', 228, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (229, '2019年测试工作日历', 2019, '2019-08-17', 229, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (230, '2019年测试工作日历', 2019, '2019-08-18', 230, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (231, '2019年测试工作日历', 2019, '2019-08-19', 231, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (232, '2019年测试工作日历', 2019, '2019-08-20', 232, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (233, '2019年测试工作日历', 2019, '2019-08-21', 233, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (234, '2019年测试工作日历', 2019, '2019-08-22', 234, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (235, '2019年测试工作日历', 2019, '2019-08-23', 235, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (236, '2019年测试工作日历', 2019, '2019-08-24', 236, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (237, '2019年测试工作日历', 2019, '2019-08-25', 237, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (238, '2019年测试工作日历', 2019, '2019-08-26', 238, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (239, '2019年测试工作日历', 2019, '2019-08-27', 239, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (240, '2019年测试工作日历', 2019, '2019-08-28', 240, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (241, '2019年测试工作日历', 2019, '2019-08-29', 241, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (242, '2019年测试工作日历', 2019, '2019-08-30', 242, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (243, '2019年测试工作日历', 2019, '2019-08-31', 243, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (244, '2019年测试工作日历', 2019, '2019-09-01', 244, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (245, '2019年测试工作日历', 2019, '2019-09-02', 245, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (246, '2019年测试工作日历', 2019, '2019-09-03', 246, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (247, '2019年测试工作日历', 2019, '2019-09-04', 247, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (248, '2019年测试工作日历', 2019, '2019-09-05', 248, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (249, '2019年测试工作日历', 2019, '2019-09-06', 249, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (250, '2019年测试工作日历', 2019, '2019-09-07', 250, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (251, '2019年测试工作日历', 2019, '2019-09-08', 251, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (252, '2019年测试工作日历', 2019, '2019-09-09', 252, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (253, '2019年测试工作日历', 2019, '2019-09-10', 253, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (254, '2019年测试工作日历', 2019, '2019-09-11', 254, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (255, '2019年测试工作日历', 2019, '2019-09-12', 255, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (256, '2019年测试工作日历', 2019, '2019-09-13', 256, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (257, '2019年测试工作日历', 2019, '2019-09-14', 257, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (258, '2019年测试工作日历', 2019, '2019-09-15', 258, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (259, '2019年测试工作日历', 2019, '2019-09-16', 259, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (260, '2019年测试工作日历', 2019, '2019-09-17', 260, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (261, '2019年测试工作日历', 2019, '2019-09-18', 261, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (262, '2019年测试工作日历', 2019, '2019-09-19', 262, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (263, '2019年测试工作日历', 2019, '2019-09-20', 263, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (264, '2019年测试工作日历', 2019, '2019-09-21', 264, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (265, '2019年测试工作日历', 2019, '2019-09-22', 265, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (266, '2019年测试工作日历', 2019, '2019-09-23', 266, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (267, '2019年测试工作日历', 2019, '2019-09-24', 267, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (268, '2019年测试工作日历', 2019, '2019-09-25', 268, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (269, '2019年测试工作日历', 2019, '2019-09-26', 269, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (270, '2019年测试工作日历', 2019, '2019-09-27', 270, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (271, '2019年测试工作日历', 2019, '2019-09-28', 271, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (272, '2019年测试工作日历', 2019, '2019-09-29', 272, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (273, '2019年测试工作日历', 2019, '2019-09-30', 273, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (274, '2019年测试工作日历', 2019, '2019-10-01', 274, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (275, '2019年测试工作日历', 2019, '2019-10-02', 275, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (276, '2019年测试工作日历', 2019, '2019-10-03', 276, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (277, '2019年测试工作日历', 2019, '2019-10-04', 277, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (278, '2019年测试工作日历', 2019, '2019-10-05', 278, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (279, '2019年测试工作日历', 2019, '2019-10-06', 279, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (280, '2019年测试工作日历', 2019, '2019-10-07', 280, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (281, '2019年测试工作日历', 2019, '2019-10-08', 281, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (282, '2019年测试工作日历', 2019, '2019-10-09', 282, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (283, '2019年测试工作日历', 2019, '2019-10-10', 283, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (284, '2019年测试工作日历', 2019, '2019-10-11', 284, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (285, '2019年测试工作日历', 2019, '2019-10-12', 285, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (286, '2019年测试工作日历', 2019, '2019-10-13', 286, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (287, '2019年测试工作日历', 2019, '2019-10-14', 287, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (288, '2019年测试工作日历', 2019, '2019-10-15', 288, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (289, '2019年测试工作日历', 2019, '2019-10-16', 289, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (290, '2019年测试工作日历', 2019, '2019-10-17', 290, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (291, '2019年测试工作日历', 2019, '2019-10-18', 291, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (292, '2019年测试工作日历', 2019, '2019-10-19', 292, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (293, '2019年测试工作日历', 2019, '2019-10-20', 293, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (294, '2019年测试工作日历', 2019, '2019-10-21', 294, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (295, '2019年测试工作日历', 2019, '2019-10-22', 295, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (296, '2019年测试工作日历', 2019, '2019-10-23', 296, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (297, '2019年测试工作日历', 2019, '2019-10-24', 297, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (298, '2019年测试工作日历', 2019, '2019-10-25', 298, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (299, '2019年测试工作日历', 2019, '2019-10-26', 299, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (300, '2019年测试工作日历', 2019, '2019-10-27', 300, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (301, '2019年测试工作日历', 2019, '2019-10-28', 301, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (302, '2019年测试工作日历', 2019, '2019-10-29', 302, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (303, '2019年测试工作日历', 2019, '2019-10-30', 303, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (304, '2019年测试工作日历', 2019, '2019-10-31', 304, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (305, '2019年测试工作日历', 2019, '2019-11-01', 305, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (306, '2019年测试工作日历', 2019, '2019-11-02', 306, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (307, '2019年测试工作日历', 2019, '2019-11-03', 307, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (308, '2019年测试工作日历', 2019, '2019-11-04', 308, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (309, '2019年测试工作日历', 2019, '2019-11-05', 309, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (310, '2019年测试工作日历', 2019, '2019-11-06', 310, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (311, '2019年测试工作日历', 2019, '2019-11-07', 311, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (312, '2019年测试工作日历', 2019, '2019-11-08', 312, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (313, '2019年测试工作日历', 2019, '2019-11-09', 313, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (314, '2019年测试工作日历', 2019, '2019-11-10', 314, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (315, '2019年测试工作日历', 2019, '2019-11-11', 315, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (316, '2019年测试工作日历', 2019, '2019-11-12', 316, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (317, '2019年测试工作日历', 2019, '2019-11-13', 317, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (318, '2019年测试工作日历', 2019, '2019-11-14', 318, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (319, '2019年测试工作日历', 2019, '2019-11-15', 319, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (320, '2019年测试工作日历', 2019, '2019-11-16', 320, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (321, '2019年测试工作日历', 2019, '2019-11-17', 321, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (322, '2019年测试工作日历', 2019, '2019-11-18', 322, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (323, '2019年测试工作日历', 2019, '2019-11-19', 323, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (324, '2019年测试工作日历', 2019, '2019-11-20', 324, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (325, '2019年测试工作日历', 2019, '2019-11-21', 325, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (326, '2019年测试工作日历', 2019, '2019-11-22', 326, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (327, '2019年测试工作日历', 2019, '2019-11-23', 327, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (328, '2019年测试工作日历', 2019, '2019-11-24', 328, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (329, '2019年测试工作日历', 2019, '2019-11-25', 329, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (330, '2019年测试工作日历', 2019, '2019-11-26', 330, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (331, '2019年测试工作日历', 2019, '2019-11-27', 331, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (332, '2019年测试工作日历', 2019, '2019-11-28', 332, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (333, '2019年测试工作日历', 2019, '2019-11-29', 333, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (334, '2019年测试工作日历', 2019, '2019-11-30', 334, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (335, '2019年测试工作日历', 2019, '2019-12-01', 335, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (336, '2019年测试工作日历', 2019, '2019-12-02', 336, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (337, '2019年测试工作日历', 2019, '2019-12-03', 337, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (338, '2019年测试工作日历', 2019, '2019-12-04', 338, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (339, '2019年测试工作日历', 2019, '2019-12-05', 339, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (340, '2019年测试工作日历', 2019, '2019-12-06', 340, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (341, '2019年测试工作日历', 2019, '2019-12-07', 341, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (342, '2019年测试工作日历', 2019, '2019-12-08', 342, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (343, '2019年测试工作日历', 2019, '2019-12-09', 343, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (344, '2019年测试工作日历', 2019, '2019-12-10', 344, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (345, '2019年测试工作日历', 2019, '2019-12-11', 345, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (346, '2019年测试工作日历', 2019, '2019-12-12', 346, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (347, '2019年测试工作日历', 2019, '2019-12-13', 347, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (348, '2019年测试工作日历', 2019, '2019-12-14', 348, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (349, '2019年测试工作日历', 2019, '2019-12-15', 349, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (350, '2019年测试工作日历', 2019, '2019-12-16', 350, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (351, '2019年测试工作日历', 2019, '2019-12-17', 351, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (352, '2019年测试工作日历', 2019, '2019-12-18', 352, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (353, '2019年测试工作日历', 2019, '2019-12-19', 353, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (354, '2019年测试工作日历', 2019, '2019-12-20', 354, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (355, '2019年测试工作日历', 2019, '2019-12-21', 355, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (356, '2019年测试工作日历', 2019, '2019-12-22', 356, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (357, '2019年测试工作日历', 2019, '2019-12-23', 357, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (358, '2019年测试工作日历', 2019, '2019-12-24', 358, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (359, '2019年测试工作日历', 2019, '2019-12-25', 359, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (360, '2019年测试工作日历', 2019, '2019-12-26', 360, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (361, '2019年测试工作日历', 2019, '2019-12-27', 361, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (362, '2019年测试工作日历', 2019, '2019-12-28', 362, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (363, '2019年测试工作日历', 2019, '2019-12-29', 363, 0, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (364, '2019年测试工作日历', 2019, '2019-12-30', 364, 1, NULL, NULL, '1');
INSERT INTO `work_day` VALUES (365, '2019年测试工作日历', 2019, '2019-12-31', 365, 1, NULL, NULL, '1');
COMMIT;

CREATE TABLE `work_order` (
                              `id` int(11) NOT NULL COMMENT '主键',
                              `code` varchar(64) NOT NULL COMMENT '工单编号',
                              `type` int(11) NOT NULL COMMENT '工单类型 1,咨询,2投诉,3 建议',
                              `title` varchar(255) NOT NULL COMMENT '工单标题',
                              `detail` longtext COMMENT '工单详情',
                              `custom_id` varchar(0) DEFAULT NULL COMMENT '客户ID',
                              `custom_name` varchar(200) DEFAULT NULL COMMENT '客户姓名',
                              `custom_phone` varchar(20) DEFAULT NULL COMMENT '客户电话',
                              `sku_code` varchar(64) DEFAULT NULL COMMENT '产品编号',
                              `order_code` varchar(64) DEFAULT NULL COMMENT '订单编号',
                              `stage` int(11) NOT NULL DEFAULT '0' COMMENT '处理状态,0待处理 1处理中 3处理完成',
                              `is_solved` int(11) NOT NULL DEFAULT '0' COMMENT '是否解决 0未解决 1已解决',
                              `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `created_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '创建者',
                              `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改日期',
                              `last_updated_by` int(11) DEFAULT NULL COMMENT '最后修改者',
                              `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 禁用,1 启用',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';

CREATE TABLE `workflow_category` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `name` varchar(255) NOT NULL COMMENT '名称',
                                     `definition_key` varchar(255) DEFAULT NULL COMMENT '所绑定的流程定义code',
                                     `parent_id` bigint(20) unsigned DEFAULT '0' COMMENT '父分类Id',
                                     `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
                                     `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改日期',
                                     `last_updated_by` bigint(20) DEFAULT NULL COMMENT '最后修改者',
                                     `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 禁用,1 启用',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `workflow_definition` (
                                       `id` varchar(64) NOT NULL COMMENT '主键',
                                       `name` varchar(200) NOT NULL COMMENT '流程名称',
                                       `description` varchar(255) DEFAULT NULL COMMENT '流程描述',
                                       `initial_code` varchar(128) NOT NULL COMMENT '流程初始编码,不随版本变化而改变',
                                       `version` int(11) NOT NULL COMMENT '版本号',
                                       `is_released` int(11) NOT NULL DEFAULT '0' COMMENT '是否已发布,1 是 0 否 默认为0',
                                       `deployment_id` varchar(64) DEFAULT NULL COMMENT '部署ID;启动流程实例需要此参数',
                                       `business_url` text COMMENT '申请单任务详情',
                                       `initial_url` text COMMENT '申请单流程详情URl',
                                       `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
                                       `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改日期',
                                       `last_updated_by` bigint(20) DEFAULT NULL COMMENT '最后修改者',
                                       `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 禁用,1 启用',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程定义表';

CREATE TABLE `workflow_form` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `definition_id` varchar(64) DEFAULT NULL COMMENT '所属流程定义ID',
                                 `form_content` longtext COMMENT '表单内容',
                                 `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
                                 `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改日期',
                                 `last_updated_by` bigint(20) DEFAULT NULL COMMENT '最后修改者',
                                 `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 禁用,1 启用',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程启动时的表单';

CREATE TABLE `workflow_history` (
                                    `id` varchar(64) NOT NULL COMMENT '主键',
                                    `name` varchar(255) NOT NULL COMMENT '流程实例的名称',
                                    `business_key` varchar(255) DEFAULT NULL COMMENT '业务数据的唯一标识符',
                                    `business_url` text,
                                    `definition_id` varchar(64) DEFAULT NULL COMMENT '流程定义的ID',
                                    `definition_version` int(11) DEFAULT NULL COMMENT '流程定义的版本号',
                                    `definition_code` varchar(128) NOT NULL COMMENT '流程定义的code',
                                    `delete_reason` varchar(255) DEFAULT NULL COMMENT '作废原因',
                                    `owner_id` bigint(20) DEFAULT NULL COMMENT '所属人ID',
                                    `start_time` timestamp NULL DEFAULT NULL COMMENT '启动时间',
                                    `end_time` datetime DEFAULT NULL COMMENT '停止时间',
                                    `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
                                    `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改日期',
                                    `last_updated_by` bigint(20) DEFAULT NULL COMMENT '最后修改者',
                                    `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 禁用,1 启用',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程实例表';

CREATE TABLE `workflow_instance` (
                                     `id` varchar(64) NOT NULL COMMENT '主键',
                                     `name` varchar(255) NOT NULL COMMENT '流程实例的名称',
                                     `business_key` varchar(255) DEFAULT NULL COMMENT '业务数据的唯一标识符',
                                     `business_url` text COMMENT '业务数据详情地址',
                                     `definition_id` varchar(64) DEFAULT NULL COMMENT '流程定义的ID',
                                     `definition_version` int(11) DEFAULT NULL COMMENT '流程定义的版本号',
                                     `definition_code` varchar(128) NOT NULL COMMENT '流程定义的code',
                                     `delete_reason` varchar(255) DEFAULT NULL COMMENT '作废原因',
                                     `owner_id` bigint(20) DEFAULT NULL COMMENT '所属人ID',
                                     `start_time` timestamp NULL DEFAULT NULL COMMENT '启动时间',
                                     `end_time` datetime DEFAULT NULL COMMENT '停止时间',
                                     `source_platform` varchar(50) DEFAULT NULL COMMENT '来源平台，如MP',
                                     `source_platform_user_id` varchar(30) DEFAULT NULL COMMENT '源平台的用户ID',
                                     `source_platform_user_name` varchar(50) DEFAULT NULL COMMENT '源平台的用户名',
                                     `created_on` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                                     `created_by` bigint(20) DEFAULT NULL COMMENT '创建者',
                                     `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改日期',
                                     `last_updated_by` bigint(20) DEFAULT NULL COMMENT '最后修改者',
                                     `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态：-1废弃，0 待启动,1运行中,2暂停',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程实例表';

SET FOREIGN_KEY_CHECKS = 1;
