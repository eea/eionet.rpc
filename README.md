RPC version 3.0
===============

This library is a re-merge of the following SVN packages:

* https://svn.eionet.europa.eu/repositories/Reportnet/RpcClient
* https://svn.eionet.europa.eu/repositories/Reportnet/Definition
* https://svn.eionet.europa.eu/repositories/Reportnet/RpcServer

They have the same API as the ones found in SVN except that the package names have been changed to eionet.something

| From | To |
| ---- | -- |
| com.tee.uit.client | eionet.rpcclient |
| com.tee.uit.definition | eionet.definition |
| com.tee.uit.server | eionet.rpcserver |
| com.tee.uit.servlets | eionet.rpcserver.servlets |

To use the packages, include this into your pom.xml in Maven.
```xml
<dependency>
    <groupId>eionet</groupId>
    <artifactId>rpcclient</artifactId>
    <version>3.0</version>
</dependency>

<dependency>
    <groupId>eionet</groupId>
    <artifactId>definition</artifactId>
    <version>3.0</version>
</dependency>

<dependency>
    <groupId>eionet</groupId>
    <artifactId>rpcserver</artifactId>
    <version>3.0</version>
</dependency>
```

Version 3.0
-----------
Since version 3.0 there is a change in the way the library is configured.  It uses rpc.properties instead of uit.properties.

Castor dependency
-----------------
The original castor was version 0.9.3.9 from ExoLab. It has since then started to live by its own. (http://castor.codehaus.org/)
We have used the 1.2 from the main Maven repository.

Configuration
-------------

The package can be configured via JNDI or a properties file. If a environment entry in JNDI is found then all required entries must be configured via JNDI. You configure the application through JNDI with the META-INF/context.xml of the web application using this package. In Tomcat all JNDI names will automatically be prefixed with `java:/comp/env`. Since it is a shared directory, ACL configurations are in the `rpc/` sub-context. Example:

```xml
<Context>
    <Environment name="rpc/componentservices" value="HelpService" type="java.lang.String" override="false"/>
    <Environment name="rpc/acl.admin" value="true" type="java.lang.Boolean" override="false"/>
</Context>
```

Alternatively copy `rpc.properties` from `src/test/resources` to your project's classpath and change the property values accordingly. Note that the file and folder paths in the `rpc.properties` are relative because of unit tests. For other usage, absolute paths should be used.

If you want to continue with property files, but specify with JNDI, what file to load the properties from, then you can add a context environment variable called `rpc/propertiesfile`
```xml
<Context>
    <Environment name="rpc/propertiesfile"
                 value="/var/local/datadict/rpc.properties"
                 type="java.lang.String" override="false"/>
</Context>
```

