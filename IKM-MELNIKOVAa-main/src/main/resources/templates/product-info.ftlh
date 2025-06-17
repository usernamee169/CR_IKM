<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Музыкальный Магазин</title>
</head>
<body>
<h1>Музыкальный Калейдоскоп</h1><hr>
<h4>Информация о продукте</h4>

<#if images?has_content>
<#list images as img>
<img src="/images/${img.id}" class="product-image" height="180px" alt="${product.title!''}"/><br><br>
</#list>
<#else>
<p>Изображений нет.</p>
</#if>

<b>Название альбома:</b> ${product.title!''}<br>
<b>Описание:</b> ${product.description!''}<br>
<b>Цена:</b> ${product.price?string('0.00')}<br>
<b>Жанр:</b> ${product.genre!''}<br>
<b>Исполниитель:</b> ${product.author!''}<br>
<hr>

<!-- Кнопка для редактирования объявления -->
<a href="/product/edit/${product.id}">
    <button>Редактировать</button>
</a>

<br><br>

<form action="/" method="GET">
    <input type="submit" value="Вернуться на главную"/>
</form><br>

<form action="/product/delete/${product.id}" method="post" onsubmit="return confirm('Вы уверены, что хотите удалить этот альбом?');">
    <input type="submit" value="Удалить альбом"/>
</form>
</body>
</html>
