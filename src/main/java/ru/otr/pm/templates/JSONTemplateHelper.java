package ru.otr.pm.templates;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;

public class JSONTemplateHelper {
    private final JSONParser jsonParser = new JSONParser();
    private JSONObject jsonObject;

    public JSONTemplateHelper(File file) {
        try
        {
            //Read JSON file
            jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        try {
            return jsonParser.parse(String.valueOf(jsonObject.get(key))).toString();
        } catch (ParseException ex) {
            System.out.println(ex.getLocalizedMessage());
            return null;
        }

    }

    public JSONObject getJSON(String key) throws ParseException {
        return (JSONObject) jsonParser.parse(jsonObject.get(key).toString());
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
