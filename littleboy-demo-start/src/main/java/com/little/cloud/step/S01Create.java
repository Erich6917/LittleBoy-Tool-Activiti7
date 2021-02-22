package com.little.cloud.step;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;

/**
 * @Comment
 * @Author LiYuan
 * @Date 2021-2-22
 */
public class S01Create {

  /**
   * 使用activiti提供的默认方式来创建mysql的表
   * <p>
   * 流程引擎创建方式 1 默认方式 2 自定义
   * <p>
   * 配置文件见  activiti.cfg.xml
   */


  /**
   * 默认读取  resources/activiti.cfg.xml 配置文件创建
   */
  @Test
  public void testCreateDefault() {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    RepositoryService repositoryService = processEngine.getRepositoryService();
    repositoryService.createDeployment();
  }


  /**
   * 动态创建，可以指定配置文件名称，以及，配置文件中的configuration名称
   */
  @Test
  public void testCreateDynamic() {

    /**
     * 动态获取config
     */
    ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
        .createProcessEngineConfigurationFromResource(
            "activiti.cfg.xml",
            "processEngineConfiguration"
        );

    processEngineConfiguration.buildProcessEngine();
  }
}
