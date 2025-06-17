<!DOCTYPE html>
<html>
<head>
    <title>Магазин Музыки</title>
</head>
<head>
    <h1>Музыкальный Калейдоскоп</h1><hr>
    <title>Редактировать</title>
</head>
<body>
<h1>Редактировать</h1>
<form action="/product/update/${product.id}" method="post">
    <label for="title">Название альбома:</label>
    <input type="text" id="title" name="title" value="${product.title}" required><br><br>

    <label for="description">Описание:</label>
    <textarea id="description" name="description" required>${product.description}</textarea><br><br>

    <label for="price">Цена:</label>
    <input type="number" id="price" name="price" step="0.01" value="${product.price?string('0.00')}" required><br><br>

    <label for="genre">Жанр:</label>
    <input type="text" id="genre" name="genre" value="${product.genre}" required><br><br>

    <label for="author">Исполнитель:</label>
    <input type="text" id="author" name="author" value="${product.author}" required><br><br>

    <button type="submit">Сохранить изменения</button>
    <a href="/product/${product.id}">
        <button type="button">Отмена</button>
    </a>
</form>
</body>
</html>
