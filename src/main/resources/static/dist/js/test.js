var screen_width = $(window).width(),
    screen_height = window.innerHeight && window.innerHeight < $(window).height() ? window.innerHeight : $(window).height(),
    deviceAgent = navigator.userAgent.toLowerCase(), cpay_ifr_width = 450, cpay_ifr_height = screen_height,
    cpay_ifr_windowHeight = 0, cpay_ifr_top = 0, http_referer = $(location).attr("href"), cPayData = "",
    cpay_val_to_int = function (e) {
        return result = e.toString(), result = result.replace(/([^0-9\.]+)/g, ""), result = parseInt(result), result
    }, CreateCpayLayer = function (e) {
        var a = cpay_ifr_width + "px", t = "100%";
        "card" == e.PCD_PAY_TYPE && "AUTH" != e.PCD_PAY_WORK && "01" != e.PCD_CARD_VER && (cpay_ifr_width = 692, cpay_ifr_top = (screen_height - (cpay_ifr_height = 500)) / 2, a = "692px", t = "550px");
        var i = "";
        i += '<div id="layer_cpay" name="layer_cpay" style="position:absolute;z-index:16777270;top:0;left:0;margin-top:0px;margin-left:0px;">', i += '<iframe id="cpay_ifr" name="cpay_ifr" style="width:' + a + "; height:" + t + "; top:" + cpay_ifr_top + '; position:absolute; z-index:16777271; background:white;" frameborder="0" scrolling="auto"></iframe>', i += "</div>", $(i).appendTo("body")
    }, PaypleCpayPopup = function (e, a) {
        cPayData = e;
        var t = $("<form></form>");
        t.attr("id", "CpayForm"), t.attr("method", "post"), t.attr("action", a);
        var i = "";
        if (e.PCD_HTTP_REFERER = http_referer, "CERT" == e.PCD_PAY_WORK || "PAY" == e.PCD_PAY_WORK) {
            if (isNaN(e.PCD_PAY_TOTAL)) return alert("결제금액 형식이 바르지 않습니다."), !1;
            var r = cpay_val_to_int(e.PCD_PAY_TOTAL);
            if (r < 1e3) return alert("결제금액은 1,000원 이상 이어야 합니다."), !1;
            e.PCD_PAY_TOTAL = r
        }
        if ($.each(e, function (e, a) {
            "PCD_PAY_GOODS" == e && (a = a.replace("&", "&amp;").replace("#", "&#35;").replace("<", "&lt;").replace(">", "&gt;").replace(/"/g, "&quot;").replace("\\", "&#39;").replace("%", "&#37;").replace("(", "&#40;").replace(")", "&#41;").replace("+", "&#43;").replace("/", "&#47;").replace(".", "&#46;")), i += '<input type="hidden" name="' + e + '" value="' + a + '">'
        }), t.append(i), e.PCD_RST_URL || (e.PCD_RST_URL = ""), -1 != e.PCD_RST_URL.indexOf("http://") || -1 != e.PCD_RST_URL.indexOf("https://")) t.appendTo($(document.body)).submit(); else if (deviceAgent.match(/(iphone|ipod|ipad|android)/)) {
            if (null == window.open("", "cpayWinOpen", "width:450px,height:100%,toolbars=no,menubars=no,status=no,resizable=no,location=no")) return alert(" 팝업이 차단되어 결제를 진행할 수 없습니다. \r\n 폰 설정에서 팝업차단을 풀어주세요. "), !1;
            t.attr("target", "cpayWinOpen"), t.appendTo($(document.body)), t.submit()
        } else t.attr("target", "cpay_ifr"), t.appendTo($(document.body)).submit(), layer_ifr_resize();
        t.remove()
    }, layer_ifr_resize = function () {
        var e = $(window).width(), a = $(document).height(), t = e / 2 - cpay_ifr_width / 2, i = cpay_ifr_top;
        $("#layer_cpay").css({
            position: "fixed",
            "z-index": 16777270,
            top: 0,
            left: 0,
            width: e,
            height: a,
            display: "block",
            background: "url(https://testcpay.payple.kr/img/background.png)",
            "background-repeat": "no-repeat",
            "background-size": "100% 100%",
            fliter: "alpha(opacity=50)"
        }), $("#cpay_ifr").css({left: t + "px", top: i, height: cpay_ifr_height})
    }, MainBodyResize = function (e) {
        $("body").css({height: e + 200}), deviceAgent.match(/(iphone|ipod|ipad|android)/) || ($("#layer_cpay").css({
            left: "0px",
            width: $(window).width(),
            height: e,
            display: "block",
            background: "url(https://testcpay.payple.kr/img/background.png)",
            "background-repeat": "no-repeat",
            "background-size": "100% 100%",
            fliter: "alpha(opacity=50)"
        }), $("#cpay_ifr").css({height: e}))
    }, PaypleCpayAuthCheck = function (e) {
        CreateCpayLayer(e);
        var a = new FormData, t = "", i = e.payple_dir_path && "" != e.payple_dir_path ? e.payple_dir_path : "",
            r = e.payple_auth_file && "" != e.payple_auth_file ? e.payple_auth_file : "payple_payAuth.html";
        a.append("payple_dir_path", i),
            e.PCD_CST_ID && "" != e.PCD_CST_ID ? (a.append("cst_id", e.PCD_CST_ID),
                a.append("custKey", e.PCD_CUST_KEY),
                t = e.PCD_AUTH_URL) : t = -1 != r.indexOf("/") && 0 == r.indexOf("/") ? r : i + "/cPayPayple/" + r,
            $.ajax({
                type: "POST",
                cache: !1,
                processData: !1,
                url: t,
                contentType: !1,
                async: !1,
                dataType: "json",
                data: a,
                crossDomain: !0,
                success: function (a) {
                    if ("success" != a.result) return alert(a.result_msg), !1;
                    e.PCD_CST_ID = a.cst_id, e.PCD_CUST_KEY = a.custKey, e.PCD_AUTH_KEY = a.AuthKey, PaypleCpayPopup(e, a.return_url)
                },
                error: function (e, a, t) {
                }
            })
    }, PaypleLinkCpayAuthCheck = function () {
        var e = new FormData, a = $("#CpayForm")[0];
        $.ajax({
            type: "POST",
            cache: !1,
            processData: !1,
            url: "/php/link/payAuth.php",
            contentType: !1,
            async: !1,
            dataType: "json",
            data: e,
            success: function (e) {
                if ("success" != e.result) return alert(e.result_msg), !1;
                PaypleCpayPopup(a, e.return_url)
            },
            error: function (e, a, t) {
            }
        })
    }, PaypleCpayPayResult = function (e, a) {
        if (a) MainBodyAction("close"), a(e); else {
            var t = e.PCD_RST_URL, i = "";
            $.each(e, function (e, a) {
                i += '<input type="hidden" name="' + e + '" value="' + a + '">'
            }), $('<form action="' + t + '" method="post">' + i + "</form>").appendTo($(document.body)).submit()
        }
    }, MainBodyAction = function (e) {
        "close" == e && ($("#cpay_ifr").remove(), $("#layer_cpay").remove())
    };
$(document).ready(function (e) {
    $(window).on("resize", function () {
        cpay_ifr_height = (cpay_ifr_windowHeight = cpay_ifr_windowHeight > cpay_ifr_height ? cpay_ifr_windowHeight : cpay_ifr_height) < $(window).height() ? cpay_ifr_windowHeight : $(window).height(), cpay_ifr_top = $(window).height() > cpay_ifr_height ? ($(window).height() - cpay_ifr_height) / 2 : 0, layer_ifr_resize()
    });
    var a = "";
    void 0 !== window.addEventListener ? a = "message" : void 0 !== window.attachEvent && (a = "onmessage"), "" != a && window.addEventListener("message", function (e) {
        var a = e.data.type, t = e.data.data;
        if ("close" == a) MainBodyAction("close"), (i = cPayData).PCD_PAY_RST = "close", i.PCD_PAY_MSG = "결제를 종료하였습니다.", PaypleCpayPayResult(i, i.callbackFunction); else if ("pay_result" == a) {
            var i;
            PaypleCpayPayResult(t, (i = cPayData).callbackFunction)
        } else "resize" == a && (cpay_ifr_top = (screen_height - (cpay_ifr_height = (cpay_ifr_windowHeight = t) < screen_height ? cpay_ifr_windowHeight : screen_height)) / 2, $("#cpay_ifr").animate({}, 3e3, function () {
            $(this).css({top: cpay_ifr_top, height: cpay_ifr_height})
        }))
    })
});