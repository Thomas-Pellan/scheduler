package fr.pellan.scheduler.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
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

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            HttpPost request = new HttpPost(url);

            StringEntity entity = buildRequestBody(body);

            if(entity == null){
               log.error("Could not build body for request, post  will not be sent");
               return null;
            }

            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            request.setEntity(entity);

            return httpClient.execute(request);
        } catch (IOException e) {
            log.error("sendHttpPost : error when closing HttpClient", e);
            return null;
        }
    }

    private StringEntity buildRequestBody(String body){
        try {
            return new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            log.error("buildRequestBody : encoding error on json body", e);
        }
        return null;
    }
}
