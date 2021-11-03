package pl.futurecollars.invoicing.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaxReport {

    private BigDecimal incomingVat;
    private BigDecimal outgoingVat;
    private BigDecimal income;
    private BigDecimal costs;
    private BigDecimal incomeMinusCosts;
    private BigDecimal vatToPay;
    private BigDecimal pensionInsurance;
    private BigDecimal incomeMinusCostsMinusPensionInsurance;
    private BigDecimal taxCalculationBase;
    private BigDecimal incomeTax;
    private BigDecimal healthInsurance9;
    private BigDecimal healthInsurance775;
    private BigDecimal incomeTaxMinusHealthInsurance;
    private BigDecimal finalIncomeTaxValue;
}




