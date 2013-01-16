var OP_RESET = 1;

function showAddUpdateVendor(id, isAdd, operation) {
	var form = $("#addUpdateVendor")[0];
	var title = $("#addUpdateFormTitle");
	if (isAdd) {
		form.reset();
		title.html("Add New Vendor:");
		form.id.value = -1;
		$("#divListOfVendors").css("display", "none");
		$("#divAddEditVendor").css("display", "block");
	} else {
		var btnAction, progressText, errMsg;
		if (operation == OP_RESET) {
			btnAction = $("#btnReset")[0];
			progressText = "Resetting...";
			errMsg = "Error resetting the vendor data...";
		} else {
			btnAction = $("#btnEdit" + id)[0];
			progressText = "Loading...";
			errMsg = "Error loading the vendor data for edit...";
		}
		var actionTextOri = btnAction.textContent;
		btnAction.textContent = progressText;
		btnAction.disabled = true;
		$
				.ajax({
					type : "GET",
					dataType : "json",
					url : "rest/vendor/" + id + ".json?_random="
							+ new Date().getTime(),
					success : function(data) {
						title.html("Edit Vendor - \"" + data["name"] + "\":");
						form.id.value = data["id"];
						form.name.value = data["name"];
						var chkBoxes = form.purchaseOrderAvailable;
						var purchaseOrderAvail = data["purchaseOrderAvailable"];
						chkBoxes[0].checked = purchaseOrderAvail;
						chkBoxes[1].checked = !purchaseOrderAvail;
						form.purchaseNumber.value = data["purchaseNumber"];
						form.orderType.value = data["orderType"];

						enablePurchaseDetails(purchaseOrderAvail);
						$("#divListOfVendors").css("display", "none");
						$("#divAddEditVendor").css("display", "block");

						btnAction.textContent = actionTextOri;
						btnAction.disabled = false;
					},
					error : function(data) {
						alert(errMsg);

						btnAction.textContent = actionTextOri;
						btnAction.disabled = false;
					}
				});
	}
	form.name.focus();
}

function enablePurchaseDetails(state) {
	var form = $("#addUpdateVendor")[0];
	var number = form.purchaseNumber;
	var type = form.orderType;
	number.disabled = !state;
	type.disabled = !state;
}

function saveVendor() {
	var jqForm = $("#addUpdateVendor");
	var form = jqForm[0];

	var errObj = validateVendor(form);
	if (errObj.error) {
		$("#divErrMsg").html(errObj.msg);
		$("#" + errObj.id).focus();
		return;
	}

	var btnSave = $("#btnSave")[0];
	var lblTextSave = btnSave.textContent;
	btnSave.textContent = "Saving...";
	btnSave.disabled = true;

	var id = form.id.value;
	var urlAddUpdate = "#";
	var method = "";
	if (id == -1) {
		urlAddUpdate = "rest/vendor/new?_random=" + new Date().getTime();
		method = "POST";
	} else {
		urlAddUpdate = "rest/vendor/" + id + "/edit?_random="
				+ new Date().getTime();
		method = "PUT";
	}
	var params = jqForm.serialize();
	$.ajax({
		url : urlAddUpdate + "&" + params,
		type : method,
		success : function() {
			btnSave.textContent = lblTextSave;
			btnSave.disabled = false;
			location.reload();
		},
		error : function() {
			alert("Error saving the vendor data...");
			btnSave.textContent = lblTextSave;
			btnSave.disabled = false;
		}
	});
}

function resetVendorForm() {
	var id = $("#addUpdateVendor")[0].id.value;
	showAddUpdateVendor(id, id == -1, OP_RESET);
}

function hideVendorForm() {
	$("#divAddEditVendor").css("display", "none");
	$("#divListOfVendors").css("display", "block");
}

function deleteVendor(id, name) {
	if (confirm("Are you sure you want to delete - \"" + name + "\"?")) {
		var btnDel = $("#btnDelete" + id)[0];
		var lblTextDel = btnDel.textContent;
		btnDel.textContent = "Deleting...";
		btnDel.disabled = true;

		$.ajax({
			url : "rest/vendor/" + id + "/delete?_random="
					+ new Date().getTime(),
			type : "DELETE",
			success : function() {
				btnDel.textContent = lblTextDel;
				btnDel.disabled = false;
				location.reload();
			},
			error : function() {
				alert("Error while deleting the vendor data...");
				btnDel.textContent = lblTextDel;
				btnDel.disabled = false;
			}
		});
	}
}

function validateVendor(form) {
	var errObj = new Object();
	errObj.error = false;
	if (form.name.value.trim().length == 0) {
		errObj.error = true;
		errObj.msg = "Name can't be empty.";
		errObj.id = form.name.id;
	} else if (form.purchaseOrderAvailable[0].checked) {
		if (form.purchaseNumber.value.trim().length == 0) {
			errObj.error = true;
			errObj.msg = "Purchase number can't be empty.";
			errObj.id = form.purchaseNumber.id;
		} else if (form.orderType.value == 0) {
			errObj.error = true;
			errObj.msg = "Select any order type.";
			errObj.id = form.orderType.id;
		}
	}
	return errObj;
}