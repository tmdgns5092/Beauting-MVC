var byteLength = (function(s,b,i,c){
    for(b=i=0;c=s.charCodeAt(i++);b+=(c==10)?2:((c>>7)?2:1));
    return b;
});
var calByte = {
    getByteLength : function(s) {

        if (s == null || s.length == 0) {
            return 0;
        }
        var size = 0;

        for ( var i = 0; i < s.length; i++) {
            size += this.charByteSize(s.charAt(i));
        }

        return size;
    },

    cutByteLength : function(s, len) {

        if (s == null || s.length == 0) {
            return '';
        }
        var size = 0;
        var rIndex = s.length;

        for ( var i = 0; i < s.length; i++) {
            size += this.charByteSize(s.charAt(i));
            if( size == len ) {
                rIndex = i + 1;
                break;
            } else if( size > len ) {
                rIndex = i;
                break;
            }
        }

        return s.substring(0, rIndex);
    },

    charByteSize : function(ch) {

        if (ch == null || ch.length == 0) {
            return 0;
        }

        var charCode = ch.charCodeAt(0);

        if (charCode <= 0x00007F) {
            return 1;
        } else if (charCode <= 0x0007FF) {
            return 2;
        } else if (charCode <= 0x00FFFF) {
            return 3;
        } else {
            return 4;
        }
    }
};

Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ii": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "AM" : "PM";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

function removeString(str) {
    return str.replace(/[^0-9]/g, "");
}

function caseIgnoreComparison(str1, str2) {
    return str1.toLowerCase() === str2.toLowerCase();
}

function phoneAppendBar(number) {
    var phone = "";
    if(number.length < 4) {
        return number;
    } else if(number.length < 7) {
        phone += number.substr(0, 3);
        phone += "-";
        phone += number.substr(3);
    } else if(number.length < 11) {
        phone += number.substr(0, 3);
        phone += "-";
        phone += number.substr(3, 3);
        phone += "-";
        phone += number.substr(6);
    } else {
        phone += number.substr(0, 3);
        phone += "-";
        phone += number.substr(3, 4);
        phone += "-";
        phone += number.substr(7);
    }
    return phone;
}

function datepickerObject() {
    return {
        dateFormat: "yy-mm-dd",
        inline: true,
        showOtherMonths: true,
        showMonthAfterYear: true,
        dayNames: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
        dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
        monthNamesShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
        monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"]
    };
}


function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}

function format(str, size, num) {
    str = str + "";
    num = num + "";
    return str.length >= size ? str : new Array(size - str.length + 1).join(num) + str;
}

function pad(n, width) {
    n = n + "";
    return n.length >= width ? n : new Array(width - n.length + 1).join("0") + n;
}

function form_submit(path, params, method, target) {
    method = method || "post";
    target = target || "_self";
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);
    form.setAttribute("target", target);

    for (var key in params) {
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", key);
        hiddenField.setAttribute("value", params[key]);
        form.appendChild(hiddenField);
    }

    document.body.appendChild(form);
    form.submit();
}

function onlyNumber(obj) {
    $(obj).keyup(function () {
        $(this).val($(this).val().replace(/[^0-9]/g, ""));
    });
}

function onlyTel(obj) {
    $(obj).keyup(function () {
        $(this).val($(this).val().replace(/[^0-9-]/g, ""));
    });
}

function thisTextComma(obj) {
    $(obj).keyup(function () {
        $(this).val(comma($(this).val().replace(/[^0-9]/g, "")));
    });
}

function thisTextCommaMissCostVersionm(obj) {
    $(obj).keyup(function () {
        var total_cost = parseInt(uncomma($("#miss-cost-modal-miss-cost").text()));
        var this_cost = parseInt(uk_noshow($(this).val().replace(/[^0-9]/g, "")));
        if (this_cost > total_cost) {
            $(this).val(comma(total_cost));
        } else {
            $(this).val(comma(this_cost));
        }
    });
}

