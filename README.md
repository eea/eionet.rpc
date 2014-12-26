RPC version 2.0
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

To use the packages, include this into your pom.xml in Maven.
```xml
<dependency>
    <groupId>eionet</groupId>
    <artifactId>rpcclient</artifactId>
    <version>2.0</version>
</dependency>

<dependency>
    <groupId>eionet</groupId>
    <artifactId>definition</artifactId>
    <version>2.0</version>
</dependency>

<dependency>
    <groupId>eionet</groupId>
    <artifactId>rpcserver</artifactId>
    <version>2.0</version>
</dependency>
```


Castor dependency
-----------------
The original castor was version 0.9.3.9 from ExoLab. It has since then started to live by its own. (http://castor.codehaus.org/)
We have used the 1.2 from the main Maven repository.

