/* risize functions */

/* 예약 달력 표시 */
function view_reservations(data, default_minute, close_time){
    var jsonA = data;
    function iconCheck(v, index){
        return uk(v.icons).indexOf(index) != -1;
    }

    $.each(jsonA, function(k, v){
        var icons = "";
        var icon = '';
        var times = v.time.split(":");
        var td;
        var span_size;
        if(Number(times[0]) < 10) td = document.getElementById(Number(times[0])+":"+times[1]+":"+v.employee_idx);
        else td = document.getElementById(v.time + ":" + v.employee_idx);

        /* 스케쥴이 있는 직원만 */
        // if(!$(td).hasClass('employee-noschedule')){
            if(iconCheck(v, "1") && iconCheck(v, "2")){ icon = '<div class="td-icon"><i class="fas fa-star"></i><i class="fas fa-phone"></i></div>';}
            else if(iconCheck(v, "1")){ icon = '<i class="fas fa-star"></i> ';}
            else if(iconCheck(v, "2")){ icon = '<i class="fas fa-phone"></i> ';}

            $(td).addClass("reservation-td");               // 예약 색
            $(td).css("background-color", v.color).css("color", "#444444");         // 예약 색
                if(v.status == 3) {
                $(td).css("background-color", "#e1e1e1");
                $(td).addClass("reservation-noshow");
            } else if(v.status == 5){
                $(td).css("background-color", "#f6f7f7");
                $(td).addClass("reservation-calculation-complete");
            } else{
                $(td).addClass("td-reservation-popup");               // 예약 팝업 클레스
            }
            $(td).attr("draggable", true);                  // 예약 드래그 허용
            $(td).removeClass("reservation-empty-td");      // 예약 드롭 허용 안함
            $(td).attr("data-value", v.idx);                // 예약 idx SET
            $(td).append('<input type="hidden" id="'+$(td).attr("id")+'_end_time" value="'+v.end_time+'">');

            var prepaid='', ticket='', product='';
            $.each(JSON.parse(v.prepaid), function(index, item){
                if(item.cost != 0){
                    if(index == 0){
                        prepaid += '{name:' + item.name + ',cost:' + item.cost +'}';
                    } else {
                        prepaid += '_______{name:' + item.name + ',cost:' + item.cost +'}';
                    }
                }
            });
            if(prepaid == '') prepaid = '{name:보유 회원권이 없습니다.}';

            $.each(JSON.parse(v.ticket), function(index, item){
                if(item.count != 0){
                    if(index == 0){
                        ticket += '{name:' + item.name + ',count:' + item.count +'}';
                    } else {
                        ticket += '_______{name:' + item.name + ',count:' + item.count +'}';
                    }
                }
            });
            if(ticket == '') ticket = '{name:보유 티켓이 없습니다.}';
            
            $.each(JSON.parse(v.product), function(index, item){
                if(index == 0){
                    product += '{name:' + item.name +'}';
                } else {
                    product += '_______{name:' + item.name +'}';
                }
            });
            if(product == '') product = '{name:보유 제품이 없습니다.}';

            /* 미등록 회원 */
            if(v.type == "1") {
                var v_memo_replace = "";
                if(v.res_memo.length > 9)  v_memo_replace = v.res_memo.substring(0, 9) + "...";
                else v_memo_replace = v.res_memo;
                $(td).append(icon + '<p class="res_name">' +v.un_name + '</p>' +
                    '<p class="res_phone">' + changeFormatPhone(v.un_phone) + '</p> ' +
                    '<p class="res_service">' + v.service_name + '</p>' +
                    '<p class="res_memo">' + v_memo_replace + '</p>' +
                    '<span></span>' +
                    '<div class="un-msj" name="' + td_id + '" data-value="' + td_id + '">' +
                    '   <input type="hidden" name="popup-msg-idx" value="0">' +
                    '   <input type="hidden" name="popup-msg-phone" value="'+v.un_phone+'">'+
                    '   <input type="hidden" name="popup-msg-memo" value="' + v.res_memo + '"> ' +
                    '</div>'
                );
            }
            /* 등록 회원 */
            else {
                var td_id = $(td).attr('id');
                var lastSale_name = undefined_check2(v.lastSale_name) != '' ? undefined_check2(v.lastSale_name) : '최근내역이 없습니다.';
                var v_memo_replace = "";
                if(v.res_memo.length > 9)  v_memo_replace = v.res_memo.substring(0, 9) + "...";
                else v_memo_replace = v.res_memo;

                $(td).append(icon + '<p class="res_name">' + v.client_name + ' (' + v.client_code + ')</p>' +
                    '<p class="res_service">' + v.service_name + '</p>' +
                    '<p class="res_memo">' + v_memo_replace + '</p>' +
                    '<div class="popup-msj" name="' + td_id + '" data-value="' + td_id + '">' +
                    '   <input type="hidden" name="popup-msg-idx" value="'+v.client_idx+'">' +
                    '   <input type="hidden" name="popup-msg-phone" value="' + v.client_phone + '"> ' +
                    '   <input type="hidden" name="popup-msg-has-prepaid" value="' + prepaid + '"> ' +
                    '   <input type="hidden" name="popup-msg-has-ticket" value="' + ticket + '"> ' +
                    '   <input type="hidden" name="popup-msg-res-history-name" value="' + lastSale_name + '"> ' +
                    '   <input type="hidden" name="popup-msg-res-history-date" value="' + undefined_check2(v.lastSale_date) + '"> ' +
                    '   <input type="hidden" name="popup-msg-has-product" value="' + product + '"> ' +
                    '   <input type="hidden" name="popup-msg-sms-check" value="' + v.sms_check + '"> ' +
                    // '   <input type="hidden" name="popup-msg-memo" value="' + v.memo + '"> ' +
                    '   <input type="hidden" name="popup-msg-memo" value="' + v.res_memo + '"> ' +
                    '</div>'
                );
                /*$(td).append(icon + '<p class="res_name">' + v.client_name + ' (' + v.client_code + ')</p>' + '<p class="res_service">' + v.service_name + '</p>' +
                '<div class="popup-msj" name="' + td_id + '" data-value="' + td_id + '">' +
                '   <div class="calendar-popup">' +
                '       <div class="calendar-popup-content popup-tel">' +
                '           <p>' + v.client_phone + '</p>' +
                '       </div>' +
                '       <div class="calendar-popup-content popup-prepayment">' +
                '           <p>선불권</p>' +
                '           <ul>' + prepaid + '</ul>' +
                '       </div>' +
                '       <div class="calendar-popup-content popup-prepayment">' +
                '           <p>티켓</p>' +
                '           <ul>' + ticket + '</ul>' +
                '       </div>' +
                '       <div class="calendar-popup-content popup-history">' +
                '           <p>최근내역</p>' +
                '           <ul>' +
                '               <li>' +
                '                   <p>' + undefined_check2(v.lastSale_name) + '</p>' +
                '                   <p class="text-point02">' + undefined_check2(v.lastSale_date) + '</p>' +
                '               </li>' +
                '           </ul>' +
                '       </div>' +
                '       <div class="calendar-popup-content popup-product">' +
                '           <p>제품</p>' +
                '           <ul>' + product + '</ul>' +
                '       </div>' +
                '       <div class="calendar-popup-content popup-memo">' +
                '           <p>' + v.memo + '</p>' +
                '       </div>' +
                '   </div>' +
                '</div>');*/
            }

            var loop_count = false;
            var end_td = document.getElementById(v.end_time+":"+v.employee_idx);
            var trNum1 = $(td).parent("tr").prevAll().length;
            var trNum2 = $(end_td).parent("tr").prevAll().length;

            if(trNum1 == trNum2) span_size = 1;
            else if(trNum2 == 0) span_size = 1;
            else span_size = Math.abs(Number(parseInt(trNum2) - parseInt(trNum1)));

            if(v.end_time == close_time){
                /* 차이(분)을 기준 시간으로 나누 값을 스판에 넣어준다 */
                var close_times = close_time.split(":");
                var this_hour = parseInt(times[0]) * 60 + parseInt(times[1]);
                var close_hour = parseInt(close_times[0]) * 60 + parseInt(close_times[1]);

                if(default_minute == "10")
                    span_size = Math.abs((this_hour - close_hour) / 10);
                else
                    span_size = Math.abs((this_hour - close_hour) / 15);


                var start_time = v.time.split(":");
                var end_time = v.end_time.split(":");

                if(default_minute == 10){
                    for(var i = Number(start_time[0]); i  <= Number(end_time[0]); i++){
                        for(var j = Number(start_time[1]); j < 60 ; j+=10){
                            if(!loop_count) loop_count = true;
                            else {
                                if (j == 0) j = "00";                                                   /* value(00) cast */
                                if (i == end_time[0] && j == Number(end_time[1])) break;                /* end_time to start_time equl break ... */
                                var remove_td = document.getElementById(i+":"+j+":"+v.employee_idx);    /* td empty */
                                $(remove_td).remove();
                                if (j == "00") j = 0;                                                   /* value(00) cast */
                            }
                            start_time[1] = 0;
                        }
                    }
                }
                /* Minute 15 */
                else if(default_minute == 15){
                    for(var i = Number(start_time[0]); i  <= Number(end_time[0]); i++){
                        for(var j = Number(start_time[1]); j < 60 ; j+=15){
                            if(!loop_count) loop_count = true;
                            else {
                                if (j == 0) j = "00";                                                   /* value(00) cast */
                                if (i == end_time[0] && j == Number(end_time[1])) break;                /* end_time to start_time equl break ... */
                                var remove_td = document.getElementById(i+":"+j+":"+v.employee_idx);    /* td empty */
                                $(remove_td).remove();
                                if (j == "00") j = 0;                                                   /* value(00) cast */
                            }
                            start_time[1] = 0;
                        }
                    }
                }

            } else{
                var start_time = v.time.split(":");
                var end_time = v.end_time.split(":");

                /* Remain Td Remove */
                /* Minute 10 */
                if(span_size > 1){
                    if(default_minute == 10){
                        for(var i = Number(start_time[0]); i  <= Number(end_time[0]); i++){
                            for(var j = Number(start_time[1]); j < 60 ; j+=10){
                                if(!loop_count) loop_count = true;
                                else {
                                    if (j == 0) j = "00";                                                   /* value(00) cast */
                                    if (i == end_time[0] && j == Number(end_time[1])) break;                /* end_time to start_time equl break ... */
                                    var remove_td = document.getElementById(i+":"+j+":"+v.employee_idx);    /* td empty */
                                    $(remove_td).remove();
                                    if (j == "00") j = 0;                                                   /* value(00) cast */
                                }
                                start_time[1] = 0;
                            }
                        }
                    }
                    /* Minute 15 */
                    else if(default_minute == 15){
                        for(var i = Number(start_time[0]); i  <= Number(end_time[0]); i++){
                            for(var j = Number(start_time[1]); j < 60 ; j+=15){
                                if(!loop_count) loop_count = true;
                                else {
                                    if (j == 0) j = "00";                                                   /* value(00) cast */
                                    if (i == end_time[0] && j == Number(end_time[1])) break;                /* end_time to start_time equl break ... */
                                    var remove_td = document.getElementById(i+":"+j+":"+v.employee_idx);    /* td empty */

                                    $(remove_td).remove();
                                    if (j == "00") j = 0;                                                   /* value(00) cast */
                                }
                                start_time[1] = 0;
                            }
                        }
                    }
                }
            }
            $(td).attr("rowspan", span_size);
        // }
    });
}

