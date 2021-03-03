/* 마케팅 문자 유틸 */
function objectSetAttribute(name, value){
    var tmp;
    tmp = document.createElement ( 'input');
    tmp.setAttribute ( 'type', 'hidden');
    tmp.setAttribute ( 'name', name);
    tmp.setAttribute ( 'value', value);
    return tmp;
}
// 페이징 버튼 동작
$('.paging_btn').click(function(){
    var object_array = new Array();

    object_array.push({name : 'pageNo', value : $(this).attr('data-value')});
    object_array.push({name : 'lastVisit', value : $('#search-filter-last-visit').val()});
    object_array.push({name : 'noVisit', value : $('#search-filter-last-no-visit').val()});
    object_array.push({name : 'rank', value : $('#search-filter-rank').val()});
    object_array.push({name : 'visit', value : $('#search-filter-visit').val()});
    object_array.push({name : 'filter', value : $('#search-filter').val()});
    object_array.push({name : 'text', value : $('#msg-search-text').val()});
    object_array.push({name : 'allCheck', value : $('#all-msg-check').prop('checked')});
    object_array.push({name : 'individual-check-array', value : JSON.stringify(client_msg_array)});
    object_array.push({name : 'exception-check-array', value : JSON.stringify(client_msg_exception_array)});
    object_array.push({name : 'fristName', value : uk($('#f_name').val())});
    object_array.push({name : 'secondName', value : uk($('#s_name').val())});
    object_array.push({name : 'thirdName', value : uk($('#t_name').val())});
    object_array.push({name : 'status', value : $('#search-filter-status').val()});
    object_array.push({name : 'sort_type', value : $('#sort-type').val()});
    subMitForm("/Messages/message", object_array);
});

// 검색
$('#msg-search-text').keydown(function(key){
    if(key.keyCode == 13){
        msgSearch();
    }
});
function msgSearch(){
    var text = $('#msg-search-text').val();
    var last_visit = $('#search-filter-last-visit').val();
    var no_visit = $('#search-filter-last-no-visit').val();
    var rank = $('#search-filter-rank').val();
    var visit = $('#search-filter-visit').val();
    var filter = $('#search-filter').val();
    var allCheck = $('#all-msg-check').prop('checked');
    var visit_checked;

    var status = $('#search-filter-status').val(); //회원권
    var sort_type = $("#sort-type").val();//정렬

    if($('#visit-show-select').is(":checked")){
        visit_checked = "last";
    } else {
        visit_checked = "no";
    }

    var data = {
        lastVisit : last_visit,
        noVisit : no_visit,
        rank : rank,
        visit : visit,
        filter : filter,
        text : text,
        allCheck : allCheck,
        visitChecked : visit_checked,
        status : status,
        sort_type : sort_type
    };

    form_submit("/Messages/message", data, 'post');
}
// /. 페이지


// 사전 등록된 발신자번호 불러오기
function senderNumberListCall(){
    var data = getSenderNumberList();
    var tmp = '';
    if(data.code == 200){
        if(data.SenderNumberList.length > 0){
            /* 발신번호가 존재한다 */
            $.each(data.SenderNumberList, function(index, value){
                // $.each(senderNumberListCalldata.SenderNumberList, function(index, value){
                if(value.state == 1){      // 등록 상태  0:대기, 1:등록, 2:취소
                    if(value.representYN)
                        tmp += '<option value="' + value.number + '" data-value="' + value.representYN + '" selected>' + value.number + '</option>';
                    else
                        tmp += '<option value="' + value.number + '" data-value="' + value.representYN + '">' + value.number + '</option>';
                }
            });
        }
        /* 발신번호가 미등록 */
        else{
            tmp = '<option value="-1">미등록</option>';
        }
        $('#senderNumberList').append(tmp);
        $('#senderNumberList').niceSelect('update');
    } else {
        alert("code : 900");
    }
}
// /. 페이지 로드

