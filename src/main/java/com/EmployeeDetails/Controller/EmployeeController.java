package com.EmployeeDetails.Controller;

import com.EmployeeDetails.Entity.Employee;
import com.EmployeeDetails.Response.Response;
import com.EmployeeDetails.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class EmployeeController {
        @Autowired
        EmployeeService service;

        @PostMapping("/save")
        public ResponseEntity<Response> saveJson(@RequestBody @Valid Employee employee) {
                return service.saveJson(employee);
        }
        @PutMapping("/save/{id}")
        public ResponseEntity<Response> updateJson(@PathVariable Integer id, @RequestBody @Valid Employee employee) {
               return service.updateJson(id,employee);
        }
        @GetMapping("/save/{id}")
        public Optional<Employee> getById(@PathVariable Integer id){
             return service.getById(id);
        }

        @GetMapping("/saved")
        public List<Employee> getAll(){
               return service.getAll();
        }
        @DeleteMapping("/deleteById/{id}")
        public String deleteById(@PathVariable Integer id) {;
                return service.deleteById(id);
        }
        @DeleteMapping("/employees")
        public void deleteEmployees(){
                service.deleteEmployees();
        }

        @GetMapping("/receive-xml")
        public String getXml(Employee employee){
                return service.getXml(employee);
        }
}
