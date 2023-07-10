/**
 * 
 */
 	function getdeptbycompid(cid)
	{
		$('#department').empty();
		$('#department').append("<option selected disabled>Please Select Department</option>");
		let b_url = $('#base_url').val();
		let appname = $('#appname').val();
		
		$.ajax({
//				url		:	'/getdeptbycompid/'+cid,
				url		:	b_url+'/'+appname+'/getdeptbycompid/'+cid,
				type	:	'GET',
				dataType:	'JSON',
				success : 	function(result)
				{
					$.each(result,function(i){
						$('#department').append("<option value='"+result[i].dept_id+"'>"+result[i].dept_name+"</option>");	
					});
				}
		});
	}
	
	$(document).ready(function() {

		$("#company").select2({
			theme : 'classic',
			width : 'resolve'
		});
		$("#designation").select2({
			theme : 'classic',
			width : 'resolve'
		});
		$("#department").select2({
			theme : 'classic',
			width : 'resolve'
		});
		$("#type_id").select2({
			width : 'resolve',
			placeholder : 'Please Select Asset Type',
			allowClear : true
		});
		$("#multi_assets").select2({
			width 		: 'resolve',
			placeholder : 'Please Select Asset(s) ',
			allowClear 	:  true
		});
	})