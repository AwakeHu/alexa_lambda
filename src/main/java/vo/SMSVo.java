package vo;


public class SMSVo {

    private NotificationDTO notification;
    private DeliveryDTO delivery;
    private String status;

    public NotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationDTO notification) {
        this.notification = notification;
    }

    public DeliveryDTO getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryDTO delivery) {
        this.delivery = delivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class NotificationDTO {
        private String messageId;
        private String timestamp;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class DeliveryDTO {
        private Integer numberOfMessageParts;
        private String destination;
        private Double priceInUSD;
        private String smsType;
        private String providerResponse;
        private Integer dwellTimeMs;
        private Integer dwellTimeMsUntilDeviceAck;

        public Integer getNumberOfMessageParts() {
            return numberOfMessageParts;
        }

        public void setNumberOfMessageParts(Integer numberOfMessageParts) {
            this.numberOfMessageParts = numberOfMessageParts;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public Double getPriceInUSD() {
            return priceInUSD;
        }

        public void setPriceInUSD(Double priceInUSD) {
            this.priceInUSD = priceInUSD;
        }

        public String getSmsType() {
            return smsType;
        }

        public void setSmsType(String smsType) {
            this.smsType = smsType;
        }

        public String getProviderResponse() {
            return providerResponse;
        }

        public void setProviderResponse(String providerResponse) {
            this.providerResponse = providerResponse;
        }

        public Integer getDwellTimeMs() {
            return dwellTimeMs;
        }

        public void setDwellTimeMs(Integer dwellTimeMs) {
            this.dwellTimeMs = dwellTimeMs;
        }

        public Integer getDwellTimeMsUntilDeviceAck() {
            return dwellTimeMsUntilDeviceAck;
        }

        public void setDwellTimeMsUntilDeviceAck(Integer dwellTimeMsUntilDeviceAck) {
            this.dwellTimeMsUntilDeviceAck = dwellTimeMsUntilDeviceAck;
        }
    }
}
