<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<div class="container">
    <form id="submitStatTimeRange" action="/stats" method="get" style="margin-top: 20px; margin-bottom: 20px;">
        <h4 style="display: inline-block; margin-right: 10px; margin-left: 0px;">С</h4>
        <input id="fromLocal" type="datetime-local" value="${fromTime}" class="form-control" style="display: inline-block; width: auto; height: auto;">
        <input name="from" id="from" type="number" hidden>
        <h4 style="display: inline-block; margin-right: 10px; margin-left: 10px;">По</h4>
        <input id="toLocal" type="datetime-local" value="${toTime}" class="form-control" style="display: inline-block; width: auto; height: auto;">
        <input name="to" id="to" type="number" hidden>
        <button type="submit" class="btn btn-primary">Применить</button>
    </form>
    <table class="table" style="">
        <thead>
        <tr>
            <th>№</th>
            <th>Наименование</th>
            <th>Реализовано</th>
            <th>Закуплено</th>
            <th>%</th>
        </tr>
        </thead>
        <tbody>
        <#list stats as stat>
        <tr>
            <th scope="row">
                <a href="${stat.type.url}">${stat.type.id}</a>
            </th>
            <td>${stat.type.name}</td>
            <td>${stat.sold}</td>
            <td>${stat.purchased}</td>
            <td>${stat.percents}%</td>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>
