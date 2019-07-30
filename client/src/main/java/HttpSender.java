import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class HttpSender {
    private static final long VALUE_TO_ADD = 100L;
    private final RequestSender.Properties properties;

    private final ExecutorService readExecutor;
    private final ExecutorService writeExecutor;

    private CloseableHttpClient client = HttpClientBuilder
            .create()
            .setConnectionManager(new PoolingHttpClientConnectionManager())
            .build();

    public HttpSender(RequestSender.Properties properties) {
        this.properties = properties;

        readExecutor = Executors.newFixedThreadPool(properties.getRCount());
        writeExecutor = Executors.newFixedThreadPool(properties.getWCount());
    }

    public void start() throws Exception {

        submitTasks(readExecutor, id -> {
            try {
                URIBuilder b = new URIBuilder(properties.getUrlRead());
                b.addParameter("id", "" + id);
                URI uri = b.build();
                return new HttpGet(uri);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        submitTasks(writeExecutor, id -> {
            try {
                HttpPost request = new HttpPost(properties.getUrlWrite());
                request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("id", "" + id));
                urlParameters.add(new BasicNameValuePair("addValue", "" + VALUE_TO_ADD));
                request.setEntity(new UrlEncodedFormEntity(urlParameters));
                return request;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("All requests enqueued");
        readExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        writeExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }


    private void submitTasks(ExecutorService executorService, Function<Integer, HttpUriRequest> requestById) {
        for (int r = 0; r < properties.getTriesCount(); r++) {
            for (int i = properties.getIdRangeBegin(); i <= properties.getIdRangeEnd(); i++) {
                HttpUriRequest request = requestById.apply(i);
                executorService.submit(() -> {
                    try (CloseableHttpResponse execute = client.execute(request)) { // this ugly syntax has been fixed in java 9
                    }
                    return null;
                });
            }
        }
        executorService.shutdown();
    }
}
