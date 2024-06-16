package com.EmployeeDetails.Service;

import com.EmployeeDetails.Entity.Employee;
import com.EmployeeDetails.Reository.RepositoryDb;
import com.EmployeeDetails.Response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
public class EmployeeService {

    @Autowired
    RepositoryDb repo;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<Response> saveJson(Employee employee) {

        Response response = new Response();
        Employee emp = repo.save(employee);
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setStatus("Saved");

        if(response.getStatus().equals("Saved"))
            return new ResponseEntity<>(response, HttpStatus.CREATED);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

//        else{
//            response.setStatus("Not saved");
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
    }
    public ResponseEntity<Response> updateJson(Integer id, Employee employee) {
        if(repo.findById(id).isPresent()) {
            Employee updatedEmployee = new Employee();
            updatedEmployee.setId(id);
            updatedEmployee.setName(employee.getName());
            updatedEmployee.setSalary(employee.getSalary());
            updatedEmployee.setDept(employee.getDept());
            updatedEmployee.setEmail(employee.getEmail());
            Employee emp = repo.save(updatedEmployee);

            Response response = new Response();
            response.setId(updatedEmployee.getId());
            response.setName(updatedEmployee.getName());
            response.setStatus("Updated");

            if (response.getStatus().equals("Updated")) {
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.setStatus("Not saved");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        Response response = new Response();
        response.setId(id);
        response.setName(null);
        response.setStatus("Id: "+id+" doesn't exists.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @Cacheable(value = "employees", key = "#id")
    public Optional<Employee> getById(Integer id) {
        if (repo.findById(id).isPresent()) {
            return repo.findById(id);
        }
        return null;
    }


    @Cacheable(value = "employees")
    public List<Employee> getAll(){
        return (repo.findAll());
    }

    @CacheEvict(value = "employees", key = "#items.id")
    public String deleteById(Integer id){
       if(repo.findById(id).isPresent()) {
           repo.deleteById(id);
           return new String("Employee with id:" + id + " was deleted successfully");
       }
           return new String("Employee with id:"+id+ " doesn't exist");
       }

    @CacheEvict(value = "employees",allEntries = true)
    public String deleteEmployees(){
        repo.deleteAll();
        return "Deleted";
    }

    public String getXml(Employee employee){
        String url = "http://localhost:9000/xml";
        List<Employee> list = repo.findAll();
        //String empl = restTemplate.postForObject(url,list,String.class);
        String response = restTemplate.postForObject(url,list,String.class);
        return response;
    }


}
