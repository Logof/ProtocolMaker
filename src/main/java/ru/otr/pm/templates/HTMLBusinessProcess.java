package ru.otr.pm.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otr.pm.bpmn.BPMNBusinessProcess;
import ru.otr.pm.bpmn.BPMNBusinessStep;

public class HTMLBusinessProcess {
    private static final Logger log = LoggerFactory.getLogger(HTMLBusinessProcess.class);
    HTMLElements htmlElements = new HTMLElements();

    public HTMLBusinessProcess(BPMNBusinessProcess bpmnBusinessProcess){
        htmlElements.generateOpenP("class=\"auto-cursor-target\"");
        htmlElements.generateConfluenceContentList();
        htmlElements.generateH1("Наименование протокола обмена");
        htmlElements.generateOpenTable2CollHeader();
        htmlElements.generateTable2CollHeader();
        htmlElements.generateTable2CollLine(new String[] {bpmnBusinessProcess.getBpmnBusinessProcessId(), bpmnBusinessProcess.getBpmnBusinessProcessName()});
        htmlElements.generateCloseTable();

        htmlElements.generateH1("Общая информация о протоколе обмена");
        htmlElements.generateOpenLi();
        htmlElements.generateLi("Значение идентификатора ВС - "+bpmnBusinessProcess.getBpmnSmev3Id());
        htmlElements.generateLi("Namespace URI - "+bpmnBusinessProcess.getBpmnSmev3Namespace());
        htmlElements.generateLi("Версия - "+bpmnBusinessProcess.getBpmnSmev3Ver());
        htmlElements.generateLi("Дата регистрации - "+bpmnBusinessProcess.getBpmnSmev3RegDate());
        htmlElements.generateLi("Тип маршрутизации  - "+bpmnBusinessProcess.getBpmnSmev3RouteType());
        htmlElements.generateLi("RootElement - "+bpmnBusinessProcess.getBpmnSmev3RootElement());
        String smev3XSDUrl = htmlElements.generateHref(bpmnBusinessProcess.getBpmnSmev3XSD(), bpmnBusinessProcess.getBpmnSmev3XSD());
        htmlElements.generateLi("xsd - "+ smev3XSDUrl);
        String smev3Url = htmlElements.generateHref(bpmnBusinessProcess.getBpmnSmev3Url(), bpmnBusinessProcess.getBpmnSmev3Url());
        htmlElements.generateLi("Ссылка на технологический портал СМЭВ - "+ smev3Url);
        htmlElements.generateCloseLi();

        htmlElements.generateH1("Назначение протокола обмена");
        htmlElements.generateP(bpmnBusinessProcess.getBpmnBusinessProcessPurpose());

        htmlElements.generateH1("БП, в котором реализуется протокол");
        htmlElements.generateP("Заполняется СА самостоятельно");

        htmlElements.generateH1("Общая логика работы протокола обмена");
        htmlElements.generateP(bpmnBusinessProcess.getBpmnBusinessProcessDescription());

        htmlElements.generateH1("Описание шагов процесса");
        log.info("Кол-во шагов {}", bpmnBusinessProcess.getBpmnBusinessSteps().size());

        for (BPMNBusinessStep bpmnBusinessStep : bpmnBusinessProcess.getBpmnBusinessSteps()) {
            if (bpmnBusinessStep.isPrintable()) {
                log.info("{}", bpmnBusinessStep.getStepName());
                htmlElements.append(new HTMLBusinessStep(bpmnBusinessStep).getGeneratedHTML());
            }
        }
        htmlElements.generateCloseP();
    }

    @Override
    public String toString(){
        return htmlElements.getHTMLElements();
    }
}
