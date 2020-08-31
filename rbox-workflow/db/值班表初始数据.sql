truncate table duty_roster;
insert into duty_roster(duty_date,person_name,person_id) select duty_date,person_name,person_id from  (
    select
        STR_TO_DATE(t1.day,'%Y-%m-%d')  duty_date,
        u.nickname person_name,
        u.id person_id
    from (
             (select '袁伟玲' user_name, '2020-01-16' day ) union
             (select '王懿浈' user_name, '2020-01-17' day) union
             (select '邓子其' user_name, '2020-01-18' day) union
             (select '袁伟玲' user_name, '2020-01-19' day) union
             (select '郭倩君' user_name, '2020-01-20' day) union
             (select '肖璐' user_name, '2020-01-21' day) union
             (select '肖璐' user_name, '2020-01-22' day) union
             (select null user_name, '2020-01-23' day) union
             (select null user_name, '2020-01-24' day) union
             (select null user_name, '2020-01-25' day) union
             (select null user_name, '2020-01-26' day) union
             (select null user_name, '2020-01-27' day) union
             (select null user_name, '2020-01-28' day) union
             (select null user_name, '2020-01-29' day) union
             (select null user_name, '2020-01-30' day) union
             (select '肖璐' user_name, '2020-01-31' day) union
             (select '肖璐' user_name, '2020-02-01' day) union
             (select '叶凯' user_name, '2020-02-02' day) union
             (select '樊笑然' user_name, '2020-02-03' day) union
             (select '邓子其' user_name, '2020-02-04' day) union
             (select '肖璐' user_name, '2020-02-05' day) union
             (select '郭倩君' user_name, '2020-02-06' day) union
             (select '陈历' user_name, '2020-02-07' day) union
             (select '操景红' user_name, '2020-02-08' day) union
             (select '王争艳' user_name, '2020-02-09' day) union
             (select '袁伟玲' user_name, '2020-02-10' day) union
             (select '肖璐' user_name, '2020-02-11' day) union
             (select '肖璐' user_name, '2020-02-12' day) union
             (select '王懿浈' user_name, '2020-02-13' day) union
             (select '郭倩君' user_name, '2020-02-14' day) union
             (select '柏仁杰' user_name, '2020-02-15' day) union
             (select '陈振亚' user_name, '2020-02-16' day) union
             (select '樊笑然' user_name, '2020-02-17' day) union
             (select '肖璐' user_name, '2020-02-18' day) union
             (select '肖璐' user_name, '2020-02-19' day) union
             (select '陈历' user_name, '2020-02-20' day) union
             (select '邓子其' user_name, '2020-02-21' day) union
             (select '楚森' user_name, '2020-02-22' day) union
             (select '孙静静' user_name, '2020-02-23' day) union
             (select '袁伟玲' user_name, '2020-02-24' day) union
             (select '王懿浈' user_name, '2020-02-25' day) union
             (select '郭倩君' user_name, '2020-02-26' day) union
             (select '肖璐' user_name, '2020-02-27' day) union
             (select '肖璐' user_name, '2020-02-28' day) union
             (select '邓团' user_name, '2020-02-29' day) union
             (select '宫新程' user_name, '2020-03-01' day) union
             (select '许凯' user_name, '2020-03-02' day)
         ) t1 left join as_user u on t1.user_name = u.nickname
    order by day
) t2

