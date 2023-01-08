package com.functionTest;

import com.functionTest.beans.JsonResponse;
import com.google.gson.Gson;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     * @throws InterruptedException
     * @throws IOException
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) throws IOException, InterruptedException {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        System.out.println("Query: " + query);
        final String name = request.getBody().orElse(query);
        System.out.println("Body: " + name);

        Gson gson = new Gson();

    

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest hRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://3d04g.mocklab.io/json/1"))
                .GET() // GET is default
                .build();

        HttpResponse<String> response = client.send(hRequest,
                HttpResponse.BodyHandlers.ofString());
 

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Body: " + response.body());
        
        JsonResponse jsonResponseObj = gson.fromJson(response.body(), JsonResponse.class);

        System.out.println("Object Value: " + jsonResponseObj.getValue());


        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }
}
