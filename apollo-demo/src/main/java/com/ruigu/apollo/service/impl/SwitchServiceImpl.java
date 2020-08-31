package com.ruigu.apollo.service.impl;


import com.ruigu.apollo.config.DemoGlobalConfig;
import com.ruigu.apollo.service.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SwitchServiceImpl implements SwitchService {
    @Autowired
    private DemoGlobalConfig demoGlobalConfig;

    @Override
    public String getSwitchData() {
        if (demoGlobalConfig.isSwitchFlag()) {
            return "new";
        } else {
            return "old";
        }
    }
}