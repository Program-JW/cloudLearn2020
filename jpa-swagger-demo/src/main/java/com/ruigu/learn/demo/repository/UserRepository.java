package com.ruigu.learn.demo.repository;


import ch.qos.logback.core.boolex.EvaluationException;
import com.ruigu.learn.demo.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * user CRUD repository
 * @author panjianwei
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity,Integer> {

    /**
     * TODO
     * 通过用户名和密码进行查询
     * @param name
     * @param password
     * @return
     */
    Optional<UserEntity> findByNameAndPassword(String name, String password);

    /**
     * TODO
     * 通过名字进行查询
     * @param name
     * @return
     */
    @Query(value = "select id,name,password,phone,avatar,type from user where name=?",nativeQuery = true)
    List<UserEntity> findBySql(String name);


}
