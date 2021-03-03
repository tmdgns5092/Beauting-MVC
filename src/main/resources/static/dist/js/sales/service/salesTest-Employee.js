/* 직원 스크립트 */

function emplMake(){
    var tmp = '';
    $.each(empl_list, function(index, value){
        tmp += '<button type="button" onclick="emplButtonsClicked(this)" class="btn btn-success empl-buttons">' +
            '<input type="hidden" name="idx" value="' + value.idx + '"/> ' +
            '<input type="hidden" name="name" value="' + value.name + '"/> ' +
            '<input type="hidden" name="code" value="' + value.code + '"/> ' +
            value.name +
            '</button>';
    });
    $('div[name=empl-body]').append(tmp);
}

function emplButtonsClicked(obj){
    if($(obj).hasClass('active')){
        $(obj).removeClass('active');

        var idx = $(obj).find('input[name=idx]').val();

        $(obj).closest('.box-body').find('table[name=empl-table] > tbody > tr').each(function(index, value){
            if($(value).find('td').find('input[name=idx]').val() == idx){
                $(value).remove();
            }
        });
    } else{
        $(obj).addClass('active');
        addUseEmployee(obj);
    }
}

function addUseEmployee(obj){
    var idx = $(obj).find('input[name=idx]').val();
    var name = $(obj).find('input[name=name]').val();
    var code = $(obj).find('input[name=code]').val();

    var tmp = '<tr>' +
        '<td>' +
        '   <input type="hidden" name="idx" value="' + idx + '"/>' +
        '   <input type="hidden" name="empl_code" value="' + code + '"/>' + name +
        '</td>' +
        '<td><input type="text" name="empl_cost" value="0"/></td>' +
        '<td onclick="emplRemove(this)"><button><i class="glyphicon glyphicon-remove"></i></button></td>';

    $(obj).closest('.box-body').find('table[name=empl-table]').append(tmp);
}

function emplRemove(obj){
    var idx = $(obj).closest('tr').find('input[name=idx]').val();

    $(obj).closest('.box-body').find('div[name=empl-body] > button').each(function(index, value){
        if($(value).find('input[name=idx]').val() == idx){
            $(value).closest('button').trigger('click');
        }
    });
    $(obj).closest('tr').remove();
}