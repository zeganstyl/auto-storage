<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<div class="container">
    <div>
        <div class="dropdown" style="width: 200px; display: inline-block; margin-top: 20px; margin-bottom: 20px;">
            <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Создать заявку...
            </button>
            <div class="dropdown-menu">
                <#list orderTypes as type>
                <a class="dropdown-item" href="/orders/submit/${type}">${type.nameView}</a>
            </#list>
        </div>
    </div>
</div>
<table class="table" style="">
    <thead>
    <tr>
        <th>№</th>
        <th>Контрагент</th>
        <th>Тип</th>
        <th>Статус</th>
        <th>Создано</th>
        <th>Завершено</th>
    </tr>
    </thead>
    <tbody>
    <#list orders as order>
    <tr>
        <td scope="row">
            <a href="${order.url}">${order.id}</a>
        </td>
        <td>${order.counterparty.name}</td>
        <td>${order.type.nameView}</td>
        <td>${order.status.nameView}</td>
        <td>${order.createTimeView}</td>
        <td>${order.completionTimeView}</td>
    </tr>
    </#list>
    </tbody>
</table>
</div>
</body>
</html>
