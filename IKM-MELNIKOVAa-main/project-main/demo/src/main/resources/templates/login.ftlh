<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Магазин Музыки. Вход/Регистрация</title>
</head>
<body>
<h2>Вход</h2>
<form action="/login" method="post">
    <label for="username">Имя пользователя:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required><br><br>

    <button type="submit">Войти</button>
</form>

<p><a href="/register">Зарегистрироваться</a></p>
</body>
</html>
