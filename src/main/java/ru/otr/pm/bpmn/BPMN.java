package ru.otr.pm.bpmn;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BPMN {
    private static BPMN bpmnFile;
    private static Collection<RootElement> rootElements;
    private static List<BPMNBusinessProcess> bpmnBusinessProcesses;
    private static Collection<BaseElement> allBaseElements;

    private static final Logger log = LoggerFactory.getLogger(BPMN.class);

    public BPMN(File bpmnFile) {
        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(bpmnFile);
        Definitions definitions = modelInstance.getDefinitions();
        rootElements = definitions.getRootElements();
        bpmnBusinessProcesses = new ArrayList<>();
    }

    public BPMN(BaseElement baseElement) {
        allBaseElements = baseElement.getChildElementsByType(BaseElement.class);
    }

    public static void readBPMN(File file) {
        if (bpmnFile == null) {
            bpmnFile = new BPMN(file);
        }
        for (RootElement rootElement : rootElements) {
            if (!(rootElement instanceof Process)) {
                rootElements.remove(rootElement);
            }
        }
    }

    public static List<BPMNBusinessProcess> getBusinessProcesses() throws IOException {
        for (RootElement element : rootElements) {
            BPMNBusinessProcess bpmnBusinessProcess = new BPMNBusinessProcess(element);
            log.info("Adding BP {}({})", element.getAttributeValue("name"), bpmnBusinessProcess.getBpmnBusinessProcessId());

            bpmnBusinessProcesses.add(bpmnBusinessProcess);
        }
        return bpmnBusinessProcesses;
    }
}