function thisTextCommaMissCostEmplVersionm(obj) {
    $(obj).keyup(function () {
        var total_cost = parseInt(uncomma($("#pay-the-misscost").val()));
        var this_cost = parseInt(uk_noshow($(this).val().replace(/[^0-9]/g, "")));
        if (this_cost > total_cost) {
            $(this).val(comma(total_cost));
        } else {
            $(this).val(comma(this_cost));
        }
    });
}

function onlyInt(obj) {
    $(obj).keyup(function () {
        $(this).val(parseInt($(this).val().replace(/[^0-9]/g, "")));
    });
}

function costOnlyInt(obj) {
    $(obj).keyup(function () {
        var value = parseInt($(this).val().replace(/[^0-9]/g, ""));
        if(uk(value) == "") value = 0;
        $(this).val(comma(value));
    });
}

function removefirstZero(hour) {
    if (hour.charAt(0) == "0") {
        return hour.charAt(1);
    } else {
        return hour;
    }
    return;
}

function removeQuotes(str) {
    return result = str.replace(/^"(.*)"$/, "$1");
}

function maxLengthCheck(object) {
    if (object.value.length > object.maxlength) {
        object.value = object.value.slice(0, object.maxlength);
    }
}

function maxValueCheck(object) {
    var data_value = parseInt(uk_noshow($(object).attr("data-value")));
    var this_value = parseInt(uk_noshow($(object).val()));
    if (data_value < this_value) {
        $(object).val(data_value);
    }
}

function uk(obj) {
    if (obj) {
        return obj;
    } else {
        return "";
    }
}

function uk_prepaid(obj) {
    if (obj) {
        return obj;
    } else {
        return "-";
    }
}

function uk_noshow(obj) {
    if (obj) {
        return obj;
    } else {
        return "0";
    }
}

function undefined_check(str) {
    if (str == "undefined") {
        return "";
    } else {
        return str;
    }
}

function undefined_check2(obj) {
    if (obj === undefined) {
        return "";
    } else {
        return obj;
    }
}

function telNumberCheck(object) {
    var checkNumber = "";
    if ($(object).val().length < 10) {
        alert("번호를 정확히 입력해 주세요");
        $(object).focus();
        return false;
    } else {
        if ($(object).val().length == 10) {
            checkNumber = $(object).val().slice(0, 3) + "-" + $(object).val().slice(3, 6) + "-" + $(object).val().slice(6, 10);
            //console.log("하이픈 넣기 휴대폰 : " + checkNumber);
            return checkNumber;
        } else {
            checkNumber = $(object).val().slice(0, 3) + "-" + $(object).val().slice(3, 7) + "-" + $(object).val().slice(7, $(object).val().length);
            //console.log("하이픈 넣기 휴대폰 : " + checkNumber);
            return checkNumber;
        }
    }
}

function changeFormatPhone(str) {
    if (str.length == 10) {
        str = str.slice(0, 3) + "-" + str.slice(3, 6) + "-" + str.slice(6, 10);
    } else {
        str = str.slice(0, 3) + "-" + str.slice(3, 7) + "-" + str.slice(7, str.length);
    }
    return str;
}

function undifendCheck(obj) {
    if (obj) {
        return obj;
    } else {
        return "";
    }
}

function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");
}

function uncomma(str) {
    str = String(str);
    return str.replace(/[^\d]+/g, "");
}

function inputNumberFormat(obj) {
    obj.value = comma(uncomma(obj.value));
}

function removeSpaces(str) {
    str = uk(str);
    return str.replace(/ /g, "");
}

function Replace(strString, strChar) {
    var strTmp = "";
    for (var i = 0; i < strString.length; i++) {
        if (strString.charAt(i) != strChar) {
            strTmp = strTmp + strString.charAt(i);
        }
    }
    return strTmp;
}

