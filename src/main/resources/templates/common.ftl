<#macro navbar>
<nav class="navbar navbar-expand-sm bg-dark navbar-dark justify-content-center">
    <a class="navbar-brand" href="/">
        <img src="/static/logo.svg" alt="Logo" style="width:40px;">
    </a>

    <ul class="navbar-nav">
        <#list navLinks as navLink>
            <li class="nav-item <#if (navLink == activeLink)>active</#if>">
                <a class="nav-link" href="${navLink.url}">${navLink.viewName}</a>
            </li>
        </#list>

        <li class="nav-item">
            <#if user??>
                <a class="nav-link" href="/logout">Выйти</a>
            <#else>
                <a class="nav-link" href="/login">Войти</a>
            </#if>
        </li>
    </ul>

    <#if user??>
        <a href="#">${user.roleView}</a>
    </#if>
</nav>
</#macro>

<#macro head>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="Anton Trushkov (Zeganstyl)">

    <title>АвтоСклад</title>
    <link href="/static/logo.svg" rel="shortcut icon" type="image/png">

    <!-- Bootstrap core CSS -->
    <link href="/static/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/static/narrow-jumbotron.css" rel="stylesheet">

    <script src="/static/jquery-3.6.0.min.js"></script>
    <script src="/static/popper.min.js"></script>
    <script src="/static/bootstrap.min.js"></script>

    <link href="/static/index.css" rel="stylesheet">
    <script src="/static/index.js"></script>
</head>
</#macro>

<#macro chooseModal>
<div class="modal fade" id="chooseItemModal" tabindex="-1" role="dialog" aria-labelledby="chooseItemLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="chooseItemLabel">Выбрать</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <table id="modalTable" class="table table-hover" style="max-height: 600px;"></table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                <button id="modalAccept" type="button" class="btn btn-primary">Принять</button>
            </div>
        </div>
    </div>
</div>
</#macro>
