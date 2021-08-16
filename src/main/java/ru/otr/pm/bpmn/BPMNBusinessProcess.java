package ru.otr.pm.bpmn;

import org.camunda.bpm.model.bpmn.impl.instance.Outgoing;
import org.camunda.bpm.model.bpmn.instance.*;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otr.pm.properties.ApplicationProperties;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static ru.otr.pm.bpmn.BPMNUtils.getBaseElementName;

public class BPMNBusinessProcess {
    private static final Logger log = LoggerFactory.getLogger(BPMNBusinessProcess.class);

    private String bpmnBusinessProcessDescription;

    private String bpmnBusinessProcessName;
    private String bpmnBusinessProcessId;
    private String bpmnBusinessProcessPurpose;

    private List<BaseElement> baseElements;
    private List<BaseElement> startPoints;

    private List<BPMNBusinessStep> bpmnBusinessSteps;

    private String bpmnSmev3Id;
    private String bpmnSmev3Namespace;
    private String bpmnSmev3Ver;
    private String bpmnSmev3RegDate;
    private String bpmnSmev3RouteType;
    private String bpmnSmev3RootElement;
    private String bpmnSmev3XSD;
    private String bpmnSmev3Url;

    private JSONUtils jsonUtils;
    private JSONObject jsonInfo;

    public List<BPMNBusinessStep> getBpmnBusinessSteps() {
        return bpmnBusinessSteps;
    }

    public String getBpmnBusinessProcessId() {
        return bpmnBusinessProcessId;
    }

    public String getBpmnBusinessProcessName() {
        return bpmnBusinessProcessName;
    }

    public BPMNBusinessProcess(BaseElement baseElement, JSONObject jsonInfo) {
        this.bpmnBusinessProcessName = getBaseElementName(baseElement);
        this.bpmnBusinessProcessId = baseElement.getId();
        //fillDataFromFile(bpmnBusinessProcessId);
        this.bpmnBusinessProcessDescription = BPMNUtils.getDocumentation(baseElement);
        this.baseElements = (List<BaseElement>) baseElement.getChildElementsByType(BaseElement.class);
        this.startPoints = buildStartPoint();

        this.jsonUtils = new JSONUtils();
        this.jsonInfo = jsonInfo;
        buildBusinessSteps();
    }

    public BPMNBusinessProcess(BaseElement baseElement) {
        this.bpmnBusinessProcessName = getBaseElementName(baseElement);
        this.bpmnBusinessProcessId = baseElement.getId();
        //fillDataFromFile(bpmnBusinessProcessId);
        this.bpmnBusinessProcessDescription = BPMNUtils.getDocumentation(baseElement);
        this.baseElements = (List<BaseElement>) baseElement.getChildElementsByType(BaseElement.class);
        this.startPoints = buildStartPoint();

        File file;
        try {
            ApplicationProperties applicationProperties = new ApplicationProperties();
            file = new File(applicationProperties.getPropValues("msTemplateFolder") + bpmnBusinessProcessId+".json");
        } catch (IOException ex) {
            file = null;
        }
        jsonUtils = new JSONUtils();
        jsonUtils.jsonFromFile(file);
        jsonInfo = jsonUtils.getJsonObject();
        if (jsonInfo != null ) {
            log.info(">> {} <<", jsonInfo.toJSONString());
            getSmevData();
            this.bpmnBusinessProcessPurpose = jsonUtils.getJSONString("Purpose");
        }


        buildBusinessSteps();
    }

    private void getSmevData() {
        JSONObject smev3 = jsonUtils.getJSONObject(jsonInfo, "SMEV3");
        this.bpmnSmev3Id = nvl(jsonUtils.getJSONString(smev3, "Id"), "-");
        this.bpmnSmev3Namespace = nvl(jsonUtils.getJSONString(smev3, "Namespace"), "-");
        this.bpmnSmev3Ver = nvl(jsonUtils.getJSONString(smev3, "Ver"), "-");
        this.bpmnSmev3RegDate = nvl(jsonUtils.getJSONString(smev3, "RegDate"), "-");
        this.bpmnSmev3RouteType= nvl(jsonUtils.getJSONString(smev3, "RouteType"), "-");
        this.bpmnSmev3RootElement= nvl(jsonUtils.getJSONString(smev3, "RootElement"), "-");
        this.bpmnSmev3XSD= nvl(jsonUtils.getJSONString(smev3, "XSD"), "-").replace("&", "&amp;");
        this.bpmnSmev3Url=nvl(jsonUtils.getJSONString(smev3, "Url"), "-").replace("&", "&amp;");
    }

