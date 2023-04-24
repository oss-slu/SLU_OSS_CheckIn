<!DOCTYPE html>
<html>
<head>
	<title>Bootstrap UI Template</title>
	<!-- Include Bootstrap CSS -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
	
	<?php
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');
		
		curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/getAllCards');
		$allCards = curl_exec($ch);

		// curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/getCheckedIn');
		// $checkedIn = curl_exec($ch);

		// curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/getCheckedOut');
		// $checkedOut = curl_exec($ch);

		// curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/newCard/' + $card);
		// $newCard = curl_exec($ch);

		// curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/removeCard/' + $card);
		// $removeCard = curl_exec($ch);

		curl_close($ch);
	?>

	<div class="container">
		<h2>Table Example</h2>
		<table class="table table-dark">
			<thead>
			<tr>
				<th>Name</th>
				<th>Status</th>
			</tr>
			</thead>
			<tbody>
			<?php foreach($allCards as $key => $value) { ?>
				<tr>
				<td><?php echo $key; ?></td>
				<td><?php echo $value ? 'In' : 'Out'; ?></td>
				</tr>
			<?php } ?>
			</tbody>
		</table>
	</div>

	<!-- Include Bootstrap JavaScript -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
