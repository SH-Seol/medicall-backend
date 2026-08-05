package com.medicall.domain.prescription;

import com.medicall.domain.prescription.dto.CreatePrescriptionCommand;
import com.medicall.domain.prescription.dto.CreatePrescriptionResult;
import com.medicall.domain.prescription.dto.PatientPrescriptionListCriteria;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;
import com.medicall.domain.prescription.dto.PrescriptionListResult;
import com.medicall.support.CursorPageResult;
import com.medicall.domain.medicine.MedicineValidator;
import com.medicall.domain.treatment.Treatment;
import com.medicall.domain.treatment.TreatmentReader;
import com.medicall.domain.treatment.TreatmentValidator;
import com.medicall.error.CoreErrorType;
import com.medicall.error.CoreException;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrescriptionService {

    private final PrescriptionReader prescriptionReader;
    private final PrescriptionWriter prescriptionWriter;
    private final TreatmentReader treatmentReader;
    private final TreatmentValidator treatmentValidator;
    private final MedicineValidator medicineValidator;
    private final PrescriptionValidator prescriptionValidator;
    private final PrescriptionQrTokenStore prescriptionQrTokenStore;

    public PrescriptionService(PrescriptionReader prescriptionReader,
                               PrescriptionWriter prescriptionWriter,
                               TreatmentReader treatmentReader,
                               TreatmentValidator treatmentValidator,
                               MedicineValidator medicineValidator,
                               PrescriptionValidator prescriptionValidator,
                               PrescriptionQrTokenStore prescriptionQrTokenStore) {
        this.prescriptionReader = prescriptionReader;
        this.prescriptionWriter = prescriptionWriter;
        this.treatmentReader = treatmentReader;
        this.treatmentValidator = treatmentValidator;
        this.medicineValidator = medicineValidator;
        this.prescriptionValidator = prescriptionValidator;
        this.prescriptionQrTokenStore = prescriptionQrTokenStore;
    }

    @Transactional
    public CreatePrescriptionResult save(Long doctorId, CreatePrescriptionCommand request) {
        Treatment treatment = treatmentReader.findById(request.treatmentId());

        List<Long> medicinesId = request.medicines().stream()
                .map(pm -> pm.medicine().id())
                .toList();

        treatmentValidator.validatePrescriptionCreation(treatment, doctorId);
        medicineValidator.validateMedicines(medicinesId);


        NewPrescription newPrescription = new NewPrescription(
                treatment.patient().id(),
                request.medicines(),
                doctorId,
                treatment.id(),
                LocalDate.now()
        );

        return prescriptionWriter.save(newPrescription);
    }

    @Transactional(readOnly = true)
    public List<PrescriptionDetailResult> getPrescriptionsByPatientIdAndDoctorId(Long patientId, Long doctorId) {
        List<Prescription> prescriptions = prescriptionReader.getAllPrescriptionsByPatientIdAndDoctorId(patientId, doctorId);
        return prescriptions.stream().map(pd -> PrescriptionDetailResult.from(pd)).toList();
    }

    @Transactional(readOnly = true)
    public PrescriptionDetailResult getPrescriptionByHospital(Long prescriptionId, Long hospitalId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);
        prescriptionValidator.validateHospitalPrescription(prescription, hospitalId);

        return PrescriptionDetailResult.from(prescription);
    }

    /**
     * 환자 처방전 목록 (커서 페이지네이션)
     */
    @Transactional(readOnly = true)
    public CursorPageResult<PrescriptionListResult> getPrescriptionListByPatient(PatientPrescriptionListCriteria criteria) {
        CursorPageResult<Prescription> result = prescriptionReader.findByPatientId(criteria);

        return CursorPageResult.of(
                result.data().stream().map(PrescriptionListResult::from).toList(),
                result.nextCursorId()
        );
    }

    @Transactional(readOnly = true)
    public PrescriptionDetailResult getPrescriptionByPatient(Long prescriptionId, Long patientId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);
        prescriptionValidator.validatePatientPrescription(prescription, patientId);

        return PrescriptionDetailResult.from(prescription);
    }

    @Transactional(readOnly = true)
    public PrescriptionDetailResult getPrescriptionByDoctor(Long prescriptionId, Long doctorId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);
        prescriptionValidator.validateDoctorPrescription(prescription, doctorId);

        return PrescriptionDetailResult.from(prescription);
    }

    /**
     * 처방전 QR에 담을 1회성 토큰 발급.
     * 처방전 소유자인지 검증한 뒤 추측 불가능한 토큰을 만들어 저장한다.
     */
    public String generatePrescriptionQrToken(Long prescriptionId, Long patientId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);

        prescriptionValidator.validatePatientPrescription(prescription, patientId);

        return prescriptionQrTokenStore.issue(prescription.id());
    }

    /**
     * 약국이 QR 토큰으로 조제를 완료 처리한다.
     * 조회는 유효 시간 동안 여러 번 가능하지만 조제는 한 번만 가능하다.
     */
    @Transactional
    public PrescriptionDetailResult dispenseByQrToken(String qrToken) {
        Long prescriptionId = prescriptionQrTokenStore.resolve(qrToken)
                .orElseThrow(() -> new CoreException(CoreErrorType.PRESCRIPTION_QR_TOKEN_INVALID));

        prescriptionWriter.dispense(prescriptionId);

        return PrescriptionDetailResult.from(prescriptionReader.getPrescriptionById(prescriptionId));
    }

    /**
     * QR 토큰으로 처방전 조회 (약국에서 스캔한 경우)
     */
    @Transactional(readOnly = true)
    public PrescriptionDetailResult getPrescriptionByQrToken(String qrToken) {
        Long prescriptionId = prescriptionQrTokenStore.resolve(qrToken)
                .orElseThrow(() -> new CoreException(CoreErrorType.PRESCRIPTION_QR_TOKEN_INVALID));

        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);

        return PrescriptionDetailResult.from(prescription);
    }
}
