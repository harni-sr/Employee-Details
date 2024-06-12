package com.EmployeeDetails.Service;

import com.EmployeeDetails.Controller.EmployeeController;
import com.EmployeeDetails.Entity.Employee;
import com.EmployeeDetails.Reository.RepositoryDb;
import com.EmployeeDetails.Response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeeServiceTest {
    //    @Autowired
//    private MockMvc mockMvc;
    @Mock
    private RepositoryDb repositoryDb;
    @InjectMocks
    private EmployeeService service;

    @MockBean
    private RestTemplate restTemplate;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
//        this.mockMvc= MockMvcBuilders.standaloneSetup(service).build();
    }

    @Test
    public void testSaveEmployee_ValidData(){
        Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        Response res = new Response(6,"Harry Styles","Saved");
        ResponseEntity<Response> expectedResponse = new ResponseEntity<>(res, HttpStatus.CREATED);

        when(repositoryDb.save(any(Employee.class))).thenReturn(employee);
        ResponseEntity<Response> response = service.saveJson(employee);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertEquals(expectedResponse.getBody().getId(),response.getBody().getId());
        assertEquals(expectedResponse.getBody().getName(),response.getBody().getName());
        assertEquals(expectedResponse.getBody().getStatus(),response.getBody().getStatus());
    }

    @Test
    public void testSaveEmployee_InvalidData(){
        Employee employee = new Employee(6,null,"MA",900000,"harry984@gmail.com");
        assertThrows(NullPointerException.class,() -> {
            service.saveJson(null);
        });
    }

    @Test
    public void testUpdateEmployeeById_ValidData() {
        int id = 6;
        Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        Response res = new Response(6,"Harry Styles","Updated");
        ResponseEntity<Response> expectedResponse = new ResponseEntity<>(res, HttpStatus.CREATED);
        ResponseEntity<Response> response = service.updateJson(id,employee);
        when(service.updateJson(id,employee)).thenReturn(response);
        assertEquals(expectedResponse.getBody().getId(),response.getBody().getId());
        assertEquals(expectedResponse.getBody().getName(),response.getBody().getName());
        assertEquals(expectedResponse.getBody().getStatus(),response.getBody().getStatus());
        assertEquals(expectedResponse.getStatusCode(),response.getStatusCode());
    }
    @Test
    public void testUpdateEmployeeById_InvalidData() {
        when(repositoryDb.findById(7)).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> {
            service.updateJson(7, null);
        });
    }
    @Test
    public void shouldGetAllEmployees() {
        Employee employee1 = new Employee(9,"John DuraiRaj","QEA",900000,"JD007@gmail.com");
        Employee employee2 = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        List<Employee> expectedEmployees = Arrays.asList(employee1,employee2);
        when(service.getAll()).thenReturn(expectedEmployees);
        List<Employee> employees = service.getAll();
        assertEquals(expectedEmployees,employees);
    }
    @Test
    public void testDeleteEmployeeById(){
        int id = 6;
        Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        when(repositoryDb.findById(Mockito.anyInt())).thenReturn(Optional.of(employee));
        doNothing().when(repositoryDb).deleteById(6);
        String response = service.deleteById(id);
        assertEquals("Employee with id:" + id + " was deleted successfully",response);
    }
    @Test
    public void testDeleteEmployeeById_NotFound(){
        int id = 6;
        when(repositoryDb.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        String response = service.deleteById(id);
        assertEquals("Employee with id:"+id+ " doesn't exist",response);
    }
    @Test
    public void testDeleteEmployee() {
        Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        String response = service.deleteEmployees();
        when(service.deleteEmployees()).thenReturn(response);
        assertEquals("Deleted",response);
    }
    @Test
    public void testGetXml(){
        Employee employee = new Employee(6,"Harry Styles","MA",900000,"harry984@gmail.com");
        List<Employee> employees = Arrays.asList(employee);
        String expectedResponse ="<ArrayList><item><id>6</id><name>Harry Styles</name><email>harry984@gmail.com</email><dept>MA</dept><salary>900000</salary></item></ArrayList>";
        when(repositoryDb.findAll()).thenReturn(employees);
        when(service.getXml(employee)).thenReturn(expectedResponse);

        String actualResponse = service.getXml(employee);
        assertEquals(expectedResponse,actualResponse);
    }
}
