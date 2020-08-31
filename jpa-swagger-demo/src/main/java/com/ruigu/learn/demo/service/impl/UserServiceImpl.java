package com.ruigu.learn.demo.service.impl;

import com.ruigu.learn.demo.model.UserEntity;
import com.ruigu.learn.demo.repository.UserRepository;
import com.ruigu.learn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/7/29 11:35
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<UserEntity> queryUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserEntity> queryAllUser() {
        return (List<UserEntity>)userRepository.findAll();
    }

    @Override
    public void addUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(int id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userRepository.delete(userEntity);
    }

    @Override
    public Optional<UserEntity> findByNameAndPassword(String name, String password) {
        return userRepository.findByNameAndPassword(name,password);
    }

    @Override
    public List<UserEntity> findBySql(String name) {
        return userRepository.findBySql(name);
    }


}
