package com.vdcrx.rest.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vdcrx.rest.api.v1.model.dto.PasswordDto;
import com.vdcrx.rest.exceptions.controller_advice.RestResponseExceptionHandler;
import com.vdcrx.rest.services.PasswordService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseControllerTest extends AbstractRestControllerTest {

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private BaseController baseController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(baseController)
                .setControllerAdvice(new RestResponseExceptionHandler())
                .build();
    }

    @Test
    public void noHandlerMappingFound() throws Exception {

//        MockHttpServletResponse response = mockMvc.perform(get("/not_found")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andReturn()
//                .getResponse();
    }

    @Test
    public void login() throws Exception{
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Authenticated!"))
                .andReturn().getResponse();
    }

    @Test
    public void updatePassword() throws Exception {
        UUID id = UUID.randomUUID();
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setCurrent("hello");
        passwordDto.setReplacement("HelloWorld1#");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(passwordDto);


        mockMvc.perform(MockMvcRequestBuilders
                .patch("/password/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        verify(passwordService).updatePassword(id, passwordDto.getReplacement(), passwordDto.getCurrent());
    }
}