package com.medicall.domain.patient;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicall.domain.patient.dto.PatientDetailResult;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;
import com.medicall.domain.patient.dto.PatientProfileUpdate;

@Service
public class PatientService {

    private final PatientReader patientReader;
    private final PatientWriter patientWriter;

    public PatientService(PatientReader patientReader, PatientWriter patientWriter) {
        this.patientReader = patientReader;
        this.patientWriter = patientWriter;
    }

    /**
     * 소셜 로그인 시 기존 회원을 찾고 없으면 생성한다.
     * 조회와 생성 사이에 같은 계정으로 동시 요청이 들어오면 유니크 제약에 걸리므로,
     * 충돌 시 한 번 더 조회해 정상 로그인으로 이어지게 한다.
     * (생성이 독립 트랜잭션에서 롤백되도록 이 메서드에는 트랜잭션을 걸지 않는다)
     */
    public Patient findOrCreateByOAuth(NewPatient newPatient){
        return patientReader.findByOAuthInfo(newPatient.oauthId(), newPatient.provider())
                .orElseGet(() -> createOnConflictRetry(newPatient));
    }

    private Patient createOnConflictRetry(NewPatient newPatient){
        try{
            return patientWriter.create(newPatient);
        }catch (DataIntegrityViolationException e){
            return patientReader.findByOAuthInfo(newPatient.oauthId(), newPatient.provider())
                    .orElseThrow(() -> new CoreException(CoreErrorType.SIGNUP_CONFLICT, e));
        }
    }

    @Transactional(readOnly = true)
    public PatientDetailResult findById(Long patientId){
        return patientReader.findById(patientId);
    }

    /**
     * 환자 내 정보 수정 (null인 항목은 기존 값 유지)
     */
    @Transactional
    public PatientDetailResult updateMyProfile(Long patientId, PatientProfileUpdate profileUpdate){
        Patient patient = patientWriter.updateProfile(patientId, profileUpdate);

        return PatientDetailResult.from(patient);
    }
}
