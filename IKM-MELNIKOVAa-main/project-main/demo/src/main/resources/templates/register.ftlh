<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Музыкальный Магазин. Регистрация</title>
</head>
<body>
<h2>Регистрация</h2>
<form action="/register" method="post">
    <label for="username">Имя пользователя:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required><br><br>

    <button type="submit">Зарегистрироваться</button>
</form>
<p><a href="/login">Войти</a></p>
</body>
</html>
