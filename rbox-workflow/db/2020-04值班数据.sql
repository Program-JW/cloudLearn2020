insert into duty_roster(duty_date,person_name,person_id) select duty_date,person_name,person_id from  (
    select
        STR_TO_DATE(t1.day,'%Y-%m-%d')  duty_date,
        u.nickname person_name,
        u.id person_id
    from (
             (select '樊笑然' user_name, '2020-04-01' day) union
             (select '陈历' user_name, '2020-04-02' day) union
             (select '邓子其' user_name, '2020-04-03' day) union
             (select '潘卓平' user_name, '2020-04-04' day) union
             (select '白冰' user_name, '2020-04-05' day) union
             (select '柏仁杰' user_name, '2020-04-06' day) union
             (select '袁伟玲' user_name, '2020-04-07' day) union
             (select '王懿浈' user_name, '2020-04-08' day) union
             (select '郭倩君' user_name, '2020-04-09' day) union
             (select '樊笑然' user_name, '2020-04-10' day) union
             (select '操景红' user_name, '2020-04-11' day) union
             (select '曹金柱' user_name, '2020-04-12' day) union
             (select '陈历' user_name, '2020-04-13' day) union
             (select '邓子其' user_name, '2020-04-14' day) union
             (select '袁伟玲' user_name, '2020-04-15' day) union
             (select '王懿浈' user_name, '2020-04-16' day) union
             (select '郭倩君' user_name, '2020-04-17' day) union
             (select '陈钢' user_name, '2020-04-18' day) union
             (select '陈钢1' user_name, '2020-04-19' day) union
             (select '樊笑然' user_name, '2020-04-20' day) union
             (select '陈历' user_name, '2020-04-21' day) union
             (select '邓子其' user_name, '2020-04-22' day) union
             (select '袁伟玲' user_name, '2020-04-23' day) union
             (select '王懿浈' user_name, '2020-04-24' day) union
             (select '陈振亚' user_name, '2020-04-25' day) union
             (select '郭倩君' user_name, '2020-04-26' day) union
             (select '樊笑然' user_name, '2020-04-27' day) union
             (select '陈历' user_name, '2020-04-28' day) union
             (select '邓子其' user_name, '2020-04-29' day) union
             (select '袁伟玲' user_name, '2020-04-30' day)
         ) t1 left join as_user u on t1.user_name = u.nickname
    order by day
) t2