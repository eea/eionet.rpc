package eionet.rpcserver.servlets;

import com.meterware.httpunit.Base64;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import org.apache.commons.lang.StringEscapeUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class XmlRpcRouterTest {

    private void checkRPCContentType(WebResponse response) {
        assertNotNull("No response received", response);
        assertEquals("content type", "text/xml", response.getContentType());
    }

    private InputStream buildMethodCall(String methodName, Object... params) throws Exception {
        StringBuilder messageBody = new StringBuilder("<?xml version='1.0'?>\n"
            + "<methodCall>"
            + "<methodName>TestService." + methodName +"</methodName>");
        if (params == null) {
            messageBody.append("<params/>");
        } else {
            messageBody.append("<params>");
            for (Object param : params) {
                messageBody.append("<param>");
                if (param instanceof String) {
                    messageBody.append("<string>").append(StringEscapeUtils.escapeXml(param.toString())).append("</string>");
                } else if (param instanceof Double) {
                    messageBody.append("<double>").append(param).append("</double>");
                } else if (param instanceof Integer) {
                    messageBody.append("<i4>").append(param).append("</i4>");
                } else if (param instanceof Boolean) {
                    messageBody.append("<boolean>").append(param).append("</boolean>");
                } else {
                    messageBody.append(StringEscapeUtils.escapeXml(param.toString()));
                }
                messageBody.append("</param>");
            }
            messageBody.append("</params>");
        }
        messageBody.append("</methodCall>\n");
        ByteArrayInputStream body = new ByteArrayInputStream(messageBody.toString().getBytes("UTF-8"));
        return body;
    }

    /*
     * Login as a user that exists in testusers.xml.
     */
    private void loginAs(WebRequest request, String username, String password) {
        String token = Base64.encode(username + ":" + password);
        request.setHeaderField("Authorization", "Basic " + token);
    }

    @Test
    public void getIsNotSupported() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("rpcrouter", XmlRpcRouter.class.getName());
        ServletUnitClient sc = sr.newClient();
        WebRequest request   = new GetMethodWebRequest("http://test.meterware.com/rpcrouter");
        WebResponse response = sc.getResponse(request);
        assertNotNull("No response received", response);
        assertEquals("content type", "text/html", response.getContentType());
        String resp = response.getText();
        String expected = "<html><head><title>XML/RPC RPC Router</title></head>"
            + "<body><h1>XML/RPC Router</h1>"
            + "<p>Sorry, I don't speak via HTTP GET- you have to use HTTP POST to talk to me</p></body></html>";
        assertEquals(expected, resp);

    }

    @Test
    public void postIsSupported() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("rpcrouter", XmlRpcRouter.class.getName());
        ServletUnitClient sc = sr.newClient();
        InputStream body = buildMethodCall("getInfo");
        WebRequest request   = new PostMethodWebRequest("http://test.meterware.com/rpcrouter", body, "text/xml");
        WebResponse response = sc.getResponse(request);
        checkRPCContentType(response);
        String resp = response.getText();
        String expected = "<?xml version=\"1.0\"?>"
            + "<methodResponse>"
            + "<params>"
            + "<param><value><array><data><value>"
            + "<struct>"
            + "<member><name>english</name><value>Bulgaria</value></member>"
            + "<member><name>bulgarian</name><value>&#1041;&#1098;&#1083;&#1075;&#1072;&#1088;&#1080;&#1103;</value></member>"
            + "</struct></value></data></array></value></param>"
            + "</params>"
            + "</methodResponse>";
        //System.out.println(resp);
        assertTrue(resp.contains("<member><name>bulgarian</name><value>&#1041;&#1098;&#1083;&#1075;&#1072;&#1088;&#1080;&#1103;</value></member>"));
        assertTrue(resp.contains("<member><name>english</name><value>Bulgaria</value></member>"));
        assertEquals(expected, resp);
    }

    @Test
    public void accessRestrictedAsUnauthenticated() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("rpcrouter", XmlRpcRouter.class.getName());
        ServletUnitClient sc = sr.newClient();
        InputStream body = buildMethodCall("getRestricted");
        WebRequest request   = new PostMethodWebRequest("http://test.meterware.com/rpcrouter", body, "text/xml");
        WebResponse response = sc.getResponse(request);
        checkRPCContentType(response);
        String resp = response.getText();
        String expected = "<?xml version=\"1.0\"?>"
            + "<methodResponse>"
            + "<fault><value>"
            + "<struct>"
            + "<member><name>faultString</name>"
            + "<value>eionet.rpcserver.ServiceException: eionet.rpcserver.ServiceException: Not authenticated</value></member>"
            + "<member><name>faultCode</name>"
            + "<value><int>0</int></value></member>"
            + "</struct></value></fault></methodResponse>";
        assertEquals(expected, resp);
    }

    @Test
    public void accessRestrictedAsTestUser() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("rpcrouter", XmlRpcRouter.class.getName());
        ServletUnitClient sc = sr.newClient();
        InputStream body = buildMethodCall("getRestricted");
        WebRequest request = new PostMethodWebRequest("http://test.meterware.com/rpcrouter", body, "text/xml");
        loginAs(request, "test", "test");
        WebResponse response = sc.getResponse(request);
        checkRPCContentType(response);
        String resp = response.getText();
        String expected = "<?xml version=\"1.0\"?>"
            + "<methodResponse>"
            + "<params>"
            + "<param><value>"
            + "<struct>"
            + "<member><name>english</name><value>Bulgaria</value></member>"
            + "<member><name>bulgarian</name><value>&#1041;&#1098;&#1083;&#1075;&#1072;&#1088;&#1080;&#1103;</value></member>"
            + "</struct></value></param></params></methodResponse>";
        //System.out.println(resp);
        assertEquals(expected, resp);
    }

    @Test
    public void accessRestrictedWithBadPassword() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("rpcrouter", XmlRpcRouter.class.getName());
        ServletUnitClient sc = sr.newClient();
        InputStream body = buildMethodCall("getRestricted");
        WebRequest request = new PostMethodWebRequest("http://test.meterware.com/rpcrouter", body, "text/xml");
        loginAs(request, "test", "badpassword");
        WebResponse response = sc.getResponse(request);
        checkRPCContentType(response);
        String resp = response.getText();
        String expected = "<?xml version=\"1.0\"?>"
            + "<methodResponse>"
            + "<fault><value>"
            + "<struct>"
            + "<member><name>faultString</name>"
            + "<value>eionet.acl.SignOnException: Not authenticated</value></member>"
            + "<member><name>faultCode</name>"
            + "<value><int>0</int></value></member>"
            + "</struct></value></fault></methodResponse>";
        //System.out.println(resp);
        assertEquals(expected, resp);
    }

}
