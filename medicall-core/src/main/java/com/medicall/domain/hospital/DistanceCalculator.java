package com.medicall.domain.hospital;

import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

    private static final double EARTH_RADIUS = 6378.137;

    /**
     * Haversine 공식으로 두 좌표 간 거리 계산 (km)
     */
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * Bounding Box 계산
     * 성능을 위해 DB에서 먼저 필터링
     */
    public BoundingBox calculateBoundingBox(double centerLat, double centerLng, double radiusKm) {
        double latDelta =  radiusKm/ 111.0;
        double lngDelta =  radiusKm / (111.0 * Math.cos(Math.toRadians(centerLat)));

        return new BoundingBox(
                centerLat - latDelta,
                centerLat + latDelta,
                centerLng - lngDelta,
                centerLng +lngDelta);
    }
}
