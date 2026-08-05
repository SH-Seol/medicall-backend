package com.medicall.domain.common.enums;

public enum AppointmentStatus {
    /** 환자가 요청함 (의사 미배정 가능) */
    REQUESTED,
    /** 병원이 수락하고 의사가 배정됨 */
    ASSIGNED,
    /** 의사가 환자에게 출발함 (이 시점부터 위치 공유) */
    EN_ROUTE,
    /** 의사가 환자 위치에 도착함 */
    ARRIVED,
    /** 진료 중 */
    IN_PROGRESS,
    /** 진료 완료 */
    COMPLETED,
    /** 환자가 취소함 */
    CANCELLED,
    /** 병원이 거절함 */
    REJECTED;

    /**
     * 방문이 아직 진행 중인 상태. (예약 슬롯을 점유한다)
     */
    public boolean isActive() {
        return this == REQUESTED || this == ASSIGNED || this == EN_ROUTE
                || this == ARRIVED || this == IN_PROGRESS;
    }

    /**
     * 의사가 출발하기 전까지만 환자가 취소할 수 있다.
     */
    public boolean isCancelable() {
        return this == REQUESTED || this == ASSIGNED;
    }
}
