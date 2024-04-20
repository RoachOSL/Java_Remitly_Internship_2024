package dev.Roach.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Statement {
    @JsonProperty("Sid")
    private String sid;
    @JsonProperty("Effect")
    private String effect;
    @JsonProperty("Action")
    private List<String> action;
    @JsonProperty("Resource")
    private String resource;
}
