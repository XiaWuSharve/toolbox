package com.example.demo.payroll_practice;

import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        Iterable<EntityModel<Employee>> employees = this.repository.findAll().stream().map(emp -> {
            return EntityModel.of(emp,
                linkTo(methodOn(EmployeeController.class).one(emp.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
        }).toList();
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    public Employee newEmployee(@RequestBody Employee newEmployee) {
        return this.repository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable Long id) {
        Employee emp = this.repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        return EntityModel.of(emp, 
        linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(), 
        linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
    }

    @PutMapping("/employees/{id}")
    public Employee replaceEmployee(@PathVariable Long id, @RequestBody Employee newEmployee) {
        return this.repository.findById(id).map(emp -> {
            emp.setName(newEmployee.getName());
            emp.setRole(newEmployee.getRole());
            return this.repository.save(emp);
        })
        .orElseGet(() -> { return this.repository.save(newEmployee); });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        this.repository.deleteById(id);
    }

}
