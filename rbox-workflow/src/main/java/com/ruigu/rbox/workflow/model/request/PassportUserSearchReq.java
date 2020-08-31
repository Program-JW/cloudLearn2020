package com.ruigu.rbox.workflow.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author yuanLin
 * @date 2020-03-04 17:38
 */
@Data
@Builder
public class PassportUserSearchReq {
    /**
     * user : [{"nickname":"昵称","username":"名称","userId":11,"position":["",""],"role":["",""]}]
     * group : {"global":{"name":"","id":""},"item":[{"id":11,"type":"filter,deny","filter":{"user":[{"nickname":"昵称","username":"名称","userId":11,"position":[],"role":[]}]}}]}
     */

    private GroupBean group;
    private List<UserBeanX> user;


    @Data
    public static class GroupBean {
        /**
         * global : {"name":"","id":""}
         * item : [{"id":11,"type":"filter,deny","filter":{"user":[{"nickname":"昵称","username":"名称","userId":11,"position":[],"role":[]}]}}]
         */

        @ApiModelProperty(value = "全局过滤条件", required = false, notes = "全局过滤条件")
        private GlobalBean global;
        private List<ItemBean> item;


        @Data
        public static class GlobalBean {

            private List<String> nameList;
            private List<Integer> idList;
            private List<Integer> leaderList;
            private Boolean leaderInherit;
            private Boolean idInherit;
            private List<Integer> userIdNotIn;

        }

        @Data
        public static class ItemBean {
            /**
             * id : 11
             * type : filter,deny
             * filter : {"user":[{"nickname":"昵称","username":"名称","userId":11,"position":[],"role":[]}]}
             */

            private int id;
            private String type;
            private FilterBean filter;


            @Data
            public static class FilterBean {
                private List<UserBeanX> user;
            }
        }
    }

    @Data
    @Builder
    public static class UserBeanX {
        /**
         * nickname : 昵称
         * username : 名称
         * userId : 11
         * position : ["",""]
         * role : ["",""]
         */

        private List<String> nicknameList;
        private List<String> usernameList;
        private List<Integer> userIdList;
        private List<String> positionList;
        private List<String> role;

    }
}
