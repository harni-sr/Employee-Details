package com.EmployeeDetails.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

import java.util.List;


@Entity
    public class Employee {
        @Id
        @GeneratedValue
        int id;
        @NotBlank(message = "Name cannot be null or empty")
        String name;
        @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+$",message = "Email should be valid")
        @NotNull(message ="Email cannot be null")
        String email;
        String dept;
        @Positive
        @Min(value = 1000,message = "Salary should be greater than 1000")
        double salary;

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getDept() {
            return dept;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setDept(String dept) {
            this.dept = dept;
        }
        public double getSalary() {
            return salary;
        }
        public void setSalary(double salary) {
            this.salary = salary;
        }

        public Employee(int id, String name, String dept, double salary,String email) {
            super();
            this.id = id;
            this.name = name;
            this.dept = dept;
            this.salary = salary;
            this.email = email;
        }

        public Employee(){
        }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dept='" + dept + '\'' +
                ", salary=" + salary +
                '}';
    }
}

