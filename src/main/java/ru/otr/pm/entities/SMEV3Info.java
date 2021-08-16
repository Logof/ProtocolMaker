package ru.otr.pm.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.parser.ParseException;
import ru.otr.pm.templates.JSONTemplateHelper;

import java.io.File;

public class SMEV3Info {
    @JsonProperty("url")
    private String url;
    @JsonProperty("id")
    private String id;
    @JsonProperty("namespace")
    private String namespace;
    @JsonProperty("VSVersion")
    private String version;
    @JsonProperty("regDate")
    private String regDate;
    @JsonProperty("routingType")
    private String routingType;
    @JsonProperty("rootTag")
    private String rootTag;
    @JsonProperty("xsdUrl")
    private String xsdUrl;

    public String getRegDate() {
        return regDate;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getRootTag() {
        return rootTag;
    }

    public String getRoutingType() {
        return routingType;
    }

    public String getVersion() {
        return version;
    }

    public String getXsdUrl() {
        return xsdUrl;
    }

    public SMEV3Info(File file) {
        JSONTemplateHelper json = new JSONTemplateHelper(file);
        url = json.getValue("url");
        id = json.getValue("SMEV3Id");
        namespace = json.getValue("SMEV3Namespace");
        version = json.getValue("SMEV3Version");
        regDate = json.getValue("SMEV3RegDate");
        routingType = json.getValue("SMEV3RoutingType");
        rootTag = json.getValue("SMEV3RootTag");
        xsdUrl = json.getValue("SMEV3XSDLink");
    }

    public SMEV3Info() {
        url = "#";
        id = "-";
        namespace = "-";
        version = "-";
        regDate = "-";
        routingType = "-";
        rootTag = "-";
        xsdUrl = "#";
    }
}
