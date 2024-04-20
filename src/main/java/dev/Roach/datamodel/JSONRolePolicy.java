package dev.Roach.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JSONRolePolicy {
    @JsonProperty("PolicyName")
    private String policyName;
    @JsonProperty("PolicyDocument")
    private PolicyDocument policyDocument;
}
