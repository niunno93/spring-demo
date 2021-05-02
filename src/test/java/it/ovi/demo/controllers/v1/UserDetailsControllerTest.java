package it.ovi.demo.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.ovi.demo.controllers.v1.dto.UserDetailDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"nocache", "embedded-db"})
class UserDetailsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveAndGet() throws Exception {
        int nUsers = 3;
        List<UserDetailDto> users = new ArrayList<>();
        for (int i = 0; i < nUsers; i++) {
            users.add(new UserDetailDto("name" + i, "email" + i + "@fake.com"));
        }

        // save
        for (UserDetailDto user : users) {
            String content = objectMapper.writeValueAsString(user);
            mvc.perform(post("/api/v1/user-details")
                    .contentType(MediaType.APPLICATION_JSON).content(content))
                    .andExpect(status().isOk()).andReturn();
        }

        // check size
        mvc.perform(get("/api/v1/user-details").queryParam("size", "2"))
                .andExpect(jsonPath("$.size").value(2));
    }

    @Test
    void postAndPut() throws Exception {
        UserDetailDto user = new UserDetailDto("name", "email@fake.com");

        // post
        String content = objectMapper.writeValueAsString(user);
        content = mvc.perform(post("/api/v1/user-details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        user = objectMapper.readValue(content, UserDetailDto.class);
        assertThat(user.getId(), is(notNullValue()));

        // change name
        Long id = user.getId();
        String patchedName = "name2";
        user.setName(patchedName);

        // verify put works
        content = objectMapper.writeValueAsString(user);
        mvc.perform(put("/api/v1/user-details/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name2"));
    }
}