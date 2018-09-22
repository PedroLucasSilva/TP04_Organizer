$(function () {
    $("#novaTag").click(function () {
        $("#tagsMenu").modal();
    });
});

function addTagMenu() {

    var name = document.querySelector('#name');

    if (name.value != "") {
        
        let ulTag = document.querySelector('#ulTagMenu');
        let liNome = document.createElement('li');
        let aNome = document.createElement('a');
        let label = document.createElement('label');
        let input = document.createElement('input');
        let span = document.createElement('span');

        aNome.setAttribute('href', '#');
        label.setAttribute('class', 'container');
        input.setAttribute('type', 'checkbox');
        input.setAttribute('name', 'tag');
        input.setAttribute("value", name.value);
        span.setAttribute('class', 'checkmark');

        label.innerHTML = name.value;

        label.appendChild(input);
        label.appendChild(span);
        aNome.appendChild(label);
        liNome.appendChild(aNome);
        ulTag.appendChild(liNome);
        
        let formCreateTag = document.querySelector("#formCreateTag");
        formCreateTag.action = "/organizer/servletcontroller?process=CreateTag";
        formCreateTag.submit();
    }
}
