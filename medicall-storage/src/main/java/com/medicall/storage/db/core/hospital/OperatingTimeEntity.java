package com.medicall.storage.db.core.hospital;

import com.medicall.domain.hospital.OperatingTime;
import com.medicall.storage.db.core.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
public class OperatingTimeEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital", nullable = false)
    private HospitalEntity hospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    private LocalTime breakStartTime;

    private LocalTime breakFinishTime;

    @Column(nullable = false)
    private boolean isClosed;

    protected OperatingTimeEntity() {}

    public OperatingTimeEntity(HospitalEntity hospital,
                               DayOfWeek dayOfWeek,
                               LocalTime openingTime,
                               LocalTime closingTime,
                               LocalTime breakStartTime,
                               LocalTime breakFinishTime){
        this.hospital = hospital;
        this.dayOfWeek = dayOfWeek;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.breakStartTime = breakStartTime;
        this.breakFinishTime = breakFinishTime;
        this.isClosed = false;
    }

    public HospitalEntity getHospital() {
        return hospital;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public LocalTime getBreakStartTime() {
        return breakStartTime;
    }

    public LocalTime getBreakFinishTime() {
        return breakFinishTime;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public OperatingTime toDomainModel(){
        return new OperatingTime(
                this.dayOfWeek,
                this.isClosed,
                this.openingTime,
                this.closingTime,
                this.breakStartTime,
                this.breakFinishTime
        );
    }

    public void updateFromDomainModel(OperatingTime operatingTime){
        this.openingTime = operatingTime.openingTime();
        this.closingTime = operatingTime.closingTime();
        this.breakStartTime = operatingTime.breakStartTime();
        this.breakFinishTime = operatingTime.breakFinishTime();
        this.isClosed = operatingTime.isClosed();
    }
}