/* reservation drop down check */
function res_drop_check(dropHour, dropMinute, dropEmployee , default_minute, objSpan, originIdx){
    var thisId = dropHour + ":" + dropMinute + ":" + dropEmployee;
    var dropId;
    var targetHour = dropHour;
    var targetMinute = dropMinute;
    var drop_flag = true;
    // if(uk(span_size) == "") span_size = 0;

    // if(span_size != 0){
    for(var i = 0; i < objSpan; i++ ){
        if(targetMinute == 0) dropId = targetHour + ":00:" + dropEmployee;
        else dropId = targetHour + ":" + targetMinute + ":" + dropEmployee;

        if($('td[id="'+ dropId +'"]').hasClass("reservation-td") || uk($('td[id="'+ dropId +'"]').attr("id")) == ""){
            if($('td[id="'+ dropId +'"]').attr("data-value") != originIdx){
                drop_flag = false;
                break;
            } else{
                i = i + objSpan;
            }
        }

        if(default_minute == 10) targetMinute = Number(parseInt(targetMinute) + 10);
        else targetMinute = Number(parseInt(targetMinute) + 15);

        if(targetMinute >= 60){
            targetHour = Number(parseInt(targetHour) + 1);
            targetMinute = Number(parseInt(targetMinute) - 60);
        }

        /*if(dropId == thisId){
            i = i + span_size;
        }*/
    }
    // } else{
    //     //console.log("span size == 0 ");
    // }

    return drop_flag;
}

