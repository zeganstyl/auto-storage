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
                <#if product.provider??>
                    <a href="${product.provider.url}" target="_blank" value="${product.provider.id}">${product.provider.name}</a>
                </#if>
            </td>
        </tr>
    </#list>
</tbody>
<script>
    productsList = [
        <#list items as product>
        {
            id: ${product.id?c},
            name: "${product.name}",
            cost: ${product.cost?c},
            providerId: ${product.provider.id?c},
            providerName: "${product.provider.name}",
            providerUrl: "${product.provider.url}"
        },
        </#list>
    ];
</script>
