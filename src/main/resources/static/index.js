$(function() {
    $('#modalTable').on('click', 'tbody tr', function(event) {
      $(this).addClass('highlight').siblings().removeClass('highlight');
    });

    var chooseItemType = ""
    var chooseItemTypeOld = chooseItemType;

    $('#chooseItemModal').on('shown.bs.modal', function () {
        var xhr = new XMLHttpRequest();
        xhr.responseType = "text"
        xhr.addEventListener("load", function() {
            $("#modalTable").html(xhr.response);
        });
        xhr.open("GET", "/list/" + chooseItemType);
        xhr.send();
    });

    $('#chooseItemModal').on('hidden.bs.modal', function () {
        $("#modalAccept").unbind("click");
    });

    function getModalSelectedRow() { return $("#modalTable tbody tr.highlight") }
    function getRowId(row) { return parseInt(row.children(":nth-child(1)").attr("value")) }
    function getRowName(row) { return row.children(":nth-child(2)").text() }
    function getRowCellValue(row, cell) { return row.children(":nth-child("+cell+")").attr("value") }
    function getRowCellText(row, cell) { return row.children(":nth-child("+cell+")").text() }

    function chooseListener(div) {
         if (chooseItemTypeOld != chooseItemType) $('#modalTable').empty();
         chooseItemTypeOld = chooseItemType;

         $("#modalAccept").one("click", function() {
             var row = getModalSelectedRow()
             div.children(".input-id").attr("value", getRowId(row));
             div.children(".link-name").attr("href", "/counterparties/" + getRowId(row));
             div.children(".link-name").text(getRowName(row));
             $('#chooseItemModal').modal('hide');
         });
     }

    function chooseProvider() {
        chooseItemType = "Providers"
        chooseListener($(this).parent());
    }
    $(".chooseProvider").click(chooseProvider);

    $(".chooseClient").click(function() {
        chooseItemType = "Clients"
        chooseListener($(this).parent());
    });

    $("#submitOrder").submit(function() {
        var products = [];

        $(".productMoveRow").each(function() {
            var row = $(this);
            var product = productsList[row.index()];
            products.push({
                id: product.id,
                count: getProductCount(row),
                note: getProductNote(row),
                provider: getProductProvider(row)
            });
        });

        $("#selectedProducts").val(JSON.stringify(products));
    });

    function getProductCount(row) { return parseInt(row.children(":nth-child(3)").children().val()); }
    function getProductCost(row) { return parseInt(row.children(":nth-child(4)").text()); }
    function getProductProvider(row) { return parseInt(row.children(":nth-child(5)").children(".input-id").val()); }
    function getProductNote(row) { return row.children(":nth-child(6)").children("input").val(); }

    function updateFullCost() {
        var fullCost = 0;
        $(".productMoveRow").each(function() {
            var row = $(this);
            fullCost += getProductCost(row) * getProductCount(row);
        });
        $("#fullCost").text(fullCost);
    }

    function addProduct(product) {
        var tr = $('<tr class="productMoveRow"></tr>');
        tr.append($('<th scope="row">'+($("#productsTableBody").children().length+1)+'</th>')); // #
        tr.append($('<td class="buyProductId" value="'+product.id+'">'+product.name+'</td>')); // name

        var productCount = 1;
        if (product.count != undefined) productCount = product.count;

        var countInput = $('<input type="number" min="1" class="form-control productCount" value="'+productCount+'">')
        countInput.change(updateFullCost);
        var countInputTR = $('<td width="100px"></td>')
        countInputTR.append(countInput);
        tr.append(countInputTR); // count

        tr.append($('<td>'+product.cost+'</td>')); // cost

        var chooseButton = $('<button type="button" class="chooseProvider btn btn-outline-secondary" data-toggle="modal" data-target="#chooseItemModal">...</button>');
        chooseButton.click(chooseProvider);
        var chooseInput = $('<input type="number" class="input-id form-control" hidden value="'+product.providerId+'" required>');
        var chooseLink = $('<a class="link-name ml-3" href="'+product.providerUrl+'">'+product.providerName+'</a>')
        var providerTD = $('<td style="white-space: nowrap;"></td>');
        providerTD.append(chooseButton);
        providerTD.append(chooseInput);
        providerTD.append(chooseLink);
        tr.append(providerTD); // provider

        var note = "";
        console.log(product.note);
        if (product.note != undefined) note = product.note;
        tr.append($('<td><input class="form-control" type="text" value="'+note+'"></td>')); // note

        var deleteButton = $('<button class="btn btn-outline-secondary deleteProduct" title="Удалить">X</button>');
        deleteButton.click(function() {
            $(this).parent().parent().remove();
            updateFullCost();
        });
        var deleteButtonTD = $("<td></td>");
        deleteButtonTD.append(deleteButton);
        tr.append(deleteButtonTD);

        $("#productsTableBody").append(tr);
        updateFullCost();
    }

    if (window.addedProducts != undefined) {
        for (var i=0; i<addedProducts.length; i++) {
            addProduct(addedProducts[i])
        }
    }

    $("#addProduct").click(function() {
       chooseItemType = "Products"
       if (chooseItemTypeOld != chooseItemType) $('#modalTable').empty();
       chooseItemTypeOld = chooseItemType;

       $("#modalAccept").one("click", function() {
            var row = getModalSelectedRow()

            var id = getRowId(row);
            var product = productsList[row.index()];

            var notExists = true;
            $('.buyProductId').each(function() {
                console.log($(this).attr("value"));
                if ($(this).attr("value") == id) notExists = false;
            });
            if (notExists) {
                addProduct(product);
            }

            $('#chooseItemModal').modal('hide');
       });
    });
});