/* reservation move ajax */
function res_move(idx, hour, minute, employee, span, forDate, shopId){
    var msg_send_flag = true;
    var type1 = selectAutoMessage(1);
    var type2 = selectAutoMessage(2);
    var type3 = selectAutoMessage(3);
    var type4 = selectAutoMessage(4);

    if(type1 == null && type2 == null && type3 == null && type4 == null) msg_send_flag = false;

    $.ajax({
        url : "/Reservation/updateReservationFromAjax",
        type: "post",
        data : {
            "idx" : idx,
            "hour" : hour,
            "minute" : minute,
            "employee" : employee,
            "span" : span,
            "forDate" : forDate,
            "shopId" : shopId
        },
        dataType : "json",
        success : function(data){
            if(data.code == 200){
                // 문자 메세지 다시 보내기
                data = msgRefundSend(msg_send_flag, idx);

                form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
            } else if(data.code == 901){
                alert("예약이 중첩되었습니다.");
                form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                return false;
            } else{
                alert("잠시 후 다시 시도해 주세요.");
                form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                return false;
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
            form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
        }
    });
}

/* 확장 */
/* resize 될 td 예약 유무 검증 */
function resize_check(id, span, loop, default_minute){
    var times = id.split(":");
    var hour = times[0];
    var minute = times[1];
    var empl = times[2];
    var span = Number(span);
    var add_minute;

    /* 기준 시간 10분 */
    if(default_minute == 10) {
        if(loop == 0){add_minute = 0;}
        else {add_minute = loop * 10;}
        span = span * 10;
    }
    /* 기준 시간 15분 */
    else if(default_minute == 15) {
        if(loop == 0){add_minute = 0;}
        else {add_minute = loop * 15;}
        span = span * 15;
    }

    if(Number(minute) + Number(span) + Number(add_minute) >= 60){
        var tmp = Number(minute) + Number(span) + Number(add_minute);
        hour = (Number(tmp)/60) + Number(hour);
        minute = tmp % 60;
    } else {
        minute = Number(minute) + Number(span) + Number(add_minute);
    }
    if(minute == 0) minute = "00";

    return Math.floor(hour) + ":" + minute + ":" + empl;
}
/* 축소 */
/* end_time */
function subtract(id, size, default_minute){
    var end_time_obj = document.getElementById(id+"_end_time");
    var origin_end_time = $(end_time_obj).val();
    var times = origin_end_time.split(":");

    var subtract_hour;
    var subtract_minute;

    var origin_hour = times[0];                                     // origin end time hour
    var origin_minute = times[1];                                   // origin end time minute
    var subtract;

    if(default_minute == 10){subtract = Math.abs(size) * 10;}                 // subtract time(minute)
    if(default_minute == 15){subtract = Math.abs(size) * 15;}                 // subtract time(minute )

    if(Number(subtract) >= 60){
        subtract_hour = Math.floor(Number(subtract) / 60);
        subtract_minute = Number(subtract) % 60;
    } else{
        subtract_hour = 0;
        subtract_minute = subtract;
    }

    if(Number(origin_minute) - Number(subtract_minute) < 0){
        subtract_hour = Number(subtract_hour) + 1;
        subtract_minute = Math.abs((Number(subtract_minute) - Number(origin_minute)) - 60);
    } else{
        subtract_minute = Math.abs(Number(origin_minute) - Number(subtract_minute));
    }
    subtract_hour = Number(origin_hour) - Number(subtract_hour);

    if(subtract_minute == 0) return subtract_hour+":00";
    else return subtract_hour+":"+subtract_minute;
}
function sizeCheck(originSize, newSize){
    return Number(Number((Number(newSize - originSize))/25));
}

// 문자 메세지 다시 보내기
function msgRefundSend(msg_send_flag, res_idx) {
    // var data = "";
    //     if(msg_send_flag) {
    //     var conf = confirm("예약 자동 문자를 다시 발송하시겠습니까?");
    //     if(conf) {
    //         // submitAjaxSync("/Reservation/autoReservationMsgReSend", {resIdx : res_idx});
    //         data = submitAjax("/Reservation/autoReservationMsgReSend", {resIdx : res_idx});
                return submitAjax("/Reservation/autoReservationMsgReSend", {resIdx : res_idx});
    //     }
    // }
    // return data;
}