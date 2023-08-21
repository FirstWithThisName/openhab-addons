package org.openhab.binding.panasonicviera.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class RemoteControl {

    private static final String URN_RENDERING_CONTROL = "schemas-upnp-org:service:RenderingControl:1";
    private static final String URL_CONTROL_DMR = "dmr/control_0";
    private static final String URL_CONTROL_NRC = "nrc/control_0";
    private static final String URL_CONTROL_NRC_DDD = "nrc/ddd.xml";
    private static final String URN_REMOTE_CONTROL = "panasonic-com:service:p00NetworkControl:1";
    private static final String URL_TEMPLATE = "http://%s:%d/%s";
    private static final String SOAP_BODY_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            + "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\""
            + " s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" + "<s:Body>"
            + "<%1$s:%2$s xmlns:%1$s=\"urn:%3$s\">" + "%4$s" + "</%1$s:%2$s>" + "</s:Body>" + "</s:Envelope>";

    private final String hostname;
    private final int port;

    public RemoteControl(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void sendKey(Key key) throws IOException, ParserConfigurationException, SAXException {
        String params = String.format("<X_KeyEvent>%s</X_KeyEvent>", key);
        try {
            sendSoapRequest(URL_CONTROL_NRC, URN_REMOTE_CONTROL, "X_SendKey", params, null);
        } catch (TvIsConnectionIsClosedException e) {
            // do nothing if tv is turned off
        }
    }

    public boolean getMute()
            throws IOException, ParserConfigurationException, SAXException, TvIsConnectionIsClosedException {
        String params = "<InstanceID>0</InstanceID><Channel>Master</Channel>";
        Document doc = sendSoapRequest(URL_CONTROL_DMR, URN_RENDERING_CONTROL, "GetMute", params, null);
        return doc.getElementsByTagName("CurrentMute").item(0).getTextContent() == "1";
    }

    public boolean getPowerStatus() throws IOException, ParserConfigurationException, SAXException {
        try {
            getMute();
            return true;
        } catch (TvIsConnectionIsClosedException e) {
            return false;
        }
    }

    private Document sendSoapRequest(String urlStr, String urn, String action, String params, String bodyElem)
            throws IOException, ParserConfigurationException, SAXException, TvIsConnectionIsClosedException {
        if (bodyElem == null) {
            bodyElem = "m";
        }
        String soapBody = String.format(SOAP_BODY_TEMPLATE, bodyElem, action, urn, params);
        URL url = new URL(String.format(URL_TEMPLATE, this.hostname, this.port, urlStr));
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection) con;
        http.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        http.setRequestProperty("Soapaction", String.format("\"urn:%s#%s\"", urn, action));
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        try (OutputStream out = http.getOutputStream()) {
            out.write(soapBody.getBytes("utf-8"));
        }

        if (http.getResponseCode() == 400 && "close".equals(http.getHeaderField("Connection"))) {
            throw new TvIsConnectionIsClosedException();
        }

        try (InputStream inputStream = http.getInputStream()) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            return doc;

            /*
             * String text = new BufferedReader(
             * new InputStreamReader(inputStream, StandardCharsets.UTF_8))
             * .lines()
             * .collect(Collectors.joining("\n"));
             * System.out.println("text = " + text);
             */
        }
    }
}
