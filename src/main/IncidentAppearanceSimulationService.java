package main;

import javafx.concurrent.Task;
import javafx.util.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IncidentAppearanceSimulationService extends IncidentSimulationService {
    private final HttpPost httpReportIncident = new HttpPost(urlPrefix + "/incident");
    private final List<Pair<Double, Double>> incidentPool = new ArrayList<>(); // Pair <long, lat>

    public IncidentAppearanceSimulationService() {
        initIncidentPool();
//        displayIncidentPool();
    }

    private void initIncidentPool() {
        try {
            String incidentsFilePath = "incidents.csv";
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(incidentsFilePath));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                final String[] coordinates = line.split(",");
                incidentPool.add(new Pair<>(
                        Double.parseDouble(coordinates[0].replace("\ufeff", "")),
                        Double.parseDouble(coordinates[1].replace("\ufeff", ""))
                ));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayIncidentPool() {
        for(int i=0; i< incidentPool.size(); i++) {
            final Pair<Double, Double> coordinates = incidentPool.get(i);
            System.out.println(i + ": " + coordinates.getKey() + ", " + coordinates.getValue());
        }
    }

    private int randomImpact() {
        final int maxImpact = 3600;
        return ThreadLocalRandom.current().nextInt(maxImpact + 1);
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                final Pair<Double, Double> coordinates = incidentPool.get(ThreadLocalRandom.current().nextInt(incidentPool.size()));
                final int impact = randomImpact();
                final JSONArray location = new JSONArray();
                final JSONObject params = new JSONObject();
                location.add(coordinates.getKey());
                location.add(coordinates.getValue());
                params.put("location", location);
                params.put("impact", impact);

                httpReportIncident.setEntity(new StringEntity(params.toJSONString(), ContentType.APPLICATION_JSON));
                final CloseableHttpResponse response = httpClient.execute(httpReportIncident);
                handleResponse(response);
                return null;
            }
        };
    }

    @Override
    protected void handleResponse(CloseableHttpResponse response) {
        final int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            try {
                final String body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                JSONObject json = (JSONObject) jsonParser.parse(body);
                final String incidentID = json.get("data").toString();
                IncidentSimulation.getInstance().incidentIDs.put(incidentID);
            } catch (IOException | InterruptedException | ParseException e) {
                e.printStackTrace();
            }
        } else {
           System.out.println("error: " + status);
        }
    }
}
