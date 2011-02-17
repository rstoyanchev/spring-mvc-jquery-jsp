<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="hotelResults">
<c:if test="${empty hotelList}">
	<p>No hotels. Please, change your search criteria.</p>
</c:if>
<c:if test="${not empty hotelList}">
	<c:url var="hotelsUrl" value="/hotels"/>
	<p>
	<table class="summary">
		<thead>
			<tr>
				<th>Name</th>
				<th>Address</th>
				<th>City, State</th>
				<th>Zip</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="hotel" items="${hotelList}">
				<tr>
					<td>${hotel.name}</td>
					<td>${hotel.address}</td>
					<td>${hotel.city}, ${hotel.state}, ${hotel.country}</td>
					<td>${hotel.zip}</td>
					<td><a href="${hotelsUrl}/${hotel.id}" class="ajaxLink">View Hotel</a></td>
				</tr>
			</c:forEach>
			<c:if test="${empty hotelList}">
				<tr>
					<td colspan="5">No hotels found</td>
				</tr>
			</c:if>
		</tbody>
	</table>
	<div class="buttonGroup">
		<c:if test="${searchCriteria.page > 0}">
			<a id="prevResultsLink"  class="ajaxLink"
				href="${hotelsUrl}?searchString=${searchCriteria.searchString}&pageSize=${searchCriteria.pageSize}&page=${searchCriteria.page - 1}">Previous</a>
		</c:if>
		<c:if test="${not empty hotelList && fn:length(hotelList) == searchCriteria.pageSize}">
			<a id="moreResultsLink" class="ajaxLink"
				href="${hotelsUrl}?searchString=${searchCriteria.searchString}&pageSize=${searchCriteria.pageSize}&page=${searchCriteria.page + 1}">Next</a>
		</c:if>
	</div>
	</p>
</c:if>
</div>	

