package au.com.aeloy.votolab;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableRobot.class)
@JsonDeserialize(as = ImmutableRobot.class)
abstract class Robot {
    @Value.Parameter
    @JsonProperty("name")
    abstract String name();
}
