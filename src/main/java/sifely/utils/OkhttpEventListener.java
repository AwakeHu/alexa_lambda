package sifely.utils;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * okhttp trace
 *
 * @author kupo
 * @since 2021/12/1
 */
public class OkhttpEventListener extends EventListener {
    public static final Factory FACTORY = new Factory() {
        final AtomicLong nextCallId = new AtomicLong(1L);

        @Override
        public EventListener create(Call call) {
            long callId = nextCallId.getAndIncrement();
            return new OkhttpEventListener(callId, call);
        }
    };

    private long st = -1L;
    private int code = -1;
    private final long callId;
    private final String remote;
    private final String url;
    private long connectStartMs = -1L;
    private long dnsStartMs = -1L;
    private long requestStartMs = -1L;

    public OkhttpEventListener(long callId, Call call) {
        this.callId = callId;
        String host = null;
        String url = null;
        try {
            host = call.request().url().host();
            url = call.request().url().toString();
        } catch (Exception e) {
        }
        if (StringUtils.isBlank(host)) {
            host = "undefined";
        }
        if (StringUtils.isBlank(url)) {
            url = "undefined";
        }
        this.remote = host;
        this.url = url;
    }

    @Override
    public void callStart(Call call) {
        this.st = System.currentTimeMillis();
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        this.dnsStartMs = System.currentTimeMillis();
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        if (dnsStartMs > 0) {
            this.traceDnsTime(dnsStartMs);
            dnsStartMs = -1L;
        }
    }

    @Override
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        this.connectStartMs = System.currentTimeMillis();
    }

    @Override
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
    }

    @Override
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        if (this.connectStartMs > 0) {
            this.traceConnectTime(this.connectStartMs);
//            this.traceSuccessfulConnectTime(this.connectStartMs);
            this.connectStartMs = -1L;
        }
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
        if (this.connectStartMs > 0) {
            this.traceConnectTime(this.connectStartMs);
            this.connectStartMs = -1L;
        }
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
    }

    @Override
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
    }

    @Override
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
    }

    @Override
    public void responseHeadersStart(Call call) {
        this.requestStartMs = System.currentTimeMillis();
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        code = response.code();
    }

    @Override
    public void responseBodyStart(Call call) {
        if (this.requestStartMs == -1) {
            this.requestStartMs = System.currentTimeMillis();
        }
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        if (this.requestStartMs > 0) {
            this.traceRespTime(this.requestStartMs);
            this.requestStartMs = -1;
        }
    }

    @Override
    public void callEnd(Call call) {
        // log.info("event listener callEnd, callId={} url={} code={}", callId, url, code);
        this.trace();
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        // 置为零用于标示失败
        this.code = 0;
        this.trace();

        if (this.requestStartMs > 0) {
            this.traceRespTime(this.requestStartMs);
            this.requestStartMs = -1;
        }
    }

    private void trace() {
        long elapse = System.currentTimeMillis() - st;
    }

    public void traceDnsTime(long startTime) {
    }

    public void traceConnectTime(long startTime) {
    }

    public void traceRespTime(long startTime) {
    }
}
