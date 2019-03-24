package au.com.aeloy.votolab;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * This test was created to do some tests with Jackson and Immutables together.
 * JacksonTests can be used to perform a lot of other different experiments with Jackson and Immutables.
 */
public class JacksonTests {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializationOfImmutableObject() {
        ImmutableRobot gary = ImmutableRobot.of("gary");

        StringWriter stringWriter = new StringWriter();

        try {
            objectMapper.writer().writeValue(stringWriter, gary);
            String result = stringWriter.getBuffer().toString();
            assertThat(result, is(equalTo("{\"name\":\"gary\"}")));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeserializationOfImmutableObject() {
        String input = "{\"name\":\"gary\"}";

        try {
            Object result = objectMapper.reader()
                    .forType(Robot.class)
                    .readValue(input);
            assertThat(result, is(instanceOf(Robot.class)));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
