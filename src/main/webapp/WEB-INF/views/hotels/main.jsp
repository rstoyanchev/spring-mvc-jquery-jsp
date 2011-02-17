
<jsp:include page="../common/header.jsp" />

<div>
	<jsp:include page="searchContent.jsp"/>
</div>
<div>
	<div class="span-16 colborder">
		<jsp:include page="listContent.jsp"/>
	</div>
	<div class="span-7 last">
		<jsp:include page="showContent.jsp"/>
	</div>
</div>

<jsp:include page="../common/footer.jsp" />

<script type="text/javascript">
	$(document).ready(function() {
		var ajaxify = function() {
			$(".ajaxForm").submit(function() {
				var data = $(this).serialize() + "&htmlFormat=nolayout";
				$.ajax({
					type : $(this).attr("method"),
					url : $(this).attr("action"), 
					data : data, 
					success : function(data) {
						var id = $(data).attr("id");
						$("#" + id).replaceWith(data);
						ajaxify();
					}
				});
				return false;  
			});
			$(".ajaxLink").click(function() {
				$.ajax({
					type : 'GET',
					url : $(this).attr("href"),
					data : "htmlFormat=nolayout",
					success : function(data) {
						var id = $(data).attr("id");
						$("#" + id).replaceWith(data);
						ajaxify();
					}
				});
				return false;
			});
		}
		ajaxify();
	});
</script>
