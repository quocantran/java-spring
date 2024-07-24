package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getAllCompanies(@Filter Specification<Company> spec,
            Pageable pageable) {

        ResponsePaginationDTO companies = this.companyService.getAllCompanies(spec, pageable);
        return new ResponseEntity<ResponsePaginationDTO>(companies, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) throws BadRequestException {
        Company res = this.companyService.updateCompany(company);
        return new ResponseEntity<Company>(res, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company res = this.companyService.createCompany(company);
        return new ResponseEntity<Company>(res, HttpStatus.CREATED);
    }

}
