/*
var me = {};
me.avatar = "https://lh6.googleusercontent.com/-lr2nyjhhjXw/AAAAAAAAAAI/AAAAAAAARmE/MdtfUmC0M4s/photo.jpg?sz=48";

var you = {};
you.avatar = "https://a11.t26.net/taringa/avatares/9/1/2/F/7/8/Demon_King1/48x48_5C5.jpg";
*/

function formatAMPM(date) {
    /*var data = date.getFullYear() + "-" + pad(date.getMonth() + 1, 2) + "-" + date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0'+minutes : minutes;*/
    return date.format('yyyy-MM-dd hh:mm a/p');
}
//-- No use time. It is a javaScript effect.
function insertChat(text, time){
    if (time === undefined)time = 0;
    var date = new Date().format('yyyy-MM-dd hh:mm a/p');/*formatAMPM(new Date());*/
    console.log(formatAMPM(new Date()));
    console.log(date);
    // var status = $('#s').is(":checked");
    var status = $('#s-parent-label').hasClass('active');
    var s = status ? 1 : 0;

    $.post('/insertChat', { content : text, status : s }, function (data) {
        if(data.code === 200){
            if(s === 0){
                var control = '<li data-value="' + data.idx + '">' +
                    '<div class="msj macro">' +
                    '<div class="avatar"></div>' +
                    '<div class="text text-l">' +
                    '<p>'+ text +'</p>' +
                    '<p><small>'+date.substring(date.indexOf(" "), date.length)+'</small></p>' +
                    '</div>' +
                    '</div>' +
                    '<i class="fa fa-close"onclick="del(this)"></i>' +
                    '</li>';
                setTimeout(function(){$("#chat_ul").append(control).scrollTop($("#chat_ul").prop('scrollHeight'));}, time);
            } else {
                var control = '<li data-value="' + data.idx + '">'+ text +'<p>'+ date + '</p><i class="fa fa-close" onclick="del(this)"></i></li>';
                setTimeout(function(){$("#chat_notice").prepend(control).scrollTop(0);}, time); // scrollTop(0) -> 최상단으로 이동
            }
        } else alert(data.code + " :: Error");
    }, 'json').fail(function (error) {
        alert(error.status);
    });
}
function del(obj) {
    $.post('/deleteChat', { idx : $(obj).closest("li").attr("data-value") }, function (data) {
        if(data.code===200)$(obj).closest("li").remove();
        else if(data.code===900)alert(data.code + " :: Error");
    }, 'json').fail(function (error) {
        alert(error.status);
    });
}
function resetChat(){
    $("#chat_ul").empty();
}

$(".mytext").on("keydown", function(e){
    if (e.which === 13){
        var text = $(this).val();
        if (text !== ""){
            insertChat(text);
            $(this).val('');
            $('#s').prop("checked", false);
        }
    }
});

$("#send_btn").click(function () {
    var text = $(".mytext").val();
    if(text !== "" && text !== undefined){
        insertChat(text);
        $(".mytext").val('');
        $('#s').prop("checked", false);
    }
});

$('body > div > div > div:nth-child(2) > span').click(function(){
    $(".mytext").trigger({type: 'keydown', which: 13, keyCode: 13});
});

//-- Clear Chat
resetChat();

//-- Print Messages
/*insertChat("me", "Hello Tom...", 0);
insertChat("you", "Hi, Pablo", 1500);
insertChat("me", "What would you like to talk about today?", 3500);
insertChat("you", "Tell me a joke",7000);
insertChat("me", "Spaceman: Computer! Computer! Do we bring battery?!", 9500);
insertChat("you", "LOL", 12000);*/


//-- NOTE: No use time on insertChat.