package com.medicall.storage.db.core.department;

import com.medicall.domain.department.Department;
import com.medicall.domain.department.DepartmentRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentCoreRepository implements DepartmentRepository {

    private final DepartmentJpaRepository departmentJpaRepository;

    public DepartmentCoreRepository(DepartmentJpaRepository jpaRepository) {
        this.departmentJpaRepository = jpaRepository;
    }

    public Optional<Department> findById(Long id){
        return departmentJpaRepository.findById(id).map(this::toDomainModel);
    }

    private Department toDomainModel(DepartmentEntity departmentEntity){
        return new Department(departmentEntity.getId(), departmentEntity.getName());
    }
}
