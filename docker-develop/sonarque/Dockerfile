FROM sonarqube:8.5-community

# Install curl
RUN apk --no-cache add curl

RUN cd /opt/sonarqube/extensions/plugins/

# Add default Java Quality Profile
ADD qualityprofile/java-customprofile.xml /qualityprofile/

VOLUME ["$SONARQUBE_HOME/data", "$SONARQUBE_HOME/extensions", "/qualityprofile"]

ADD start_with_profile.sh /opt/sonarqube/start_with_profile.sh

ENTRYPOINT ["/opt/sonarqube/start_with_profile.sh"]