// 전체 선택 (체크박스)
$('#all-msg-check').click(function(){
    if($(this).prop("checked")){
        client_msg_array = new Array();
        $('#msg-client-table > tbody > tr').each(function(index, value){
            $(value).find('td[name=checkbox]').find('input[name=individual-check]').prop("checked", true);
        });
        defaultNameSet();
        receiverView(client_msg_exception_array, 'all');
    } else {
        client_msg_exception_array = new Array();
        $('#msg-client-table > tbody > tr').each(function(index, value) {
            $(value).find('td[name=checkbox]').find('input[name=individual-check]').prop("checked", false);
        });
        receiverView(client_msg_array, 'individual');
    }
});
$(document).on('click', 'tr[name=client_tr]', function () {
    $(this).find('input[name=individual-check][type=checkbox]').trigger('click');
});
// 개별 체크 확인
$(document).on('click', 'input[type=checkbox][name=individual-check]', function(){
    var client_idx = $(this).closest('tr').attr('data-value');
    var client_name = $(this).closest('tr').find('p[name=name]').text();
    var tmp = {client_idx : client_idx, client_name : client_name};

    // 전체 선택 제외 상태
    if(!$('#all-msg-check').prop('checked')){
        if($(this).prop('checked')){
            client_msg_array.push(tmp);
        } else{
            client_msg_array = jQuery.grep(client_msg_array, function(value){
                return JSON.stringify(value) != JSON.stringify(tmp);
            });
        }

        receiverView(client_msg_array, 'individual');
    }
    // 전체 선택 상태
    else{
        if(!$(this).prop('checked')){
            client_msg_exception_array.push(tmp);
            // 상위 3개 해제 && PageNo 1
            if($('#pageNo').val() == 1){
                allCheckTrueAndTopNameRelease();
            }
        } else {
            client_msg_exception_array = jQuery.grep(client_msg_exception_array, function(value){
                return JSON.stringify(value) != JSON.stringify(tmp);
            });
        }
        receiverView(client_msg_exception_array, 'all');
    }
});

/* 상위 3개 해제 시 */
function allCheckTrueAndTopNameRelease(){
    if($('#all-msg-check').prop('checked')){
        $('#f_name').val('');
        $('#s_name').val('');
        $('#t_name').val('');
        $('#msg-client-table > tbody > tr').each(function (index, value){
            if($(value).find('td[name=checkbox]').find('input[type=checkbox][name=individual-check]').prop('checked')){
                if($('#f_name').val() == ''){
                    var name = $(value).find('p[name=name]').text();
                    $('#f_name').val(name);
                }
                else if($('#s_name').val() == '' && uk($('#f_name').val()) != ''){
                    var name = $(value).find('p[name=name]').text();
                    $('#s_name').val(name);
                }
                else if($('#t_name').val() == '' && uk($('#f_name').val()) != '' && uk($('#s_name').val()) != ''){
                    var name = $(value).find('p[name=name]').text();
                    $('#t_name').val(name);
                }
            }
        });
        receiverView(client_msg_exception_array, 'all');
    }
}

/* 수신자 표시 */
function receiverView(array, stat){
    var total_count = parseInt(uncomma(uk_noshow($('#total_client_count').text())));
    var receivers = '';

    if(stat == 'all'){
        total_count = total_count - array.length;
        var count = 0;
        $('#hiddenNameSpace > input').each(function (index, value){
            if(uk($(value).val()) != ''){
                receivers += '<li>' + $(value).val() + '</li>';
                total_count = total_count - 1;
            }
        });
        if($('#msg-client-table > tbody > tr').length > 3 || $('#pageEndNo').val() != '1'){
            receivers += '<li>외 ' + total_count + '명</li>';
        }
        $('#receiver_ul').empty();
        $('#receiver_ul').append(receivers);

    } else if(stat == 'individual') {
        total_count = array.length;
        $.each(array, function(index, value){
            if(index > 2)
                return false;
            receivers += '<li>' + value.client_name + '</li>';
        });
        if(array.length > 3){
            receivers += '<li>외 ' + (array.length - 3) + '명</li>';
        }
        $('#receiver_ul').empty();
        $('#receiver_ul').append(receivers);
    }
}

$('input[type=radio][name=msg-type]').change(function(){
    if($(this).val() == '0'){
        $('.long_mg_div').hide();
    }
    else if ($(this).val() == '1'){
        $('.long_mg_div').show();
    }
});

function byteCheck(el){
    var str = el.val();
    var strLength = 0;
    var strPiece = "";

    for (i = 0; i < str.length; i++){
        var code = str.charCodeAt(i);
        var ch = str.substr(i,1).toUpperCase();
        strPiece = str.substr(i,1);
        code = parseInt(code);

        if ((ch < "0" || ch > "9") && (ch < "A" || ch > "Z") && ((code > 255) || (code < 0))){
            strLength = strLength + 3; //UTF-8 3byte 로 계산
        }else{
            strLength = strLength + 1;
        }
    }
    return strLength;
}

function cutByLen(str, maxByte) {
    for(b=i=0;c=str.charCodeAt(i);) {
        b+=c>>7?2:1;
        if (b > maxByte)
            break;
        i++;
    }
    return str.substring(0,i);
}
