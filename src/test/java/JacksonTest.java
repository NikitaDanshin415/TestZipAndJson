import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pojo.UserPojo;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTest {
    @Test
    void testJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/test/resources/jacksontest.json");
        UserPojo user = objectMapper.readValue(jsonFile, UserPojo.class);

        assertThat(user.getFirstName()).isEqualTo("TestFirstName");
        assertThat(user.getLastName()).isEqualTo("TestLastName");
        assertThat(user.getAge()).isEqualTo(25);
    }
}
