insert into duty_roster(duty_date,person_name,person_id) select duty_date,person_name,person_id from  (
    select
        STR_TO_DATE(t1.day,'%Y-%m-%d')  duty_date,
        u.nickname person_name,
        u.id person_id
    from (
             (select '樊笑然' user_name, '2020-03-02' day) union
             (select '肖璐' user_name, '2020-03-03' day) union
             (select '肖璐' user_name, '2020-03-04' day) union
             (select '陈历' user_name, '2020-03-05' day) union
             (select '邓子其' user_name, '2020-03-06' day) union
             (select '许凯' user_name, '2020-03-07' day) union
             (select '樊笑然' user_name, '2020-03-08' day) union
             (select '肖璐' user_name, '2020-03-09' day) union
             (select '肖璐' user_name, '2020-03-10' day) union
             (select '袁伟玲' user_name, '2020-03-11' day) union
             (select '王懿浈' user_name, '2020-03-12' day) union
             (select '郭倩君' user_name, '2020-03-13' day) union
             (select '何毅' user_name, '2020-03-14' day) union
             (select '王冲' user_name, '2020-03-15' day) union
             (select '樊笑然' user_name, '2020-03-16' day) union
             (select '陈历' user_name, '2020-03-17' day) union
             (select '邓子其' user_name, '2020-03-18' day) union
             (select '肖璐' user_name, '2020-03-19' day) union
             (select '肖璐' user_name, '2020-03-20' day) union
             (select '徐静' user_name, '2020-03-21' day) union
             (select '杜明宏' user_name, '2020-03-22' day) union
             (select '袁伟玲' user_name, '2020-03-23' day) union
             (select '王懿浈' user_name, '2020-03-24' day) union
             (select '肖璐' user_name, '2020-03-25' day) union
             (select '肖璐' user_name, '2020-03-26' day) union
             (select '郭倩君' user_name, '2020-03-27' day) union
             (select '俞霞菲' user_name, '2020-03-28' day) union
             (select '王争艳' user_name, '2020-03-29' day) union
             (select '肖璐' user_name, '2020-03-30' day) union
             (select '肖璐' user_name, '2020-03-31' day)
         ) t1 left join as_user u on t1.user_name = u.nickname
    order by day
) t2