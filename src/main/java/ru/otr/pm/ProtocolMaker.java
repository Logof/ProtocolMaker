package ru.otr.pm;

import freemarker.template.utility.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otr.pm.bpmn.BPMN;
import ru.otr.pm.bpmn.BPMNBusinessProcess;
import ru.otr.pm.bpmn.BPMNBusinessStep;
import ru.otr.pm.confluence.Confluence;
import ru.otr.pm.templates.HTMLBusinessProcess;
import ru.otr.pm.templates.HTMLBusinessStep;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ProtocolMaker {
    private static final String FILE_PATH = "/home/user/IdeaProjects/ProtocolMaker/src/main/resources/BP.bpmn";
    private static final Logger log = LoggerFactory.getLogger(ProtocolMaker.class);

    //private static List<BusinessStep> businessSteps = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        String filePath;
        try {
            filePath = args[0];
            if (filePath == null || FilenameUtils.getExtension(filePath).toUpperCase() != "BPMN")
                filePath = FILE_PATH;

        } catch (Exception e) {
            filePath = FILE_PATH;
        }
        log.info("Чтение файла BPMN {}", filePath);

        BPMN.readBPMN(new File(FILE_PATH));
        List<BPMNBusinessProcess> bpmnBusinessProcesses = BPMN.getBusinessProcesses();

        for (BPMNBusinessProcess businessProcess : bpmnBusinessProcesses) {
            //List<BPMNBusinessStep> bpmnBusinessSteps = businessProcess.getBpmnBusinessSteps();
            HTMLBusinessProcess htmlBusinessProcess = new HTMLBusinessProcess(businessProcess);


            Confluence confluence = new Confluence();
            //log.info("htmlBusinessProcess: {}", htmlBusinessProcess.toString());
            confluence.publishPage(businessProcess.getBpmnBusinessProcessName()+"(тест4)", htmlBusinessProcess.toString());
        }
    }
}
