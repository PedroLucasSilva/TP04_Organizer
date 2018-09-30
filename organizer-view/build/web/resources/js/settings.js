
function validateSettingFields() {

    //Não sei se precisa dos ifs, to chutando
    let name = document.querySelector("#userName");
    let password = document.querySelector("#password");

    if (name.value == "") {
        alert("O campo nome deve estar preenchido");
    } else {
        //Dá submit 
        let formSettings = document.querySelector("#formSettings");
        formSettings.action = "/organizer/servletcontroller?process=UpdateUser";
        formSettings.submit();
        return true;
    }
}

function validateFieldsChangePassword() {
    let currentPassword = document.querySelector("#currentPassword");
    let newPassword = document.querySelector("#newPassword");
    let confirmNewPassword = document.querySelector("#confirmNewPassword");
    let oldPassword = document.querySelector("#oldPassword");


    if (currentPassword.value == "" || newPassword.value == "" || confirmNewPassword.value == "") {
        alert("Por gentileza, preencha todos os campos!");

        return false;
    } else if (newPassword.value != confirmNewPassword.value) {
        alert("As senhas devem ser iguais!");
        return false;

    } else if (CryptoJS.MD5(currentPassword.value) != oldPassword.value) {
        alert("As senhas atuais não são iguais!")
        return false;
    } else {
        let password = document.querySelector("#password");
        password.value = newPassword.value;

        return true;
    }
}

function validateFieldsDeleteAccount() {

    let deleteAccountPassword = document.querySelector("#deleteAccountPassword");
    let oldPassword = document.querySelector("#oldPassword");

    if (deleteAccountPassword.value == "") {
        alert("Por gentileza, preencha todos os campos!")
        return false;
    } else if (CryptoJS.MD5(deleteAccountPassword.value) != oldPassword.value) {
        alert(deleteAccountPassword.value)
        alert(CryptoJS.MD5(deleteAccountPassword.value))
        alert("As senhas atuais não são iguais!")
        return false;
    } else {
        let formDelete = document.querySelector("#formDelete");
        formDelete.action = "/organizer/servletcontroller?process=DeleteUser";
        formDelete.submit();
        return true;
    }
}

