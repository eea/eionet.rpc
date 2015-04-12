package eionet.rpcserver;

import java.util.HashMap;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UITServiceRosterTest {

    static String rpcContextLocation = "java:comp/env/rpc/";
     
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUpIC() throws Exception {
        JNDISupport.setUpCore();
        JNDISupport.addSubCtxToTomcat("rpc");
        UITServiceRoster.reset();
    }

    @After
    public void cleanUpIC() throws Exception {
        JNDISupport.cleanUp();
        UITServiceRoster.reset();
    }

    @Test
    public void loadViaJNDI() throws Exception {
        JNDISupport.addSubCtxToTomcat("somethingelse");
        JNDISupport.addPropToTomcat("rpc/acl.admin", Boolean.valueOf(true));
        JNDISupport.addPropToTomcat("rpc/services.definition.file", "ServiceDefinition.xml");

        Hashtable<Object, Object> props = UITServiceRoster.loadProperties();

        assertEquals(Boolean.valueOf(true), props.get("acl.admin"));
        assertEquals("ServiceDefinition.xml", props.get("services.definition.file"));
    }

    @Test
    public void getServices() throws Exception {
        JNDISupport.addPropToTomcat("rpc/services.definition.file", "target/test-classes/ServiceFilename.xml");
        HashMap services = UITServiceRoster.getServices();
        assertNotNull(services);
    }

    @Test
    public void getTestService() throws Exception {
        JNDISupport.addPropToTomcat("rpc/services.definition.file", "target/test-classes/ServiceFilename.xml");
        UITServiceIF service = UITServiceRoster.getService("TestService");
        assertNotNull(service);
    }

    @Test
    public void getTestServiceFromClassPath() throws Exception {
        JNDISupport.addPropToTomcat("rpc/services.definition.file", "ServiceFilename.xml");
        UITServiceIF service = UITServiceRoster.getService("TestService");
        assertNotNull(service);
    }

    @Test
    public void badServicesFileName() throws Exception {
        JNDISupport.addPropToTomcat("rpc/services.definition.file", "NoSuchServiceDefinition.xml");
        exception.expect(ServiceException.class);
        exception.expectMessage("Services file NoSuchServiceDefinition.xml not found.");
        UITServiceIF service = UITServiceRoster.getService("TestService");
        assertNotNull(service);
    }

    //@Ignore
    @Test
    public void badServicesFileContent() throws Exception {
        JNDISupport.addPropToTomcat("rpc/services.definition.file", "target/test-classes/testusers.xml");
        //exception.expect(ServiceException.class);
        //exception.expectMessage("File NoSuchServiceDefinition.xml not found.");
        UITServiceIF service = UITServiceRoster.getService("TestService");
        assertNull(service);
    }

    @Test
    public void unparsableServicesFileContent() throws Exception {
        JNDISupport.addPropToTomcat("rpc/services.definition.file", "target/test-classes/rpc.properties");
        exception.expect(ServiceException.class);
        exception.expectMessage("Error reading services from file target/test-classes/rpc.properties");
        UITServiceIF service = UITServiceRoster.getService("TestService");
        assertNull(service);
    }
}
