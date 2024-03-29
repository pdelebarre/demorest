package com.philippe.demorest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
class EmployeeController {

  private final EmployeeRepository repository;

  EmployeeController(EmployeeRepository repository) {
    this.repository = repository;
  }

  // Aggregate root

  @GetMapping("/employees")
  CollectionModel<EntityModel<Employee>> all() {
    List<EntityModel<Employee>> employees = repository.findAll().stream().map(employee -> new EntityModel<>(employee,linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),linkTo(methodOn(EmployeeController.class).all()).withRel("employees"))).collect(Collectors.toList());
    return new CollectionModel<EntityModel<Employee>>(employees,linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
  }

  @PostMapping("/employees")
  Employee newEmployee(@RequestBody Employee newEmployee) {
    return repository.save(newEmployee);
  }

  // Single item

  @GetMapping("/employees/{id}")
  EntityModel<Employee> one(@PathVariable Long id) {

    Employee employee = repository.findById(id)
      .orElseThrow(() -> new EmployeeNotFoundException(id));

    return new EntityModel<>(employee,
        linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
        linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
  }

  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

    return repository.findById(id)
      .map(employee -> {
        employee.setName(newEmployee.getName());
        employee.setRole(newEmployee.getRole());
        return repository.save(employee);
      })
      .orElseGet(() -> {
        newEmployee.setId(id);
        return repository.save(newEmployee);
      });
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }
}