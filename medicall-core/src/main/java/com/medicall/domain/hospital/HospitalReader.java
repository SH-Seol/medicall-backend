package com.medicall.domain.hospital;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.hospital.dto.HospitalDetailResult;
import com.medicall.domain.hospital.dto.HospitalSearchCriteria;
import com.medicall.domain.hospital.dto.HospitalSearchResult;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.support.CursorPageResult;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class HospitalReader {

    private static final double DEFAULT_RADIUS_KM = 10.0;

    private final HospitalRepository hospitalRepository;
    private final DistanceCalculator distanceCalculator;

    public HospitalReader(HospitalRepository hospitalRepository, DistanceCalculator distanceCalculator) {
        this.hospitalRepository = hospitalRepository;
        this.distanceCalculator = distanceCalculator;
    }

    public Optional<List<Appointment>> getAppointments(Long hospitalId) {
        return hospitalRepository.findAppointmentsByHospitalId(hospitalId);
    }

    public Hospital findById(Long hospitalId) {
        return hospitalRepository.findById(hospitalId).orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));
    }

    public HospitalDetailResult findByIdWithLocation(Long hospitalId, double lat, double lng) {
        Hospital hospital = hospitalRepository.findById(hospitalId).orElseThrow(() -> new CoreException(CoreErrorType.HOSPITAL_NOT_FOUND));
        double distance = distanceCalculator.calculateDistance(lat, lng, hospital.address().latitude(), hospital.address().longitude());

        return HospitalDetailResult.of(hospital, distance);
    }

    /**
     * 사용자 주소 기반 주변 병원 조회
     */
    public CursorPageResult<HospitalSearchResult> searchNearby(HospitalSearchCriteria criteria) {
        BoundingBox boundingBox = distanceCalculator.calculateBoundingBox(
                criteria.lat(),
                criteria.lng(),
                DEFAULT_RADIUS_KM
        );

        List<Hospital> candidates = hospitalRepository.findAllWithinBoundingBox(
                boundingBox,
                criteria.keyword(),
                criteria.departmentId(),
                criteria.cursorId(),
                criteria.size() + 1);

        List<HospitalSearchResult> filtered = candidates.stream()
                .map(hospital -> {
                    double distance = distanceCalculator.calculateDistance(
                            criteria.lat(), criteria.lng(),
                            hospital.address().latitude(), hospital.address().longitude()
                    );
                    return HospitalSearchResult.of(hospital, distance);
                }).filter(hwd -> hwd.distance() <= 10.0)
                .sorted(Comparator.comparingDouble(HospitalSearchResult::distance))
                .toList();

        return buildCursorPageResult(filtered, criteria.size());
    }

    public Optional<Hospital> findByOAuthInfo(String oauthId, String provider) {
        return hospitalRepository.findByOAuthInfo(oauthId, provider);
    }

    /**
     * cursor형 페이지로 병원 목록 전환
     */
    private CursorPageResult<HospitalSearchResult> buildCursorPageResult(List<HospitalSearchResult> cursorPageResult, int size) {
        boolean hasNextPage = cursorPageResult.size() > size;

        List<HospitalSearchResult> content = hasNextPage ? cursorPageResult.subList(0, size) : cursorPageResult;

        Long nextCursorId = hasNextPage && !content.isEmpty() ? content.get(content.size() - 1).id(): null;

        return CursorPageResult.of(content, nextCursorId);
    }
}
