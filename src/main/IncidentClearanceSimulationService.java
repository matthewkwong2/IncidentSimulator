package main;

import javafx.concurrent.Task;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONObject;

import java.net.URI;
import java.net.URL;

public class IncidentClearanceSimulationService extends IncidentSimulationService {
    private final String url = urlPrefix + "/incident";
    private final HttpDelete httpClearIncident = new HttpDelete();

    public IncidentClearanceSimulationService() {

    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                final String incidentID = IncidentSimulation.getInstance().incidentIDs.take();
                httpClearIncident.setURI(new URI(url + "?id=" + incidentID));
                final CloseableHttpResponse response = httpClient.execute(httpClearIncident);
                handleResponse(response);
                return null;
            }
        };
    }

    @Override
    protected void handleResponse(CloseableHttpResponse response) {
        final int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            System.out.println("Cleared");
        } else {
            System.out.println("error: " + status);
        }
    }
}
