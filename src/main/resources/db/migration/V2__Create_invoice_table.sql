CREATE TABLE public.invoices
(
invoice_id uuid NOT NULL UNIQUE,
invoice_number varchar(50) NOT NULL,
issue_date date NOT NULL,
issuer_id uuid NOT NULL,
receiver_id uuid NOT NULL,
PRIMARY KEY (invoice_id),
CONSTRAINT issuer_fk FOREIGN KEY(issuer_id) REFERENCES public.companies(company_id),
CONSTRAINT receiver_fk FOREIGN KEY(receiver_id) REFERENCES public.companies(company_id)
)