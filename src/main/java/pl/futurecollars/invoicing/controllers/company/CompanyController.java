package pl.futurecollars.invoicing.controllers.company;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.dto.CompanyDto;
import pl.futurecollars.invoicing.dto.CompanyListDto;
import pl.futurecollars.invoicing.service.company.CompanyService;

@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
public class CompanyController implements CompanyControllerApi {

    private final CompanyService companyService;

    @Override
    public ResponseEntity<CompanyDto> save(@RequestBody @Valid CompanyDto company) {
        log.debug("Adding new company to Database");
        return ResponseEntity.ok()
            .body(companyService.save(company));
    }

    @Override
    public ResponseEntity<List<CompanyDto>> getAll() {
        log.debug("Getting all companies from Database");
        return ResponseEntity.ok()
            .body(companyService.getAll());
    }

    @Override
    public ResponseEntity<List<CompanyListDto>> getList() {
        log.debug("Getting short list of all companies from Database");
        return ResponseEntity.ok()
            .body(companyService.getList());
    }

    @Override
    public ResponseEntity<CompanyDto> getById(@PathVariable UUID id) {
        log.debug("Getting company Id: {} from Database", id);
        try {
            return ResponseEntity.ok()
                .body(companyService.getById(id));
        } catch (Exception e) {
            log.error("Exception: {} occurred while getting company Id: {} from Database", e, id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<CompanyDto> update(@RequestBody @Valid CompanyDto company) {
        log.debug("Updating company Id: {} in Database", company.getCompanyId());
        return ResponseEntity.ok()
            .body(companyService.update(company));
    }

    @Override
    public ResponseEntity<Boolean> update(@PathVariable UUID id) {
        log.debug("Deleting company Id: {} from Database", id);
        try {
            return ResponseEntity.ok()
                .body(companyService.delete(id));
        } catch (Exception e) {
            log.error("Exception: {} occurred while deleting company Id: {} from Database", e, id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
