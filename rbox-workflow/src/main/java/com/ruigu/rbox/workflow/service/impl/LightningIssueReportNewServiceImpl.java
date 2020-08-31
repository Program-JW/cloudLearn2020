package com.ruigu.rbox.workflow.service.impl;

import com.UpYun;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.ruigu.rbox.cloud.kanai.util.ConvertUtil;
import com.ruigu.rbox.cloud.kanai.web.page.PageImpl;
import com.ruigu.rbox.workflow.config.ReportEmailTargetParamProperties;
import com.ruigu.rbox.workflow.config.UpyunConfig;
import com.ruigu.rbox.workflow.exceptions.VerificationFailedException;
import com.ruigu.rbox.workflow.feign.PassportFeignClient;
import com.ruigu.rbox.workflow.model.ServerResponse;
import com.ruigu.rbox.workflow.model.dto.DepartmentIssueDTO;
import com.ruigu.rbox.workflow.model.dto.LightningIssueReportNewDTO;
import com.ruigu.rbox.workflow.model.dto.WholeReportDTO;
import com.ruigu.rbox.workflow.model.enums.QueryFormType;
import com.ruigu.rbox.workflow.model.enums.ResponseCode;
import com.ruigu.rbox.workflow.model.vo.EmailAttachment;
import com.ruigu.rbox.workflow.model.vo.GroupAndUserVO;
import com.ruigu.rbox.workflow.model.vo.lightning.AllUserDepartmentNameVO;
import com.ruigu.rbox.workflow.model.vo.lightning.DepartmentIssueVO;
import com.ruigu.rbox.workflow.model.vo.lightning.LightningIssueReportNewVO;
import com.ruigu.rbox.workflow.model.vo.lightning.UserDepartmentsAndNameVO;
import com.ruigu.rbox.workflow.repository.LightningIssueApplyRepository;
import com.ruigu.rbox.workflow.repository.LightningIssueLogRepository;
import com.ruigu.rbox.workflow.service.LightningIssueReportNewService;
import com.ruigu.rbox.workflow.service.QuestNoticeService;
import com.upyun.UpException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author caojinghong
 * @date 2020/01/09 15:15
 */
@Service
@Slf4j
public class LightningIssueReportNewServiceImpl implements LightningIssueReportNewService {
    @Autowired
    private LightningIssueLogRepository logRepository;
    @Autowired
    private UpyunConfig upyunConfig;
    @Autowired
    private QuestNoticeService questNoticeService;
    @Autowired
    private PassportFeignClient passportFeignClient;
    @Autowired
    private LightningIssueApplyRepository applyRepository;
    @Value("${rbox.workflow.lightning.report.user-id.targetList1}")
    private List<Integer> targetList1;
    @Autowired
    private ReportEmailTargetParamProperties reportEmailTargetParamProperties;

