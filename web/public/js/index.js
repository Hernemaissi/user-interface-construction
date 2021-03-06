$(document).ready(function() {
    var loanAmountInput = $("#inputLoanAmount");
    hideFormErrors();

    $("#loanForm").submit(function() {
        hideFormErrors();
        if ($(loanAmountInput).val() === '') {
            $(loanAmountInput).parents(".control-group").addClass("error");
            $("#formErrors").show();
            $(loanAmountInput).parents(".controls").find(".help-inline").show();
            return false;
        }
        $.each($("input[value!='']"), function(i, v) {
            $(v).parents("#advancedInputs").addClass("in").css("height", "auto");
        })
        $.ajax({
            url: window.manageme.postFormUrl(),
            type: 'POST',
            data: $("#loanForm").serializeArray()
        }).fail(function(error){
            $.each(jQuery.parseJSON(error.responseText), function(i, v) {
                $("#" + v.key).parents(".controls").find(".help-inline").text(v.message).show();
                $("#" + v.key).parents(".control-group").addClass("error");
                $("#" + v.key).parents("#advancedInputs").addClass("in").css("height", "auto");
            })
        }).success(function(results) {
            var loanTableTableTBody = $("table#loanTable").children("tbody");
            $(loanTableTableTBody).empty();

            if (results.suggested === "bankAccount") {
                $("#suggested .lead").text("Use money on your bank account.");
            } else if (results.suggested === "both") {
                $("#suggested .lead").text("Money on your bank account is not enough, you need to take a loan.");
            } else {
                //$("#suggested .lead").text("Loan.");
                $("#suggested .lead").text("");
            }

            if (typeof(results.loanMap) != "undefined") {
                var loanAmount = results.loanAmount;
                $("#resultAmount").text(new Number(loanAmount).formatMoney(2, '.', ' ') + " €");
                $("#resultBank").text($("#inputBank option:selected").text());

                $.each(results.loanMap, function(i, v) {
                    console.log("loanMap " + i + ": " + v);
                    $("<tr><td>" + i + " months</td>"
                        + "<td>" + v.INTEREST_RATE + " %</td>"
                        + "<td>" + v.OPENING_FEE.formatMoney(2, '.', ' ') + " €</td>"
                        + "<td>" + v.TRANSACTION_FEE.formatMoney(2, '.', ' ') + " €</td>"
                        + "<td>" + v.MONTHLY_PAYMENT.formatMoney(2, '.', ' ') + " €</td>"
                        + "<td>" + v.TOTAL_AMOUNT.formatMoney(2, '.', ' ') + " €</td>"
                        + "</tr>").appendTo(loanTableTableTBody);
                });
                $("#loanResults").show();
            } else {
                $("#loanResults").hide();
            }
            var bonusCardTableTBody = $("table#bonusCards").children("tbody");
            $(bonusCardTableTBody).empty();
            $.each(results.bonusCardAmounts, function(i, v) {
                $("<tr><td>" + i + "</td><td>" + v.formatMoney(2, '.', ' ') + " €</td></tr>").appendTo(bonusCardTableTBody);
            });
            $("#results").show();
            $('html, body').animate({ scrollTop: $('#results').offset().top }, 'slow');
        });
        return false;
    });

    function hideFormErrors() {
        $("#formErrors").hide();
        $("#results").hide();
        $(".controls").find(".help-inline").hide();
        $(".control-group").removeClass("error");
    }

    Number.prototype.formatMoney = function(c, d, t) {
        var n = this, c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "," : d, t = t == undefined ? "." : t, s = n < 0 ? "-" : "", i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", j = (j = i.length) > 3 ? j % 3 : 0;
        return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
    };
});