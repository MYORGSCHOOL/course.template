package ru.template.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.template.example.configuration.JacksonConfiguration;
import ru.template.example.controller.dto.DocumentDto;
import ru.template.example.controller.dto.IdsDto;
import ru.template.example.controller.dto.Status;
import ru.template.example.documents.service.DocumentServiceImpl;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class DocumentControllerTest {
    private static final String BASE_PATH = "/documents";
    private final ObjectMapper mapper = new JacksonConfiguration().objectMapper();
    private MockMvc mockMvc;
    @MockBean
    private DocumentServiceImpl service;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void successWhenSaveTest() throws Exception {
        var documentDto = new DocumentDto();
        var organization = randomAlphabetic(100);
        var patient = "Иванов Иван Иванович";
        var type = randomAlphabetic(100);
        var descriptions = randomAlphabetic(100);

        when(service.save(any())).thenReturn(any());

        documentDto.setOrganization(organization);
        documentDto.setPatient(patient);
        documentDto.setType(type);
        documentDto.setDescription(descriptions);
        documentDto.setDate(Date.from(Instant.now()));
        documentDto.setStatus(Status.of("1", "NEW"));
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().isOk());

    }

    @Test
    public void errorWhenSaveTest() throws Exception {
        var documentDto = new DocumentDto();
        var organization = randomAlphabetic(1000);
        var patient = "Иванов Иван Иванович";
        var type = randomAlphabetic(1000);
        var descriptions = randomAlphabetic(1000);
        when(service.save(any())).thenThrow(new IllegalStateException("Это слишком!"));

        documentDto.setOrganization(organization);
        documentDto.setPatient(patient);
        documentDto.setType(type);
        documentDto.setDescription(descriptions);
        documentDto.setDate(Date.from(Instant.now()));
        documentDto.setStatus(Status.of("1", "NEW"));
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().is5xxServerError());
    }

    @Test
    public void getTest() throws Exception {
        var documentDto = new DocumentDto();
        when(service.findAll()).thenReturn(List.of(documentDto));

        mockMvc.perform(get(BASE_PATH)).andExpect(status().isOk());
    }

    @Test
    public void sendTest() throws Exception {
        var documentDto = new DocumentDto();
        var organization = randomAlphabetic(100);
        var patient = "Иванов Иван Иванович";
        var type = randomAlphabetic(100);
        var descriptions = randomAlphabetic(100);

        when(service.update(any())).thenReturn(any());

        documentDto.setOrganization(organization);
        documentDto.setPatient(patient);
        documentDto.setType(type);
        documentDto.setDescription(descriptions);
        documentDto.setDate(Date.from(Instant.now()));
        documentDto.setStatus(Status.of("2", "IN_PROCESS"));
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().isOk());
    }
    @Test
    public void deleteTest() throws Exception {
        Long id = 1L;
        doNothing().when(service).delete(id);
        mockMvc.perform(delete(BASE_PATH + "/{id}", id)).andExpect(status().isOk());
    }

    @Test
    public void deleteAllTest() throws Exception {
        IdsDto idsDto = new IdsDto();
        idsDto.setIds(Set.of(1L, 2L));
        doNothing().when(service).deleteAll(idsDto.getIds());
        mockMvc.perform(deleteAction(BASE_PATH, idsDto)).andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder postAction(String uri, Object dto) throws JsonProcessingException {
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }
   private MockHttpServletRequestBuilder deleteAction(String uri, Object dto) throws JsonProcessingException {
        return delete(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }
}