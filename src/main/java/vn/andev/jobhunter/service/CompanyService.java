package vn.andev.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.andev.jobhunter.domain.Company;
import vn.andev.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public List<Company> handleGetListCompany() {
        return this.companyRepository.findAll();
    }

    public Company handleUpdateCompany(Long id, Company body) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setName(body.getName());
            currentCompany.setDescription(body.getDescription());
            currentCompany.setAddress(body.getAddress());
            currentCompany.setLogo(body.getLogo());
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

    public void handleDeleteCompany(Long id) {
        this.companyRepository.deleteById(id);
    }

}
