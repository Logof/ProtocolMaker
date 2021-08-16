package ru.otr.pm.bpmn;

import org.camunda.bpm.model.bpmn.instance.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otr.pm.properties.ApplicationProperties;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class BPMNBusinessStep {
    private static final Logger log = LoggerFactory.getLogger(BPMNBusinessStep.class);

    private String stepId;

    private String stepName;

    private String purposeStep;
    private String shortDescription;

    private String microserviceCode;
    private String microserviceName;
    private String microserviceMode;

    private Map<String, String> extensions = new HashMap<>();
    private Map<String, String> inputParametr = new HashMap<>();
    private Map<String, String> outputParametr = new HashMap<>();

    private String dataIn;
    private String dataOut;

    private boolean printable;
    private static List<String> outgoings;
    private JSONObject jsonStepInfo;

    public BPMNBusinessStep(BaseElement step) {

        this.stepId = step.getId();
        this.purposeStep = BPMNUtils.getDocumentation(step);
        this.stepName = BPMNUtils.getBaseElementName(step);
        this.extensions = BPMNUtils.getBaseElemenExtension(step);
        this.inputParametr = BPMNUtils.getBaseElemenInputParametr(step);
        this.outputParametr = BPMNUtils.getBaseElemenOutputParametr(step);
        this.outgoings = BPMNUtils.getNextElement(step);
        this.printable = !(step instanceof SequenceFlow || step instanceof StartEvent || step instanceof EndEvent || step instanceof SubProcess);


        String folder = "";
        try {
            ApplicationProperties applicationProperties = new ApplicationProperties();
            folder =  applicationProperties.getPropValues("msTemplateFolder");
        } catch (IOException ex) {
            folder = null;
        }

        this.dataIn = dataFromFile(new File(folder+this.stepId+"_in.txt"));
        this.dataOut = dataFromFile(new File(folder+this.stepId+"_out.txt"));
    }

    public String dataFromFile(File fileName) {
        if (fileName.exists()) {
            log.info("Чтение файла {}", fileName.getPath());
            try {
                return Files.readString(fileName.toPath());
            } catch (IOException ex) {
                log.warn("Error: {}", ex.getStackTrace());
                return null;
            }
        }
        log.warn("Файл {} не найден", fileName.getPath());
        return null;
    }

    public Map<String, String> getExtensions() {
        return extensions;
    }

    public Map<String, String> getInputParametr() {
        return inputParametr;
    }

    public Map<String, String> getOutputParametr() {
        return outputParametr;
    }

    public String getMicroserviceMode() {
        return microserviceMode;
    }

    public String getMicroserviceName() {
        return microserviceName;
    }

    public String getPurposeStep() {
        return purposeStep;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getStepName() {
        return stepName;
    }

    public boolean isPrintable() {
        return printable;
    }

    public String getStepId(){
        return stepId;
    }

    public static List<String> getOutgoings(){
        return outgoings;
    }

    public String getDataIn() {
        return dataIn;
    }

    public String getDataOut(){
        return dataOut;
    }

    public void setMicroserviceName(String microserviceName) {
        this.microserviceName = microserviceName;
    }

    public void setMicroserviceMode(String microserviceMode) {
        this.microserviceMode = microserviceMode;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
