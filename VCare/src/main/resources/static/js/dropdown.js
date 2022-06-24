function getSubCategories(hospitalBranchId){
var appname=window.location.pathname.substr(0, window.location.pathname.lastIndexOf('/uploadDoctorProfile'));
document.getElementById('objdoctor').action = appname+"/uploadDoctorProfile?getQry=getSubsCatagory&hospitalBranchId="+hospitalBranchId;

document.getElementById('objdoctor').submit();
}

function selectBranch(serviceName){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectBranch'));
	/*var doctorId=document.getElementById('doctorId').value;*/
	document.getElementById('objdoctor').action=appname+"/selectBranch?getQry=selectBranch&serviceName="+serviceName;
	document.getElementById('objdoctor').submit();
}

function selectedService(serviceName){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectedService'));
	document.getElementById('appointment').action=appname+"/selectedService?getQry=selectedService&serviceName="+serviceName;
	document.getElementById('appointment').submit();
}

function selectedBranches(branchId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectedBranches'));
	document.getElementById('appointment').action=appname+"/selectedBranches?getQry=selectedService&branchId="+branchId;
	document.getElementById('appointment').submit();
}  

function selectedDoctors(doctorId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectedDoctors'));
	var hospitalBranchId=document.getElementById('BranchId').value;
	/*var patientId=document.getElementById('patient').value;*/
	document.getElementById('appointment').action=appname+"/selectedDoctors?getQry=selectedDoctors&hospitalBranchId="+hospitalBranchId+"&doctorId="+doctorId;
	document.getElementById('appointment').submit();
}


function selectService(serviceName){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectDoc'));
	/*var doctorId=document.getElementById('doctorId').value;*/
	document.getElementById('appointmentObj').action=appname+"/selectDoc?getQry=selectService&serviceName="+serviceName;
	document.getElementById('appointmentObj').submit();
}

function doctorAvailable(doctorId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/selectDoc'));
	/*var doctorId=document.getElementById('doctorId').value;*/
	document.getElementById('appointmentObj').action=appname+"/selectDoc?getQryDoc=doctorAvailable&doctorId="+doctorId;
	document.getElementById('appointmentObj').submit();
}

function selectedDate(date){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/ihgufyd'));
	var doctorId=document.getElementById('doctorId').value;
	var pid=document.getElementById('patientId').value;
	var hospitalBranchid=document.getElementById('hbid').value;
	document.getElementById('doc').action=appname+"/Viewdoctors?getQryDate=selectDate&Date="+date;
	document.getElementById('doc').submit();
}

function dateAvailable(date){
var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/ihgufyd'));
var doctorId=document.getElementById('doctorId').value;
var hospitalBranchId=document.getElementById('hospitalBranch').value;
document.getElementById('appointmentObj').action=appname+"/selectDoc?getQryDate=dateAvailable&Date="+date+"&doctorId="+doctorId+"&hospitalBranchId="+hospitalBranchId;
document.getElementById('appointmentObj').submit();
}


function offlineappointmentform(){
var doctorId=document.getElementById('doctor').value;
var pid=document.getElementById('patient').value;
document.appointment.submit();

var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/ihgufyd'));
}


function checkService(serviceName){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/checkService'));
	document.getElementById('appointment').action=appname+"/checkService?getQry=checkService&serviceName="+serviceName;
	document.getElementById('appointment').submit();
}

function checkBranches(branchId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/checkBranches'));
	document.getElementById('appointment').action=appname+"/checkBranches?getQry=checkBranches&branchId="+branchId;
	document.getElementById('appointment').submit();
}

function checkDoctors(doctorId){
	var appname=window.location.pathname.substr(0,window.location.pathname.lastIndexOf('/checkDoctors'));
	var hospitalBranchId=document.getElementById('BranchId').value;
	/*var patientId=document.getElementById('patient').value;*/
	document.getElementById('appointment').action=appname+"/checkDoctors?getQry=checkDoctors&hospitalBranchId="+hospitalBranchId+"&doctorId="+doctorId;
	document.getElementById('appointment').submit();
}


