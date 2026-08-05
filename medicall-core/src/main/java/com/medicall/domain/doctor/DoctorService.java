package com.medicall.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.department.Department;
import com.medicall.domain.department.DepartmentReader;
import com.medicall.domain.department.Specialty;
import com.medicall.domain.department.SpecialtyReader;
import com.medicall.domain.doctor.dto.DoctorProfileUpdate;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.domain.doctor.dto.DoctorResult;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {

    private final DoctorWriter doctorWriter;
    private final DoctorReader doctorReader;
    private final DepartmentReader departmentReader;
    private final SpecialtyReader specialtyReader;

    public DoctorService(DoctorWriter doctorWriter, DoctorReader doctorReader,
                         DepartmentReader departmentReader, SpecialtyReader specialtyReader) {
        this.doctorWriter = doctorWriter;
        this.doctorReader = doctorReader;
        this.departmentReader = departmentReader;
        this.specialtyReader = specialtyReader;
    }

    /**
     * 소셜 로그인 시 기존 회원을 찾고 없으면 생성한다.
     * 동시 요청으로 유니크 제약에 걸리면 한 번 더 조회해 정상 로그인으로 이어지게 한다.
     * (생성이 독립 트랜잭션에서 롤백되도록 이 메서드에는 트랜잭션을 걸지 않는다)
     */
    public Doctor findOrCreateByOAuth(Doctor newDoctor){
        return doctorReader.findByOAuthInfo(newDoctor.oauthId(), newDoctor.provider())
                .orElseGet(() -> createOnConflictRetry(newDoctor));
    }

    private Doctor createOnConflictRetry(Doctor newDoctor){
        try{
            return doctorWriter.createDoctor(newDoctor);
        }catch (DataIntegrityViolationException e){
            return doctorReader.findByOAuthInfo(newDoctor.oauthId(), newDoctor.provider())
                    .orElseThrow(() -> new CoreException(CoreErrorType.SIGNUP_CONFLICT, e));
        }
    }

    @Transactional
    public Doctor registerDepartment(NewDoctor newDoctor){
        Department department = departmentReader.findById(newDoctor.departmentId());
        Doctor doctorToCreate = new Doctor(newDoctor.name(), newDoctor.introduction(), null, newDoctor.imageUrl(), department, null, null);
        return doctorWriter.createDoctor(doctorToCreate);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getDoctorAppointments(Doctor doctor){
        return doctorReader.getDoctorAppointments(doctor);
    }

    /**
     * 의사 내 정보 수정 (null인 항목은 기존 값 유지)
     */
    @Transactional
    public DoctorResult updateMyProfile(Long doctorId, DoctorProfileUpdate profileUpdate){
        if(profileUpdate.departmentId() != null){
            departmentReader.findById(profileUpdate.departmentId());
        }
        if(profileUpdate.specialtyId() != null){
            validateSpecialtyBelongsToDepartment(doctorId, profileUpdate);
        }
        Doctor doctor = doctorWriter.updateProfile(doctorId, profileUpdate);

        return DoctorResult.from(doctor);
    }

    /**
     * 병원 소속 의사 목록
     */
    @Transactional(readOnly = true)
    public List<DoctorResult> getDoctorsByHospital(Long hospitalId){
        return doctorReader.findAllByHospitalId(hospitalId).stream()
                .map(DoctorResult::from)
                .toList();
    }

    /**
     * 세부 전공은 반드시 선택한(또는 기존) 진료과에 속해야 한다.
     */
    private void validateSpecialtyBelongsToDepartment(Long doctorId, DoctorProfileUpdate profileUpdate){
        Specialty specialty = specialtyReader.findById(profileUpdate.specialtyId());

        Long departmentId = profileUpdate.departmentId() != null
                ? profileUpdate.departmentId()
                : doctorReader.findById(doctorId).department() != null
                        ? doctorReader.findById(doctorId).department().id()
                        : null;

        if(!specialty.departmentId().equals(departmentId)){
            throw new CoreException(CoreErrorType.SPECIALTY_DEPARTMENT_MISMATCH);
        }
    }

    @Transactional(readOnly = true)
    public DoctorResult findById(Long doctorId){
        Doctor doctor =  doctorReader.findById(doctorId);

        return DoctorResult.from(doctor);
    }
}
