<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<@common.chooseModal/>
<div class="container">
    <h1 class="mt-3">Тип товара<#if product??> №${product.id}</#if></h1>

    <form action="/products/submit" method="post">
        <input name="id" type="number" class="form-control" hidden value="${(product.id)!0}" required>
        <label class="mt-3">Название</label>
        <input name="name" type="text" required class="form-control" value="${(product.name)!""}">
        <label class="mt-3">Модель</label>
        <input name="model" type="text" required class="form-control" value="${(product.model)!""}">
        <label class="mt-3">Модель автомобиля</label>
        <input name="carModel" type="text" class="form-control" value="${(product.carModel)!""}">
        <label class="mt-3">Требуемое минимальное количество на складе</label>
        <input name="minRequired" type="number" min="0" class="form-control" value="${(product.minRequired)!0}">
        <label class="mt-3">Стоимость</label>
        <input name="cost" type="number" class="form-control" value="${(product.cost)!0}">
        <label class="mt-3">Поставщик</label>
        <div class="mt-3">
            <button type="button" class="chooseProvider btn btn-outline-secondary" data-toggle="modal" data-target="#chooseItemModal">...</button>
            <input name="providerId" type="number" class="input-id form-control" hidden value="${(product.provider.id)!0}" required>
            <a class="link-name ml-3" href="${(product.provider.url)!"#"}">${(product.provider.name)!""}</a>
            <a style="float: right;" href="/counterparties/submit">Новый поставщик</a>
        </div>
        <label class="mt-3">Примечание</label>
        <textarea name="note" class="form-control">${(product.note)!""}</textarea>
        <div class="mt-3">
            <button type="submit" style="float: right; width: 200px;" class="btn btn-primary">Принять</button>
        </div>
    </form>
</div>
</body>
</html>
