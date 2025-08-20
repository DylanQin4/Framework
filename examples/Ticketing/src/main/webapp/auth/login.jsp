<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String message = "";
    if(request.getAttribute("message") != null) message = (String) request.getAttribute("message");

    String errors = "";
    if(request.getAttribute("errors") != null) errors = (String) request.getAttribute("errors");

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
    <h2 class="mb-4">Connexion</h2>

    <c:if test="${not empty errors}">
        <div style="color: red;">
            ${errors}
        </div>
    </c:if>

    <c:if test="${not empty message}">
        <div style="color: green;">
            ${message}
        </div>
    </c:if>

    <form action="login" method="post" class="text-start">
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="text" class="form-control" id="email" name="email" required value="admin@example.com">
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Mot de passe</label>
            <input type="password" class="form-control" id="password" name="pwd" required value="admin123">
        </div>
        <button type="submit" class="btn btn-primary w-100">Se connecter</button>
        
        <div class="mt-3 text-center">
            Vous n'êtes pas inscrit ?  <a href="registration">Créer un compte</a>
        </div>
    </form>
</div>
</body>
</html>
