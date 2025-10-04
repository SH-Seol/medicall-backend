package com.medicall.storage.db.domain.chat;

import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.storage.db.domain.common.enums.ChatRoomType;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
import com.medicall.storage.db.domain.patient.PatientEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatRoom extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private HospitalEntity hospital;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<ChatMessage> messages = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomType chatRoomType;

    private LocalDateTime lastMessageTime;

    public ChatRoom (PatientEntity patient, HospitalEntity hospital) {
        this.patient = patient;
        this.hospital = hospital;
        this.doctor = null;
        this.chatRoomType = ChatRoomType.PATIENT_HOSPITAL;
    }

    public ChatRoom (PatientEntity patient, DoctorEntity doctor) {
        this.patient = patient;
        this.doctor = doctor;
        this.hospital = null;
        this.chatRoomType = ChatRoomType.PATIENT_DOCTOR;
    }

    protected ChatRoom() {}

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public ChatRoomType getChatRoomType() {
        return chatRoomType;
    }

    public List<ChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    public HospitalEntity getHospital() {
        return hospital;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public PatientEntity getPatient() {
        return patient;
    }
}
