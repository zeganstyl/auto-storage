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
    var productsList = [
        <#list items as product>
        {
            id: ${product.id},
            name: "${product.name}",
            carModel: "${product.carModel}",
            cost: ${product.cost},
            providerId: ${product.provider.id},
            providerName: "${product.provider.name}"
        },
        </#list>
    ];
</script>
