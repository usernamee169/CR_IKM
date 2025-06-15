<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<title>Магазин Музыки</title>
<body>
<h1>Музыкальный Калейдоскоп</h1>
<div style="text-align: left;">
    <form action="/logout" method="post">
        <button type="submit">Выйти</button>
    </form>
</div>
<hr>
<h4>Музыкальные альбомы со всего мира</h4>

<!-- Форма поиска -->
<form action="/" method="get">
    Поиск по названию альбома:
    <input type="text" name="title" maxlength="100" placeholder="Введите название" required/><br><br>
    <input type="submit" value="Пoиcк"/>
</form>
<br>
<hr>
<br>
<!-- Форма для просмотра всех объявлений -->
<form action="/" method="GET">
    <input type="submit" value="Посмотреть все альбомы"/>
</form>

<!-- Список альбомов -->
<#if products?has_content>
<#list products as product>
<div>
    <p>
        <b>${product.title}</b> ${product.price?string("0.##")} руб. |
        <a href="/product/${product.id}">Подробнее...</a>
    </p>
</div>
</#list>
<#else>
<h3>Альбомов нет</h3>
</#if>
<hr>

<!-- Форма создания нового альблма -->
<h3>Добавить новый альбом</h3>
<form action="/product/create" method="post" enctype="multipart/form-data">
    Название альбома:
    <input type="text" name="title" maxlength="100" placeholder="Введите название" required/><br><br>

    Описание:
    <textarea name="description" maxlength="500" placeholder="Введите описание" required></textarea><br><br>

    Цена:
    <input type="number" name="price" min="0" max="1000000" step="0.01" required/><br><br>

    Жанр:
    <input type="text" name="genre" maxlength="100" placeholder="Введите жанр" required/><br><br>

    Исполнитель:
    <input type="text" name="author" maxlength="100" placeholder="Введите исполнителя" required/><br><br>

    Обложка альбома:
    <input type="file" name="file1" accept="image/*"/><br><br>

    <input type="submit" value="Добавить альбом"/>
</form>
</body>
</html>
