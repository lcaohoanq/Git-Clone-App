package com.lcaohoanq.service;

import com.lcaohoanq.constant.HttpStatusCode;
import com.lcaohoanq.error.ErrorHandler;
import com.lcaohoanq.util.GithubUtils;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GithubService {

    private final String GITHUB_TOKEN = Dotenv.configure().load().get("GITHUB_TOKEN");

    public boolean isPublicRepository(String repoUrl) throws ErrorHandler, IOException {
        String apiUrl = GithubUtils.convertToApiUrl(repoUrl);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrl);
        request.addHeader("Authorization", "token " + GITHUB_TOKEN);
        // Optional: add your GitHub token here if you want to access private repositories

        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatusCode.OK.getCode()) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(jsonResponse);
            System.out.println(jsonObject);
            return !jsonObject.getBoolean("private");
        } else if (statusCode == HttpStatusCode.NOT_FOUND.getCode()) {
            throw new ErrorHandler(
                HttpStatusCode.NOT_FOUND.name(),
                HttpStatusCode.NOT_FOUND.getCode()
            );
        } else {
            throw new ErrorHandler(
                HttpStatusCode.INTERNAL_SERVER_ERROR.name(),
                HttpStatusCode.INTERNAL_SERVER_ERROR.getCode()
            );
        }
    }



    public static void main(String[] args) {
        System.out.println();
    }
}
