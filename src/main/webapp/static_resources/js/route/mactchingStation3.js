/**
 * 
 */
	
 $(function(){
	intEvent();
	addOption();
});
//保存table的数据，全局
var g_tabledata =[];

function intEvent(){ //初始化事件
	$("#table").on("load-success.bs.table",function(e,res){
		 g_tabledata = res;
		 search();
	});
	$('#stationId').change(function(){ //选择框改变事件
		search();
	});
}
function search(){
	var name = $('#stationId').find("option:selected").text();
	//alert(name);
	if(name==""){
		$('#table').bootstrapTable('load', g_tabledata);
	}else{
		var data = new Array();
	 	$.each(g_tabledata,function(i,item){
	 		if(item.vo.stationName == name){
	 			data.push(item);
	 		}
	 	});
	    $('#table').bootstrapTable('load', data);
		}
}
function addOption(){
	 $.post("user/findSameNameStation",{},function(data){
 	$('#stationId').append("<option value='' name=''</option>");
 	for(index in data){
 		var id = data[index][0];
 		var name = data[index][1];
 		$('#stationId').append("<option value='"+id+"'>"+name+"</option>");
 	}
 },'json');
}
function numFormatter(value,row,index){
	return index+1;
}

function dirFormatter(value,row,index){
	if(!value){
		return "上行";
}
return "下行";
}

//设置checkbox是否可选
function stateFormatter(value,row,index){
	if(row.match){
		this.checkboxEnabled = false; 
	}else{
		this.checkboxEnabled = true; 
	}
}
function matchFormatter(value,row,index){
	if(row.match){
		return "是";
	}
	return "";
}
queryParam= function(params) {
	var stationId = $("#stationId option:selected").val();
	params.stationId=stationId;
	return params;
}
/*responseHandler=function(res) {
	g_tabledata = res;
	return res;
}*/

//匹配选中项，保存
function match(){
	var selectedVal = $('#table').bootstrapTable('getSelections');
//	var allselectedVal = $('#table').bootstrapTable('getAllSelections');
//	console.log(selectedVal);
//	console.log(allselectedVal);
	if(selectedVal.length>1){
		var stationIds = new Array();
		$.each(selectedVal,function(i,item){
			stationIds.push(item.vo.stationId);
	 	});
		$.post('user/saveStationsRel',{stationIds:stationIds},function(result){
			refresh();
			alert(result);
		});
	}else{
		alert("请选择匹配项")
	}
	//$('#table').bootstrapTable('showRow', {index:100});
}

//刷新表格,重新拿到所有数据
function refresh(){
	var stationId = $("#stationId option:selected").val();
	$('#table').bootstrapTable('refresh', {query: 
																		{stationId: stationId} 
												     } );
}