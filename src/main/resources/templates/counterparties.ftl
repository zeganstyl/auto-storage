<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<div class="container">
    <div class="mt-3 mb-3">
        <a href="/counterparties/submit">Добавить контрагента</a>
    </div>
    <table class="table" style="">
        <thead>
        <tr>
            <th>№</th>
            <th>Имя</th>
            <th>Тип</th>
            <th>Телефон</th>
            <th>Email</th>
            <th>Примечание</th>
        </tr>
        </thead>
        <tbody>
        <#list counterparties as counterparty>
            <tr>
                <th scope="row">
                    <a href="${counterparty.url}">${counterparty.id}</a>
                </th>
                <td>${counterparty.name}</td>
                <td>${counterparty.type.nameView}</td>
                <td>${counterparty.phone}</td>
                <td>${counterparty.email}</td>
                <td>${counterparty.note}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>
