package vn.andev.jobhunter.controller;

import java.util.List;

import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.andev.jobhunter.domain.Company;
import vn.andev.jobhunter.service.CompanyService;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("")
    public ResponseEntity<List<Company>> getCompany() {
        List<Company> companies = this.companyService.handleGetListCompany();
        return ResponseEntity.ok(companies);
    }

    @PostMapping("")
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company reqBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(reqBody));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @Valid @RequestBody Company reqBody) {
        return ResponseEntity.ok(this.companyService.handleUpdateCompany(id, reqBody));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

}
