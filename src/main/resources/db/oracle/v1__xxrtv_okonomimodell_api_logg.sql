-------------------------------------------------------------------------------
-- Skjema            : OEBS_API
-- Tabell            : XXRTV_OKONOMIMODELL_API_LOGG
-- Kildefil          : V1__xxrtv_okonomimodell_api_logg.sql
-- Beskrivelse       : Loggtabell for API-kall.
-------------------------------------------------------------------------------

-- Oppretter selve loggtabellen i xxrtv-skjemaet.
-- Tabellen lagrer ett rad per API-kall (inn eller ut) med metadata og payload.
-- Tabellen er partisjonert på tidspunkt slik at gamle data kan håndteres effektivt.
CREATE TABLE xxrtv.xxrtv_okonomimodell_api_logg(
    kall_logg_id        NUMBER        NOT NULL  -- Unik ID for hvert logginnslag, settes automatisk av trigger
    , korrelasjon_id      VARCHAR2(50)  NOT NULL  -- Korrelasjon-ID som knytter forespørsel og svar sammen på tvers av tjenester
    , tidspunkt           TIMESTAMP(9)  NOT NULL  -- Tidspunkt for kallet, med nanosekund-presisjon
    , type                VARCHAR2(10)  NOT NULL  -- Type kall: 'PLSQL' eller 'REST'
    , kall_retning        VARCHAR2(10)  NOT NULL  -- Retning på kallet: 'INN' (innkommende) eller 'UT' (utgående)
    , method              VARCHAR2(10)            -- HTTP-metode, f.eks. GET, POST (kun relevant for REST-kall)
    , operation           VARCHAR2(100) NOT NULL  -- Navn på operasjonen/endepunktet som ble kalt
    , status              NUMBER                  -- HTTP-statuskode eller feilkode fra kallet
    , kalltid             NUMBER        NOT NULL  -- Tid brukt på kallet i millisekunder
    , request             CLOB                    -- Innholdet i forespørselen (kan være stort, derfor CLOB)
    , response            CLOB                    -- Innholdet i svaret (kan være stort, derfor CLOB)
    , logginfo            CLOB                    -- Ekstra logginformasjon, f.eks. feilmeldinger
    , CONSTRAINT xxrtv_kalo_okonomimodell_api_pk PRIMARY KEY(kall_logg_id))  -- Primærnøkkel på kall_logg_id
    -- Partisjonering på tidspunkt: tabellen deles automatisk opp i månedlige partisjoner.
    -- Dette gjør det enklere å slette gamle data og forbedrer spørringsytelse.
    PARTITION BY RANGE(tidspunkt)
INTERVAL(NUMTOYMINTERVAL(1, 'MONTH'))
( PARTITION kall_logg_data_p1 VALUES LESS THAN( DATE '2021-09-01'));

-- Indeks på status-kolonnen for rask oppslag på HTTP-statuskoder (LOCAL = per partisjon).
CREATE INDEX xxrtv_kalo_okonomimodell_api_U3 ON xxrtv_okonomimodell_api_logg
    (status) LOCAL;

-- Synonym slik at tabellen kan refereres uten skjemaprefix (xxrtv.) i spørringer.
create synonym xxrtv_okonomimodell_api_logg for xxrtv.xxrtv_okonomimodell_api_logg;

-- Sekvens som genererer unike, stigende tall for kall_logg_id.
-- Brukes av triggeren nedenfor til å sette ID automatisk ved INSERT.
CREATE  SEQUENCE xxrtv_okonomimodell_api_seq
    START WITH 1
    INCREMENT BY 1;

-- Trigger som kjører FØR hver INSERT på tabellen.
-- Henter neste verdi fra sekvensen og setter den som kall_logg_id,
-- slik at man ikke trenger å sende inn ID manuelt ved INSERT.
CREATE OR REPLACE TRIGGER xxrtv_kalo_okonomimodell_api_bir
  BEFORE INSERT ON xxrtv_okonomimodell_api_logg
  FOR EACH ROW
BEGIN
SELECT xxrtv_okonomimodell_api_seq.nextval
INTO   :new.kall_logg_id
FROM   DUAL;
END;

-- Check-constraint som sørger for at type kun kan være 'PLSQL' eller 'REST'.
ALTER TABLE xxrtv_okonomimodell_api_logg
    ADD (CONSTRAINT xxrtv_type_ck1 CHECK (type IN ('PLSQL', 'REST')));

-- Check-constraint som sørger for at kall_retning kun kan være 'INN' eller 'UT'.
ALTER TABLE xxrtv_okonomimodell_api_logg
    ADD (CONSTRAINT xxrtv_kall_retning_ck1 CHECK (kall_retning IN ('INN','UT')));

-- Indeks på operation og kall_retning for rask filtrering på endepunkt og retning.
CREATE INDEX xxrtv_kalo_U1 ON xxrtv_okonomimodell_api_logg
    (operation, kall_retning);

-- Indeks på korrelasjon_id for rask oppslag når man skal finne alle logginnslag for ett kall.
CREATE INDEX xxrtv_kalo_U2 ON xxrtv_okonomimodell_api_logg
    (korrelasjon_id);