$("#padButton").click(function () {
    $("#themeStyle").attr("href", "css/theme-default.css");
    localStorage.setItem("theme", 1);
});

$("#modButton").click(function () {
    $("#themeStyle").attr("href", "css/theme-01.css");
    localStorage.setItem("theme", 2);
});

$("#extButton").click(function () {
    $("#themeStyle").attr("href", "css/theme-02.css");
    localStorage.setItem("theme", 3);
});

$("document").ready(function() {   
    var theme = localStorage.getItem("theme");
    
    if(theme == 1){
        $("#themeStyle").attr("href", "css/theme-default.css");
    }else if(theme == 2){
        $("#themeStyle").attr("href", "css/theme-01.css");
    }else if(theme == 3){
        $("#themeStyle").attr("href", "css/theme-02.css");
    }
});


