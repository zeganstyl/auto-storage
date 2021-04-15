<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<@common.navbar/>
<div class="container">
    <div style="margin-top: 20px; margin-bottom: 20px;">
        <h4 style="display: inline-block; margin-right: 10px; margin-left: 0px;">С</h4>
        <input type="datetime-local" class="form-control" style="display: inline-block; width: auto; height: auto;">
        <h4 style="display: inline-block; margin-right: 10px; margin-left: 10px;">По</h4>
        <input type="datetime-local" class="form-control" style="display: inline-block; width: auto; height: auto;">
    </div>
    <table class="table" style="">
        <thead>
        <tr>
            <th>№</th>
            <th>Наименование</th>
            <th>Реализовано</th>
            <th>Закуплено</th><th>%</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th scope="row" style="text-decoration-line: underline; color: rgb(0, 128, 255);">1</th>
            <td>Дверное стекло</td>
            <td>10</td>
            <td>20</td><td>50%</td>
        </tr>
        <tr>
            <th scope="row" style="text-decoration-line: underline; color: rgb(0, 128, 255);">2</th>
            <td>Фара</td>
            <td>...</td>
            <td>5</td>
        </tr>
        <tr>
            <th scope="row" style="color: rgb(0, 128, 255); text-decoration-line: underline;">3</th>
            <td>...</td>
            <td>...</td>
            <td>...</td>
        </tr>
        </tbody>
    </table>
</div>
</body>
</html>
