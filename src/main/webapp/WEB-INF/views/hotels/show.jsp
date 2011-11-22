<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="hotelDetails">
<p>
	<c:if test="${hotel == null}">
		Hotel not yet selected.
	</c:if>
	<c:if test="${hotel != null}">
		
		<h3 class="alt">${hotel.name}</h3>
		<address>
			${hotel.address}
			<br />
			${hotel.city}, ${hotel.state}, ${hotel.zip}
			<br />
			${hotel.country}
		</address>
		<a href="${hotel.id}/edit" class="ajaxLink">Edit Hotel</a> 
	</c:if>
</p>
</div>