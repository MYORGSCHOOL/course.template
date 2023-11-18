package ru.template.example.documents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.IdsDto;
import ru.template.example.documents.service.DocumentServiceImpl;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class DocumentControllerTest {
    //TODO: Разобраться в этом
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
        var organization = randomAlphabetic(100);

        when(service.save(any())).thenReturn(any());

        var documentDto = new DocumentDto();
        documentDto.setId(5L);
        documentDto.setOrganization(organization);
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1)).save(documentDto);
    }

    @Test
    public void errorWhenSaveTest() throws Exception {
        //TOO long name
        var organization = randomAlphabetic(1000);

        when(service.save(any())).thenThrow(new IllegalStateException("Это слишком!"));

        var documentDto = new DocumentDto();
        documentDto.setId(5L);
        documentDto.setOrganization(organization);
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().is5xxServerError());
    }

    @Test
    public void getTest() throws Exception {
        when(service.findAll()).thenReturn(anyList());

        var organization = randomAlphabetic(100);
        var documentDto = new DocumentDto();
        documentDto.setId(5L);
        documentDto.setOrganization(organization);
        // Mockito.verify(service, Mockito.times(1)).save(documentDto);
        mockMvc.perform(getAction(BASE_PATH)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void sendTest() throws Exception {
        var organization = randomAlphabetic(1000);
        var name = randomAlphabetic(1000);
        var description = randomAlphabetic(1000);
        var type = randomAlphabetic(1000);

        var documentDto = new DocumentDto();
        documentDto.setOrganization(organization);
        documentDto.setPatient(name);
        documentDto.setType(type);
        documentDto.setDescription(description);
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteTest() throws Exception {
        var organization = randomAlphabetic(1000);
        var name = randomAlphabetic(1000);
        var description = randomAlphabetic(1000);
        var type = randomAlphabetic(1000);
        var id = 5L;

        var documentDto = new DocumentDto();
        documentDto.setId(5L);
        documentDto.setOrganization(organization);
        documentDto.setPatient(name);
        documentDto.setType(type);
        documentDto.setDescription(description);
        mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().is2xxSuccessful());

        var path = BASE_PATH + "/" + id;
        mockMvc.perform(deleteAction(path)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteAllTest() throws Exception {

        var id = 5L;
        var recordsNumber = 10L;
        Set<Long> ids = new HashSet<>();

        for (long i = id; i < id + recordsNumber; i++) {
            var organization = randomAlphabetic(1000);
            var name = randomAlphabetic(1000);
            var description = randomAlphabetic(1000);
            var type = randomAlphabetic(1000);

            var documentDto = new DocumentDto();
            documentDto.setId(i);
            documentDto.setOrganization(organization);
            documentDto.setPatient(name);
            documentDto.setType(type);
            documentDto.setDescription(description);
            mockMvc.perform(postAction(BASE_PATH, documentDto)).andExpect(status().is2xxSuccessful());
            ids.add(i);
        }
        var idsDto = new IdsDto();
        idsDto.setIds(ids);
        mockMvc.perform(deleteAction(BASE_PATH, idsDto)).andExpect(status().is2xxSuccessful());
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

    private MockHttpServletRequestBuilder deleteAction(String uri) {
        return delete(uri);
    }

    private MockHttpServletRequestBuilder getAction(String uri) {
        return get(uri);
    }
}
