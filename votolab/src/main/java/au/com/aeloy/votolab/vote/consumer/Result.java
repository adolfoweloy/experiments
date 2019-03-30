package au.com.aeloy.votolab.vote.consumer;

import au.com.aeloy.votolab.vote.Vote;
import org.immutables.value.Value;

@Value.Immutable
public interface Result {
    @Value.Parameter Vote vote();
    @Value.Parameter String messageId();
}
