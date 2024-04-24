package com.kim.flowable.util;

import org.apache.commons.io.FileUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JinXin
 * @date 2024/4/12 16:19
 * @description
 */
@Component
public class ProcessSupport {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    public void generateProcessDiagram(String processDefinitionKey) {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey).singleResult();
        if (hpi == null) {
            return;
        }
        List<String> highLightedActivities = new ArrayList<>();
        List<String> hightLightedFlows = new ArrayList<>();
        double scaleFactor = 1.0;
        boolean drawSqquenceFlowNameWithNoLabelDI = true;
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(hpi.getId()).list();
        for (HistoricActivityInstance hai : list) {
            if (hai.getActivityType().equals("sequenceFlow")) {
                hightLightedFlows.add(hai.getActivityId());
            } else {
                highLightedActivities.add(hai.getActivityId());
            }
        }
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        InputStream inputStream = generator.generateDiagram(bpmnModel, "PNG", highLightedActivities, hightLightedFlows, scaleFactor, drawSqquenceFlowNameWithNoLabelDI);
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(processDefinitionKey+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
