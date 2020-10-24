package de.riedeldev.dcmdc.core.model.requests;

import java.util.List;

import lombok.Data;

@Data
public final class CreateContainerAnswer {
    public String Id;
    public List<String> Warnings;

}
