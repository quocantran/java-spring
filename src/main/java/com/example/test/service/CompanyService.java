package com.example.test.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Optional;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.repository.CompanyRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        Company res = this.companyRepository.save(company);
        return res;
    }

    public ResponsePaginationDTO getAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> company = this.companyRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setPage(company.getNumber() + 1);
        meta.setPageSize(company.getSize());

        meta.setPages(company.getTotalPages());
        meta.setTotal(company.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(company.getContent());

        return resultPaginationDTO;

    }

    public Company updateCompany(Company company) throws BadRequestException {
        Company current = companyRepository.findById(company.getId());
        if (current == null) {
            throw new BadRequestException("Company not found");
        }

        current.setName(company.getName());
        current.setAddress(company.getAddress());
        current.setLogo(company.getLogo());
        current.setDescription(company.getDescription());
        return this.companyRepository.save(current);

    }

    public Company getCompanyById(long id) {
        Company company = this.companyRepository.findById(id);
        return company;
    }
}
