<!DOCTYPE html>
<#import "/common.ftl" as common>
<html>
<@common.head/>
<body>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="row justify-content-md-center">
            <div class="account-wall">
                <div id="my-tab-content" class="tab-content">
                    <div id="login">
                        <img class="profile-img img-fluid rounded-circle" src="/static/user.svg" alt="">
                        <form class="form-signin" action="/login" method="post">
                            <select name="login" class="form-control" required autofocus>
                                <option value="Seller">Продавец</option>
                                <option value="Manger">Менеджер</option>
                                <option value="StorageKeeper">Кладовщик</option>
                                <option value="Root">Админ</option>
                            </select>
                            <input name="path" hidden class="form-control" value="${path}">
                            <input type="submit" class="btn btn-lg btn-default btn-block" value="Войти">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
