package eionet.rpcserver.testservices;

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class TestService {

    public Vector<Map<String, String>> getInfo() {
        Vector<Map<String, String>> v = new Vector<Map<String, String>>();

        Hashtable<String, String> h = new Hashtable<String, String>();
        h.put("english", "Bulgaria");
        h.put("bulgarian", "България");
        v.add(h);
        return v;
    }

}
