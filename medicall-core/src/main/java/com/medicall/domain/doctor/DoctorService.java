package com.medicall.domain.doctor;

import com.medicall.domain.appointment.Appointment;
import com.medicall.domain.department.Department;
import com.medicall.domain.department.DepartmentReader;
import com.medicall.domain.doctor.dto.DoctorResult;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DoctorService {

    private final DoctorWriter doctorWriter;
    private final DoctorReader doctorReader;
    private final DepartmentReader departmentReader;

    public DoctorService(DoctorWriter doctorWriter, DoctorReader doctorReader, DepartmentReader departmentReader) {
        this.doctorWriter = doctorWriter;
        this.doctorReader = doctorReader;
        this.departmentReader = departmentReader;
    }

    public Doctor registerDepartment(NewDoctor newDoctor){
        Department department = departmentReader.findById(newDoctor.departmentId());
        Doctor doctorToCreate = new Doctor(newDoctor.name(), newDoctor.introduction(), null, newDoctor.imageUrl(), department, null, null);
        return doctorWriter.createDoctor(doctorToCreate);
    }

    public List<Appointment> getDoctorAppointments(Doctor doctor){
        return doctorReader.getDoctorAppointments(doctor);
    }

    public DoctorResult findById(Long doctorId){
        Doctor doctor =  doctorReader.findById(doctorId);

        return DoctorResult.from(doctor);
    }
}
