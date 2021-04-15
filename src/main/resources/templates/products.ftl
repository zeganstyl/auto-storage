<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<div class="container-md">
    <div class="mt-3 mb-3">
        <a href="/products/submit">Добавить тип товара</a>
    </div>
    <table class="table" style="">
        <thead>
        <tr>
            <th>№</th>
            <th>Название</th>
            <th>Модель</th>
            <th>Марка</th>
            <th>Поставщик</th>
            <th>Стоимость</th>
            <th>Мин.</th>
            <th>Макс.</th>
        </tr>
        </thead>
        <tbody>
        <#list products as product>
        <tr>
            <th scope="row">
                <a href="${product.url}">${product.id}</a>
            </th>
            <td>${product.name}</td>
            <td>${product.model}</td>
            <td>${product.carModel}</td>
            <td>
                <a href="${product.provider.url}">${product.provider.name}</a>
            </td>
            <td>${product.cost}</td>
            <td>${product.minRequired}</td>
            <td>${product.maxRequired}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>
