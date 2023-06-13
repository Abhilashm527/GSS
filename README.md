# Global Scheduler 

Need to add the these 2 table  to use the service:

Shcedule table :
CREATE TABLE IF NOT EXISTS public.schedule
(
    scheduleid integer NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    target character varying(255) COLLATE pg_catalog."default",
    active boolean,
    targettype character varying(255) COLLATE pg_catalog."default",
    cron_expression character varying(255) COLLATE pg_catalog."default",
    zoneid character varying(255) COLLATE pg_catalog."default",
    createdtime timestamp without time zone,
    lastrun timestamp without time zone,
    nextrun timestamp without time zone,
    state character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT schedule_pkey PRIMARY KEY (scheduleid)
)

Schedule History table :

CREATE TABLE IF NOT EXISTS public.schedule_history
(
    schedulehistoryid integer NOT NULL,
    scheduleid integer NOT NULL,
    target character varying(255) COLLATE pg_catalog."default",
    active boolean,
    targettype character varying(255) COLLATE pg_catalog."default",
    lastrun timestamp without time zone,
    lastrunstate character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT schedulehistoryid_pkey PRIMARY KEY (schedulehistoryid)
)
