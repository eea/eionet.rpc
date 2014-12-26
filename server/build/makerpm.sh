####################################################################################
# makes RPM source package and install package
# of JAR
####################################################################################
# usage makerpm.sh [version]
# if version specified (1.0), the tag has to be created in CVS, called release_1_0
# if version is not specified,then 1.0, then the latestcode is taken from CVS
# and version no 1.0 is used as default

###################################################################################
# !!!!!!!!!! NEEDS ADJUSTEMENT !!!!!!!!!!!
# RPM directory : parent for SOURCE, BUILD and other rpm directories 
###################################################################################

# !!! EEA !!!
# which one is correct?
rpm_dir=/bin/rpm
# rpm_dir=/usr/src/redhat

###################################################################################


ver=$1

if [ "$ver" = "" ] ; then
 ver="1.0"
 cvs export -D today -d uit-server-$ver Java/RpcServer
else
 rel=`echo $ver | tr "\." "_"`
 cvs export -r release_$rel -d uit-server-$ver Java/RpcServer
fi;

#############################################
# Export ANT libraries, needed for compiling
#############################################
cvs export -D today -d uit-server-$ver Java/lib


########################################
# Make com.tee.uit.definition source
########################################
# to be made by installation script ?
# java -cp $java_lib/xercesImpl.jar:$lib_dir/castor.jar org.exolab.castor.builder.SourceGenerator -i $xsd_dir/services.xsd -verbose -package com.tee.uit.definition -dest $dest_dir -f

###########
# make tgz
###########
tar cfz uit-server.tgz uit-server-$ver

###############################
# copy *.spec file to working
###############################
cp uit-server-$ver/build/rpcserver.spec .

######################
# Remove temp folder
######################
rm -rf uit-server-$ver

######################
# RPM source directory
######################
if [ -d $rpm_dir/SOURCES ]; then
	mv uit-server.tgz $rpm_dir/SOURCES
else
	echo "RPM directory $rpm_dir does not exist. Cannot create RPM package"
	exit
fi;


############
#build rpm
############
rpm -ba rpcserver.spec

echo "Success!"

echo "TAR archive was created in $rpm_dir/SOURCES"
echo "The source package was created in $rpm_dir/SRPMS"
echo "The binary package can be find in $rpm_dir/RPMS/i386"
# cp $rpm_dir/SRPMS/*.rpm .
# cp $rpm_dir/RPMS/i386/*.rpm .

rm rpcserver.spec

##################
# README file
##################

echo "To install the binary package in default location type: rpm -i uit-server-$ver-1.i386.rpm" > RPCSERVER-README
echo "To install the binary package in a different location (e.g. /webs/webrod/WEB-INF/lib) type: rpm -i --prefix /webs/webrod/WEB-INF uit-server-$ver-1.i386.rpm" >> RPCSERVER-README

echo "To compile the source package type: rpm --rebuild uit-server-$ver-1.src.rpm" >> RPCSERVER-README
echo "Compiling requires rpm-build module to be installed. Type:  rpm -q rpm-build to check " >> RPCSERVER-README

