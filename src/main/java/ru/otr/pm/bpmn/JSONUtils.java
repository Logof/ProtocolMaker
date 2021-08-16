package ru.otr.pm.bpmn;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONUtils {
    private static final Logger log = LoggerFactory.getLogger(JSONUtils.class);

    private JSONObject jsonObject = null;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void jsonFromFile(File fileName) {
        if (fileName.exists()) {
            log.info("Чтение файла {}", fileName.getPath());
            JSONParser parser = new JSONParser();
            try {
                jsonObject = (JSONObject) parser.parse(new FileReader(fileName));
                log.info("{}", jsonObject.toJSONString());
            } catch (ParseException | IOException ex) {
                jsonObject = null;
                log.warn("Error: {}", ex.getStackTrace());
            }
        } else {
            log.warn("Файл {} не найден", fileName.getPath());
        }
    }

    public JSONObject getJSONObject(String jsonName) {
        return (JSONObject)jsonObject.get(jsonName);
    }

    public String getJSONString(String jsonName) {
        return (String) jsonObject.get(jsonName);
    }

    public String getJSONString(JSONObject jsonObjectIn, String jsonName) {
        return (String) jsonObjectIn.get(jsonName);
    }


    public JSONObject getJSONObject(JSONObject jsonObjectI, String jsonName) {
        return (JSONObject)jsonObjectI.get(jsonName);
    }
}
