<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>

<script>
    var addedProducts = [
        <#list productMoves as move>
        {
            id: ${move.productType.id},
            name: "${move.productType.name}",
            cost: ${move.cost},
            count: ${move.count},
            note: "${move.note}",
            providerId: ${move.provider.id},
            providerName: "${move.provider.name}",
            providerUrl: "${move.provider.url}"
        },
        </#list>
    ];
</script>

<@common.head/>
<body>
<@common.navbar/>
<@common.chooseModal/>
<div class="container">
    <h1 class="mt-3" style="">${orderType.nameView}<#if order??> №${order.id}</#if></h1>
    <form id="submitOrder" action="/orders/submit/${orderType}" method="post">
        <input type="number" name="id" hidden value="${(order.id)!0}">
        <label class="mt-3">${orderType.counterpartyName}</label>
        <div>
            <button type="button" class="${orderType.chooseClass} btn btn-outline-secondary" data-toggle="modal" data-target="#chooseItemModal">...</button>
            <input name="counterparty" type="number" class="input-id form-control" hidden value="${(order.counterparty.id)!0}" required>
            <a class="link-name ml-3" href="${(order.counterparty.url)!"#"}">${(order.counterparty.name)!""}</a>
            <a style="float: right;" target="_blank" href="/counterparties/submit">${orderType.new}</a>
        </div>
        <#if orderType.showPayment>
        <label class="mt-3">Способ оплаты</label>
        <select name="paymentMethod" class="form-control" required>
            <option value="Card">По карте</option>
            <option value="Cash">Наличными</option>
        </select>
        </#if>

        <#if order??>
        <label class="mt-3">Статус заявки</label>
        <select name="status" class="form-control" required>
            <#list orderStatuses as status>
            <option value="${status}" <#if order?? && order.status == order>selected</#if>>${status.nameView}</option>
            </#list>
        </select>
        </#if>

        <label class="mt-3">Примечание</label>
        <textarea name="note" class="form-control">${(order.note)!""}</textarea>

        <#if isSupply>
        <div class="mt-3">
            <button id="supplyByOrders" type="button" class="btn btn-outline-secondary" style="width: 200px; text-align: center;">Закупить по заказам</button>
            <button id="supplyByStorage" type="button" class="btn btn-outline-secondary" style="width: 200px; text-align: center;">Пополнить запасы</button>
        </div>
        </#if>

        <@common.selectProductsTable/>

        <div>
            <button type="submit" class="btn btn-primary" style="display: inline-block; float: right; width: 200px;">Принять</button>
            <#if orderType.showPayment>
            <span>Итого к оплате:</span>
            <span id="fullCost" class="ml-2 mr-2">0</span>
            <span>₽</span>
            </#if>
        </div>
    </form>
</div>
</body>
</html>
