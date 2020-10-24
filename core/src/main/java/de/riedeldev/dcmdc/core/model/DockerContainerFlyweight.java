package de.riedeldev.dcmdc.core.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
@SuppressWarnings("unchecked")
public class DockerContainerFlyweight {

    public DockerContainerFlyweight(Map<String, Object> inspectConfiguration) {
        this.configuration = inspectConfiguration;
    }

    private Map<String, Object> configuration;

    public List<String> getNames() {
        return (List<String>) configuration.get("Names");
    }

    public String getId() {
        return (String) configuration.get("Id");
    }

    public String getImage() {
        return (String) configuration.get("Image");
    }

}
