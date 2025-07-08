package com.raison.gbr.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseSilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Sl.No")
    private Integer id;

    @Column(name = "Date_Time")
    private LocalDateTime dateTime;

    @Column(name = "Silo Name")
    private String siloName;

    @Column(name = "Material Name")
    private String materialName;

    @Column(name = "Intake")
    private Double intake;

    @Column(name = "Destination Bin")
    private Double destinationBin;

    @Column(name = "Actual Weight")
    private Double actualWeight;

    @Column(name = "Weight Added")
    private Double weightAdded;

    @Column(name = "Weight Taken")
    private Double weightTaken;

    // Getters and Setters
    public LocalDateTime getDateTime() { return dateTime; }
    public String getSiloName() { return siloName; }
    public String getMaterialName() { return materialName; }
    public Double getIntake() { return intake; }  
    public Double getDestinationBin() { return destinationBin; }
    public Double getActualWeight() { return actualWeight; }
    public Double getWeightAdded() { return weightAdded; }
    public Double getWeightTaken() { return weightTaken; }
}
