package no.nav.oebs.okonomimodell.db.repository;

import no.nav.oebs.okonomimodell.db.entity.Konteringsinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentJpaRepository extends JpaRepository<Konteringsinfo, Long> {

    @Query(value = """
            SELECT JSON_PAYLOAD
            FROM APPS.XXRTV_GL_KONTERINGSINFO_V
             """, nativeQuery = true)
    List<String> findAllAsJson();

    @Query(value = """
            SELECT JSON_PAYLOAD
            FROM APPS.XXRTV_GL_KONTERINGSINFO_V 
            WHERE segment_type = :segmentType
            """, nativeQuery = true)
    List<String> findBySegmentType(@Param("segmentType") String segmentType);
}
