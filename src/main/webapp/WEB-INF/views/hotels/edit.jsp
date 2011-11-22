<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="hotelDetails">
	<spring:url var="hotelsUrl" value="/hotels/{id}">
		<spring:param name="id" value="${hotel.id}"/>
	</spring:url>
	<form:form modelAttribute="hotel" action="${hotelsUrl}" method="post" cssClass="inline ajaxForm">
	    <span><form:errors path="*"/></span>
		<fieldset>
			<legend>Edit Hotel</legend>
			<div>
				<label for="searchString">Name:</label>
				<form:input id="name" path="name"/>
			</div>
			<div>
				<label for="city">City:</label>
				<form:input id="city" path="city"/>
			</div>
			<div>
				<button id="saveHotel" type="submit">Save</button>
			</div>		
	    </fieldset>
	</form:form>
</div>