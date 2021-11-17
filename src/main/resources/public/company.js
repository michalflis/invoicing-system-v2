const loadCompanies = async () => {
    const response = await fetch('http://localhost:8080/companies/list')
    const companies = await response.json()

    const companiesTable = document.getElementById('companies-table')
    companies.forEach(company => {
        const row = companiesTable.insertRow(-1)

        const nameCell = row.insertCell(0)
        nameCell.innerText = company.name

        const taxIdCell = row.insertCell(1)
        taxIdCell.innerText = company.taxIdentificationNumber

    });
}

const serializeFormToJson = form => JSON.stringify(
    Array.from(new FormData(form).entries())
        .reduce((m, [key, value]) =>
            Object.assign(m, {[key]: value}), {})
);


function handleAddCompanyFormSubmit() {
    const form = $("#addCompanyForm");
    form.on('submit', function (e) {
        e.preventDefault();

        const csrfToken = document.cookie
            .split('; ')
            .find(row => row.startsWith('XSRF-TOKEN='))
            .split('=')[1];

        $.ajaxPrefilter(function (options, originalOptions, jqXhr) {
            jqXhr.setRequestHeader('X-XSRF-TOKEN', csrfToken);
        });

        $.ajax({
            url: 'companies',
            type: 'post',
            contentType: 'application/json',
            data: serializeFormToJson(this),
            success: function (data) {
                $("#companies-table tr:gt(0)").remove();
                loadCompanies()
            },
            error: function (jqXhr, textStatus, errorThrown) {
                alert(jqXhr.status + " " + errorThrown)
            }
        });
    });
}

window.onload = () => {
    loadCompanies();
    handleAddCompanyFormSubmit()
}