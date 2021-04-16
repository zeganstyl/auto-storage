<thead>
<tr class="clickableRow">
    <th>№</th>
    <th>Имя</th>
    <th>Тип</th>
    <th>Телефон</th>
    <th>Эл.почта</th>
</tr>
</thead>
<tbody>
    <#list items as counterparty>
        <tr class="clickableRow">
            <th scope="row" value="${counterparty.id}">
                <a href="${counterparty.url}" target="_blank">${counterparty.id}</a>
            </th>
            <td>${counterparty.name}</td>
            <td>${counterparty.type.nameView}</td>
            <td>${counterparty.phone}</td>
            <td>${counterparty.email}</td>
        </tr>
    </#list>
</tbody>
