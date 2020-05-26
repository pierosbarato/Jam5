<html lang="en">
<head>
<meta charset="utf-8">
<title>Jam</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<!-- bootstrap -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" />

<script type="text/javascript"
	src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js'></script>
<script type="text/javascript"
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.bundle.min.js"></script>

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css"/>

<!-- x-editable (bootstrap version) -->
<link href="css/bootstrap-editable.css" rel="stylesheet" />
<script src="js/bootstrap-editable.js"></script>

<link href="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/css/select2.min.css" rel="stylesheet" />
<!-- 
<link href="select2/css/select2-bootstrap4.css" rel="stylesheet" />
 -->
<script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"></script>
<!-- main.js -->
<script src="main.js"></script>
</head>

<body>

	<div class="container">

		<h1>Jam ... </h1>

		<div>
			<span>Cust:</span>
			<a href="#" id="cust2" data-type="select2" data-pk="1" data-value="BS"
				data-title="Select country">Piero</a>
		</div>
 
		<div>
			<span>User:</span> <a href="#" id="username" data-type="text"
				data-placement="right" data-title="Enter username">superuser</a>
		</div>

		<div>
			<span>Level:</span> <a href="#" id="status"></a>
		</div>

		<br/>
		<div>
			Jam5:
			<a class="btn btn-success my-2 my-sm-0" href="jQuery-Menu-Editor/test.html">
				<i class="fas fa-sync-alt"></i> Edit Menu</a>
			<a class="btn btn-success my-2 my-sm-0" href="jam.html">
				<i class="fas fa-sync-alt"></i> Edit Pane</a>
			<a class="btn btn-success my-2 my-sm-0" href="nest/nest.html">
				<i class="fas fa-sync-alt"></i> Next</a>
			<a class="btn btn-success my-2 my-sm-0" href="server">
				<i class="fas fa-sync-alt"></i> Server</a>
		</div>

		<br/>

		<div class="col-md-4">
		<select class="form-control" id="item-select" style="width: 100%;"></select>
		</div>
		
		<script>

			$('#item-select').select2({
//				theme: 'bootstrap4',
				allowClear : true,
				placeholder : "Choose a Customer",
				ajax : {
//					url: "http://sh-example-simple.herokuapp.com",
					url : "/Jam5/server/get",
					dataType : "json",
					width : 'style',
					delay : 250,
					data : function(params) {
						return {
							q : params.term,
							page : params.page,
							per_page : 10
						};
					},
					processResults : function(data, page) {
						return {
							// Select2 requires an id, so we need to map the results and add an ID
							// You could instead include an id in the tsv you add to soulheart ;)
							results : data.matches.map(function(item) {
								return {
									id : item.id,
									text : item.text
								};
							}),
							pagination : {
								// If there are 10 matches, there's at least another page
								more : data.matches.length === 10
							}
						};
					},
					cache : true
				},
//				minimumInputLength: 1,
				templateResult : formatRepo,
				templateSelection : formatRepoSelection
			});

			function formatRepo2(repo) {
//				if(repo.loading) {
					return repo.id + '\t' + repo.text;
//				}
			}

			var firstEmptySelect = true; // Indicate header was create

			function formatRepo(item) {

				if (!item.id) {
					// trigger when remote query
					firstEmptySelect = true; // reset
					return item.text;
				}

				var $container; // This is custom templete container.

				// Create header
				if (firstEmptySelect) {

					firstEmptySelect = false;

					$container = $('<div class="row" style=\"margin:0\">'
							+ '<div><a class="btn btn-success">Clienti</a></div>'
							+ '<div><a class="btn btn-success">Fornitori</a></div>'
							+ '</div>' + '<div class="row" style=\"margin:0\">'
							+ '<div class="col-md-1"><b>ID</b></div>'
							+ '<div class="col-md-5"><b>Name</b></div>' +
//	            				'</div>' +
//	                    		'<div class="row" style=\"margin:0\">' +
//	                    		'<div class="col-md-1">' + item.id + '</div>' +
//	                    		'<div class="col-md-5">' + item.text + '</div>' +
							'</div>');

				} else {
					$container = $('<div class="row" style=\"margin:0\">'
							+ '<div class="col-md-1">' + item.id + '</div>'
							+ '<div class="col-md-5">' + item.text + '</div>'
							+ '</div>');
				}

				return $container
			}

			function formatRepoSelection(repo) {
				return repo.id + " - " + repo.text;
			}
		</script>
	</div>

</body>
</html>
