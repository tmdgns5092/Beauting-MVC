/* 신규 예약 모달 */
$('.emptyTd').click(function () {
    $('#createResModal').draggable({handle : ".modal-header"});
    $("#createResModal").modal({backdrop: 'static', keyboard: false});
});
/* 신규 고객 모달 */
$('#insertClient').click(function () {
    $('#auto').trigger('click');
    /* 직원 리스트 */
    readEmployeeList();
    $('#insertClientModal').draggable({handle : ".modal-header"});
    $("#insertClientModal").modal({backdrop: 'static', keyboard: false});
});
/* 신규 직원 모달 */
$('#insertEmployee').click(function () {

    $.ajax({
        url : "selectEmployeeNumber",
        type: "post",
        dataType : "json",
        success : function(data){
            if(data.code == 200){
                if(data.eNumber > 0) $("#emp_number").val(Number(data.eNumber +1));
                else $("#emp_number").val("1");
            } else{
                alert("code 900");
            }
        },
        error : function(){
            alert("에러가 발생했습니다."); location.href = document.URL;
        }
    });
    var now = new Date();
    var year= now.getFullYear();
    var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
    var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
    var chan_val = year + '-' + mon + '-' + day;
    $("#emp_joinDatePicker").val(chan_val);

    $('#insertEmployeeModal').draggable({handle : ".modal-header"});
    $("#insertEmployeeModal").modal({backdrop: 'static', keyboard: false});
});


/* 매장 직원 리스트 가져오기 */
function readEmployeeList(){
    $.ajax({
        url : "readEmployeeList",
        type: "post",
        dataType : "json",
        success : function(data){
            if(data.code == 200){
                //console.log(JSON.stringify(data.list));

                //console.log();
                for(var i = 0; i<data.listSize; i++){
                    //console.log("value : " + data.list[i].id +", name : " + data.list[i].name);
                    $("#employee_list").append("<option value='" + data.list[i].id + "'>" + data.list[i].name + "</option>");
                }
            } else{
                //console.log("code : 900");
            }
        },
        error : function(){

        }
    });
}