
function validateFields(){
    let email = document.querySelector("#email");
    let name = document.querySelector("#name");
    let password = document.querySelector("#password");
    let confirmPassword = document.querySelector("#confirm-password");
    
    if((email.value == "") || (name.value == "") || (password.value == "") || (confirmPassword.value == "")){
        alert("Por gentileza, preencha todos os campos!")
        return false;
    }
    if(password.value != confirmPassword.value){
        alert("As senhas devem ser iguais!");
        password.style.borderColor = "#fc1616";
        confirmPassword.style.borderColor = "#fc1616";
        return false;
    }else{
        let formRegister = document.querySelector("#formRegister");
        formRegister.action = "/organizer/servletcontroller?process=CreateUser";
        formRegister.submit();
        return true;
    }

}


