<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<section id="marketing" style="margin-right: 0px; margin-left: 0px;">
    <div class="container">
        <div class="row">
            <div>
                <div class="dropdown" style="width: 200px; display: inline-block; margin-top: 20px; margin-bottom: 20px;">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Создать заявку на...
                    </button>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="/orders/submit/Buy">Покупку</a>
                        <a class="dropdown-item" href="/orders/submit/Return">Возврат</a>
                        <a class="dropdown-item" href="/orders/submit/Receive">Приемку</a>
                        <a class="dropdown-item" href="/orders/submit/ReturnToProvider">Заказ у поставщика</a>
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
                    </tr>
                </thead>
                <tbody>
                    <#list orders as order>
                        <tr>
                            <td scope="row" style="text-decoration-line: underline; color: rgb(0, 128, 255);">
                                <a href="${order.url}">order.id</a>
                            </td>
                            <td>
                                <a href="${order.counterparty.url}">${order.counterparty.name}</a>
                            </td>
                            <td>order.typeView</td>
                            <td>order.statusView</td>
                            <td>order.createTimeView</td>
                        </tr>
                    </#list>
                </tbody>
            </table>
        </div>
    </div>
</section>
</body>
</html>
