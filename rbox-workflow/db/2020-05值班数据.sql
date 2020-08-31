insert into duty_roster(duty_date,person_name,person_id) select duty_date,person_name,person_id from  (
    select
        STR_TO_DATE(t1.day,'%Y-%m-%d')  duty_date,
        u.nickname person_name,
        u.id person_id
    from (
             (select '楚森' user_name, '2020-05-01' day) union
             (select '杜明宏' user_name, '2020-05-02' day) union
             (select '邓小明' user_name, '2020-05-03' day) union
             (select '董净茜' user_name, '2020-05-04' day) union
             (select '董松松' user_name, '2020-05-05' day) union
             (select '王懿浈' user_name, '2020-05-06' day) union
             (select '郭倩君' user_name, '2020-05-07' day) union
             (select '樊笑然' user_name, '2020-05-08' day) union
             (select '陈历' user_name, '2020-05-09' day) union
             (select '邓团' user_name, '2020-05-10' day) union
             (select '邓子其' user_name, '2020-05-11' day) union
             (select '袁伟玲' user_name, '2020-05-12' day) union
             (select '王懿浈' user_name, '2020-05-13' day) union
             (select '郭倩君' user_name, '2020-05-14' day) union
             (select '樊笑然' user_name, '2020-05-15' day) union
             (select '冯居原' user_name, '2020-05-16' day) union
             (select '宫新程' user_name, '2020-05-17' day) union
             (select '陈历' user_name, '2020-05-18' day) union
             (select '邓子其' user_name, '2020-05-19' day) union
             (select '袁伟玲' user_name, '2020-05-20' day) union
             (select '王懿浈' user_name, '2020-05-21' day) union
             (select '郭倩君' user_name, '2020-05-22' day) union
             (select '何毅' user_name, '2020-05-23' day) union
             (select '侯玉婷' user_name, '2020-05-24' day) union
             (select '樊笑然' user_name, '2020-05-25' day) union
             (select '陈历' user_name, '2020-05-26' day) union
             (select '邓子其' user_name, '2020-05-27' day) union
             (select '袁伟玲' user_name, '2020-05-28' day) union
             (select '王懿浈' user_name, '2020-05-29' day) union
             (select '侯占义' user_name, '2020-05-30' day) union
             (select '黄承晟' user_name, '2020-05-31' day)
         ) t1 left join as_user u on t1.user_name = u.nickname
    order by day
) t2