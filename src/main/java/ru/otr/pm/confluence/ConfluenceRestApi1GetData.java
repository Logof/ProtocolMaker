package ru.otr.pm.confluence;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ConfluenceRestApi1GetData {

    //private static final String BASE_URL = "http://localhost:1990/confluence";
    private static final String BASE_URL = "https://confluence.otr.ru/";
    private static final String USERNAME = "kokorev.alexey";
    private static final String PASSWORD = "Logof0721";
    private static final String ENCODING = "utf-8";

    private static String getContentRestUrl(final Long contentId, final String[] expansions) throws UnsupportedEncodingException
    {
        final String expand = URLEncoder.encode(StringUtils.join(expansions, ","), ENCODING);

        return String.format("%s/rest/api/content/%s?expand=%s&os_authType=basic&os_username=%s&os_password=%s", BASE_URL, contentId, expand, URLEncoder.encode(USERNAME, ENCODING), URLEncoder.encode(PASSWORD, ENCODING));
    }

    public static void main(final String[] args) throws Exception
    {
        //This is the Page ID that can be found
        //if you go to the "Page Information" section
        //within Confluence
        final long pageId = 122508626;

        HttpClient client = new DefaultHttpClient();

        // Get current page version
        String pageObj = null;
        HttpEntity pageEntity = null;
        try
        {
            //System.out.println(getContentRestUrl(pageId, new String[] {"body.storage", "version", "ancestors"}));
            HttpGet getPageRequest = new HttpGet(getContentRestUrl(pageId, new String[] {"body.storage", "version", "ancestors"}));
            HttpResponse getPageResponse = client.execute(getPageRequest);
            pageEntity = getPageResponse.getEntity();

            pageObj = IOUtils.toString(pageEntity.getContent());

            System.out.println("Get Page Request returned " + getPageResponse.getStatusLine().toString());
            System.out.println("");
            System.out.println(pageObj);
        }
        finally
        {
            if (pageEntity != null)
            {
                EntityUtils.consume(pageEntity);
            }
        }


    }
}
