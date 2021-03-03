/*
var tableRows = {
    '1': { name: 'First row', isEditable: true, isRemovable: true },
    '2': { name: 'Second row', isEditable: true, isRemovable: true },
    '3': { name: 'Third row', isEditable: true, isRemovable: true }
};

var menu1 = new BootstrapMenu('.reservation-td',{
    menuEvent: 'click',
    menuSourceL: 'element',
    menuPosition: 'belowLeft',
    fetchElementData: function($rowElem) {
        // console.log("row elem : " + $rowElem);
        alert(JSON.stringify($rowElem));
        var rowId = $rowElem.data('rowId');
        console.log("rowId : " + rowId);
        console.log("tableRows : " + tableRows[rowId]);
        return tableRows[rowId];
    },
    actions: [{
        name: '고객입장',
        onClick: function() {
            alert('고객입장');
            toastr.info("'Action' clicked!");
        }
    }, {
        name: '계산',
        onClick: function() {
            alert('계산');
            toastr.info("'Another action' clicked!");
        }
    }, {
        name: '예약수정',
        onClick: function(row) {
            console.log(row);
            alert('예약수정');
            toastr.info("'A third action' clicked!");
        }
    },{
        name: '예약취소',
        onClick: function() {
            alert('예약취소');
            toastr.info("'A third action' clicked!");
        }
    },{
        name: '예약불이행',
        onClick: function() {
            alert('예약불이행');
            toastr.info("'A third action' clicked!");
        }
    },{
        name: '고객정보',
        onClick: function() {
            alert('고객정보');
            toastr.info("'A third action' clicked!");
        }
    },{
        name: 'X 닫 기',
        onClick: function() {
            return false;
        }
    }]
});*/
