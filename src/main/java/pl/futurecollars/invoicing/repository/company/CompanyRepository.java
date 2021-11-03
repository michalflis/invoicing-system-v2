package pl.futurecollars.invoicing.repository.company;

import java.util.UUID;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.futurecollars.invoicing.model.Company;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, UUID> {
}
