ACCEPT passord PROMPT 'Skriv inn passordet som OKOMOD_API skal ha: ' HIDE
CREATE USER OKOMOD_API IDENTIFIED BY &passord;
GRANT CREATE SESSION TO OKOMOD_API;

--Grant SELECT access to the view that the API will use to fetch data for its responses.
GRANT SELECT ON "APPS"."XXRTV_GL_KONTERINGSINFO_V" TO "OKOMOD_API";
-- todo: legge til synonym for okomod_api på denne viewen, så slipper vi å ha med "APPS" i spørringene.

-- Grant access to the log table for the API user, so it can write logs of API calls.
GRANT SELECT ON "XXRTV"."XXRTV_OKONOMIMODELL_API_LOGG" TO "OKOMOD_API";
GRANT UPDATE ON "XXRTV"."XXRTV_OKONOMIMODELL_API_LOGG" TO "OKOMOD_API";
GRANT INSERT ON "XXRTV"."XXRTV_OKONOMIMODELL_API_LOGG" TO "OKOMOD_API";
GRANT DELETE ON "XXRTV"."XXRTV_OKONOMIMODELL_API_LOGG" TO "OKOMOD_API";
-- todo: legget til synonym for okomod_api på loggtabellen også, så slipper vi å ha med "XXRTV" i spørringene.

-- Sequences only need SELECT privileges, so we grant that to the API user.
GRANT SELECT ON xxrtv_okonomimodell_api_seq TO "OKOMOD_API";
-- todo: Skal


--Hvordan benyttes denne filen, hvis det finnes fra før hva gjør det da at den feiler?