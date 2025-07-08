package com.raison.gbr.DAO;

import java.time.LocalDateTime;

public class SiloDAO {

    private LocalDateTime dateTime;
    private String siloName;
    private String materialName;
    private Double actualWeight;
    private Double weightAdded;
    private Double weightTaken;

    // Constructor
    public SiloDAO(LocalDateTime dateTime, String siloName, String materialName, 
                    Double actualWeight, Double weightAdded, Double weightTaken) {
        this.dateTime = dateTime;
        this.siloName = siloName;
        this.materialName = materialName;
        this.actualWeight = actualWeight;
        this.weightAdded = weightAdded;
        this.weightTaken = weightTaken;
    }

    // Getters
    public LocalDateTime getDateTime() { return dateTime; }
    public String getSiloName() { return siloName; }
    public String getMaterialName() { return materialName; }
    public Double getActualWeight() { return actualWeight; }
    public Double getWeightAdded() { return weightAdded; }
    public Double getWeightTaken() { return weightTaken; }


}
