package com.medicall.storage.db.domain.hospital;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HospitalJpaRepository extends JpaRepository<HospitalEntity, Long> {

    /**
     * 진료과와 업무 시간을 한 쿼리에서 함께 fetch join 하면 MultipleBagFetchException이 발생하므로
     * 두 번에 나눠 조회한다. 같은 트랜잭션(영속성 컨텍스트)에서 실행되어 동일 엔티티에 채워진다.
     */
    @Transactional(readOnly = true)
    default Optional<HospitalEntity> findByIdWithDetails(Long id) {
        Optional<HospitalEntity> hospital = findByIdWithDepartments(id);
        hospital.ifPresent(found -> findByIdWithOperatingTimes(id));

        return hospital;
    }

    @Query(value = """
        SELECT DISTINCT h FROM HospitalEntity h
        LEFT JOIN FETCH h.address
        LEFT JOIN FETCH h.departments hd
        LEFT JOIN FETCH hd.department
        WHERE h.id = :id
        """)
    Optional<HospitalEntity> findByIdWithDepartments(@Param("id") Long id);

    @Query(value = """
        SELECT DISTINCT h FROM HospitalEntity h
        LEFT JOIN FETCH h.operatingTimes
        WHERE h.id = :id
        """)
    Optional<HospitalEntity> findByIdWithOperatingTimes(@Param("id") Long id);

    List<HospitalEntity> findByNameIgnoreCase(String keyword);
    Optional<HospitalEntity> findByOauthIdAndOauthProvider(String oAuthId, String provider);
}
