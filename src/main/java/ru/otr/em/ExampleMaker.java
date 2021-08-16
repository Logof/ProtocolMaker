package ru.otr.em;

import org.json.simple.JSONObject;
import sk.antons.jmom.Jmom;
import sk.antons.json.JsonValue;
import sk.antons.json.parse.JsonParser;

public class ExampleMaker {
    public static void main(String[] args) {
        System.out.println("Work");

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("firstName", "John");
        jsonObject.put("lastName", "Smith");
        jsonObject.put("age", 25);

        System.out.println(jsonObject.toJSONString());

        JsonValue json = JsonParser.parse(jsonObject.toJSONString());

        Jmom jmom = Jmom.instance()
                .copy("/firstName", "/NewObject", true)
                .remove("/age")
                //.copy("/NewObject/Name", "/NewObject/Title", true)
        ;
        jmom.apply(json);
        System.out.println(json.toCompactString());


        /************** Input ******************/
        String[] inputs = {"${internal_attachments}", "${payload}", "${in_container}"};
        for (String input : inputs) {

        }

        /************** Output ******************/
        String[] outputs = {"${id_reestr_record}", "${in_container}", "${multi_delivery}"};
    }
}
