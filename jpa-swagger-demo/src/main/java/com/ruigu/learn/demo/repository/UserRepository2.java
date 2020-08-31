package com.ruigu.learn.demo.repository;


import com.ruigu.learn.demo.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * user CRUD repository
 * @author panjianwei
 */
@Repository
public interface UserRepository2 extends CrudRepository<UserEntity,Integer>, QuerydslPredicateExecutor<UserEntity> {



}
