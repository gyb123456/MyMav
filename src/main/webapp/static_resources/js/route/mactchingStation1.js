/**
 * 
 */

$(function(){
	intEvent();
})

function intEvent(){//初始化事件
	$("#table").on("load-success.bs.table",function(e,res){
				var data = res[res.length-1];
				var temp=0
				var xuhao =1;
				for(i=1;i<res.length;i++){
					if(res[i-1].groupId==res[i].groupId){//下一行与当前行不是同一组就将这一组的开始一行到当前行组数列融合
						$("#table").bootstrapTable('mergeCells',{index:temp,field:"groupId",rowspan:(i-temp+1)});
					}else{
							temp=i;
							xuhao++;
					}
				}
		//alert("一共"+xuhao+"个站点");
		});
}
function firstStationFormatter(value){
	if(!value){
		return "-";
	}
	return "是";
}
 
function numFormatter(value,row,index){
	return index+1;
}