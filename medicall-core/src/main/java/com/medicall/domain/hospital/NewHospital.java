package com.medicall.domain.hospital;

import com.medicall.domain.address.Address;
import java.util.List;

public record NewHospital(
        String name,
        String telephoneNumber,
        Address address,
        String imageUrl,
        List<Long> departments
) {
}
