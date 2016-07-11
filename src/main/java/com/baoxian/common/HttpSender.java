package com.baoxian.common;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpSender {

    private static Logger logger = Logger.getLogger(HttpSender.class);

    public static String post(String sendStr, String url) {
        HttpClient client = null;
        String responseStr = "";
        try {
            client = buildHttpClient();
            HttpResponse response = postHttpRequest(sendStr, client, url, null, null);
            HttpEntity entity = response.getEntity();
            responseStr = EntityUtils.toString(entity, "GBK").trim();
        } catch (Exception e) {
            logger.error("HttpClient请求异常: ", e);
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return responseStr;
    }

    private static X509TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    private static HttpClient buildHttpClient() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(sslcontext);
            ClientConnectionManager ccm = new DefaultHttpClient().getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
            HttpParams params = new BasicHttpParams();
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            HttpClient httpclient = new DefaultHttpClient(ccm, params);
            httpclient.getParams().setParameter(HTTP.USER_AGENT, "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
            return httpclient;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static HttpResponse postHttpRequest(String sendStr, HttpClient httpclient, String url, List<NameValuePair> parameters, Map<String, String> cookieData) {
        try {
            HttpPost post = new HttpPost(url);
            post.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN)");
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            if (cookieData != null) {
                boolean first = true;
                StringBuilder cookie = new StringBuilder();
                for (Map.Entry<String, String> me : cookieData.entrySet()) {
                    if (first) first = false;
                    else cookie.append(";");
                    cookie.append(me.getKey() + "=" + me.getValue());
                }
                post.setHeader("Cookie", cookie.toString());
            }
            if (parameters != null) {
                UrlEncodedFormEntity uef = new UrlEncodedFormEntity(parameters, "GBK");
                post.setEntity(uef);
            } else {
                StringEntity entity = new StringEntity(sendStr, "GBK");
                post.setEntity(entity);
            }
            HttpResponse response = httpclient.execute(post);
            return response;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
