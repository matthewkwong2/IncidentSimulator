package main;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public abstract class IncidentSimulationService<Void> extends ScheduledService<Void> {
    final String urlPrefix = "http://fyp18012s1.cs.hku.hk";
    final CloseableHttpClient httpClient = HttpClients.createDefault();
    final JSONParser jsonParser = new JSONParser();
    @Override
    protected abstract Task<Void> createTask();
    protected abstract void handleResponse(CloseableHttpResponse response);
}
