package de.riedeldev.dcmdc.core.model;

import lombok.Data;

@Data
public class ClientStatus {

    private String uuid;

    private Float cpuUsage;
    private Float memoryUsage;
    private Float maxMemory;

    private Long timestamp;

}
