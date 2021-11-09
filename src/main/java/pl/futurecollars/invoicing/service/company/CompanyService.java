package pl.futurecollars.invoicing.service.company;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.dto.CompanyDto;
import pl.futurecollars.invoicing.dto.CompanyListDto;
import pl.futurecollars.invoicing.dto.mappers.CompanyListMapper;
import pl.futurecollars.invoicing.dto.mappers.CompanyMapper;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.repository.company.CompanyRepository;
import pl.futurecollars.invoicing.repository.generic.GenericRepository;

@AllArgsConstructor
@Service
public class CompanyService implements GenericRepository<CompanyDto> {

    private final CompanyRepository companyRepository;
    private final CompanyListMapper companyListMapper;
    private final CompanyMapper companyMapper;

    @Override
    public CompanyDto save(CompanyDto company) {
        return companyMapper.companyToDto(companyRepository.save(companyMapper.dtoToEntity(company)));
    }

    @Override
    public CompanyDto getById(UUID id) {
        return companyMapper.companyToDto(companyRepository
            .findById(id).orElseThrow(() -> new RuntimeException("Company Id: " + id + " does not exist")));
    }

    @Override
    public List<CompanyDto> getAll() {
        List<Company> companyList = new ArrayList<>();
        companyRepository.findAll().forEach(companyList::add);
        return companyList.stream().map(companyMapper::companyToDto).collect(Collectors.toList());
    }

    public List<CompanyListDto> getList() {
        List<Company> companyList = new ArrayList<>();
        companyRepository.findAll().forEach(companyList::add);
        return companyList.stream().map(companyListMapper::companyListToDto).collect(Collectors.toList());
    }

    @Override
    public CompanyDto update(CompanyDto updatedCompany) {
        if (companyRepository.findById(companyMapper.dtoToEntity(updatedCompany).getCompanyId()).isPresent()) {
            return companyMapper.companyToDto(companyRepository.save(companyMapper.dtoToEntity(updatedCompany)));
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
