/**
 * 
 */



$(document).ready(function() {
	$('#produitList').DataTable({
		"lengthMenu":[[5,10,15,20,-1],[5,10,15,20,"All"]],
		"ordering":false,
		stateSave:true
	});

	$("#produitList").on('page.dt', function() {
		$('html, body').animate({
			scrollTop: $('#produitList').offset().top
		}, 200);
	});
});


$(document).ready(function() {
	$('.delete-product').on('click', function (){
		/*<![CDATA[*/
		var path = /*[[@{/}]]*/'/remove2';
		/*]]>*/

		var id=$(this).attr('id');

		bootbox.confirm({
			message: "etes vous sur de vouloir supprimer? c'est irreverssible.",
			buttons: {
				cancel: {
					label:'<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label:'<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				return confirmed;
			}
		});
	});


	$('#deleteSelected').click(function() {
		var idList= $('.checkboxProduct');
		var productIdList=[];
		for (var i = 0; i < idList.length; i++) {
			if(idList[i].checked==true) {
				productIdList.push(idList[i]['id'])
			}
		}

		console.log(productIdList);

		/*<![CDATA[*/
		var path = /*[[@{/}]]*/'removeList';
		/*]]>*/

		bootbox.confirm({
			message: "Are you sure to remove all selected products? It can't be undone.",
			buttons: {
				cancel: {
					label:'<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label:'<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				if(confirmed) {
					$.ajax({
						type: 'POST',
						url: path,
						data: JSON.stringify(productIdList),
						contentType: "application/json",
						success: function(res) {
							console.log(res);
							location.reload()
						},
						error: function(res){
							console.log(res);
							location.reload();
						}
					});
				}
			}
		});
	});

	function checkPasswordMatch() {
		var password = $("#txtNewPassword").val();
		var confirmPassword = $("#txtConfirmPassword").val();

		if(password == "" && confirmPassword =="") {
			$("#checkPasswordMatch").html("");
			$("#updateUserInfoButton").prop('disabled', false);
		} else {
			if(password != confirmPassword) {
				$("#checkPasswordMatch").html("Passwords do not match!");
				$("#updateUserInfoButton").prop('disabled', true);
			} else {
				$("#checkPasswordMatch").html("Passwords match");
				$("#updateUserInfoButton").prop('disabled', false);
			}
		}
	}


	$("#selectAllProducts").click(function() {
		if($(this).prop("checked")==true) {
			$(".checkboxProduct").prop("checked",true);
		} else if ($(this).prop("checked")==false) {
			$(".checkboxProduct").prop("checked",false);
		}
	})
});
