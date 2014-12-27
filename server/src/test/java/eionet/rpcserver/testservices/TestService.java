package eionet.rpcserver.testservices;

import eionet.acl.AppUser;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class TestService {

    /**
     * Constructor for use of unauthenticated services.
     */
    public TestService() {
       // Ignore
    }

    /**
     * Constructor for use of authenticated services.
     */
    public TestService(AppUser user) {
       // Ignore
    }

    public Vector<Map<String, String>> getInfo() {
        Vector<Map<String, String>> v = new Vector<Map<String, String>>();

        Hashtable<String, String> h = new Hashtable<String, String>();
        h.put("english", "Bulgaria");
        h.put("bulgarian", "България");
        v.add(h);
        return v;
    }

    public Map<String, String> getRestricted() {
        Hashtable<String, String> h = new Hashtable<String, String>();
        h.put("english", "Bulgaria");
        h.put("bulgarian", "България");
        return h;
    }
}
