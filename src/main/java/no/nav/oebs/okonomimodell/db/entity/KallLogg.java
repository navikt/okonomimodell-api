package no.nav.oebs.okonomimodell.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "XXRTV_OKONOMIMODELL_API_LOGG", schema = "XXRTV")
public class KallLogg {

	public static final String RETNING_INN = "INN";
	public static final String RETNING_UT = "UT";

	public static final String TYPE_PLSQL = "PLSQL";
	public static final String TYPE_REST = "REST";
	@Id
	@SequenceGenerator(name = "XXRTV_OKONOMIMODELL_API_SEQ", sequenceName = "XXRTV_OKONOMIMODELL_API_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XXRTV_OKONOMIMODELL_API_SEQ")
	@Column(name = "KALL_LOGG_ID")
	private Long id;

	@Column(name = "KORRELASJON_ID")
	private String korrelasjonId;

	@Column(name = "TIDSPUNKT")
	private LocalDateTime tidspunkt;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "KALL_RETNING")
	private String kallRetning;

	@Column(name = "METHOD")
	private String method;

	@Column(name = "OPERATION")
	private String operation;

	@Column(name = "STATUS")
	private Integer status;

	// Brukes for beregning av kalltid.
	@Transient
	private long startTid;

	@Column(name = "KALLTID")
	private Long kalltid;

	@Column(name = "REQUEST")
	private String request;

	@Column(name = "RESPONSE")
	private String response;

	@Column(name = "RESPONSE_OEBS")
	private String responseOebs;

	@Column(name = "LOGGINFO")
	private String logginfo;
}
