package dev.Roach.datamodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.Roach.deserializers.ConditionDeserializer;
import dev.Roach.deserializers.GeneralListOrStringOrMapDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class Statement {
    @JsonProperty("Sid")
    private String sid;
    @JsonProperty("Effect")
    private String effect;

    @JsonProperty("Action")
    @JsonDeserialize(using = GeneralListOrStringOrMapDeserializer.class)
    private Object action;

    @JsonProperty("NotAction")
    @JsonDeserialize(using = GeneralListOrStringOrMapDeserializer.class)
    private Object notAction;

    @JsonProperty("Resource")
    @JsonDeserialize(using = GeneralListOrStringOrMapDeserializer.class)
    private Object resource;

    @JsonProperty("NotResource")
    @JsonDeserialize(using = GeneralListOrStringOrMapDeserializer.class)
    private Object notResource;

    @JsonProperty("Condition")
    @JsonDeserialize(using = ConditionDeserializer.class)
    private Map<String, Object> condition;

    @JsonProperty("Principal")
    @JsonDeserialize(using = GeneralListOrStringOrMapDeserializer.class)
    private Object principal;

    @JsonProperty("NotPrincipal")
    @JsonDeserialize(using = GeneralListOrStringOrMapDeserializer.class)
    private Object notPrincipal;
}
