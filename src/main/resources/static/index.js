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
        xhr.open("GET", "/list/" + chooseItemType);
        xhr.send();
    });

    function getModalSelectedRow() { return $("#modalTable tbody tr.highlight") }
    function getRowId(row) { return parseInt(row.children(":nth-child(1)").attr("value")) }
    function getRowName(row) { return row.children(":nth-child(2)").text() }
    function getRowCellValue(row, cell) { return row.children(":nth-child("+cell+")").attr("value") }
    function getRowCellText(row, cell) { return row.children(":nth-child("+cell+")").text() }

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

       $("#modalAccept").one("click", function() {
            var row = getModalSelectedRow()

            var id = getRowId(row);
            var notExists = true;
            $('.buyProductId').each(function() {
                console.log($(this).attr("value"));
                if ($(this).attr("value") == id) notExists = false;
            });
            if (notExists) {
                var tr = $('<tr class="productMoveRow"></tr>');
                tr.append($('<th scope="row">'+($("#productsTableBody").children().length+1)+'</th>')); // #
                tr.append($('<td class="buyProductId" value="'+id+'">'+getRowName(row)+'</td>')); // name
                tr.append($('<td><input type="number" min="1" class="productCount" value="1"></input></td>')); // count
                tr.append($('<td>'+getRowCellText(row, 5)+'</td>')); // cost
                tr.append($('<td value="'+getRowCellValue(row, 6)+'">'+getRowCellText(row, 6)+'</td>')); // provider

                var deleteButton = $('<button class="btn btn-outline-secondary deleteProduct" title="Удалить">X</button>');
                deleteButton.click(function() {
                    $(this).parent().parent().remove();
                });
                var deleteButtonTD = $("<td></td>");
                deleteButtonTD.append(deleteButton);
                tr.append(deleteButtonTD);

                $("#productsTableBody").append(tr);
            }

            $('#chooseItemModal').modal('hide');
       });
    });
});
