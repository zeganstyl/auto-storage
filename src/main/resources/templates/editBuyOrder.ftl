<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<@common.chooseModal/>
<div class="container">
    <h1 class="mt-3" style="">Заявка на покупку №123123</h1>
    <form>
        <label class="mt-3">Клиент</label>
        <div>
            <button id="chooseClient" type="button" class="btn btn-outline-secondary" data-toggle="modal" data-target="#chooseItemModal">...</button>
            <input name="clientId" type="number" class="input-id form-control" hidden value="${(order.counterparty.id)!0}" required>
            <a class="link-name ml-3" href="${(order.counterparty.url)!"#"}">${(order.counterparty.name)!""}</a>
            <a style="float: right;" href="/counterparties/submit">Новый клиент</a>
        </div>
        <label class="mt-3">Способ оплаты</label>
        <select class="form-control" required>
            <option value="value1">По карте</option>
            <option value="value2">Наличными</option>
        </select>

        <div class="mt-3">
            <button id="addProduct" type="button" class="btn btn-outline-secondary" style="width: 200px; text-align: center;" data-toggle="modal" data-target="#chooseItemModal">Добавить покупку</button>
            <a href="/products/submit" target="_blank" style="float: right;">Новый тип товара</a>
        </div>

        <table class="table mt-3" style="">
            <thead>
            <tr>
                <th>#</th>
                <th>Товар</th>
                <th>Штук</th>
                <th>Стоимость, ₽</th>
                <th>Поставщик</th>
                <th></th>
            </tr>
            </thead>
            <tbody id="productsTableBody">
                <#list productMoves as productMove>
                    <tr class="productMoveRow">
                        <th scope="row" value="${productMove.id}">1</th>
                        <td value="${productMove.productType.id}">${productMove.productType.name}</td>
                        <td>${productMove.count}</td>
                        <td>${productMove.productType.cost}</td>
                        <td value="${productMove.provider.id}">${productMove.provider.name}</td>
                        <td>
                            <button class="btn btn-outline-secondary deleteProduct" title="Удалить">X</button>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>

        <div>
            <button type="submit" class="btn btn-primary" style="display: inline-block; float: right; width: 200px;">Принять</button>
            <h3>Итого к оплате: 10000 Р</h3>
        </div>
    </form>
</div>
</body>
</html>
