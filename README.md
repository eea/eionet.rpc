RPC client version 2.0
======================

This library has the same API as the one found in https://svn.eionet.europa.eu/repositories/Reportnet/RpcClient

except that the package name has been changed to eionet.rpcclient


To use the package, include this into you pom.xml in Maven.
```xml
<dependency>
    <groupId>eionet</groupId>
    <artifactId>rpcclient</artifactId>
    <version>2.0</version>
</dependency>
```


Castor dependency
-----------------

The original castor was version 1.0 from ExoLab. It has since then started to live by its own. (http://castor.codehaus.org/)
We have used the 1.2 from the main Maven repository.

