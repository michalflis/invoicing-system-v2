import { TestBed } from '@angular/core/testing';

import { TaxReportService } from './tax-report.service';

describe('TaxReportService', () => {
  let service: TaxReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaxReportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
