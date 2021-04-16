<thead>
<tr class="clickableRow">
    <th>№</th>
    <th>Название</th>
    <th>Модель</th>
    <th>Марка</th>
    <th>Стоимость</th>
    <th>Поставщик</th>
</tr>
</thead>
<tbody>
<#list items as product>
<tr class="clickableRow">
    <th scope="row" value="${product.id}">
        <a href="${product.url}" target="_blank">${product.id}</a>
    </th>
    <td>${product.name}</td>
    <td>${product.model}</td>
    <td>${product.carModel}</td>
    <td>${product.cost}</td>
    <td>
        <a href="${product.provider.url}">${product.provider.name}</a>
    </td>
</tr>
</#list>
</tbody>
