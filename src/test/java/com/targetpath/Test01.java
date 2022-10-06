package com.targetpath;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author DengBo_Zhong
 * @version V1.0
 * @date 2022/10/1 23:35
 */

public class Test01 {

    @Test
    @Ignore
    public void test(){
        System.out.println("Test01");

        //获取 processEngineConfiguration 对象
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        // 配置相关的数据库的链接信息

        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("root");
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/auth-center?serverTimeZone=UTC&nullCatalogMeansCurrent=true");

        // 如果数据库中的表结构不存在就新建
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);


        // 通过processEngineConfiguration对象构建我们需要的processEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }

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

    /**
     * 流程定义部署
     */
    @Test
    @Ignore
    public void testDeploy(){
        // 通过processEngineConfiguration对象构建我们需要的processEngine对象
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        // 获取RepositoryService
        RepositoryService repositoryService = processEngineConfiguration.getRepositoryService();

        // 完成流程的部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/ holiday-request.bpmn20.xml") //关联要部署的流程
                .name("请假流程")
                .deploy();

        System.out.println("deploy.getId() = " + deploy.getId());
        System.out.println("deploy.getName() = " + deploy.getName());

    }

    /**
     * 查询流程定义
     */
    @Test
    @Ignore
    public void testDeployQuery(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = processDefinitionQuery.deploymentId("1")
                .singleResult();
        System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
        System.out.println("processDefinition.getName() = " + processDefinition.getName());
        System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
        System.out.println("processDefinition.getId() = " + processDefinition.getId());

    }

    /**
     * 删除流程定义
     */
    @Test
    @Ignore
    public void testDeleteDeploy(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 删除部署的流程，第一个参数是ID 如果部署的流程启动了就不允许删除了
        // repositoryService.deleteDeployment("1");
        // 第二个参数是级联删除，如果流程启动了，相关的任务一并会删除，
        repositoryService.deleteDeployment("2501",true);

    }

    /**
     * 启动一个流程
     */
    @Test
    @Ignore
    public void testRunProcess(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        HashMap<String, Object> variables = new HashMap<>();
        variables.put("employee","钟登博");
        variables.put("nrOfHoildays",3);
        variables.put("desciption","不干了，离职");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        System.out.println("processInstance.getProcessDefinitionId() = " + processInstance.getProcessDefinitionId());
        System.out.println("processInstance.getActivityId() = " + processInstance.getActivityId());
        System.out.println(processInstance.getId());

    }

    /**
     * 查询流程定义的任务
     */
    @Test
//    @Ignore
    public void testQueryTask(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest") //指定查询的流程编号
                .taskAssignee("钟登博")
                .list();

        for(Task task : list){
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getDescription() = " + task.getDescription());
            System.out.println("task.getId() = " + task.getId());
        }
    }

    /** 
     * @Description  完成一个任务
            
     * @Author Zhong-Dengbo
     * @Date 10:59 2022/10/5
     * @Param []
     * @return void
     **/
    @Test
    @Ignore
    public void testCompleteTask(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest")
                .taskAssignee("钟登博")
                .singleResult();

        HashMap<String, Object> map = new HashMap<>();
        map.put("approved",false);

        taskService.complete(task.getId(),map);
    }

    @Test
    @Ignore
    public void testQueryHistory(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId("holidayRequest:1:7503")
                .finished()
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        for (HistoricActivityInstance history: list){
            System.out.println("history.getActivityId() = " + history.getActivityId());
            System.out.println("history.getDurationInMillis() = " + history.getDurationInMillis());
            System.out.println("history.getActivityName() = " + history.getActivityName());
            System.out.println("history.getAssignee() = " + history.getAssignee());
        }
    }

    @Test
    @Ignore
    public void testSuspended(){
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("holidayRequest:1:3")
                .singleResult();
        boolean suspended = processDefinition.isSuspended();
        System.out.println("suspended = " + suspended);
        if (suspended){
            System.out.println("processDefinition.getId()+processDefinition.getName() = " + processDefinition.getId() + processDefinition.getName());
            repositoryService.activateProcessDefinitionById("holidayRequest:1:3");
        }else{
            System.out.println("processDefinition.getId()+processDefinition.getName() = " + processDefinition.getId() + processDefinition.getName());
            repositoryService.suspendProcessDefinitionById("holidayRequest:1:3");

        }
    }
}