    public String getBpmnBusinessProcessPurpose(){
        return bpmnBusinessProcessPurpose;
    }

    private List<BaseElement> buildStartPoint() {
        List<BaseElement> result = new ArrayList<>();
        for (BaseElement baseElement : baseElements) {
            if (baseElement instanceof StartEvent) {
                result.add(baseElement);
            }

            if (baseElement instanceof IntermediateCatchEvent && ((IntermediateCatchEvent)baseElement).getIncoming().size() == 0) {
                result.add(baseElement);
            }
        }
        return result;
    }

    private BaseElement getNextElement(String id) {
        for (BaseElement baseElement : baseElements) {
            if (baseElement.getId().equals(id)) {
                return baseElement;
            }
        }
        return null;
    }

    public void getNextElement(BaseElement baseElement) {
        if (bpmnBusinessSteps == null) {
            bpmnBusinessSteps = new ArrayList<>();
        }
        BPMNBusinessStep bpmnBusinessStep = new BPMNBusinessStep(baseElement);
        BaseElement nextBaseElement = null;

        if (!bpmnBusinessSteps.contains(bpmnBusinessStep)) {
            if (jsonInfo != null) {
                JSONObject jsonObjectStep = jsonUtils.getJSONObject(jsonInfo, bpmnBusinessStep.getStepId());
                if (jsonObjectStep != null) {
                    bpmnBusinessStep.setMicroserviceName(jsonUtils.getJSONString(jsonObjectStep, "microserviceName"));
                    bpmnBusinessStep.setMicroserviceMode(jsonUtils.getJSONString(jsonObjectStep, "microserviceMode"));
                    bpmnBusinessStep.setShortDescription(jsonUtils.getJSONString(jsonObjectStep, "shortDescription"));
                }
            }
            bpmnBusinessSteps.add(bpmnBusinessStep);



            if (baseElement instanceof SubProcess) {
                BPMNBusinessProcess bpmnBusinessProcess = new BPMNBusinessProcess(baseElement, jsonInfo);
                bpmnBusinessSteps.addAll(bpmnBusinessProcess.getBpmnBusinessSteps());
            }

            if (baseElement instanceof SequenceFlow) {
                nextBaseElement = getNextElement(((SequenceFlow) baseElement).getTarget().getId());
                getNextElement(nextBaseElement);
            } else {
                Collection<Outgoing> outgoings = baseElement.getChildElementsByType(Outgoing.class);
                if (outgoings.size() > 0) {
                    for (Outgoing outgoing : outgoings) {
                        nextBaseElement = getNextElement(outgoing.getRawTextContent());

                        if (!(nextBaseElement instanceof SequenceFlow)) {
                            log.info("!{}", nextBaseElement.getId());
                        }
                        getNextElement(nextBaseElement);
                    }
                }
            }
        }
    }

    public void buildBusinessSteps() {
        for(BaseElement startPoint : startPoints) {
            getNextElement(startPoint);
        }
    }

    public String getBpmnBusinessProcessDescription() {
        return nvl(bpmnBusinessProcessDescription, "-");
    }

    public String getBpmnSmev3Id() {
        return nvl(bpmnSmev3Id, "-");
    }

    public String getBpmnSmev3Namespace() {
        return nvl(bpmnSmev3Namespace, "-");
    }

    public String getBpmnSmev3RegDate() {
        return nvl(bpmnSmev3RegDate, "-");
    }

    public String getBpmnSmev3RootElement() {
        return nvl(bpmnSmev3RootElement, "-");
    }

    public String getBpmnSmev3RouteType() {
        return nvl(bpmnSmev3RouteType, "-");
    }

    public String getBpmnSmev3Url() {
        return nvl(bpmnSmev3Url, "-");
    }

    public String getBpmnSmev3Ver() {
        return nvl(bpmnSmev3Ver, "-");
    }

    public String getBpmnSmev3XSD() {
        return nvl(bpmnSmev3XSD, "-");
    }

    private String nvl(String value1, String value2) {
        return value1 == "" ? value2 : value1;
    }
}
