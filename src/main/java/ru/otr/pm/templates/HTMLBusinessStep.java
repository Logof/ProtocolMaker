package ru.otr.pm.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otr.pm.bpmn.BPMNBusinessStep;

import java.util.Map;

public class HTMLBusinessStep {
    private static final Logger log = LoggerFactory.getLogger(HTMLBusinessStep.class);
    HTMLElements htmlElements = new HTMLElements();

    public HTMLBusinessStep(BPMNBusinessStep bpmnBusinessStep) {
        StringBuilder htmlBusinessStepElements = new StringBuilder();
        htmlElements.generateH2(bpmnBusinessStep.getStepName());
        htmlElements.generateH3("Цель шага:");
        htmlElements.generateP(bpmnBusinessStep.getPurposeStep());
        htmlElements.generateH3("Краткое описание шага:");
        htmlElements.generateP(bpmnBusinessStep.getShortDescription());
        htmlElements.generateH3("Микросервис:");
        htmlElements.generateConfluenceLink(bpmnBusinessStep.getMicroserviceName());
        htmlElements.generateH3("Режим работы микросервиса:");
        htmlElements.generateP(bpmnBusinessStep.getMicroserviceMode());
        htmlElements.generateH3("Настройки Camunda:");
        /************************************************************************************************************/
        htmlElements.generateH4("Extensions:");
        htmlElements.generateOpenTable2CollHeader();
        htmlElements.generateTable2CollHeader();
        Map<String, String> extensions = bpmnBusinessStep.getExtensions();
        for (Map.Entry<String, String> rowExtensiom : extensions.entrySet()) {
            htmlElements.generateTable2CollLine(new String[] {rowExtensiom.getKey(), rowExtensiom.getValue()} );
        }
        htmlElements.generateCloseTable();
        /************************************************************************************************************/
        htmlElements.generateH4("Listeners:");
        htmlElements.generateOpenTable2CollHeader();
        htmlElements.generateTable2CollHeader();
        htmlElements.generateTable2CollLine(new String[] {"-", "-"} );
        htmlElements.generateCloseTable();
        /************************************************************************************************************/
        htmlElements.generateH4("Input/Output:");
        htmlElements.generateOpenTable5CollHeader();
        htmlElements.generateTable5CollHeader();
        Map<String, String> inputs = bpmnBusinessStep.getInputParametr();
        for (Map.Entry<String, String> rowExtensiom : inputs.entrySet()) {
            htmlElements.generateTable5CollLine(new String[] {"In", rowExtensiom.getKey(), rowExtensiom.getValue()} );
        }
        Map<String, String> outputs = bpmnBusinessStep.getOutputParametr();
        for (Map.Entry<String, String> rowExtensiom : outputs.entrySet()) {
            htmlElements.generateTable5CollLine(new String[] {"Out", rowExtensiom.getKey(), rowExtensiom.getValue()} );
        }
        htmlElements.generateCloseTable();

        /************************************************************************************************************/
        htmlElements.generateH3("Данные, полученные на входе микросервиса");
        htmlElements.generateConfluenceSpollerOpen("Пример IS-сообщения (header+body)");
        htmlElements.generateConfluenceCode("Пример IS-сообщения (header+body)", "<![CDATA["+bpmnBusinessStep.getDataIn()+"]]>");
        htmlElements.generateConfluenceSpollerClose();
        /************************************************************************************************************/
        htmlElements.generateH3("Данные, полученные на выходе микросервиса");
        htmlElements.generateConfluenceSpollerOpen("Пример IS-сообщения (header+body)");
        htmlElements.generateConfluenceCode("Пример IS-сообщения (header+body)", "<![CDATA["+bpmnBusinessStep.getDataOut()+"]]>");
        htmlElements.generateConfluenceSpollerClose();
    }

    public String getGeneratedHTML() {
        return htmlElements.getHTMLElements();
    }

}
