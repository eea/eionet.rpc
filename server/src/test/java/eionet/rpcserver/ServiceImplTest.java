package eionet.rpcserver;

import eionet.definition.Service;
import eionet.definition.Method;
import eionet.definition.Methods;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ServiceImplTest {

    @Test
    public void getMethods() throws Exception {
        Service service = new Service();
        Methods ms = new Methods();

        Method m1 = new Method();
        m1.setName("m1");
        ms.addMethod(m1);


        Method m2 = new Method();
        m2.setName("m2");
        ms.addMethod(m2);
        service.setMethods(ms);

        ServiceImpl si = new ServiceImpl(service);
        String[] methodNames = si.getMethodNames();
        assertEquals("m1", methodNames[0]);
        assertEquals("m2", methodNames[1]);
    }

}
