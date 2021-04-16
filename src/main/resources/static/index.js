$(function() {
    $('#modalTable').on('click', 'tbody tr', function(event) {
      $(this).addClass('highlight').siblings().removeClass('highlight');
    });

    var chooseItemType = ""

    $('#chooseItemModal').on('shown.bs.modal', function () {
        var xhr = new XMLHttpRequest();
        xhr.responseType = "text"
        xhr.addEventListener("load", function() {
            $("#modalTable").html(xhr.response);
        });
        xhr.open("GET", "/counterparties/type/" + chooseItemType);
        xhr.send();
    });

    function getModalSelectedRow() { return $("#modalTable tbody tr.highlight") }
    function getRowId(row) { return parseInt(row.children(":nth-child(1)").attr("value")) }
    function getRowName(row) { return row.children(":nth-child(2)").text() }

    function chooseListener(div) {
         $("#modalAccept").one("click", function() {
             var row = getModalSelectedRow()
             div.children(".input-id").attr("value", getRowId(row));
             div.children(".link-name").attr("href", "/counterparties/" + getRowId(row));
             div.children(".link-name").text(getRowName(row));
             $('#chooseItemModal').modal('hide');
         });
     }

    $("#chooseProvider").click(function() {
        chooseItemType = "Providers"
        chooseListener($(this).parent());
    });

    $("#chooseClient").click(function() {
        chooseItemType = "Clients"
        chooseListener($(this).parent());
    });

    $("#addProduct").click(function() {
        chooseItemType = "Products"
        chooseListener($(this).parent());
    });
});
