<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<div class="container">
    <table class="table" style="">
        <thead>
        <tr>
            <th>№</th>
            <th>Наименование</th>
            <th>Ячейка</th>
            <th title="Заявка приемки">Принят по</th>
            <th>Примечание</th>
        </tr>
        </thead>
        <tbody>
        <#list products as product>
        <tr>
            <th scope="row">
                <a href="${product.url}">${product.id}</a>
            </th>
            <td>${product.type.name}</td>
            <td>${product.cell}</td>
            <td>
                <a href="${product.acceptanceOrder.url}">${product.acceptanceOrder.id}</a>
            </td>
            <td>${product.note}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>
