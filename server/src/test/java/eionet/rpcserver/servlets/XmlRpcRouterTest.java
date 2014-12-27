package eionet.rpcserver.servlets;

import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

public class XmlRpcRouterTest {

    private void checkRPCContentType(WebResponse response) {
        assertNotNull("No response received", response);
        assertEquals("content type", "text/xml", response.getContentType());
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
        String expected = "<html><head><title>XML/RPC RPC Router</title></head><body><h1>XML/RPC Router</h1><p>Sorry, I don't speak via HTTP GET- you have to use HTTP POST to talk to me</p></body></html>";
        assertEquals(expected, resp);

    }

    @Test
    public void postIsSupported() throws  Exception {
        ServletRunner sr = new ServletRunner();
        sr.registerServlet("rpcrouter", XmlRpcRouter.class.getName());
        ServletUnitClient sc = sr.newClient();
        String messageBody = "<?xml version='1.0'?>\n"
            + "<methodCall>"
            + "<methodName>TestService.getInfo</methodName>"
            + "<params/>"
            + "</methodCall>\n";
        ByteArrayInputStream body = new ByteArrayInputStream(messageBody.getBytes(Charset.forName("UTF-8")));
        WebRequest request   = new PostMethodWebRequest("http://test.meterware.com/rpcrouter", body, "text/xml");
        WebResponse response = sc.getResponse(request);
        checkRPCContentType(response);
        String resp = response.getText();
        String expected = "<?xml version=\"1.0\"?>"
            + "<methodResponse><params>"
            + "<param><value><array><data><value>"
            + "<struct><member><name>english</name><value>Bulgaria</value></member>"
            + "<member><name>bulgarian</name><value>&#1041;&#1098;&#1083;&#1075;&#1072;&#1088;&#1080;&#1103;</value></member>"
            + "</struct></value></data></array></value></param>"
            + "</params></methodResponse>";
        //System.out.println(resp);
        assertEquals(expected, resp);
    }

}
