$("[name='tag']").click(sendTagsAndTypes);
$("[name='tipo']").click(sendTagsAndTypes);
$(document).on('input', "[name='pesquisar']", search);

//função para enviar as tags e tipos selecionados para a Servlet
function sendTagsAndTypes(){
    var form = document.createElement("form");
    form.setAttribute('id',"formSendFilter");
    
    form.setAttribute('method',"POST");
    var caminhourl = "servletcontroller?process=ItemFilter";
    form.setAttribute('action', caminhourl);
    
    var li = $("#ulTypes > li");
    for(var i = 0; i < li.length; i++){
        form.appendChild(li[i]);
    }
    
    li = $("#ulTagMenu > li");
    for(var i = 0; i < li.length; i++){
        form.appendChild(li[i]);
    }
    document.getElementsByTagName('body')[0].appendChild(form);
    document.getElementById("formSendFilter").style.display="none";
    form.submit();
}

//function to filter the item list in the main page by the name
function search(){
    //search in ul
    var filter, list, li, i, value;
    value = $("[name='pesquisar']").val();
    filter = value.toLowerCase().trim();
    list = document.getElementById("ulItens");
    li = $("#ulItens > li");
    //loop through the list direct li's and hide those who don't match the search query
    for (i = 0; i < li.length; i++) {
        var div = li[i].getElementsByTagName("div");
        if (div[0]){
            if (div[0].innerHTML.toLowerCase().indexOf(filter) > -1) {
                li[i].style.display = "";
            } else {
                li[i].style.display = "none";
            }
        }
    }
}