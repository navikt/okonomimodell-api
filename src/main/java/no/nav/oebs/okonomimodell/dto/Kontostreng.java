package no.nav.oebs.okonomimodell.dto;

public record Kontostreng(
        String artskonto,
        String kostnadssted,
        String produkt,
        String oppgave,
        String felles,
        String statskonto,
        String kilde,
        String tilsagnsaar,
        String frittfelt1,
        String frittfelt2,
        String fullmaktskode,
        String regnskapsforer
) {

    private static final String TWO_ZEROS = "00";
    private static final String SIX_ZEROS = "000000";
    private static final String TWELVE_ZEROS = "000000000000";

    private static final Kontostreng DEFAULT_KONTOSTRENG = new Kontostreng(
            TWELVE_ZEROS,
            SIX_ZEROS,
            SIX_ZEROS,
            SIX_ZEROS,
            SIX_ZEROS,
            TWELVE_ZEROS,
            SIX_ZEROS,
            SIX_ZEROS,
            SIX_ZEROS,
            SIX_ZEROS,
            TWO_ZEROS,
            TWO_ZEROS);

    public static Kontostreng of(String artskonto,
                                 String kostnadssted,
                                 String produkt,
                                 String oppgave,
                                 String felles,
                                 String statskonto,
                                 String kilde,
                                 String tilsagnsaar,
                                 String frittfelt1,
                                 String frittfelt2,
                                 String fullmaktskode,
                                 String regnskapsforer){
       return new Kontostreng(
                artskonto != null ? artskonto : DEFAULT_KONTOSTRENG.artskonto(),
                kostnadssted != null ? kostnadssted : DEFAULT_KONTOSTRENG.kostnadssted(),
                produkt != null ? produkt : DEFAULT_KONTOSTRENG.produkt(),
                oppgave != null ? oppgave : DEFAULT_KONTOSTRENG.oppgave(),
                felles != null ? felles : DEFAULT_KONTOSTRENG.felles(),
                statskonto != null ? statskonto : DEFAULT_KONTOSTRENG.statskonto(),
                kilde != null ? kilde : DEFAULT_KONTOSTRENG.kilde(),
                tilsagnsaar != null ? tilsagnsaar : DEFAULT_KONTOSTRENG.tilsagnsaar(),
                frittfelt1 != null ? frittfelt1 : DEFAULT_KONTOSTRENG.frittfelt1(),
                frittfelt2 != null ? frittfelt2 : DEFAULT_KONTOSTRENG.frittfelt2(),
                fullmaktskode != null ? fullmaktskode : DEFAULT_KONTOSTRENG.fullmaktskode(),
                regnskapsforer != null ? regnskapsforer : DEFAULT_KONTOSTRENG.regnskapsforer()
        );
    }
}