function comNumberCheck(com_num) {
    return true;
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function tableTrDown(obj) {
    var $tr = $(obj).parent().parent();
    $tr.next().after($tr);
}

function tableTrUp(obj) {
    var $tr = $(obj).parent().parent();
    $tr.prev().before($tr);
}

function dateNOW() {
    var date = new Date();
    var aaaa = date.getFullYear();
    var gg = date.getDate();
    var mm = (date.getMonth() + 1);
    if (gg < 10) {
        gg = "0" + gg;
    }
    if (mm < 10) {
        mm = "0" + mm;
    }
    var cur_day = aaaa + "-" + mm + "-" + gg;
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    return cur_day + " " + hours + ":" + minutes + ":" + seconds;
}

function dateNOWday(){
    var date = new Date();
    var aaaa = date.getFullYear();
    var gg = date.getDate();
    var mm = (date.getMonth() + 1);
    if (gg < 10) {
        gg = "0" + gg;
    }
    if (mm < 10) {
        mm = "0" + mm;
    }
    var cur_day = aaaa + "-" + mm + "-" + gg;
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    return cur_day;
}


function returnMinuteTypeNumber(str) {
    str = parseInt(str);
    if (str < 10) {
        return "0" + str;
    } else {
        return str;
    }
}

function submitAjax(url, param) {
    var return_data;
    //console.log("param : " + JSON.stringify(param));
    $.ajax({
        url: url, type: "post", data: param, dataType: "json", async: false, success: function (data) {
            //console.log(JSON.stringify(data));
            return_data = data;
        }, error: function (data) {
            data.code = 900;
            return_data = data;
        }
    });
    return return_data;
}

function submitAjaxSync(url, param) {
    // console.log(JSON.stringify(param));
    var return_data;
    //console.log("param : " + JSON.stringify(param));
    $.ajax({
        url: url, type: "post", data: param, dataType: "json", success: function (data) {
            //console.log(JSON.stringify(data));
            return_data = data;
        }, error: function (data) {
            data.code = 900;
            return_data = data;
        }
    });
    return return_data;
}

function subMitForm(url, param){
    var form = document.createElement ('form');

    $.each(param, function(index, value){
        var obj = objectSetAttribute(value.name, value.value);
        form.appendChild(obj);
    });

    form.setAttribute('method', 'post');
    form.setAttribute('action', url);
    document.body.appendChild(form);
    form.submit();
}

function replaceTwoBlank(str) {
    str = $.trim(str);
    str = str.replace(/ +/g, " ", " ");
    return str;
}

function abBlankReplace(str) {
    return str.replace(/^\s+|\s+$/g, "");
}

function phoneAndTelOnlyNumber(str) {
    return str.replace(/[^0-9]/g, "");
}

function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}

function chkEmail(str) {
    var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    return re.test(str);
    // var returnData;
    // if(str== '' || str == 'undefined') returnData = false;
    // if(!email_check(str) ) returnData = false;
    // else returnData = true;
    //
    // return returnData;
}

