package com.gmudryk.backupservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmudryk.backupservice.BackupServiceApplication;
import com.gmudryk.backupservice.entity.BackupInfo;
import com.gmudryk.backupservice.entity.BackupStatus;
import com.gmudryk.backupservice.entity.TodoItemServerUser;
import com.gmudryk.backupservice.repository.BackupRepository;
import com.gmudryk.backupservice.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BackupServiceApplication.class)
@WebAppConfiguration
@SpringBootTest
public class BackupInfoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    WebApplicationContext applicationContext;

    private MockRestServiceServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BackupRepository backupRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${todo.item.server.user.data.url}")
    private URI getUserDataUrl;

    @Before
    public void setup() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }

    @After
    public void destroy() {
        backupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testSuccessCreatedUsersBackup() throws Exception {

        String testUser = "{\n" +
                "        \"username\": \"test_galyna\",\n" +
                "        \"email\": \"test1@gmail.com\",\n" +
                "        \"todos\": [\n" +
                "            {\n" +
                "                \"id\": 1,\n" +
                "                \"subject\": \"test3443543_subject\",\n" +
                "                \"dueDate\": \"2018-03-01 14:58:00\",\n" +
                "                \"done\": true\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\": 2,\n" +
                "                \"subject\": \"test456546456_subject\",\n" +
                "                \"dueDate\": \"2018-03-02 14:58:00\",\n" +
                "                \"done\": true\n" +
                "            }\n" +
                "        ]\n" +
                "    }";

        TodoItemServerUser todoItemServerUser = objectMapper.readValue(testUser, TodoItemServerUser.class);

        List<TodoItemServerUser> users = new ArrayList();
        for (int i = 0; i < 1000; i++) {
            todoItemServerUser.setId(new Long(i));
            users.add(todoItemServerUser);
        }

        String result = objectMapper.writeValueAsString(users);

        mockServer.
                expect(MockRestRequestMatchers.requestTo(getUserDataUrl))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).
                andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).
                        contentType(MediaType.APPLICATION_JSON)
                        .body(result));

        MvcResult mvcResult = mockMvc.perform(post("/backups")).andExpect(status().isOk())
                .andReturn();

        Long backupId = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BackupInfo.class).getId();

        mvcResult = mockMvc.perform(get("/backups")).andExpect(status().isOk())
                .andReturn();
        List<BackupInfo> backupInfos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BackupInfo>>() {
        });
        Assert.assertTrue(backupInfos.size() == 1);
        Assert.assertEquals(BackupStatus.DONE, backupInfos.get(0).getStatus());
        Assert.assertEquals(backupId, backupInfos.get(0).getId());

    }

    @Test
    public void testEmptyUserBackup() throws Exception {

        String testUser = "{}";

        TodoItemServerUser todoItemServerUser = objectMapper.readValue(testUser, TodoItemServerUser.class);

        List<TodoItemServerUser> users = new ArrayList(1);
        users.add(todoItemServerUser);

        String result = objectMapper.writeValueAsString(users);

        mockServer.
                expect(MockRestRequestMatchers.requestTo(getUserDataUrl))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).
                andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).
                        contentType(MediaType.APPLICATION_JSON)
                        .body(result));

        mockMvc.perform(post("/backups")).andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testUserWithoutRequiredFieldBackup() throws Exception {

        String testUser = "{\n" +
                "        \"todos\": [\n" +
                "            {\n" +
                "                \"id\": 1,\n" +
                "                \"subject\": \"test3443543_subject\",\n" +
                "                \"dueDate\": \"2018-03-01 14:58:00\",\n" +
                "                \"done\": true\n" +
                "            },\n" +
                "            {\n" +
                "                 \"id\": 2,\n" +
                "                \"subject\": \"test456546456_subject\",\n" +
                "                \"dueDate\": \"2018-03-02 14:58:00\",\n" +
                "                \"done\": true\n" +
                "            }\n" +
                "        ]\n" +
                "    }";

        TodoItemServerUser todoItemServerUser = objectMapper.readValue(testUser, TodoItemServerUser.class);

        List<TodoItemServerUser> users = new ArrayList(1);
        users.add(todoItemServerUser);

        String result = objectMapper.writeValueAsString(users);

        mockServer.
                expect(MockRestRequestMatchers.requestTo(getUserDataUrl))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).
                andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK).
                        contentType(MediaType.APPLICATION_JSON)
                        .body(result));

        mockMvc.perform(post("/backups")).andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void testGettingUserDataException() throws Exception {

        mockServer.
                expect(ExpectedCount.manyTimes(), MockRestRequestMatchers.requestTo(getUserDataUrl))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).
                andRespond(MockRestResponseCreators.withBadRequest().
                        contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/backups")).andExpect(status().isInternalServerError())
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/backups")).andExpect(status().isOk())
                .andReturn();
        List<BackupInfo> backupInfos = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<BackupInfo>>() {
        });
        Assert.assertTrue(backupInfos.size() == 1);
        Assert.assertEquals(BackupStatus.FAILED, backupInfos.get(0).getStatus());

    }
}
