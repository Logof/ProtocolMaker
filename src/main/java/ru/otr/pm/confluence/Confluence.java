package ru.otr.pm.confluence;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otr.pm.properties.ApplicationProperties;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Confluence {
    private static final Logger log = LoggerFactory.getLogger(Confluence.class);

    private final String USER_NAME;
    private final String USER_PASS;
    private final String BASE_URL;
    private final String ENCODING = "utf-8";

    private Confluence confluence;

    public Confluence() throws IOException {
        ApplicationProperties properties =  new ApplicationProperties();
        USER_NAME = properties.getPropValues("login");
        USER_PASS = properties.getPropValues("password");
        BASE_URL = properties.getPropValues("confluenceUrl");
    }

    public void login() {
        try {
            if (confluence == null) {
                confluence = new Confluence();
            }
            System.out.println(USER_NAME + "=" + USER_PASS);
        } catch (IOException ex) {

        }
    }

    private String createContentRestUrl() throws UnsupportedEncodingException
    {
        return String.format("%s/rest/api/content/?&os_authType=basic&os_username=%s&os_password=%s", BASE_URL, URLEncoder.encode(USER_NAME, ENCODING), URLEncoder.encode(USER_PASS, ENCODING));
    }

    public void publishPage(String pageTitle, String htmlContent) throws Exception
    {
        log.info("Title: {}, \n Content: {}", pageTitle, htmlContent);
        String pageContent = "<h1>Things That Are Awesome</h1><ul><li>Birds</li><li>Mammals</li><li>Decapods</li></ul>";
        String pageSpace = "AISFSSP";
        String labelToAdd = "ProtocolMaker";
        int parentPageId = 147344235;

        JSONObject newPage = defineConfluencePage(pageTitle, htmlContent, pageSpace, labelToAdd, parentPageId);

        createConfluencePageViaPost(newPage);

    }

    public void createConfluencePageViaPost(JSONObject newPage) throws Exception
    {
        HttpClient client = new DefaultHttpClient();

        // Send update request
        HttpEntity pageEntity = null;

        try
        {
            //2016-12-18 - StirlingCrow: Left off here.  Was finally able to get the post command to work
            //I can begin testing adding more data to the value stuff (see above)
            HttpPost postPageRequest = new HttpPost(createContentRestUrl());

            StringEntity entity = new StringEntity(newPage.toString(), ContentType.APPLICATION_JSON);
            postPageRequest.setEntity(entity);
            //pageEntity = postPageRequest.getEntity();
            log.info("{}", IOUtils.toString(postPageRequest.getEntity().getContent()));

            HttpResponse postPageResponse = client.execute(postPageRequest);
            pageEntity = postPageResponse.getEntity();

            log.info("Push Page Request returned {}", postPageResponse.getStatusLine().toString());
            log.info("{}", IOUtils.toString(pageEntity.getContent()));
        }
        finally
        {
            EntityUtils.consume(pageEntity);
        }
    }

    public static JSONObject defineConfluencePage(String pageTitle,
                                                  String wikiEntryText,
                                                  String pageSpace,
                                                  String label,
                                                  int parentPageId) throws JSONException
    {
        //This would be the command in Python (similar to the example
        //in the Confluence example:
        //
        //curl -u <username>:<password> -X POST -H 'Content-Type: application/json' -d'{
        // "type":"page",
        // "title":"My Awesome Page",
        // "ancestors":[{"id":9994246}],
        // "space":{"key":"JOUR"},
        // "body":
        //        {"storage":
        //                   {"value":"<h1>Things That Are Awesome</h1><ul><li>Birds</li><li>Mammals</li><li>Decapods</li></ul>",
        //                    "representation":"storage"}
        //        },
        // "metadata":
        //             {"labels":[
        //                        {"prefix":"global",
        //                        "name":"journal"},
        //                        {"prefix":"global",
        //                        "name":"awesome_stuff"}
        //                       ]
        //             }
        // }'
        // http://localhost:8080/confluence/rest/api/content/ | python -mjson.tool

        JSONObject newPage = new JSONObject();

        // "type":"page",
        // "title":"My Awesome Page"
        newPage.put("type","page");
        newPage.put("title", pageTitle);

        // "ancestors":[{"id":9994246}],
        JSONObject parentPage = new JSONObject();
        parentPage.put("id", parentPageId);

        JSONArray parentPageArray = new JSONArray();
        parentPageArray.put(parentPage);

        newPage.put("ancestors", parentPageArray);

        // "space":{"key":"JOUR"},
        JSONObject spaceOb = new JSONObject();
        spaceOb.put("key", pageSpace);
        newPage.put("space", spaceOb);

        // "body":
        //        {"storage":
        //                   {"value":"<p><h1>Things That Are Awesome</h1><ul><li>Birds</li><li>Mammals</li><li>Decapods</li></ul></p>",
        //                    "representation":"storage"}
        //        },
        JSONObject jsonObjects = new JSONObject();

        jsonObjects.put("value", wikiEntryText);
        jsonObjects.put("representation","storage");

        JSONObject storageObject = new JSONObject();
        storageObject.put("storage", jsonObjects);

        newPage.put("body", storageObject);


        //LABELS
        // "metadata":
        //             {"labels":[
        //                        {"prefix":"global",
        //                        "name":"journal"},
        //                        {"prefix":"global",
        //                        "name":"awesome_stuff"}
        //                       ]
        //             }
        JSONObject prefixJsonObject1 = new JSONObject();
        prefixJsonObject1.put("prefix","global");
        prefixJsonObject1.put("name","journal");
        JSONObject prefixJsonObject2 = new JSONObject();
        prefixJsonObject2.put("prefix","global");
        prefixJsonObject2.put("name",label);

        JSONArray prefixArray = new JSONArray();
        prefixArray.put(prefixJsonObject1);
        prefixArray.put(prefixJsonObject2);

        JSONObject labelsObject = new JSONObject();
        labelsObject.put("labels", prefixArray);

        newPage.put("metadata",labelsObject);

        return newPage;
    }
}