function email_check(email) {
    var regex = /([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    return (email != '' && email != 'undefined' && regex.test(email));
}

function isMobile(phoneNum) {

    var regExp = /(01[016789])([1-9]{1}[0-9]{2,3})([0-9]{4})$/;
    var myArray;
    if (regExp.test(phoneNum)) {
        myArray = regExp.exec(phoneNum);
        return true;
    } else {
        return false;
    }
}

/* 잔디 회원가입 웹훅 */
function jandiMembershipNotification(url, id, comName, ceoName, tel, addressLg, addresSm, postCode, mid){
    var array = new Array();
    array.push({
        "title": "회원 정보",
        "description": "아이디 - " + id + "\n 회사 명 - " + comName + "\n 원장 이름 - " + ceoName +
        "\n 연락처 - " + tel + "\n 주소 - " + addressLg + " " + addresSm + " " + postCode + "\n 원장 아이디 - " + mid
    });
    var tmp = {
        "body" : "[뷰팅네일 매장 가입]",
        "connectColor" : "#FAC11B",
        "array" : JSON.stringify(array)
    };

    $.ajax({url: url, type: "post", data: tmp, dataType: "json"});
}


/* =========================  OnCheckPhone ========================*/
function OnCheckPhone(oTa, key) {
    if(key.keyCode != 8){
        var oForm = oTa.form ;
        var sMsg = oTa.value ;
        var onlynum = "" ;
        var imsi=0;
        onlynum = RemoveDash2(sMsg);  //하이픈 입력시 자동으로 삭제함
        onlynum =  checkDigit(onlynum);  // 숫자만 입력받게 함
        var retValue = "";

        if(event.keyCode != 12 ) {
            if(onlynum.substring(0,2) == '02') {  // 서울전화번호일 경우  10자리까지만 나타나교 그 이상의 자리수는 자동삭제
                if (GetMsgLen(onlynum) <= 1) oTa.value = onlynum ;
                if (GetMsgLen(onlynum) == 2) oTa.value = onlynum + "-";
                if (GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,3) ;
                if (GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,4) ;
                if (GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,5) ;
                if (GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) ;
                if (GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,5) + "-" + onlynum.substring(5,7) ; ;
                if (GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,8) ;
                if (GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,5) + "-" + onlynum.substring(5,9) ;
                if (GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,10) ;
                if (GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,10) ;
                if (GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,2) + "-" + onlynum.substring(2,6) + "-" + onlynum.substring(6,10) ;
            }
            if(onlynum.substring(0,2) == '05' ) {  // 05로 시작되는 번호 체크
                if(onlynum.substring(2,3) == 0 ) {  // 050으로 시작되는지 따지기 위한 조건문
                    if (GetMsgLen(onlynum) <= 3) oTa.value = onlynum ;
                    if (GetMsgLen(onlynum) == 4) oTa.value = onlynum + "-";
                    if (GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,5) ;
                    if (GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,6) ;
                    if (GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7) ;
                    if (GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) ;
                    if (GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7) + "-" + onlynum.substring(7,9) ; ;
                    if (GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) + "-" + onlynum.substring(8,10) ;
                    if (GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7) + "-" + onlynum.substring(7,11) ;
                    if (GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) + "-" + onlynum.substring(8,12) ;
                    if (GetMsgLen(onlynum) == 13) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) + "-" + onlynum.substring(8,12) ;
                } else {
                    if (GetMsgLen(onlynum) <= 2) oTa.value = onlynum ;
                    if (GetMsgLen(onlynum) == 3) oTa.value = onlynum + "-";
                    if (GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,4) ;
                    if (GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,5) ;
                    if (GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) ;
                    if (GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) ;
                    if (GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,8) ; ;
                    if (GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,9) ;
                    if (GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,10) ;
                    if (GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11) ;
                    if (GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11) ;
                }
            }

            if(onlynum.substring(0,2) == '03' || onlynum.substring(0,2) == '04'  || onlynum.substring(0,2) == '06'  || onlynum.substring(0,2) == '07'  || onlynum.substring(0,2) == '08' ) {  // 서울전화번호가 아닌 번호일 경우(070,080포함 // 050번호가 문제군요)
                if (GetMsgLen(onlynum) <= 2) oTa.value = onlynum ;
                if (GetMsgLen(onlynum) == 3) oTa.value = onlynum + "-";
                if (GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,4) ;
                if (GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,5) ;
                if (GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) ;
                if (GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) ;
                if (GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,8) ; ;
                if (GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,9) ;
                if (GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,10) ;
                if (GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11) ;
                if (GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11) ;

            }
            if(onlynum.substring(0,2) == '01') {  //휴대폰일 경우
                if (GetMsgLen(onlynum) <= 2) oTa.value = onlynum ;
                if (GetMsgLen(onlynum) == 3) oTa.value = onlynum + "-";
                if (GetMsgLen(onlynum) == 4) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,4) ;
                if (GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,5) ;
                if (GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) ;
                if (GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) ;
                if (GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,8) ;
                if (GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,9) ;
                if (GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,6) + "-" + onlynum.substring(6,10) ;
                if (GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11) ;
                if (GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,3) + "-" + onlynum.substring(3,7) + "-" + onlynum.substring(7,11) ;
            }

            if(onlynum.substring(0,1) == 1) {  // 1588, 1688등의 번호일 경우
                if (GetMsgLen(onlynum) <= 3) oTa.value = onlynum ;
                if (GetMsgLen(onlynum) == 4) oTa.value = onlynum + "-";
                if (GetMsgLen(onlynum) == 5) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,5) ;
                if (GetMsgLen(onlynum) == 6) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,6) ;
                if (GetMsgLen(onlynum) == 7) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,7) ;
                if (GetMsgLen(onlynum) == 8) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) ;
                if (GetMsgLen(onlynum) == 9) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) ;
                if (GetMsgLen(onlynum) == 10) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) ;
                if (GetMsgLen(onlynum) == 11) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) ;
                if (GetMsgLen(onlynum) == 12) oTa.value = onlynum.substring(0,4) + "-" + onlynum.substring(4,8) ;
            }
        }
    }
}

