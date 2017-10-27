package com.berchina;

import java.util.List;

import org.activiti.engine.FormService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final ObjectMapper om = new ObjectMapper();
    static {
        om.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
    
    public static void main(String[] args) throws JsonProcessingException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("onboarding.bpmn20.xml").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        logger.info(om.writerFor(ProcessDefinition.class).writeValueAsString(processDefinition));
        
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        logger.info(om.writerFor(ProcessInstance.class).writeValueAsString(processInstance));
        
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        logger.info("size: {}\n{}", tasks.size(), om.writerFor(new TypeReference<List<Task>>() {}).writeValueAsString(tasks));
        
        FormService formService = processEngine.getFormService();
        for (Task task : tasks) {
            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            logger.info(om.writerFor(FormData.class).writeValueAsString(taskFormData));
        }
        
//        IdentityService identityService = processEngine.getIdentityService();
//        List<User> users = identityService.createUserQuery().list();
//        logger.info("size: {}\n{}", users.size(), om.writerFor(new TypeReference<List<User>>() {}).writeValueAsString(users));
        
//        HistoryService historyService = processEngine.getHistoryService();
//        ManagementService managementService = processEngine.getManagementService();
//        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
        
        
    }
    
    
}
