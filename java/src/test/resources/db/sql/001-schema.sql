CREATE TABLE proxy_log (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  listening_port INT NULL,
  http_code INT NULL,
  response_time  INT NULL,
  mock BOOLEAN  DEFAULT false NULL,
  date_sent TIMESTAMP NULL,
  date_received TIMESTAMP NULL,
  analysed BOOLEAN  DEFAULT false NULL,
  http_method varchar(10) NULL,
  uri VARCHAR(500) NULL,
  request_headers TEXT NULL,
  request_body MEDIUMBLOB NULL,
  response_headers TEXT NULL,
  response_body MEDIUMBLOB NULL,
  referenceid_01 varchar(100) NULL,
  referenceid_02 varchar(100) NULL,
  referenceid_03 varchar(100) NULL,
  referenceid_04 varchar(100) NULL,
  referenceid_05 varchar(100) NULL,
  referenceid_06 varchar(100) NULL,
  referenceid_07 varchar(100) NULL,
  referenceid_08 varchar(100) NULL,
  referenceid_09 varchar(100) NULL,
  referenceid_10 varchar(100) NULL
);