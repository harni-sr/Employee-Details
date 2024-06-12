package com.EmployeeDetails.Controller;

import com.EmployeeDetails.Entity.Employee;
import com.EmployeeDetails.Response.Response;
import com.EmployeeDetails.Service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.client.RestTemplate;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private EmployeeService service;
    @InjectMocks
    private EmployeeController controller;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(controller).build();
    }
    @Test
    public void shouldCreateEmployee() throws Exception {
        Employee employee = new Employee(9,"John DuraiRaj","QEA",900000,"JD007@gmail.com");
        Response response = new Response(9,"John DuraiRaj","Saved");
        //ResponseEntity<Response> responseEntity = ResponseEntity.ok(response);
        //when(service.saveJson(any(Employee.class))).thenReturn(ResponseEntity.ok(response));
        when(service.saveJson(Mockito.any())).thenReturn(ResponseEntity.ok(response));
        mockMvc.perform(post("/save").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee))
                        .param("Employee Request",objectMapper.writeValueAsString(employee)))
                        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value("9"))
                        .andExpect(jsonPath("$.name").value("John DuraiRaj"))
                        .andExpect(jsonPath("$.status").value("Saved"));
//                        .andExpect(content().string(objectMapper.writeValueAsString(response)));
    }
    @Test
    public void shouldUpdateEmployee() throws Exception {
        int id = 6;
        Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        Response response = new Response(id,"Harry Styles","Updated");
        when(service.updateJson(any(Integer.class),any(Employee.class))).thenReturn(ResponseEntity.ok(response));
//        when(service.updateJson(Mockito.anyInt(),Mockito.any())).thenReturn(ResponseEntity.ok(response));

       mockMvc.perform(put("/save/{id}",id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(id))
                        .andExpect(jsonPath("$.name").value("Harry Styles"))
                        .andExpect(jsonPath("$.status").value("Updated"));
    }
    @Test
    public void shouldGetEmployeeById() throws Exception {
        int id = 6;
        Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        when(service.getById(id)).thenReturn(Optional.of(employee));
        mockMvc.perform(get("/save/{id}",id))
                  .andExpect(status().isOk())
                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                  .andExpect(jsonPath("$.id").value(id));
    }
    @Test
    public void shouldGetAllEmployees() throws Exception {
        Employee employee1 = new Employee(9,"John DuraiRaj","QEA",900000,"JD007@gmail.com");
        Employee employee2 = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        when(service.getAll()).thenReturn(Arrays.asList(employee1,employee2));
        mockMvc.perform(get("/saved"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    public void shouldDeleteEmployeeById() throws Exception {
            int id = 6;
            Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
            String response = "Employee with id:" + id + " was deleted successfully";
            when(service.deleteById(id)).thenReturn(response);
            mockMvc.perform(delete("/deleteById/{id}",id))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=ISO-8859-1")))
                    .andExpect(content().string("Employee with id:" + id + " was deleted successfully"));
    }
    @Test
    public void shouldDeleteEmployees() throws Exception {
        Employee employee1 = new Employee(9,"John DuraiRaj","QEA",900000,"JD007@gmail.com");
        Employee employee2 = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        when(service.deleteEmployees()).thenReturn("Deleted");
        mockMvc.perform(delete("/employees"))
                .andExpect(status().isOk());
    }
    @Test
    public void shouldGetXml() throws Exception{
        Employee employee1 = new Employee(9,"John DuraiRaj","QEA",900000,"JD007@gmail.com");
        String expectedXml = "<ArrayList><item><id>9</id><name>John DuraiRaj</name><email>JD007@gmail.com</email><dept>QEA</dept><salary>900000</salary></item></ArrayList>";
        String  actualXml = service.getXml(employee1);
        when(service.getXml(any())).thenReturn(expectedXml);
        mockMvc.perform(get("/receive-xml")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee1)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedXml));
    }

}