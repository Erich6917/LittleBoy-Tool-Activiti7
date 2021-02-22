package com.little.cloud.step;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

/**
 * @Comment 部署相关
 * @Author LiYuan
 * @Date 2021-2-22
 */
@Slf4j
public class S02Deploy {


  String key = "myEvection";
  String staff_zhagnsan = "zhangsan";

  /**
   * 流程部署 1/2 加载bpmn和png内容
   */
  @Test
  public void testDeployment() throws UnsupportedEncodingException {
//        1、创建ProcessEngine
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2、获取RepositoryServcie
    RepositoryService repositoryService = processEngine.getRepositoryService();
//        3、使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据中
    String name = new String("出差申请流程1".getBytes("utf-8"), "utf-8");
    Deployment deploy = repositoryService.createDeployment().name(name)
        .addClasspathResource("bpmn/evection.bpmn")
        .addClasspathResource("bpmn/evection.png")
        .deploy();
    //        4、输出部署信息
    System.out.println("流程部署id=" + deploy.getId());
    System.out.println("流程部署名字=" + deploy.getName());
  }


  /**
   * 流程部署 2/2 加载 zip包 批量加载
   */
  @Test
  public void testDeployProcessByZip() {
//        获取流程引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取RepositoryService
    RepositoryService repositoryService = processEngine.getRepositoryService();
//        流程部署
//        读取资源包文件，构造成inputStream
    InputStream inputStream = this.getClass()
        .getClassLoader()
        .getResourceAsStream("bpmn/evection.zip");
//        用inputStream 构造ZipInputStream
    ZipInputStream zipInputStream = new ZipInputStream(inputStream);
//        使用压缩包的流进行流程的部署
    Deployment deploy = repositoryService.createDeployment()
        .addZipInputStream(zipInputStream)
        .deploy();
    System.out.println("流程部署id=" + deploy.getId());
    System.out.println("流程部署的名称=" + deploy.getName());
  }


  /**
   * 启动流程实例 `act_hi_actinst`    流程实例执行历史信息 `act_hi_identitylink` 流程参与用户的历史信息 `act_hi_procinst`
   * 流程实例的历史信息 `act_hi_taskinst`     流程任务的历史信息 `act_ru_execution`    流程执行信息 `act_ru_identitylink`
   * 流程的正在参与用户信息 `act_ru_task`         流程当前任务信息
   */
  @Test
  public void testStartProcess() {
//        1、创建ProcessEngine
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2、获取RunTimeService
    RuntimeService runtimeService = processEngine.getRuntimeService();
//        3、根据流程定义的id启动流程
    ProcessInstance instance = runtimeService.startProcessInstanceByKey(key);
//        4、输出内容

    System.out.println("流程定义ID：" + instance.getProcessDefinitionId());
    System.out.println("流程实例ID：" + instance.getId());
    System.out.println("流程实例名称：" + instance.getName());
    System.out.println("当前活动的ID：" + instance.getActivityId());
  }


  /**
   * 查询个人待执行的任务
   */
  @Test
  public void testFindPersonalTaskList() {
//        1、获取流程引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        2、获取taskService
    TaskService taskService = processEngine.getTaskService();
//        3、根据流程key 和 任务的负责人 查询任务
    List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(key)
        .taskAssignee(staff_zhagnsan)
        .list();
//        4、输出
    for (Task task : taskList) {
      System.out.println("流程实例id=" + task.getProcessInstanceId());
      System.out.println("任务Id=" + task.getId());
      System.out.println("任务负责人=" + task.getAssignee());
      System.out.println("任务名称=" + task.getName());
    }
  }

  /**
   * 完成个人任务
   */
  @Test
  public void testCompletTask() {
//        获取引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取操作任务的服务 TaskService
    TaskService taskService = processEngine.getTaskService();
    List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(key)
        .taskAssignee(staff_zhagnsan)
        .list();
    if (CollectionUtils.isEmpty(taskList)) {
      log.info("{} 任务数量为空", staff_zhagnsan);
      return;
    }
    log.info("{} 共计任务数量 {}", staff_zhagnsan, taskList.size());

    log.info("尝试完成第一个任务");
    Task task = taskList.get(0);
    System.out.println("流程实例id=" + task.getProcessInstanceId());
    System.out.println("任务Id=" + task.getId());
    System.out.println("任务负责人=" + task.getAssignee());
    System.out.println("任务名称=" + task.getName());
//        完成jerry的任务 、完成jack的任务、完成rose的任务
    taskService.complete(task.getId());

//   张三之后 完成jerry的任务 、完成jack的任务、完成rose的任务
  }

}
