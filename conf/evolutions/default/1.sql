# Contacts schema

# --- !Ups

CREATE SEQUENCE contact_id_seq;
CREATE TABLE contact (
    id integer NOT NULL DEFAULT nextval('contact_id_seq'),
    name varchar(255),
    phone varchar(15)
);

# --- !Downs

DROP TABLE contact;
DROP SEQUENCE contact_id_seq;
Now if you refresh your browser, Play will warn you that your database needs evolution: