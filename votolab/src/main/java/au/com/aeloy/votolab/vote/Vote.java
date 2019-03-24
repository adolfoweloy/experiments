package au.com.aeloy.votolab.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableVote.class)
@JsonDeserialize(as = ImmutableVote.class)
public interface Vote {
    @JsonProperty("subject") @Value.Parameter
    String subject();

    @JsonProperty("score") @Value.Parameter
    String score();
}
