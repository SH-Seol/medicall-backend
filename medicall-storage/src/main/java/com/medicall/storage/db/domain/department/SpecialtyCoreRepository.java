package com.medicall.storage.db.domain.department;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.medicall.domain.department.Specialty;
import com.medicall.domain.department.SpecialtyRepository;

@Repository
public class SpecialtyCoreRepository implements SpecialtyRepository {

    private final SpecialtyJpaRepository specialtyJpaRepository;

    public SpecialtyCoreRepository(SpecialtyJpaRepository specialtyJpaRepository) {
        this.specialtyJpaRepository = specialtyJpaRepository;
    }

    public Optional<Specialty> findById(Long id) {
        return specialtyJpaRepository.findByIdWithDepartment(id).map(SpecialtyEntity::toDomainModel);
    }

    public List<Specialty> findAllByDepartmentId(Long departmentId) {
        return specialtyJpaRepository.findAllByDepartmentIdWithDepartment(departmentId).stream()
                .map(SpecialtyEntity::toDomainModel)
                .toList();
    }
}