function RemoveDash2(sNo) {
    var reNo = ""
    for(var i=0; i<sNo.length; i++) {
        if ( sNo.charAt(i) != "-" ) {
            reNo += sNo.charAt(i)
        }
    }
    return reNo
}

function GetMsgLen(sMsg) { // 0-127 1byte, 128~ 2byte
    var count = 0
    for(var i=0; i<sMsg.length; i++) {
        if ( sMsg.charCodeAt(i) > 127 ) {
            count += 2
        }
        else {
            count++
        }
    }
    return count
}

function checkDigit(num) {
    var Digit = "1234567890";
    var string = num;
    var len = string.length;
    var retVal = "";

    for (i = 0; i < len; i++)
    {
        if (Digit.indexOf(string.substring(i, i+1)) >= 0)
        {
            retVal = retVal + string.substring(i, i+1);
        }
    }
    return retVal;
}
/* ========================= /.OnCheckPhone ========================*/

/* 임시 */
/* 고객정보 모달*/
function client_Info(idx) {
    var iModal = $('#c-info-modal');
    $('#c-info-main_tab').click();
    $('#c-info-phone_message').text('');
    $.post('/Client/selectClientInfoAjax', {idx: idx}, function (data) {
        iModal.find('.modal-body').attr("data-value", data.idx);
        var total_prepaid = 0;
        if (data.product == null) data["product"] = '[]';
        for (var key in data) {
            if (data[key] == null) {
                data[key] = '';
            }
            //console.log('key:' + key + ' / ' + 'value:' + data[key]);
            if (key == 'rank' || key == 'kind' || key == 'visit') {
                iModal.find('select[name=' + key + ']').val(data[key]);
                $('select').niceSelect('update');
            } else if (key == 'phone') {
                var phone = data[key];
                if (phone == '') continue;
                var length = phone.length;
                phone = phone.substring(0, 3) + '-' + phone.substring(3, length - 4) + '-' + phone.substring(length - 4, length);
                $('#c-info-phone').val(phone);
                $('#org-phone').val(phone);
            } else if (key == 'sms_check') {
                if (data[key] == 1) $('#c-info-sms-check').prop('checked', true);
                else $('#c-info-sms-check').prop('checked', false);
            } else if (key == 'memo') {
                // iModal.find('textarea[placeholder]').val(''); // textarea 값을 초기화
                iModal.find('textarea[name=memo]').text(data.memo);
            } else if (key == 'status') {
                iModal.find('#c-info-status-' + data[key]).prop('checked', true);
            } else if (key == 'prepaid' || key == 'ticket') {
                iModal.find('div[name=' + key + ']').empty();
                var name = key, json = [];
                if (data[key] != null && data[key] != '') json = JSON.parse(data[key]);
                if (json.length == 0) {
                    var ul = document.createElement("ul");
                    $(ul).addClass("client-data-none01");
                    var txt;
                    if (key == 'prepaid') txt = '잔여 선불권이 없습니다.';
                    else if (key == 'ticket') txt = '잔여 티켓이 없습니다.';
                    ul.innerHTML = "<li><p>" + txt + "</p></li>";
                    iModal.find('div[name=' + name + ']').append(ul);
                } else {
                    var cost = 0, count = 0;
                    json.forEach(function (item) {
                        var ul = document.createElement("ul");
                        var str = "" + "<li style='width: 30%'>" + "<p name='idx' data-value='" + item.idx + "'><span name='name' data-value='" + item.name + "'>" + item.name + "</span></p>" + "</li>" + "<li class='div-datapick' style='width: 35%'>" + "<input type='hidden' name='sale_idx' value='" + item.sale_idx + "'>" + "<button type='button' class=''><i class='fas fa-calendar-alt'></i></button>" + "<input type='text' name='validity' value='" + item.validity + "' class='' readonly>" + "</li>";
                        if (key == 'ticket') {
                            str += "" + "<li style='width: 33.6%'>" +
                                "<p class='ci_show_number' name='" + name + "' data-value='" + item.count + "' style='font-weight: 600; cursor:pointer;' onclick='clientModalPreapidEdit(this);'>" +
                                "<I><U>" + item.count + "  회</U></I>" +
                                "</p>" +
                                "<input type='hidden' name='cost' value='" + item.cost + "'>" +
                                "<input type='text' name='ticket_edit' value='" + item.count + "' style='display: none;' onkeydown='onlyNumber(this);' onkeyup='editApply(this,event);' onblur='editOutFocus(this)'>" +
                                "</li>";
                        } else {
                            str += "" + "<li style='width: 33.6%'>" +
                                "<p class='ci_show_number' name='" + name + "' data-value='" + item.cost + "' style='font-weight: 600; cursor:pointer;'  onclick='clientModalPreapidEdit(this);'>" +
                                "<I><U>" + numberWithCommas(parseInt(item.cost)) + "  원</U></I>" +
                                "</p>" +
                                "<input type='text' name='prepaid_edit' value='" + item.cost + "' style='display: none;' onkeydown='onlyNumber(this);' onkeyup='editApply(this,event);' onblur='editOutFocus(this)'>" +
                                "</li>";
                            total_prepaid += parseInt(item.cost);
                        }
                        str += "<li><span style='cursor: pointer;' onclick='prepaidTicketRemove(this);'>×</span></li>";
                        ul.innerHTML = str;
                        iModal.find('div[name=' + name + ']').append(ul);
                        $(ul).attr('data-value', item.sale_idx);
                        if (key == 'prepaid') {
                            cost += item.cost;
                            if (item.cost == 0) {
                                $(ul).css('display', 'none');
                            }
                        } else if (key == 'ticket') {
                            count += item.count;
                            if (item.count == 0) {
                                $(ul).css('display', 'none');
                            }
                        }
                    });
                    if (key == 'ticket' && count == 0) {
                        var str = "<ul class='client-data-none01'><li><p>잔여 티켓이 없습니다.</p></li></ul>";
                        iModal.find('div[name=' + name + ']').append(str);
                    } else if (key == 'prepaid' && cost == 0) {
                        var str = "<ul class='client-data-none01'><li><p>잔여 선불권이 없습니다.</p></li></ul>";
                        iModal.find('div[name=' + name + ']').append(str);
                    }
                }
            } else if (key == 'product') {
                var json = [];
                if (data[key] != null && data[key] != '') json = JSON.parse(data[key]);
                $('#keep-folder > ul').empty();
                // console.log($('#keep-folder > ul').length);
                if (json.length != 0) {
                    json.forEach(function (item) {
                        var folder_li = document.createElement('li');
                        var div = '' + '<div class="keep-product-add">' + '<div class="keep-folder-top">' + '<h5 name="folder_name">' + item.name + '</h5>' + '<div name="divToggle" class="folder-name-editer" style="display: none;">' + '<input type="text" placeholder="수정할 폴더명을 입력해주세요." name="modify_name">' + '<button onclick="folder_save(this)">저장</button>' + '<button onclick="folder_modify(this)" name="cancel">취소</button>' + '</div>' + '<div class="keep-folder-top-btn" name="folder_tool">' + '<button type="button" onclick="folder_modify(this)" name="modify">수정</button>' + '<button type="button" onclick="folder_delete(this)">삭제</button>' + '</div>' + '</div>' + '<div>' + '<input type="text" placeholder="제품명을 입력해주세요." name="product_name">' + '<button onclick="keep_product_create(this)">추가</button>' + '</div>' + '</div>' + '<div class="keep-product">' + '<ul></ul>' + '</div>';
                        folder_li.innerHTML = div;
                        $('#keep-folder > ul').append(folder_li);
                        var products = item.products.split('ː');
                        products.forEach(function (value) {
                            var product_li = document.createElement('li');
                            var element = '<p>' + value + '</p>' + '<button type="button" onclick="product_keep_delete(this)"><i class="fas fa-times"></i></button>';
                            product_li.innerHTML = element;
                            // console.log(product_li);
                            // console.log(folder_li);
                            $(folder_li).find('.keep-product > ul').append(product_li);
                        });
                    });
                }
            } else if (key.indexOf('total_') != -1 || key == 'point' || key == 'miss_cost') {
                iModal.find('p[name=' + key + ']').attr('data-value', data[key]);
                iModal.find('p[name=' + key + ']').text(numberWithCommas(data[key]));
            } else {
                iModal.find('input[name=' + key + ']').val(data[key]);
            }
        }
        /* 회원권, 티켓 생성시 데이터 피커 적용 */
        $('input[name=validity]').datepicker({
                dateFormat: "yy-mm-dd",
                inline: true,
                showOtherMonths: true,
                showMonthAfterYear: true,
                dayNames: ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"],
                dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
                monthNamesShort: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"],
                monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"]
            });
    }, 'json').fail(function (error) {
        alert(error.status);
    });
}

