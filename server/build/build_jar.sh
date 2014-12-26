#folder for services.xsd
xsd_dir=../etc

#destination for source files
dest_dir=../src

#library for castor.jar
lib_dir=../lib

if [ ! -f build.xml ]; then
	echo "Before running this script change directory to RpcServer/build"
	exit
fi;

# buildfile
build_file=build.xml


if [ -f ../../lib/ant.jar ]; then
	java_lib=../../lib
else
	java_lib=..
fi;

# generate uit-definition source 
# !! For some reason it did not work with xerces.jar :( 
java -cp $java_lib/xercesImpl.jar:$lib_dir/castor.jar org.exolab.castor.builder.SourceGenerator -i $xsd_dir/services.xsd -verbose -package com.tee.uit.definition -dest $dest_dir -f
echo "!!!!!!!!!!! Source OK !!!!!!!!!!"

# build JAR
libs=$java_lib/ant.jar:$java_lib/xml-apis.jar:$JAVA_HOME/lib/tools.jar:$java_lib/xercesImpl.jar:$lib_dir/castor.jar


if [ ! -d ../classes ]; then
	mkdir ../classes
fi;

java -cp $libs org.apache.tools.ant.Main -f $build_file build_def_jar
java -cp $libs org.apache.tools.ant.Main -f $build_file buildjar

# clean
rm -r ../classes

# no need to hold uit-definition source ??
# comment in
# rm -r ../src/com/tee/uit/definition



