$(function(){


    var $lis = $('ul').find('li'),
    	length = $lis.length;

     $lis.each(function(index,item){
     	$(item).attr('data-id',index);
     });


     function slider($lis,index,length){
			$lis.each(function(index,item){
				item.className = '';
			});
			index +=length;     	
 			$($lis[index%length]).addClass('active');
 			$($lis[(index-1)%length]).addClass('left1');
 			$($lis[(index-2)%length]).addClass('left2');
 			$($lis[(index+1)%length]).addClass('right1');
 			$($lis[(index+2)%length]).addClass('right2');
     }


     slider($lis,2,length);

     $lis.on('click',function(e){
     	var id = parseInt($(e.target).parents('li').attr('data-id'));
     	slider($lis,id,length);
     })

});

