
<div>
	<jsp:include page="search.jsp"/>
</div>
<div>
	<div class="span-16 colborder">
		<jsp:include page="list.jsp"/>
	</div>
	<div class="span-7 last">
		<jsp:include page="show.jsp"/>
	</div>
</div>


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
