package pl.futurecollars.invoicing.dto.mappers;

import org.mapstruct.Mapper;
import pl.futurecollars.invoicing.dto.CompanyDto;
import pl.futurecollars.invoicing.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDto companyToDto(Company company);

    Company dtoToEntity(CompanyDto companyDto);
}
