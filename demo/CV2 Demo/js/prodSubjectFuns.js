// JavaScript Document
function Delete(ID){
	if(confirm(PRODSUBJECT_DELETE)){
		document.location.href = "prodSubjectAct"+FILE_EXT+"?act=delete&sid="+ID;
	}
}

function Active(ID){
	if(confirm(PRODSUBJECT_ACTIVE)){
		document.location.href = "prodSubjectAct"+FILE_EXT+"?act=subjectactive&sid="+ID;
	}
}

function Verify(ID){
	if(confirm(PRODSUBJECT_VERIFY)){
		document.location.href = "prodSubjectAct"+FILE_EXT;
	}
}

function NoVerify(ID){
	if(confirm(PRODSUBJECT_NOVERIFY)){
		document.location.href = "prodSubjectAct"+FILE_EXT+"?act=subjectnoverify&sid="+ID;
	}
}

function addSubject(){
	document.location.href = "prodSubjectInsert"+FILE_EXT;
}

function editSubject(ID){
	document.location.href = "prodSubjectEdit"+FILE_EXT+"?sid="+ID;
}

function viewSubject(ID){
	document.location.href = "prodSubjectView"+FILE_EXT+"?sid="+ID;
}

function copySubject(ID){
	document.location.href = "prodSubjectInsert"+FILE_EXT+"?sid="+ID;
}

function viewAdvertises(ID){
	document.location.href = "prodAdv"+FILE_EXT+"?sid="+ID;
}

function viewTypes(ID){
	document.location.href = "prodType"+FILE_EXT+"?sid="+ID;
}

function preView(ID){
	document.location.href = "prodSubjectPreView"+FILE_EXT+"?sid="+ID;
}

function merchantView(ID){
	document.location.href = "prodSubjectView"+FILE_EXT+"?sid="+ID;
}