    @Override
    public ServerResponse exportExcel(Integer queryType, LocalDateTime startQueryTime, LocalDateTime endQueryTime) throws Exception {
        LocalDate queryMonth;
        String name = "闪电链报表数据-";
        LocalDateTime start;
        LocalDateTime end;
        if (QueryFormType.LAST_MONTH.getCode().equals(queryType)) {
            // 获取上个月日期
            queryMonth = LocalDate.now().minusMonths(1);
            name = name + queryMonth.getYear() + "年" + queryMonth.getMonthValue() + "月";
            start = LocalDateTime.of(queryMonth.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
            end = LocalDateTime.of(queryMonth.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
        } else if (QueryFormType.CURRENT_MONTH.getCode().equals(queryType)) {
            queryMonth = LocalDate.now();
            name = name + queryMonth.getYear() + "年" + queryMonth.getMonthValue() + "月";
            start = LocalDateTime.of(queryMonth.with(TemporalAdjusters.firstDayOfMonth()), LocalTime.MIN);
            end = LocalDateTime.of(queryMonth.with(TemporalAdjusters.lastDayOfMonth()), LocalTime.MAX);
        } else if (QueryFormType.LAST_DAY.getCode().equals(queryType)) {
            queryMonth = LocalDate.now().plusDays(-1);
            name = name + queryMonth.getYear() + "年" + queryMonth.getMonthValue() + "月" + queryMonth.getDayOfMonth() + "日";
            start = LocalDateTime.of(queryMonth, LocalTime.MIN);
            end = LocalDateTime.of(queryMonth, LocalTime.MAX);
        } else if (QueryFormType.CUSTOM_TIME.getCode().equals(queryType)) {
            if (startQueryTime == null || endQueryTime == null) {
                return ServerResponse.fail(ResponseCode.REQUEST_ERROR.getCode(),"请输入必要的时间参数!");
            }
            queryMonth = startQueryTime.toLocalDate();
            LocalDate endQueryDate = endQueryTime.toLocalDate();
            name = name + queryMonth.getYear() + "年" + queryMonth.getMonthValue() + "月" + queryMonth.getDayOfMonth() + "日至"+ endQueryDate.getYear() + "年" + endQueryDate.getMonthValue() + "月" + endQueryDate.getDayOfMonth() + "日";
            start = startQueryTime;
            end = endQueryTime;
        }
        else {
            throw new VerificationFailedException(ResponseCode.REQUEST_ERROR.getCode(), "queryType错误");
        }
        Date startTime = Date.from( start.atZone( ZoneId.systemDefault()).toInstant());
        Date endTime = Date.from( end.atZone( ZoneId.systemDefault()).toInstant());

        File tempFile = File.createTempFile(name, ".xlsx");

        ExcelWriter excelWriter = EasyExcel.write(tempFile).build();
        WriteSheet writeSheet1 = EasyExcel.writerSheet(0,"整体").head(WholeReportDTO.class).build();
        WriteSheet writeSheet2 = EasyExcel.writerSheet(1, "分人").head(LightningIssueReportNewVO.class).build();
        WriteSheet writeSheet3 = EasyExcel.writerSheet(2,"按部门").head(DepartmentIssueVO.class).build();

        Pageable page = PageRequest.of(0,Integer.MAX_VALUE);
        // 查询整体的数据
        Page<Map> wholeMaps= applyRepository.queryWholeData(startTime, endTime, page);
        List<WholeReportDTO> sheet1Data = PageImpl.of(wholeMaps, x -> ConvertUtil.mapToObject(x, WholeReportDTO.class)).getContent();
        WholeReportDTO wholeReportDto = sheet1Data.get(0);
        if (wholeReportDto.getIssueCount().equals(0)) {
            wholeReportDto.setIssueDemandCount(0);
            wholeReportDto.setEvaluateScoreCount(0);
            excelWriter.write(sheet1Data, writeSheet1);
            excelWriter.finish();
            String upYunFilePath = uploadFile(tempFile, name);
            return ServerResponse.ok("查询月份无数据，又拍云存储地址： "+upYunFilePath);
        }
        excelWriter.write(sheet1Data, writeSheet1);

        // 查询分人的数据
        Page<Map> maps = logRepository.queryReportFormNew(startTime, endTime, page);
        List<LightningIssueReportNewDTO> reportDtos = PageImpl.of(maps, x -> ConvertUtil.mapToObject(x, LightningIssueReportNewDTO.class)).getContent();

        // 查询的月份有数据
        if (CollectionUtils.isNotEmpty(reportDtos)) {
            List<Integer> userIds = new ArrayList<>();
            for (LightningIssueReportNewDTO everyData: reportDtos) {
                userIds.add(everyData.getUserId());
            }
            ServerResponse<List<UserDepartmentsAndNameVO>> listServerResponse = passportFeignClient.queryUserDepartmentsAndName(userIds);
            List<UserDepartmentsAndNameVO> userList = listServerResponse.getData();
            if (listServerResponse.getCode() != ResponseCode.SUCCESS.getCode() || CollectionUtils.isEmpty(userList)) {
                throw new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(), "请求权限中心获取用户部门信息数据异常");
            }

            Map<Integer, UserDepartmentsAndNameVO> collect = handleListToMap(userList);
            List<LightningIssueReportNewVO> sheet2Data = new ArrayList<>();
            for (LightningIssueReportNewDTO everyData: reportDtos) {
                Integer userId = everyData.getUserId();
                UserDepartmentsAndNameVO userDepartmentsAndNameVO = collect.get(userId);
                if (userDepartmentsAndNameVO == null) {
                    log.error("报表 请求权限中心获取用户部门信息有数据异常，异常用户id: {}",userId);
                    continue;
                }
                LightningIssueReportNewVO reportVO = new LightningIssueReportNewVO();
                BeanUtils.copyProperties(everyData, reportVO);
                reportVO.setPersonName(userDepartmentsAndNameVO.getNickname());
                reportVO.setFirstLevelDepartmentName(userDepartmentsAndNameVO.getFirstLevelDepartment());
                reportVO.setSecondLevelDepartmentName(userDepartmentsAndNameVO.getSecondaryLevelDepartment());
                if (StringUtils.isBlank(reportVO.getSecondLevelDepartmentName())) {
                    reportVO.setSecondLevelDepartmentName("无");
                }
                sheet2Data.add(reportVO);
            }
            List<LightningIssueReportNewVO> returnData= sheet2Data.stream().sorted(Comparator.comparing(LightningIssueReportNewVO::getFirstLevelDepartmentName).thenComparing(LightningIssueReportNewVO::getSecondLevelDepartmentName)).collect(Collectors.toList());
            excelWriter.write(returnData, writeSheet2);

        }
        // 查询按部门统计的数据
        Page<Map> departmentMaps = applyRepository.queryDepartmentData(startTime, endTime, page);
        List<DepartmentIssueDTO> departmentContent = PageImpl.of(departmentMaps, x -> ConvertUtil.mapToObject(x, DepartmentIssueDTO.class)).getContent();
        Map<String, GroupInfo> groupAndUserInfo = getGroupAndUserInfo();
        if (CollectionUtils.isNotEmpty(departmentContent) && groupAndUserInfo != null) {
            List<DepartmentIssueVO> sheet3Data = new ArrayList<>();
            for (DepartmentIssueDTO departmentIssueDTO : departmentContent) {
                GroupInfo groupInfo = groupAndUserInfo.get(String.valueOf(departmentIssueDTO.getSecondLevelDepartmentId()));
                DepartmentIssueVO departmentIssueVO = new DepartmentIssueVO();
                BeanUtils.copyProperties(departmentIssueDTO, departmentIssueVO);
                departmentIssueVO.setFirstLevelDepartmentName(groupInfo.getFirstLevelGroup());
                departmentIssueVO.setSecondLevelDepartmentName(groupInfo.getSecondLevelGroup());
                sheet3Data.add(departmentIssueVO);
            }
            excelWriter.write(sheet3Data, writeSheet3);
        }

        // 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
        // 上传文件至又拍云
        String upYunFilePath = uploadFile(tempFile, name);

        // 删除文件
        tempFile.delete();

        // 发送邮件
        List<Integer> targetIds = targetList1;
        List<Integer> ccTargetIds = reportEmailTargetParamProperties.getCcTargetList();
        Set<EmailAttachment> emailAttachments = new HashSet<>();
        EmailAttachment emailAttachment = new EmailAttachment();
        emailAttachment.setName(name+".xlsx");
        emailAttachment.setUrl(upYunFilePath);
        emailAttachments.add(emailAttachment);
        String title = name;
        String content = "请见附件!";
        return questNoticeService.sendEmailNotice(null, title, content, targetIds, ccTargetIds, emailAttachments);
    }

    @Override
    public ServerResponse getAllUserDepartmentInfo() throws IOException {
        String name = "全公司所有人所在一二级部门名称";
        File tempFile = File.createTempFile(name, ".xlsx");

        ExcelWriter excelWriter = EasyExcel.write(tempFile).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(0,"全体员工").head(AllUserDepartmentNameVO.class).build();
        List<Integer> userIds = new ArrayList<>();
        for (int i = 658; i < 2074; i++) {
            userIds.add(i);
        }
        ServerResponse<List<UserDepartmentsAndNameVO>> listServerResponse = passportFeignClient.queryUserDepartmentsAndName(userIds);
        List<UserDepartmentsAndNameVO> userList = listServerResponse.getData();
        if (listServerResponse.getCode() != ResponseCode.SUCCESS.getCode() || CollectionUtils.isEmpty(userList)) {
            throw new VerificationFailedException(ResponseCode.INTERNAL_ERROR.getCode(), "请求权限中心获取用户部门信息数据异常");
        }
        Set<Integer> userIdsSet = userList.stream().map(UserDepartmentsAndNameVO::getUserId).collect(Collectors.toSet());
        Map<Integer, UserDepartmentsAndNameVO> collect = handleListToMap(userList);
        List<AllUserDepartmentNameVO> userInfoList = new ArrayList<>();
        for (Integer everyUserId: userIdsSet) {
            UserDepartmentsAndNameVO userDepartmentsAndNameVO = collect.get(everyUserId);
            AllUserDepartmentNameVO everyUser = new AllUserDepartmentNameVO();
            everyUser.setUserId(everyUserId);
            everyUser.setPersonName(userDepartmentsAndNameVO.getNickname());
            everyUser.setFirstLevelDepartmentName(userDepartmentsAndNameVO.getFirstLevelDepartment());
            everyUser.setSecondLevelDepartmentName(userDepartmentsAndNameVO.getSecondaryLevelDepartment());
            userInfoList.add(everyUser);
        }
        excelWriter.write(userInfoList, writeSheet);
        // 千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
        return ServerResponse.ok();
    }

    /**
     * 获取路径
     * @param filePrefix 文件名称的前缀（被抽取放置在properties文件处）
     * @param fileName 文件名称
     * @return 返回文件的相对路径
     */
    private String getFilePath(String filePrefix,String fileName) {
        return filePrefix + fileName + UUID.randomUUID().toString()+".xlsx";
    }

    /**
     * 上传文件至又拍云
     * @param tempFile 临时文件
     * @param fileName 文件名称
     * @return 返回文件上传至又拍云后的地址（绝对路径）
     * @throws IOException IO异常
     * @throws UpException 又拍云异常
     */
    private String uploadFile(File tempFile, String fileName) throws IOException, UpException {
        log.info("开始上传报表文件");
        String filePath =  getFilePath(upyunConfig.getFilePathPrefix(),fileName);
        UpYun upyun = new UpYun(upyunConfig.getBucketName(), upyunConfig.getUsername(), upyunConfig.getPassword());
        boolean isSuccess = upyun.writeFile(filePath, tempFile, true);
        if (!isSuccess) {
            throw new UpException("报表文件上传失败");
        }
        log.info("报表文件上传成功");
        return upyunConfig.getPrefix() + filePath;
    }

    /**
     * 调用passport的按级别查询部门接口，并处理
     * @return 返回map集合，键为二级部门Id,对应的值为二级部门信息
     */
    private Map<String, GroupInfo> getGroupAndUserInfo(){
        ServerResponse<List<GroupAndUserVO>> groupAndUserInfoResponse = passportFeignClient.getGroupAndUserInfo(null, null);
        if (groupAndUserInfoResponse.getCode() != ResponseCode.SUCCESS.getCode()) {
            log.error("通过权限中心获取部门信息失败");
            return null;
        }
        List<GroupAndUserVO> groupAndUserInfo = groupAndUserInfoResponse.getData();
        // 二级部门id map
        Map<String, GroupInfo> map = new HashMap<>(32);
        for (GroupAndUserVO info : groupAndUserInfo) {
            if (CollectionUtils.isEmpty(info.getChildren())) {
                continue;
            }
            for (GroupAndUserVO subInfo : info.getChildren()) {
                if (subInfo.getValue() != null) {
                    GroupInfo groupInfo = new GroupInfo();
                    groupInfo.setFirstLevelGroup(info.getLabel());
                    groupInfo.setSecondLevelGroup(subInfo.getLabel());
                    map.put(subInfo.getValue().toString(), groupInfo);
                }
            }
        }
        return map;
    }

    /**
     * 处理查询到是用户所在一二级部门，将有两个部门的名称拼接，并以map集合形式返回
     * @param userList 待处理的集合
     * @return 处理之后的Map
     */
    private Map<Integer, UserDepartmentsAndNameVO> handleListToMap(List<UserDepartmentsAndNameVO> userList){
        return userList.stream().collect(Collectors.toMap(
                UserDepartmentsAndNameVO::getUserId, Function.identity(),
                (user1, user2) -> {
                    String firstLevelDepartment1 = user1.getFirstLevelDepartment();
                    String firstLevelDepartment2 = user2.getFirstLevelDepartment();
                    String secondaryLevelDepartment1 = user1.getSecondaryLevelDepartment();
                    String secondaryLevelDepartment2 = user2.getSecondaryLevelDepartment();
                    String second = "/";
                    if (StringUtils.isBlank(secondaryLevelDepartment1)) {
                        user1.setSecondaryLevelDepartment("");
                        second = "";
                    }
                    if (StringUtils.isBlank(secondaryLevelDepartment2)) {
                        user2.setSecondaryLevelDepartment("");
                        second = "";
                    }
                    String secondName = user1.getSecondaryLevelDepartment() + second + user2.getSecondaryLevelDepartment();
                    user1.setSecondaryLevelDepartment(secondName);

                    String first = "/";
                    if (StringUtils.isEmpty(firstLevelDepartment1)) {
                        user1.setFirstLevelDepartment("");
                        first = "";
                    }
                    if (StringUtils.isEmpty(firstLevelDepartment2)) {
                        user2.setFirstLevelDepartment("");
                        first = "";
                    }
                    String firstName = user1.getFirstLevelDepartment() + first + user2.getFirstLevelDepartment();
                    user1.setFirstLevelDepartment(firstName);
                    return user1;
                }));
    }
    @Data
    private static class GroupInfo {
        private String firstLevelGroup;
        private String secondLevelGroup;
    }
}
