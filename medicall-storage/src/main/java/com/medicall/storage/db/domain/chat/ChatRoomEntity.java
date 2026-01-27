package com.medicall.storage.db.domain.chat;

import com.medicall.domain.chat.ChatRoom;
import com.medicall.storage.db.domain.appointment.AppointmentEntity;
import com.medicall.storage.db.domain.common.domain.BaseEntity;
import com.medicall.domain.common.enums.ChatRoomType;
import com.medicall.storage.db.domain.doctor.DoctorEntity;
import com.medicall.storage.db.domain.hospital.HospitalEntity;
import com.medicall.storage.db.domain.patient.PatientEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatrooms",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"appointment_id", "chat_room_type"}
                )
})
public class ChatRoomEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private HospitalEntity hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEntity appointment;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "chat_room_type", nullable = false)
    private ChatRoomType chatRoomType;

    private LocalDateTime lastMessageTime;

    public ChatRoomEntity(PatientEntity patient, HospitalEntity hospital, AppointmentEntity appointment, ChatRoomType chatRoomType) {
        this.patient = patient;
        this.hospital = hospital;
        this.doctor = null;
        this.chatRoomType = ChatRoomType.PATIENT_HOSPITAL;
        this.appointment = appointment;
    }

    public ChatRoomEntity(PatientEntity patient, DoctorEntity doctor, AppointmentEntity appointment, ChatRoomType chatRoomType, LocalDateTime lastMessageTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.hospital = null;
        this.chatRoomType = ChatRoomType.PATIENT_DOCTOR;
        this.appointment = appointment;
    }

    protected ChatRoomEntity() {}

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public ChatRoomType getChatRoomType() {
        return chatRoomType;
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

    public static ChatRoomEntity from(
            ChatRoom chatRoom,
            AppointmentEntity appointment,
            PatientEntity patient,
            DoctorEntity doctor,
            HospitalEntity hospital
    ) {
        ChatRoomEntity e = new ChatRoomEntity();
        e.appointment = appointment;
        e.patient = patient;
        e.chatRoomType = chatRoom.type();

        if (chatRoom.type() == ChatRoomType.PATIENT_DOCTOR) {
            e.doctor = doctor;
            e.hospital = null;
        } else if (chatRoom.type() == ChatRoomType.PATIENT_HOSPITAL) {
            e.hospital = hospital;
            e.doctor = null;
        }

        return e;
    }

    public ChatRoom toDomainModel() {
        return new ChatRoom(
                id,
                appointment.getId(),
                patient.getId(),
                doctor != null ? doctor.getId() : null,
                hospital != null ? hospital.getId() : null,
                chatRoomType,
                lastMessageTime
        );
    }
}
