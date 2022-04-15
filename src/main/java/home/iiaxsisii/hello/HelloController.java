package home.iiaxsisii.hello;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @RequestMapping("/")
    public Map index() throws SocketException {

        Map<String, String> responseMap = new HashMap<>();

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface networkInterface : Collections.list(nets))
            displayInterfaceInformation(networkInterface, responseMap);

        String ip4Url = "http://169.254.169.254/latest/meta-data/local-ipv4";
        String instanceIdUrl = "http://169.254.169.254/latest/meta-data/instance-id";

        try {
            String ip4 = httpGetCall(ip4Url);
            String instanceId = httpGetCall(instanceIdUrl);

            responseMap.put("Instance Ip", ip4);
            responseMap.put("Instance Id", instanceId);
            return responseMap;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return responseMap;
    }

    void displayInterfaceInformation(NetworkInterface networkInterface, Map<String, String> responseMap) throws SocketException {
        responseMap.put("Network Display Name", networkInterface.getDisplayName());
        responseMap.put("Network Name", networkInterface.getName());
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        int count = 0;
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            count++;
            responseMap.put(count +" InetAddress", inetAddress.getAddress().toString());
            responseMap.put(count + " HostAddress", inetAddress.getHostAddress().toString());
        }
    }

    private String httpGetCall(String getUrl) throws IOException {
        String responseBody = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(getUrl);
            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            responseBody = httpclient.execute(httpget, responseHandler);
        }
        return responseBody;
    }
}
