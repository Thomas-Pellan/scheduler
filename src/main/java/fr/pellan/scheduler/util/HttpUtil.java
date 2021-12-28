package fr.pellan.scheduler.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class HttpUtil {

    public HttpResponse sendHttpPost(String url, String body){

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        StringEntity entity = null;
        try {
            entity = new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            log.error("sendHttpPost : encoding error on json body", e);
        }

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        if(entity != null){
            request.setEntity(entity);
        }

        try {
            return httpClient.execute(request);
        } catch (IOException e) {
            log.error("sendHttpPost : error when calling POST call to external app", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("sendHttpPost : error when closing HttpClient", e);
            }
        }
        return null;
    }
}
