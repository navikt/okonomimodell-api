package no.nav.oebs.okonomimodell.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "XXRTV_GL_KONTERINGSINFO_V", schema = "APPS")
public class Konteringsinfo {

    @Id
    @Column(name= "SEGMENT_VERDI")
    private String segmentVerdi;

    @Column(name = "SEGMENT_TYPE")
    private String segmentType;

    @Column(name = "SYSTEM")
    private String system;

    @Column(name = "JSON_PAYLOAD", columnDefinition = "CLOB")
    private String jsonPayload;
}
