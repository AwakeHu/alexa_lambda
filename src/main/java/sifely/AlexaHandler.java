package sifely;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlexaHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        String request = getRequest(inputStream);
        String response = null;
        try {
//            request = getRequest(inputStream);

            Map <String,String> headers = new HashMap<>();
            headers.put("User-Agent", "Alexa");
            headers.put("Content-Type", "application/json");
            // 获取 token 的值
            headers.put("Authorization", findTokenValue(request));
            String url = System.getenv("DOMAIN_URL");
            System.out.println("url:"+url);
            System.out.println("param:"+request);
            System.out.println("headers:"+JSONUtil.toJsonStr(headers));
            Map<String, String> apiResponse = OkHttpPostClient.postJsonHttp(url, request, headers);
//            Map<String, String> apiResponse = OkHttpPostClient.postJsonHttp("http://dev-app-server.sifely.com:8090/smart/home/execute", JSONUtil.toJsonStr(param), headers);
            System.out.println("Response:" + apiResponse);
            if(new Integer(apiResponse.get("statusCode")) >= 200 && new Integer(apiResponse.get("statusCode")) <= 300){
                String responseContent = apiResponse.get("responseContent");
                outputStream.write(JSONUtil.toJsonStr(responseContent).getBytes(Charset.forName("UTF-8")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String findTokenValue(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonString);
        return findTokenRecursive(rootNode);
    }

    private static String findTokenRecursive(JsonNode node) {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if ("token".equals(field.getKey())) {
                    return field.getValue().asText();
                } else {
                    String result = findTokenRecursive(field.getValue());
                    if (result != null) {
                        return result;
                    }
                }
            }
        } else if (node.isArray()) {
            for (JsonNode arrayElement : node) {
                String result = findTokenRecursive(arrayElement);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String jsonString = "";
        try {
            String tokenValue = findTokenValue(jsonString);
            System.out.println("Token value: " + tokenValue);  // 输出: desired_token
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getRequest(java.io.InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }



}
