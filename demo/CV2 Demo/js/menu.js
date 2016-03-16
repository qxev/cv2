// JavaScript Document
var cur_menu_id = ''; 
var submenu_timer=null;

function set_menu(menu_id)
{
	if(menu_id=='NONE') return;

	sub  = document.getElementById('submenu_' + menu_id);

	sub.style.display = 'block';
}

function unset_menu(menu_id)
{
	if(menu_id=='NONE') return;

	sub  = document.getElementById('submenu_' + menu_id);

	sub.style.display = 'none';
}

function change_submenu(menu_id)
{
	if(cur_menu_id=='') cur_menu_id = old_menu_id;

	unset_menu(cur_menu_id);
	cur_menu_id = menu_id;
	set_menu(cur_menu_id);

	if(submenu_timer!=null) clearTimeout(submenu_timer);
}

function restore_submenu_clear()
{
	unset_menu(cur_menu_id);
	cur_menu_id = old_menu_id;
	set_menu(cur_menu_id);
}

function restore_submenu()
{
	//restore_submenu_clear();
	submenu_timer = setTimeout('restore_submenu_clear()', 500);
}