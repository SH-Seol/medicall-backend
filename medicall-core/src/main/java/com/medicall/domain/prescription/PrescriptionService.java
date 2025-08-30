package com.medicall.domain.prescription;

import com.medicall.domain.medicine.MedicineValidator;
import com.medicall.domain.prescription.dto.CreatePrescriptionRequest;
import com.medicall.domain.prescription.dto.ReadPrescriptionResponse;
import com.medicall.domain.treatment.Treatment;
import com.medicall.domain.treatment.TreatmentReader;
import com.medicall.domain.treatment.TreatmentValidator;
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

    public PrescriptionService(PrescriptionReader prescriptionReader,
                               PrescriptionWriter prescriptionWriter,
                               TreatmentReader treatmentReader,
                               TreatmentValidator treatmentValidator,
                               MedicineValidator medicineValidator) {
        this.prescriptionReader = prescriptionReader;
        this.prescriptionWriter = prescriptionWriter;
        this.treatmentReader = treatmentReader;
        this.treatmentValidator = treatmentValidator;
        this.medicineValidator = medicineValidator;
    }

    public Long save(Long doctorId, CreatePrescriptionRequest request) {
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

    public List<ReadPrescriptionResponse> getPrescriptionsByPatientIdAndDoctorId(Long patientId, Long doctorId) {
        List<Prescription> prescriptions = prescriptionReader.getAllPrescriptionsByPatientIdAndDoctorId(patientId, doctorId);
        return prescriptions.stream().map(pd -> new ReadPrescriptionResponse(
                pd.id(),
                pd.patient(),
                pd.medicines(),
                pd.hospital(),
                pd.doctor(),
                pd.treatment(),
                pd.date()
        )).toList();
    }

    public ReadPrescriptionResponse getPrescriptionById(Long prescriptionId) {
        Prescription prescription = prescriptionReader.getPrescriptionById(prescriptionId);

        return new ReadPrescriptionResponse(
                prescription.id(),
                prescription.patient(),
                prescription.medicines(),
                prescription.hospital(),
                prescription.doctor(),
                prescription.treatment(),
                prescription.date()
        );
    }
}
