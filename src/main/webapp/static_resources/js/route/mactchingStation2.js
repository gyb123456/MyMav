/**
 * 
 */
	
 $(function(){
	intEvent();
	addOption();
});
//保存table的数据，全局
var g_tabledata =[];
//某个站点是否匹配，全局
var g_match = false;
var g_stationId = null;
var g_station_data =null;
function intEvent(){ //初始化事件
	$("#table").on("load-success.bs.table",function(e,res){
		 g_tabledata = res;
		 search();
	});
	$('#stationId').change(function(){ //站点选择框改变事件
		search();
	});
	$('#route').change(function(){ //弹出框里线路选择改变事件
		 $('#dir').empty();
		 var routeId = $('#route').selectpicker('val')
		 $.post("user/getStnByRut",{routeId:routeId},function(data){
			 g_station_data = data;
			 var s1='',s2='',x1='',x2='';//上行、下行
			 for (var i = 0; i < data.length; i++) {
				var vo = data[i];
				if(vo.dir==false){//上行
					s2=vo.stationName;
				}
				if(vo.dir==true){//下行
					x2=vo.stationName;
				}
			 }
			 for (var i = data.length-1; i >0; i--) {
					var vo = data[i];
					if(vo.dir==false){//上行
						s1=vo.stationName;
					}
					if(vo.dir==true){//下行
						x1=vo.stationName;
					}
		 	}
			 if(s1!='' && s2!=''){
				 $('#dir').append("<option value='0'>"+s1+"—>"+s2+"</option>");
			 }if(x1!='' && x2!=''){
				 $('#dir').append("<option value='1'>"+x1+"—>"+x2+"</option>");
			 }
			 $('#dir').selectpicker('refresh');
			 setStations();
			 },'json');
	});
	$('#dir').change(function(){ //弹出框里方向选择改变事件
		 console.log("方向变了");
		 setStations();
		 
	});
	$('#myModal').draggable({
        handle: ".modal-header"   // 只能点击头部拖动
    });
}

/**
 * 设置弹出框里的站点
 */
function setStations(){
	$('#station').empty();
	var dir = $('#dir').selectpicker('val');
	console.log("dir="+dir);
	for (var int = 0; int < g_station_data.length; int++) {
		 var vo = g_station_data[int];
		 if(dir == 0){
			 if(vo.dir==false){
				 $('#station').append("<option value='"+ vo.stationId+"'>"+ vo.stationName+"</option>");
			 }
		 }else{
			 if(vo.dir==true){
				 $('#station').append("<option value='"+ vo.stationId+"'>"+ vo.stationName+"</option>");
			 }
		 }
	}
	$('#station').selectpicker('refresh');
}
function search(){
	var name = $('#stationId').find("option:selected").text();
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
	 $.post("user/findAllStation",{},function(data){
	 	$('#stationId').append("<option value='' name=''</option>");
	 	for(index in data){
	 		var id = data[index][0];
	 		var name = data[index][1];
	 		$('#stationId').append("<option value='"+id+"'>"+name+"</option>");
	 	}
	 },'json');
	 
	 $.post("user/getAllRoute",{},function(data){
		 	$('#route').append("<option value='' name=''</option>");
		 	for(index in data){
		 		var id = data[index][0];
		 		var name = data[index][1];
		 		$('#route').append("<option value='"+id+"'>"+name+"</option>");
		 	}
		 	$('#route').selectpicker('refresh');
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

function relFormatter(value,row,index){
	var stationId = row.vo.stationId;
	var match = row.match;
	var btn = "<input type='button'  class='btn btn-sm btn-primary' data-toggle='modal' data-target='#myModal' onclick=\"view('"+stationId+"', "+match+")\" value='查看'/>";
	return btn;
}
//查看
function view(stationId, match){
	g_match = match;
	g_stationId = stationId;
	refreshModel();
}
queryParam= function(params) {
	var stationId = $("#stationId option:selected").val();
	params.stationId=stationId;
	return params;
}



//刷新表格,重新拿到所有数据
function refresh(){
	var stationId = $("#stationId option:selected").val();
	$('#table').bootstrapTable('refresh', {query: 
																		{stationId: stationId} 
												     } );
}
function refreshModel(){
	$('#tableModel').bootstrapTable('refresh', {query: {stationId: g_stationId} 
	} );
}
//---------------------------modal-----------------------------------------------------------
/*queryParamModel	= function(params) {
	params.stationId=$('#stationId').val();
	return params;
}*/
function firstStationFormatter(value){
	if(!value){
		return "";
	}
	return "是";
}
function delFormatter(value,row,index){
	var btn = "-";
	if(g_match){
		btn= "<input type='button'  class='btn btn-sm btn-danger' onclick=\"delRel('"+value+"')\" value='移除'/>";
	}
	return btn;
}
//匹配选中项，保存
function saveRel(){
	var selectedVal = $('#table').bootstrapTable('getSelections');
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

//删除站点的对应关系
function delRel(stationId){
	var hint ="确定移除该站点吗？";
	if(stationId==g_stationId){
		hint="确定移除自己吗？";
	}
  var r=confirm(hint);
  if (r==true)
    {
	  $.post("user/delRel",{stationId:stationId},function(data){
			if(data=="SUCCESS"){
				if(stationId==g_stationId){//说明删除自身
					g_match = false;
				}
				refreshModel();
				refresh();
			}
			alert(data);
		});
    }
}

/**
 * 弹出框里的添加站点功能，其实是更新操作
 */
function updateRel(){
	var stationId =  $('#station').selectpicker('val');
	var tableData = $('#tableModel').bootstrapTable('getData');
	console.log(tableData);
	var stationIds = new Array();
 	$.each(tableData,function(i,item){
		stationIds.push(item.stationId);
 	});
 	console.log(stationIds);
 	$.post("user/updateRel",{stationId:stationId, stationIds:stationIds},function(data){
		if(data=="SUCCESS"){
			g_match = true;
			refreshModel();
			refresh();
			alert(data);
		}
	});
}