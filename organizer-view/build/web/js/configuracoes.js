$(function () {
    $(".perfil").click(function () {
        $("#trocarImgPerfil").modal();
    });

    $("#excluiConta").click(function () {
        $("#excluirModal").modal();
    });

    $("#logout").click(function () {
        $("#logoutModal").modal();
    });

    $("#selectOfItemType").click(function () {
        if ($('#selectOfItemType option:selected').val() == 'SIM') {
            document.querySelector('#dateOfItem').readOnly = true;
            $('#helpItem').show();
        } else {
            document.querySelector('#dateOfItem').readOnly = false;
            $('#helpItem').hide();
        }
    });

})
