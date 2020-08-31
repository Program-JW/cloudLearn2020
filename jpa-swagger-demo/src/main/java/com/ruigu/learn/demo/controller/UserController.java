package com.ruigu.learn.demo.controller;

import com.ruigu.learn.demo.model.UserEntity;
import com.ruigu.learn.demo.service.impl.UserServiceImpl;
import com.ruigu.learn.demo.service.impl.UserServiceImpl2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author PanJianWei
 * @version 1.0
 * @date 2020/7/29 11:16
 */

@RestController
@RequestMapping("/user")
@Api(value = "用户controller", tags = {"用户操作接口"})
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserServiceImpl2 userService2;

    @Resource
    private DataSource dataSource;

    @GetMapping("/test")
    public void test() throws SQLException {
        System.out.println("---------------------------");
        System.out.println(dataSource.getConnection());
    }



    /**
     * 添加用户
     *
     * @param userEntity
     */
    @ApiOperation(value = "创建用户", notes = "创建用户时的注意事项")
    @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, paramType = "body", dataType = "UserEntity")
    @PostMapping("/addUser")
    public void addUser(@RequestBody UserEntity userEntity) {
        // TODO
        //userService.addUser(userEntity);
        userService2.addUser(userEntity);
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @GetMapping("/deleteUser")
    public void deleteUser(@RequestParam int id) {
        // TODO
    }

    /**
     * 查询某个用户
     *
     * @param id
     * @return
     */
    @GetMapping("/queryUserById")
    public List<UserEntity> queryUserById(@RequestParam int id) {
        // TODO

        return null;
    }

    /**
     * 查询所有用户
     *
     * @return
     */
    @GetMapping("/queryAllUser")
    public List<UserEntity> queryAllUser() {
        // TODO

        return null;
    }


    /**
     * 通过用户名和密码查询用户
     *
     * @param name
     * @param password
     * @return
     */
    @GetMapping("/findByNameAndPassword")
    public Optional<UserEntity> findByNameAndPassword(@RequestParam("name") String name,
                                                      @RequestParam("password") String password) {

//        System.out.println(userService.findBySql(name));
//        System.out.println(userService2.queryUserById(1));
//        System.out.println(userService2.queryAllUser());
//        System.out.println(userService2.findByNameAndPassword(name, password));
//        Page<UserEntity> userEntityPage = userService2.findAllPage();
//        if (userEntityPage.hasNext()) {
//            System.out.println(userEntityPage.getContent());
//            userEntityPage.getTotalPages();
//        }
        System.out.println(userService2.findAllPage2());
//        System.out.println(userService2.queryAllUserNativeSql());
        return userService.findByNameAndPassword(name, password);
    }

    /**
     * 更新用户电话号码
     *
     * @param id
     * @param phone
     */
    @GetMapping("/updateUserPhone")
    public void updateUserPhone(@RequestParam("id") int id,
                                @RequestParam("phone") String phone) {
        userService2.updateUserPhone(phone, id);
    }


}
