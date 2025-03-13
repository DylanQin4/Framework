<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String error = "";
    if(request.getAttribute("error") != null) error = (String) request.getAttribute("error");
%>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f8f9fa;
        }
        .login-container {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }
    </style>
</head>
<body>
<div class="login-container text-center">
    <h2 class="mb-4">Inscription</h2>

    <form action="register" method="POST" class="text-center">
        <c:if test="${not empty error}">
            <div style="color: red;" class="text-center w-full mb-2">
                ${error}
            </div>
        </c:if>
        <div class="mb-2 text-start">
            <label for="username" class="form-label">Nom d'utilisateur</label>
            <input type="text" class="form-control" id="username" name="username" required value="${inputValues.username}">
            <c:if test="${not empty errors.username}">
                <span style="color: red;">${errors.username}</span>
            </c:if>
        </div>
        <div class="mb-2 text-start">
            <label for="email" class="form-label">Email</label>
            <input type="text" class="form-control" id="email" name="email" required value="${inputValues.email}">
            <c:if test="${not empty errors.email}">
                <span style="color: red;">${errors.email}</span>
            </c:if>
        </div>
        <div class="mb-2 text-start">
            <label for="password" class="form-label">Mot de passe</label>
            <input type="password" class="form-control" id="password" name="pwd" required value="${inputValues.pwd}">
            <c:if test="${not empty errors.pwd}">
                <span style="color: red;">${errors.pwd}</span>
            </c:if>
        </div>
        <button type="submit" class="btn btn-primary w-100 mt-2">Se connecter</button>

        <div class="mt-3 text-center">
            Vous avez deja un compte ?&nbsp;
            <a href="login">Se connecter</a>
        </div>
    </form>
</div>
</body>
</html>
