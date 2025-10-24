package com.medicall.domain.prescription;

import com.medicall.domain.prescription.dto.CreatePrescriptionCommand;
import com.medicall.domain.prescription.dto.PatientPrescriptionListCriteria;
import com.medicall.domain.prescription.dto.PrescriptionDetailResult;
import com.medicall.domain.medicine.MedicineValidator;
import com.medicall.domain.prescription.dto.PrescriptionListResult;
import com.medicall.domain.treatment.Treatment;
import com.medicall.domain.treatment.TreatmentReader;
import com.medicall.domain.treatment.TreatmentValidator;
import com.medicall.support.CursorPageResult;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PrescriptionService {

    private final PrescriptionReader prescriptionReader;
    private final PrescriptionWriter prescriptionWriter;
    private final TreatmentReader treatmentReader;
    private final TreatmentValidator treatmentValidator;
    private final MedicineValidator medicineValidator;
    private final PrescriptionValidator prescriptionValidator;

    public PrescriptionService(PrescriptionReader prescriptionReader,
                               PrescriptionWriter prescriptionWriter,
                               TreatmentReader treatmentReader,
                               TreatmentValidator treatmentValidator,
                               MedicineValidator medicineValidator,
                               PrescriptionValidator prescriptionValidator) {
        this.prescriptionReader = prescriptionReader;
        this.prescriptionWriter = prescriptionWriter;
        this.treatmentReader = treatmentReader;
        this.treatmentValidator = treatmentValidator;
        this.medicineValidator = medicineValidator;
        this.prescriptionValidator = prescriptionValidator;
    }

    public Long save(Long doctorId, CreatePrescriptionCommand request) {
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

    public List<PrescriptionDetailResult> getPrescriptionsByPatientIdAndDoctorId(Long patientId, Long doctorId) {
        List<Prescription> prescriptions = prescriptionReader.getAllPrescriptionsByPatientIdAndDoctorId(patientId, doctorId);
        return prescriptions.stream().map(pd -> new PrescriptionDetailResult(
                pd.id(),
                pd.patient(),
                pd.medicines(),
                pd.hospital(),
                pd.doctor(),
                pd.treatment(),
                pd.date()
        )).toList();
    }

    public PrescriptionDetailResult getPrescriptionByHospital(Long prescriptionId, Long hospitalId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);
        prescriptionValidator.validateHospitalPrescription(prescription, hospitalId);

        return new PrescriptionDetailResult(
                prescription.id(),
                prescription.patient(),
                prescription.medicines(),
                prescription.hospital(),
                prescription.doctor(),
                prescription.treatment(),
                prescription.date()
        );
    }

    public PrescriptionDetailResult getPrescriptionByPatient(Long prescriptionId, Long patientId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);
        prescriptionValidator.validatePatientPrescription(prescription, patientId);

        return new PrescriptionDetailResult(
                prescription.id(),
                prescription.patient(),
                prescription.medicines(),
                prescription.hospital(),
                prescription.doctor(),
                prescription.treatment(),
                prescription.date()
        );
    }

    public PrescriptionDetailResult getPrescriptionByDoctor(Long prescriptionId, Long doctorId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);
        prescriptionValidator.validateDoctorPrescription(prescription, doctorId);

        return new PrescriptionDetailResult(
                prescription.id(),
                prescription.patient(),
                prescription.medicines(),
                prescription.hospital(),
                prescription.doctor(),
                prescription.treatment(),
                prescription.date()
        );
    }

    public String generatePrescriptionQrToken(Long prescriptionId, Long patientId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);

        prescriptionValidator.validatePatientPrescription(prescription, patientId);

        return null;
    }
}
