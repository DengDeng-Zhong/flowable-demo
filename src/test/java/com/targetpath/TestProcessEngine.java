package com.targetpath;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Zhong-Dengbo
 * @version V1.0
 * @ClassName: TestProcessEngine
 * @date 2022/10/5 14:22
 */
public class TestProcessEngine {

    ProcessEngineConfiguration processEngineConfiguration = null;
    @Before
    public void before(){
        //获取 processEngineConfiguration 对象
        processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        // 配置相关的数据库的链接信息

        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("root");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/auth-center?serverTimeZone=UTC&nullCatalogMeansCurrent=true");

        // 如果数据库中的表结构不存在就新建
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    }

    @Test
    public void testProcessEngine(){

    }
}
