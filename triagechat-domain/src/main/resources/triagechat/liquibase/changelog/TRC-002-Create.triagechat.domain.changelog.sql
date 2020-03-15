/**
 * Schema creation for Triage Chat
 *
 */

CREATE SEQUENCE message_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
END;

CREATE TABLE "message" (
    "id"                         BIGINT DEFAULT nextval('message_id_seq' :: REGCLASS) NOT NULL,
    "uuid"                       UUID                                                        NOT NULL DEFAULT uuid_generate_v4(),
    "create_date"                TIMESTAMP WITH TIME ZONE                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "update_date"                TIMESTAMP WITH TIME ZONE                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "version"                    BIGINT                                                      NOT NULL,
    "text"                       TEXT                                                        NOT NULL,

    PRIMARY KEY ("id")
);
END;

CREATE UNIQUE INDEX "message_idx01"
    ON "message" ("uuid");


------------------------------------------------------------------------------------------------------------------------


CREATE SEQUENCE tag_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
END;

CREATE TABLE "tag" (
    "id"                         BIGINT DEFAULT nextval('tag_id_seq' :: REGCLASS) NOT NULL,
    "uuid"                       UUID                                                NOT NULL DEFAULT uuid_generate_v4(),
    "create_date"                TIMESTAMP WITH TIME ZONE                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "update_date"                TIMESTAMP WITH TIME ZONE                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "version"                    BIGINT                                              NOT NULL,
    "name"                       VARCHAR(70)                                         NOT NULL,

    PRIMARY KEY ("id")
);
END;

CREATE UNIQUE INDEX "tag_idx01"
    ON "tag" ("uuid");

CREATE INDEX "tag_idx02"
    ON "tag" ("name");


------------------------------------------------------------------------------------------------------------------------


CREATE SEQUENCE message_tag_id_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
END;

CREATE TABLE "message_tag" (
    "id"                          BIGINT DEFAULT nextval('message_tag_id_seq' :: REGCLASS) NOT NULL,
    "uuid"                        UUID                                                        NOT NULL DEFAULT uuid_generate_v4(),
    "create_date"                 TIMESTAMP WITH TIME ZONE                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "update_date"                 TIMESTAMP WITH TIME ZONE                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "version"                     BIGINT                                                      NOT NULL,
    "message_id"                  BIGINT                                                      NOT NULL,
    "tag_id"                      BIGINT                                                      NOT NULL,

    PRIMARY KEY ("id")
);
END;

CREATE UNIQUE INDEX "message_tag_idx01"
    ON "message_tag" ("uuid");

CREATE INDEX "message_tag_idx02"
    ON "message_tag" ("message_id");

CREATE INDEX "message_tag_idx03"
    ON "message_tag" ("tag_id");
