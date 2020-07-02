$(function () {
	$("#titoloTextBoxContainer").dxTextBox({
		placeholder: "Inserisci il titolo del documento",
		inputAttr: {
			id:"titolo",
            class:"form-control"
        },
		validationRules: [{
                type: "required",
                message: "Il titolo è obbligatorio"
            }]
	});
	$("#noteTextBoxContainer").dxTextArea({
		editorOptions: {
                height: 90
            },
		inputAttr: {
			id:"note",
            class:"form-control"
        }
	});
	$("#fileUploaderContainer").dxFileUploader({
		multiple: true,
		inputAttr: {
			id:"fileUpload"
        },
		selectButtonText: "Seleziona i documenti",
        labelText: "",
        name: "files[]"
    });
});