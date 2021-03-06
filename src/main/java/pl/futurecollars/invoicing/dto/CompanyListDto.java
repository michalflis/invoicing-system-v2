package pl.futurecollars.invoicing.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyListDto {

    @ApiModelProperty(value = "Company name", required = true, example = "XYZ")
    private String name;

    @ApiModelProperty(value = "UUID generated by app", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID companyId;

    @ApiModelProperty(value = "Tax identification number", required = true, example = "1234567819")
    private String taxIdentificationNumber;


}
