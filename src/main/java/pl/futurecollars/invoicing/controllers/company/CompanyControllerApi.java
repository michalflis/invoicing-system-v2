package pl.futurecollars.invoicing.controllers.company;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.dto.CompanyDto;
import pl.futurecollars.invoicing.dto.CompanyListDto;

@RequestMapping(path = "/companies", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"company-controller"})
public interface CompanyControllerApi {

    @ApiOperation("Add new company")
    @PostMapping
    ResponseEntity<CompanyDto> save(@RequestBody CompanyDto company);

    @ApiOperation("Get list of all companies")
    @GetMapping
    ResponseEntity<List<CompanyDto>> getAll();

    @ApiOperation("Get short list of all companies")
    @GetMapping(path = "/list")
    ResponseEntity<List<CompanyListDto>> getList();

    @ApiOperation("Get company by Id")
    @GetMapping(path = "/{id}")
    ResponseEntity<CompanyDto> getById(@PathVariable UUID id);

    @ApiOperation("Update company")
    @PutMapping
    ResponseEntity<CompanyDto> update(@RequestBody CompanyDto company);

    @ApiOperation("Delete company by Id")
    @DeleteMapping(path = "/{id}")
    ResponseEntity<Boolean> update(@PathVariable UUID id);
}
