FROM amazoncorretto:8u382

COPY app.jar /usr/local/src/

WORKDIR /usr/local/src

CMD ["-jar", \
	 "app.jar" \
]

ENTRYPOINT ["java"]