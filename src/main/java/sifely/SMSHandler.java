package sifely;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import vo.SMSVo;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SMSHandler implements RequestHandler<CloudWatchLogsEvent, String> {



    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String handleRequest(CloudWatchLogsEvent cloudWatchLogsEvent, Context context) {
        try {
            String logData = cloudWatchLogsEvent.getAwsLogs().getData();
            byte[] decodedLogData = Base64.decode(logData);
            System.out.println("logData: "+logData);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(decodedLogData);
            GzipCompressorInputStream gzipStream = new GzipCompressorInputStream(byteStream);
            JsonNode logEvent = objectMapper.readTree(gzipStream);
            JsonNode logEvents = logEvent.get("logEvents");

            for (JsonNode event : logEvents) {
                JsonNode messageNode = event.get("message");
                String message = messageNode.asText();
                System.out.println("Log Message: " + message);
                // 这里可以添加更多逻辑来处理每个日志事件
                SMSVo smsVo = JSONUtil.toBean(message, SMSVo.class);
                String status = smsVo.getStatus();
                String messageId = smsVo.getNotification().getMessageId();
                String providerResponse = smsVo.getDelivery().getProviderResponse();
                String destination = smsVo.getDelivery().getDestination();
                System.out.println("messageId:" + messageId + ";phone:" + destination + ";status:" + status + ";result:" + providerResponse);
                //http请求到sifely,通过接口更改发送状态
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
        return "Successfully processed CloudWatch log events.";
    }

}
