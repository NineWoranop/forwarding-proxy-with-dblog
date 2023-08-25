xcopy /Y %~dp0java\target\forwarding-proxy-with-dblog-0.0.1-SNAPSHOT.jar %~dp0docker-build
cd  %~dp0\docker-build & docker build -t "ninetom/forwarding-proxy" . & cd ..