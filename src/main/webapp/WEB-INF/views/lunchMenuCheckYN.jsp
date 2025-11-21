<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.JSONArray"%>
<%@ page import="kr.co.pionnet.scdev4.bot.domain.common.constant.BotConst"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
#search_area {
	height: 24px;
	margin: 9px 5px 8px 0px;
}

.menu-value {
	padding: 2.5px 5px 2.5px 5px;
	width: 165px;
}

.menu-value:hover {
	background-color: #D5D5D5;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</head>
<body>
	<% String menuCheckYN = (String) request.getAttribute("menuCheckYN");%>
	
	<div class="chooseLunchMenu">
		<%
			if (menuCheckYN.equals(BotConst.RESULT_NO)) {
		%>
		<div class="lunchMentText">
			<strong>점심 메뉴를 선택해주세요 :)</strong>
		</div>
		<form method="post" onsubmit="javascript:setMenuCode();"
			accept-charset="UTF-8">
			<div id="submit_area">
				<input type="text" id='search_area' name="menuName" size="20">
				<input type="submit" id="submit-button" name="submitBtn" value="확인"
					size="10">
			</div>
			<input type="hidden" id='menuCode_area' name="menuCode">
			<div id='autoMaker'></div>
		</form>
		<%
			}
		%>
	</div>
</body>
</html>
<script>
	var menuEnumList = <%=(JSONArray)request.getAttribute("menuList")%>
	var recommMenu = "<%=(String)request.getAttribute("recommMenu")%>"
	
	$('#search_area').keyup(
		function() {
			var searchText = $(this).val();
			$('#autoMaker').children().remove();

			if (searchText != '') {
				for (var i = 0; i < menuEnumList.length; i++) {
					if (menuEnumList[i].name.indexOf(searchText) > -1 && menuEnumList[i].name != recommMenu) {
						$('#autoMaker').append(
							$('<div class="menu-value">').text(menuEnumList[i].name));
					}
				}

				$('#autoMaker').children().each(function() {
					$(this).click(function() {
						$('#search_area').val($(this).text());
					});
				});
			}
		});

	function setMenuCode() {
		$('#menuCode_area').val('');

		var searchText = $('#search_area').val();

		for (var i = 0; i < menuEnumList.length; i++) {
			if (menuEnumList[i].name == searchText) {
				$('#menuCode_area').val(menuEnumList[i].code);
			}
		}
	}
</script>

