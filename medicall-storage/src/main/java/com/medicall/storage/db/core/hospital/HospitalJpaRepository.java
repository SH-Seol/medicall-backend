package com.medicall.storage.db.core.hospital;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalJpaRepository extends JpaRepository<HospitalEntity, Long> {
    @Query(value = """
        SELECT DISTINCT h FROM HospitalEntity h
        LEFT JOIN FETCH h.departments hd
        LEFT JOIN FETCH hd.department
        LEFT JOIN FETCH h.operatingTimes
        WHERE h.id = :id
        """)
    Optional<HospitalEntity> findByIdWithDetails(@Param("id") Long id);
    List<HospitalEntity> findByNameIgnoreCase(String keyword);
    Optional<HospitalEntity> findByOauthIdAndOauthProvider(String oAuthId, String provider);
}
