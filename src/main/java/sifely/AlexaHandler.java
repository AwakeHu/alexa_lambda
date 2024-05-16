package sifely;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
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

public class AlexaHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        String request = getRequest(inputStream);
        String response = null;
        try {
//            request = getRequest(inputStream);
            System.out.println("Request:" + request);

            Map<String,Object> param = new HashMap<>();
            param.put("dtBody", request);
            param.put("source","remote");

            Map <String,String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            Map<String, String> apiResponse = OkHttpPostClient.postJsonHttp("https://b759-2409-8a55-3c85-bcf4-f130-bbd1-3f34-ec8b.ngrok-free.app/smart/home/oauthToken", JSONUtil.toJsonStr(param), headers);
            System.out.println("Response:" + apiResponse);
            if(new Integer(apiResponse.get("statusCode")) >= 200 && new Integer(apiResponse.get("statusCode")) <= 300){
                String responseContent = apiResponse.get("responseContent");
                JSONObject entries = JSONUtil.parseObj(apiResponse.get("responseContent"));
                Object result = entries.get("result");
                outputStream.write(JSONUtil.toJsonStr(result).getBytes(Charset.forName("UTF-8")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static String getRequest(java.io.InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void main(String[] args) throws IOException {
        AlexaHandler alexaHandler = new AlexaHandler();
        alexaHandler.handleRequest(null,
                null, null);

    }


}
