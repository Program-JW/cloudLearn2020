package com.ruigu.learn.demo.service.impl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ruigu.learn.demo.model.QUserEntity;
import com.ruigu.learn.demo.model.UserEntity;
import com.ruigu.learn.demo.repository.UserRepository2;
import com.ruigu.learn.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/7/29 11:35
 */

@Service
public class UserServiceImpl2 implements UserService {

    @Autowired
    private UserRepository2 userRepository2;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private EntityManager entityManager;


    @Override
    public Optional<UserEntity> queryUserById(int id) {
        QUserEntity entity = QUserEntity.userEntity;
        return Optional.of(jpaQueryFactory.selectFrom(entity)
                .where(entity.id.eq(id))
                .fetchFirst());
    }

    @Override
    public List<UserEntity> queryAllUser() {
        QUserEntity entity = QUserEntity.userEntity;
        return jpaQueryFactory.select(entity)
                .from(entity)
                .fetch();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserEntity userEntity) {
        userRepository2.save(userEntity);
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public Optional<UserEntity> findByNameAndPassword(String name, String password) {
        QUserEntity entity = QUserEntity.userEntity;
        Predicate predicate = entity.isNotNull().or(entity.isNull());
        predicate = ExpressionUtils.and(predicate, entity.name.eq(name));
        predicate = ExpressionUtils.and(predicate, entity.password.eq(password));
        return userRepository2.findOne(predicate);
    }

    @Override
    public List<UserEntity> findBySql(String name) {
        return null;
    }

    /**
     * 使用repository进行分页
     *
     * @return
     */
    public Page<UserEntity> findAllPage() {
        QUserEntity entity = QUserEntity.userEntity;
        Sort sort = Sort.by("id").descending();
        return userRepository2.findAll(entity.isNotNull(), PageRequest.of(0, 3, sort));
    }

    public List<UserEntity> findAllPage2() {
        Pageable pageable = PageRequest.of(1, 3);
        QUserEntity entity = QUserEntity.userEntity;
        return jpaQueryFactory.selectFrom(entity)
                .orderBy(entity.id.desc())
                .offset(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * 更新用户电话号码
     *
     * @param phone
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPhone(String phone, int id) {
        QUserEntity entity = QUserEntity.userEntity;
        jpaQueryFactory.update(entity)
                .set(entity.phone, phone)
                .where(entity.id.eq(id))
                .execute();
    }

    /**
     * 使用原生的sql
     *
     * @return
     */
    public BigInteger queryAllUserNativeSql() {
        StringBuffer stringBuffer = new StringBuffer("select count(1) from user where 1=1 ");
        return (BigInteger) entityManager.createNativeQuery(stringBuffer.toString()).getSingleResult();
    }


}