/* 고객 정보 모달 잔여금액 클릭 */
function clientModalPreapidEdit(obj){
    $(obj).hide();
    if($(obj).attr("name") == "prepaid") {
        $(obj).closest("li").find("input[name=prepaid_edit]").show();
        $(obj).closest("li").find("input[name=prepaid_edit]").select();
    } else {
        $(obj).closest("li").find("input[name=ticket_edit]").show();
        $(obj).closest("li").find("input[name=ticket_edit]").select();
    }
}
/* 에디트 텍스트 아웃 포커스 */
function editOutFocus(obj){
    if($(obj).attr("name") == "prepaid_edit") {
        $(obj).hide();
        $(obj).closest("li").find("p[name=prepaid]").show();
    } else {
        $(obj).hide();
        $(obj).closest("li").find("p[name=ticket]").show();
    }
}
/* 수정된 값 적용 */
function editApply(obj, event){
    var val = $(obj).val();
    if(event.keyCode == 13){
        editOutFocus(obj);
    } else {
        if($(obj).attr("name") == "prepaid_edit"){
            $(obj).closest("li").find("p[name=prepaid]").html("<I><U>" + comma(val) + "  원 </U></I>");
            $(obj).closest("li").find("p[name=prepaid]").attr("data-value", val);
        } else {
            $(obj).closest("li").find("p[name=ticket]").html("<I><U>" + val + "  회 </U></I>");
            $(obj).closest("li").find("p[name=ticket]").attr("data-value", val);
        }
    }
}
/* 해당 회원권 삭제 */
function prepaidTicketRemove(obj){
    $(obj).closest('ul').remove();
}



/* 고객 등록후 수신내역 정보 수정 */
function updateClientState(data) {
    $.ajax({
        url: '/updateClientState',
        type: 'post',
        data: data,
        dataType: 'json',
        async : false,
        success: function (data) {
            if(data.code == 200) console.log('수정완료');
            else console.log('업데이트 실패');
        },
        error: function (request, status, error) {
            alert('오류가 발생했습니다. 잠시후 다시 시도해주십시오. ::' + request.status);
            console.log("request:code : " + request.status + "\nmessage:" + request.responseText + "\nerror : " + error);
        }
    });
}