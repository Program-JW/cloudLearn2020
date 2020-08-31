-- 查看确认中的问题列表
SELECT
    *
FROM
    (
        SELECT
            ( SELECT GROUP_CONCAT( DISTINCT map.first_level_department_name ) FROM temp_user_department_map map WHERE map.user_id = i.created_by ) '申请人一级部门',
            ( SELECT GROUP_CONCAT( DISTINCT map.second_level_department_name ) FROM temp_user_department_map map WHERE map.user_id = i.created_by ) '申请人二级部门',
            (SELECT group_concat( g.NAME )FROM as_user_group_asso ug,as_group g WHERE g.id = ug.group_id AND g.STATUS = 1 AND g.is_deleted = 0 AND ug.user_id = u.id) '申请人所在组',
            i.created_by '申请人ID',
            u.nickname '申请人',
            i.description '问题描述',
            CASE i.STATUS WHEN 0 THEN '发起' WHEN 1 THEN '待受理' WHEN 2 THEN '受理中' WHEN 3 THEN '待确认' WHEN 4 THEN '已解决' WHEN 5 THEN '未解决' WHEN 7 THEN '已撤销' ELSE '' END AS '问题状态',
            i.created_on '问题创建时间',
            (SELECT log.created_on FROM lightning_issue_log log WHERE log.issue_id = i.id AND i.current_solver_id = log.created_by AND log.action = 3 ORDER BY log.id DESCLIMIT 1) '受理人点击已解决时间'
        FROM
            lightning_issue_apply i
                LEFT JOIN as_user u ON i.created_by = u.id
                LEFT JOIN as_user u1 ON i.current_solver_id = u1.id
        WHERE
                i.STATUS = 3
    ) t
ORDER BY `申请人一级部门` asc,`申请人二级部门` asc

;

-- 按二级部门聚合查看确认中的问题数
SELECT
    t.level1,t.level2 '二级部门',count(*) ’确认中的数量‘
FROM
    (
    SELECT
    ( SELECT GROUP_CONCAT( DISTINCT map.first_level_department_name ) FROM temp_user_department_map map WHERE map.user_id = i.created_by ) level1,
    ( SELECT GROUP_CONCAT( DISTINCT map.second_level_department_name ) FROM temp_user_department_map map WHERE map.user_id = i.created_by ) level2,
    i.id
    FROM
    lightning_issue_apply i
    LEFT JOIN as_user u ON i.created_by = u.id
    LEFT JOIN as_user u1 ON i.current_solver_id = u1.id
    WHERE
    i.STATUS = 3
    ) t
GROUP BY t.level1,t.level2;
