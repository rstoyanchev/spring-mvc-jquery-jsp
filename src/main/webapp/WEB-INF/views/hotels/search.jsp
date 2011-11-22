<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<c:url var="hotelsUrl" value="/hotels"/>
<form:form id="searchForm" modelAttribute="searchCriteria" action="${hotelsUrl}" method="get" cssClass="inline ajaxForm">
    <span class="errors span-18">
    	<form:errors path="*"/>
    </span>
	<fieldset>
		<legend>Search Hotels</legend>
		<div class="span-8">
			<label for="searchString">Search String:</label>
			<form:input id="searchString" path="searchString"/>
		</div>
		<div class="span-6">
			<div>
				<label for="pageSize">Maximum results:</label>
				<form:select id="pageSize" path="pageSize">
					<form:option label="5" value="5"/>
					<form:option label="10" value="10"/>
					<form:option label="20" value="20"/>
				</form:select>
			</div>
		</div>
		<div class="span-3 last">
			<button id="findHotels" type="submit">Find Hotels</button>
		</div>		
    </fieldset>
</form:form>
