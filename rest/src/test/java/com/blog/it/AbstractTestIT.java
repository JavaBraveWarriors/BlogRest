package com.blog.it;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

class AbstractTestIT {

    protected String port = "8089";
    protected String address = "http://localhost";

    protected static String endpoint;
    protected static RestTemplate restTemplate = new RestTemplate();
    protected static HttpHeaders headers = new HttpHeaders();

    protected String createURLWithPort(String URI) {
        return address + ":" + port + "/" + endpoint + URI;
    }
}
