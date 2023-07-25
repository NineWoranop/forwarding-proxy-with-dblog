#!/bin/bash
#
# Start SonarQube
./bin/run.sh &

LANGUAGE=java
PROFILE_NAME=CustomProfile
PROFILE_FILE=java-customprofile.xml
BASE_URL=http://127.0.0.1:9000

function isUp {
  curl -s -u admin:admin -f "$BASE_URL/api/system/info"
}

# Wait for server to be up
PING=`isUp`
while [ -z "$PING" ]
do
  sleep 5
  PING=`isUp`
done

# Restore qualityprofile
curl -v -u admin:admin -F "backup=@/qualityprofile/$PROFILE_FILE" -X POST "$BASE_URL/api/qualityprofiles/restore"

# Set default profile
curl -v -u admin:admin -X POST "$BASE_URL/api/qualityprofiles/set_default?language=$LANGUAGE&qualityProfile=$PROFILE_NAME"

wait
