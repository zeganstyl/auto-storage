<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<@common.chooseModal/>
<div class="container">
    <h1 class="mt-3">Продукат на складе №${product.id}</h1>

    <form action="${product.url}" method="post">
        <input name="id" type="number" class="form-control" hidden value="${(product.id)!0}" required>
        <div>
            Товар
            <a href="${product.type.url}">№${product.type.id} - ${product.type.name}</a>
        </div>
        <div>
            Заявка приемки:
            <a href="${product.acceptanceOrder.url}">${product.acceptanceOrder.id}</a>
        </div>
        <label class="mt-3">Ячейка</label>
        <input name="cell" type="text" required class="form-control" value="${product.cell}">
        <label class="mt-3">Примечание</label>
        <textarea name="note" class="form-control">${product.note}</textarea>
        <div class="mt-3">
            <button type="submit" style="float: right; width: 200px;" class="btn btn-primary">Принять</button>
        </div>
    </form>
</div>
</body>
</html>
