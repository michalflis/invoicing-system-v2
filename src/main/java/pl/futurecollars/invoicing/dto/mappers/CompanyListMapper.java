package pl.futurecollars.invoicing.dto.mappers;

import org.mapstruct.Mapper;
import pl.futurecollars.invoicing.dto.CompanyListDto;
import pl.futurecollars.invoicing.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyListMapper {

    CompanyListDto companyListToDto(Company company);
}
