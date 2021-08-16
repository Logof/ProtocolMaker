package ru.otr.pm.bpmn;

import org.camunda.bpm.model.bpmn.impl.instance.Outgoing;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.camunda.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.lang.Process;
import java.util.*;

public final class BPMNUtils {

    private static BPMNUtils bpmnUtils;

    private BPMNUtils() {

    }

    private static void init() {
        if (bpmnUtils == null) {
            new BPMNUtils();
        }
    }

    public static List<String> getNextElement(BaseElement baseElement) {
        init();

        List<String> result = new ArrayList<>();
        for (Outgoing outgoing : baseElement.getChildElementsByType(Outgoing.class)){
            result.add(outgoing.getRawTextContent());
        }
        return result;
    }

    public static String getBaseElementName(BaseElement baseElement) {
        init();
        if (baseElement instanceof CallActivity){
            return ((CallActivity) baseElement).getName();
        }
        if (baseElement instanceof Task) {
            return ((Task) baseElement).getName();
        }
        if (baseElement instanceof IntermediateThrowEvent) {
            return ((IntermediateThrowEvent) baseElement).getName();
        }
        if (baseElement instanceof IntermediateCatchEvent) {
            return ((IntermediateCatchEvent) baseElement).getName();
        }
        if (baseElement instanceof SubProcess) {
            return "Подпроцесс " +((SubProcess) baseElement).getName();
        }
        if (baseElement instanceof EndEvent) {
            return null;
        }
        if (baseElement instanceof StartEvent) {
            return null;
        }
        if (baseElement instanceof RootElement) {
            return ((RootElement) baseElement).getAttributeValue("name");
        }
        return null;
    }

    public static Map<String, String> getBaseElemenExtension(BaseElement baseElement) {
        init();

        Map<String, String> result = new TreeMap<>();

        ExtensionElements extensionElements = baseElement.getExtensionElements();
        if (extensionElements != null) {
            Collection<ModelElementInstance> elements = extensionElements.getElements();
            if (elements.size() > 0) {
                for (ModelElementInstance element : elements) {
                    if (element instanceof CamundaProperties) {
                        for (CamundaProperty property : element.getChildElementsByType(CamundaProperty.class)) {
                            result.put(property.getCamundaName(), property.getCamundaValue());
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Map<String, String> getBaseElemenInputParametr(BaseElement baseElement) {
        init();

        Map<String, String> result = new TreeMap<>();

        ExtensionElements extensionElements = baseElement.getExtensionElements();
        if (extensionElements != null) {
            Collection<ModelElementInstance> elements = extensionElements.getElements();

            if (elements.size() > 0) {
                for (ModelElementInstance element : elements) {
                    if (element instanceof CamundaInputOutput) {
                        for (CamundaInputParameter parameter : element.getChildElementsByType(CamundaInputParameter.class)) {
                            result.put(parameter.getCamundaName(), parameter.getRawTextContent());
                        }
                    }
                }
            }
        }
        return result;
    }

    public static Map<String, String> getBaseElemenOutputParametr(BaseElement baseElement) {
        init();
        Map<String, String> result = new TreeMap<>();
        ExtensionElements extensionElements = baseElement.getExtensionElements();
        if (extensionElements != null) {
            Collection<ModelElementInstance> elements = extensionElements.getElements();
            if (elements.size() > 0) {
                for (ModelElementInstance element : elements) {
                    if (element instanceof CamundaInputOutput) {
                        for (CamundaOutputParameter parameter : element.getChildElementsByType(CamundaOutputParameter.class)) {
                            result.put(parameter.getCamundaName(), parameter.getRawTextContent());
                        }
                    }
                }
            }
        }
        return result;
    }

    public static String getDocumentation(RootElement baseElement) {
        init();
        Collection<Documentation> documentations = baseElement.getDocumentations();

        if (documentations.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Documentation doc : documentations) {
            String content = doc.getTextContent();
            if (content == null || content.isEmpty()) {
                continue;
            }
            if (builder.length() != 0) {
                builder.append("\n");
            }
            builder.append(content.trim());
        }
        return builder.toString();
    }

    public static String getDocumentation(BaseElement baseElement) {
        init();

        if (baseElement instanceof RootElement) {
            return getDocumentation((RootElement) baseElement);
        }

        Collection<Documentation> documentations = baseElement.getDocumentations();

        if (documentations.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Documentation doc : documentations) {
            String content = doc.getTextContent();
            if (content == null || content.isEmpty()) {
                continue;
            }
            if (builder.length() != 0) {
                builder.append("\n");
            }
            builder.append(content.trim());
        }
        return builder.toString();
    }
}
