document.addEventListener('DOMContentLoaded', function () {
    var loginBtn = document.getElementById('loginBtn');
    var loginContainer = document.getElementById('loginContainer');

    loginBtn.addEventListener('click', function () {
        // Alternar la visibilidad del contenedor de inicio de sesión
        if (loginContainer.style.display === 'block') {
            loginContainer.style.display = 'none';
        } else {
            loginContainer.style.display = 'block';
        }
    });
});
