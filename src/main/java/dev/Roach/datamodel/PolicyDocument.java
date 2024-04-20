package dev.Roach.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PolicyDocument {
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Statement")
    private List<Statement> statement;
}
