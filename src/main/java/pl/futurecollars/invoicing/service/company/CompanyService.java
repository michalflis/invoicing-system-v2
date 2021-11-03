package pl.futurecollars.invoicing.service.company;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.repository.company.CompanyRepository;
import pl.futurecollars.invoicing.repository.generic.GenericRepository;

@AllArgsConstructor
@Service
public class CompanyService implements GenericRepository<Company> {

    private final CompanyRepository companyRepository;

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company getById(UUID id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company Id: " + id + " does not exist"));
    }

    @Override
    public List<Company> getAll() {
        List<Company> companyList = new ArrayList<>();
        companyRepository.findAll().forEach(companyList::add);
        return companyList;
    }

    @Override
    public Company update(Company updatedCompany) {
        if (companyRepository.findById(updatedCompany.getCompanyId()).isPresent()) {
            return companyRepository.save(updatedCompany);
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        if (companyRepository.findById(id).isPresent()) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        companyRepository.deleteAll();
    }
}
