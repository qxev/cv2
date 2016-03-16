function setFormValue(urls,obj){
    show_ferret_object(obj);
    clearTest();
    show('ferret_div');
    var f = document.getElementById('ferret_form');
    f.action=urls;
}

function clearTest(){
    var t=document.getElementById('ferret_key');
    t.value='';
}

function show_ferret_object(obj){
    hide('ferret_client');
    hide('ferret_se');
    hide('ferret_campaign');
    hide('ferret_group');
    var str='ferret_'+obj;
    show(str);
}
function shutForm(){
    hide('ferret_div');
}
function hide(id){
    var e = document.getElementById(id);
    e.style.display = 'none';
}
function show(id){
    var e = document.getElementById(id);
    e.style.display='';
}