package pl.futurecollars.invoicing.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyListDto {

    @ApiModelProperty(value = "Company name", required = true, example = "XYZ")
    private String name;

    @ApiModelProperty(value = "Tax identification number", required = true, example = "1234567819")
    private String taxIdentificationNumber;


}