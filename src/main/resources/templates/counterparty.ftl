<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<div class="container">
    <h1 style="">Контрагент</h1>

    <form style="width: 100%" method="post" action="/counterparties/submit">
        <input name="id" value="${(counterparty.id)!0}" hidden>
        <div class="mb-3" style="">
            <label>Имя</label>
            <input name="name" type="text" class="form-control" required value="${(counterparty.name)!""}">
        </div>
        <div class="mb-3" style="">
            <label>Эл. почта</label>
            <input name="email" type="text" class="form-control" value="${(counterparty.email)!""}">
        </div>
        <div class="mb-3" style="">
            <label>Телефон</label>
            <input name="phone" type="text" class="form-control" value="${(counterparty.phone)!""}">
        </div>
        <div class="mb-3" style="">
            <div class="mb-3" style="">
                <label>Тип</label>
                <select name="type" class="form-control">
                    <#list counterpartyTypes as type>
                        <option value="${type}" <#if counterparty?? && counterparty.type == type>selected</#if>>${type.nameView}</option>
                    </#list>
                </select>
            </div>
            <label>Примечание</label>
            <textarea name="note" class="form-control">${(counterparty.note)!""}</textarea>
        </div>
        <div>
            <button style="float: right; width: 200px;" class="btn btn-primary">Принять</button>
        </div>
    </form>
</div>
</body>
</html>
