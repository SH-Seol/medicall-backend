package com.medicall.domain.hospital;

public record BoundingBox(
        Double minLat,
        Double maxLat,
        Double minLon,
        Double maxLon
) {
}
