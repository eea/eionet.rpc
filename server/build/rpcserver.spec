Summary: RPC Server
Name: uit-server
Version: 1.0
Release: 1
Source0: %{name}.tgz
License: MPL
Group: Small Java coponents
BuildRoot: %{_topdir}/tmp_root
Prefix: /opt/eionet/java
Requires: uit-security >= 1.0
%description
Common and reusable component for providing
%prep
%setup -q
%build
 cd build
 chmod +x build_jar.sh
./build_jar.sh
%install
if [ ! -d %{buildroot}%{prefix}/lib ]; then
   mkdirhier %{buildroot}%{prefix}/lib
fi;
cp etc/uit.properties %{buildroot}%{prefix}
cp lib/uit-server.jar %{buildroot}%{prefix}/lib
cp lib/uit-definition.jar %{buildroot}%{prefix}/lib
%clean
rm -r $RPM_BUILD_ROOT
cd ..
rm -r uit-server-%{version}
%files
%{prefix}/lib/uit-server.jar
%{prefix}/lib/uit-definition.jar
%config %{prefix}/uit.properties
