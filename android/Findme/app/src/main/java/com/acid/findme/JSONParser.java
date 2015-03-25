package com.acid.findme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;

import com.acid.findme.exception.JSONParserException;

public class JSONParser {
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public final String JSON_PARSER_TAG = "JSONParser";
 
    // constructor
    public JSONParser() {
 
    }
 
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
            List<NameValuePair> params) throws JSONParserException {
 
        // Making HTTP request
        try {
 
            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
 
            } else if(method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;

                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }           
 
        } catch (UnsupportedEncodingException e) {
            Log.e(JSON_PARSER_TAG, "UnsupportedEncodingException: " + e.toString());
            e.printStackTrace();
            throw new JSONParserException();
        } catch (ClientProtocolException e) {
            Log.e(JSON_PARSER_TAG, "ClientProtocolException: " + e.toString());
            e.printStackTrace();
            throw new JSONParserException();
        } catch (IOException e) {
            Log.e(JSON_PARSER_TAG, "IOException: " + e.toString());
            e.printStackTrace();
            throw new JSONParserException();
        }
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader( is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e(JSON_PARSER_TAG, "Buffer Error converting result " + e.toString());
            e.printStackTrace();
            throw new JSONParserException();
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(JSON_PARSER_TAG, " JSON Parser Error, Error parsing data " + e.toString());
            e.printStackTrace();
            JSONParserException toThrow = new JSONParserException();
            toThrow.setReason("Could not reach Server");
            throw toThrow;
        }
 
        // return JSON String
        return jObj;
    }
}