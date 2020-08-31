package com.ruigu.rbox.workflow.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * @author caojinghong
 * @date 2020/01/02 15:23
 */
@Configuration
public class JpaQueryFactory {

    @Bean("JpaFactory")
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager){
        return new JPAQueryFactory(entityManager);
    }
}
