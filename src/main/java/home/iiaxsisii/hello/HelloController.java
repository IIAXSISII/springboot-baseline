package home.iiaxsisii.hello;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
