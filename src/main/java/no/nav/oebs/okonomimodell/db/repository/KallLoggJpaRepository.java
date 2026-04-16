package no.nav.oebs.okonomimodell.db.repository;

import no.nav.oebs.okonomimodell.db.entity.KallLogg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KallLoggJpaRepository extends JpaRepository<KallLogg, Long> {

}
