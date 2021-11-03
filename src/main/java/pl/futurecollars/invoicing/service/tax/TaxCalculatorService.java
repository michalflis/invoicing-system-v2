package pl.futurecollars.invoicing.service.tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.TaxReport;
import pl.futurecollars.invoicing.service.invoice.InvoiceService;

@RequiredArgsConstructor
@Service
public class TaxCalculatorService {

    private final InvoiceService invoiceService;

    private BigDecimal basicCalculation(Predicate<Invoice> predicate, Function<InvoiceEntry, BigDecimal> function) {
        return invoiceService.getAll()
                .stream()
                .filter(predicate)
                .flatMap(invoice -> invoice.getEntries().stream())
                .map(function)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal extendedCalculation(Predicate<Invoice> firstFilter, Predicate<InvoiceEntry> secondFilter,
                                           Function<InvoiceEntry, BigDecimal> function) {
        return invoiceService.getAll()
                .stream()
                .filter(firstFilter)
                .flatMap(invoice -> invoice.getEntries().stream())
                .filter(secondFilter)
                .map(function)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal personalUsageCarVat(String taxIdentificationNumber) {
        return extendedCalculation(invoice -> invoice.getIssuer().getTaxIdentificationNumber().equals(taxIdentificationNumber),
                InvoiceEntry::getCarUsedForPersonalReason,
                InvoiceEntry::getVatValue).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal personalUsageCarVatReduction(String taxIdentificationNumber) {
        return personalUsageCarVat(taxIdentificationNumber).divide(BigDecimal.valueOf(2), 2, RoundingMode.DOWN);
    }

    private BigDecimal personalUsageCarCostIncrease(String taxIdentificationNumber) {
        return personalUsageCarVat(taxIdentificationNumber).subtract(personalUsageCarVatReduction(taxIdentificationNumber))
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal income(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getIssuer().getTaxIdentificationNumber().equals(taxIdentificationNumber),
                InvoiceEntry::getTotalPrice).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal costs(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getReceiver().getTaxIdentificationNumber().equals(taxIdentificationNumber),
                InvoiceEntry::getTotalPrice).add(personalUsageCarCostIncrease(taxIdentificationNumber))
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal incomingVat(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getIssuer().getTaxIdentificationNumber().equals(taxIdentificationNumber),
                InvoiceEntry::getVatValue).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal outgoingVat(String taxIdentificationNumber) {
        return basicCalculation(invoice -> invoice.getReceiver().getTaxIdentificationNumber().equals(taxIdentificationNumber),
                InvoiceEntry::getVatValue).subtract(personalUsageCarVatReduction(taxIdentificationNumber))
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal incomeMinusCosts(String taxIdentificationNumber) {
        return income(taxIdentificationNumber).subtract(costs(taxIdentificationNumber)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal vatToPay(String taxIdentificationNumber) {
        return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber))
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal incomeMinusCostsMinusPensionInsurance(Company company) {
        return incomeMinusCosts(company.getTaxIdentificationNumber())
                .subtract(company.getPensionInsurance()).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal taxCalculationBase(Company company) {
        return incomeMinusCostsMinusPensionInsurance(company).setScale(0, RoundingMode.HALF_UP)
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal incomeTax(Company company) {
        return taxCalculationBase(company).multiply(BigDecimal.valueOf(0.19)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal healthyInsurance9(Company company) {
        return company.getHealthyInsurance().multiply(BigDecimal.valueOf(0.09)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal healthyInsurance775(Company company) {
        return company.getHealthyInsurance().multiply(BigDecimal.valueOf(0.0775)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal incomeTaxMinusHealthyInsurance(Company company) {
        return incomeTax(company).subtract(healthyInsurance775(company)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal finalIncomeTaxValue(Company company) {
        return incomeTaxMinusHealthyInsurance(company).setScale(0, RoundingMode.DOWN)
            .setScale(2, RoundingMode.HALF_UP);
    }

    public TaxReport getTaxReport(Company company) {
        String taxIdentificationNumber = company.getTaxIdentificationNumber();
        return TaxReport.builder()
                .incomingVat(incomingVat(taxIdentificationNumber))
                .outgoingVat(outgoingVat(taxIdentificationNumber))
                .income(income(taxIdentificationNumber))
                .costs(costs(taxIdentificationNumber))
                .incomeMinusCosts(incomeMinusCosts(taxIdentificationNumber))
                .vatToPay(vatToPay(taxIdentificationNumber))
                .pensionInsurance(company.getPensionInsurance())
                .incomeMinusCostsMinusPensionInsurance(incomeMinusCostsMinusPensionInsurance(company))
                .taxCalculationBase(taxCalculationBase(company))
                .incomeTax(incomeTax(company))
                .healthInsurance9(healthyInsurance9(company))
                .healthInsurance775(healthyInsurance775(company))
                .incomeTaxMinusHealthInsurance(incomeTaxMinusHealthyInsurance(company))
                .finalIncomeTaxValue(finalIncomeTaxValue(company))
                .build();
    }
}
