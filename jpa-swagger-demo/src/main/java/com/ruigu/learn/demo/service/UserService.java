package com.ruigu.learn.demo.service;

import com.ruigu.learn.demo.model.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/7/29 11:30
 */
public interface UserService {

    /**
     * TODO
     *
     * @param id
     * @return
     */
    Optional<UserEntity> queryUserById(int id);

    /**
     * TODO
     *
     * @return
     */
    List<UserEntity> queryAllUser();

    /**
     * TODO
     *
     * @param userEntity
     */
    void addUser(UserEntity userEntity);


    /**
     * TODO
     *
     * @param id
     */
    void deleteUser(int id);


    /**
     * TODO
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
    List<UserEntity> findBySql(String name);


}
