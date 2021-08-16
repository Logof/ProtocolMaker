package ru.otr.pm.templates;

public class HTMLElements {
    StringBuilder htmlBulder;


    public HTMLElements() {
        htmlBulder = new StringBuilder();
    }

    public String getHTMLElements() {
        return htmlBulder.toString();
    }

    public void generateH1(String string){
        htmlBulder.append(String.format("<h1><strong>%s</strong></h1>", string));
    }

    public void generateH2(String string){
        htmlBulder.append(String.format("<h2><strong>%s</strong></h2>", string));
    }

    public void generateH3(String string){
        htmlBulder.append(String.format("<h3><strong>%s</strong></h3>", string));
    }

    public void generateH4(String string){
        htmlBulder.append(String.format("<h4><strong>%s</strong></h4>", string));
    }

    public void generateP(String string){
        htmlBulder.append(String.format("<p>%s</p>", string).replace("\n", "<br/>"));
    }

    public void generateOpenP(String string){
        htmlBulder.append(String.format("<p %s>", string));
    }

    public void generateCloseP(){
        htmlBulder.append(String.format("</p>"));
    }

    public void generateP(String string, String attr){
        htmlBulder.append(String.format("<p %s>%s</p>", attr, string));
    }

    public void generateConfluenceLink(String string){
        htmlBulder.append(String.format("<ac:link><ri:page ri:content-title=\"%s\"/></ac:link>", string));
    }

    public void generateOpenTable2CollHeader(){
        htmlBulder.append(String.format("<table class=\"wrapped\"><colgroup><col/><col/></colgroup>"));
    }

    public void generateOpenTable5CollHeader(){
        htmlBulder.append(String.format("<table class=\"wrapped\"><colgroup><col/><col/><col/><col/><col/><col/></colgroup>"));
    }

    public void generateTable2CollHeader(){
        htmlBulder.append(String.format("<tbody><tr><th><p>Ключ</p></th><th><p>Значение</p></th></tr>"));
    }

    public void generateTable2CollLine(String[] collValues){
        htmlBulder.append(String.format("<tr><td>%s</td><td>%s</td></tr>", collValues[0], collValues[1]));
    }

    public void generateTable5CollLine(String[] collValues){
        htmlBulder.append(String.format("<tr><td>%s</td><td>%s</td><td>-</td><td>%s</td><td>-</td><td>-</td></tr>", collValues[0], collValues[1], collValues[2]));
    }

    public void generateTable5CollHeader(){
        htmlBulder.append(String.format("<tbody><tr><th>In/Out</th><th>Переменная локального контекста</th><th>Тип</th><th>Переменная глобального контекста</th><th>Значение для схемы</th><th>Комментарий</th></tr>"));
    }

    public void generateCloseTable(){
        htmlBulder.append(String.format("</tbody></table>"));
    }

    public void generateConfluenceContentList(){
        htmlBulder.append("<ac:structured-macro ac:name=\"expand\" ac:schema-version=\"1\" ac:macro-id=\"8056db28-56c3-46a4-941c-37349eae71da\">");
        htmlBulder.append("<ac:parameter ac:name=\"title\">Оглавление</ac:parameter>");
        htmlBulder.append("<ac:rich-text-body>");
        htmlBulder.append("<p>");
        htmlBulder.append("<ac:structured-macro ac:name=\"toc\" ac:schema-version=\"1\" ac:macro-id=\"ed0a4a75-6d81-4b41-842d-baa6e07fe94a\">");
        htmlBulder.append("<ac:parameter ac:name=\"maxLevel\">3</ac:parameter>");
        htmlBulder.append("</ac:structured-macro>");
        htmlBulder.append("</p>");
        htmlBulder.append("</ac:rich-text-body>");
        htmlBulder.append("</ac:structured-macro>");
    }

    public void append(String string) {
        htmlBulder.append(string);
    }


    public void generateOpenLi(){
        htmlBulder.append("<ol><li style=\"list-style-type: none;\"><ul>");
    }

    public void generateHrefElement(String url, String urlName) {
        htmlBulder.append("<a href=\"").append(url.replace("&", "&amp;")).append("\">").append(urlName.replace("&", "&amp;")).append("</a>");
    }

    public String generateHref(String url, String urlName) {
        StringBuilder href = new StringBuilder();
        return href.append("<a href=\"").append(url).append("\">").append(urlName).append("</a>").toString();
    }

    public void generateLi(String value){
        htmlBulder.append("<li>").append(value).append("</li>");
    }

    public void generateCloseLi(){
        htmlBulder.append("</ul></li></ol>");
    }

    public void generateConfluenceSpollerOpen(String elementName){
        htmlBulder.append("<ac:structured-macro ac:name=\"expand\" ac:schema-version=\"1\" ac:macro-id=\"e6d7b59e-31ef-4bbb-b9c9-15500f5deec1\">")
                .append("<ac:parameter ac:name=\"title\">").append(elementName).append("</ac:parameter>")
                .append("<ac:rich-text-body>");
    }

    public void generateConfluenceSpollerClose() {
        htmlBulder.append("</ac:rich-text-body></ac:structured-macro>");
    }

    public void generateConfluenceCode(String title, String content) {
        htmlBulder.append("<ac:structured-macro ac:name=\"code\" ac:schema-version=\"1\" ac:macro-id=\"b2a0cfba-4cc4-489c-a7e4-8f034b9ff872\">")
                .append("<ac:parameter ac:name=\"title\">").append(title).append("</ac:parameter>")
                .append("<ac:plain-text-body>").append(content).append("</ac:plain-text-body>")
                .append("</ac:structured-macro>");


    }
}
