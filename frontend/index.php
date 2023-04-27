<!DOCTYPE html>
<html>
<head>
	<title>Check-In System</title>
	<!-- Include Bootstrap CSS -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>
	<?php
		$ch = curl_init();

		// Set request method to POST
		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');

		// Set timeout to 0 seconds to disable waiting for response
		curl_setopt($ch, CURLOPT_TIMEOUT, 0);

		// Disable response output
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, false);

		if(isset($_POST['start'])) {		
			curl_setopt($ch, CURLOPT_URL, "http://tdrpi.local:8080/checkin/init");
			curl_exec($ch);
		}

		if(isset($_POST['add'])) {		
			// get the text from the form
			$text = $_POST['addUser'];
			
			$curl = 'http://tdrpi.local:8080/checkin/newCard/'.$text;
			
			curl_setopt($ch, CURLOPT_URL, $curl);
			curl_exec($ch);
		}
		
		if(isset($_POST['remove'])) {		
			// get the text from the form
			$text = $_POST['removeUser'];
			
			$curl = 'http://tdrpi.local:8080/checkin/removeCard/'.$text;
			
			curl_setopt($ch, CURLOPT_URL, $curl);
			curl_exec($ch);
		}

		curl_close($ch);

		$ch = curl_init();
		
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'GET');

		curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/getAllCards');
		$allCards = curl_exec($ch);
		
		curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/getCheckedIn');
		$checkedIn = curl_exec($ch);
		
		curl_setopt($ch, CURLOPT_URL, 'http://tdrpi.local:8080/checkin/getCheckedOut');
		$checkedOut = curl_exec($ch);
		
		curl_close($ch);
	?>

	<div class="container-fluid bg-secondary text-white">
		<div class = "row justify-content-center">
			<div class = "col-6">
				<h1>Open Source with SLU: Check-In Info</h1>
			</div>
		</div>

		<div class = "row">
			<div class = "col-4">
				<h2>Start Check-In System</h2>
				<form method="post">
					<div class = "form-group">
						<div class = "form-row">
							<button type="submit" name="start" class = "btn btn-dark btn-lg">Start</button>
						</div>
					</div>
				</form>
			</div>

			<div class = "col-8">
				<h2>All Users</h2>
				<table class="table table-striped table-dark">
					<thead>
					<tr>
						<th>Name</th>
						<th>Status</th>
					</tr>
					</thead>
					<tbody>
						<?php 
						$data = json_decode($allCards, true);
						foreach($data as $key => $value) { ?>
						<tr>
							<td><?php echo $key; ?></td>
							<td><?php echo $value ? 'In' : 'Out'; ?></td>
						</tr>
						<?php }?>
					</tbody>
				</table>
			</div>
		</div>
		
		<div class="row">
			<div class="col-4">
				<h2>Add a User</h2>
				<form method="post">
					<div class="form-group">
						<label for="textEntry">Enter Name:</label>
						<input type="text" class = "form-control" id="addUser" name="addUser">
						<button type="submit" name="add" class = "btn btn-dark btn-lg">Add User</button>
					</div>
				</form>
			</div>

			<div class = "col-8">
				<h2>Checked In</h2>
				<table class="table table-striped table-dark">
					<thead>
					<tr>
						<th>Name</th>
					</tr>
					</thead>
					<tbody>
					<?php 
						$data = json_decode($checkedIn, true);
						foreach($data as $name) { ?>
						<tr>
						<td><?php echo $name; ?></td>
						</tr>
					<?php }?>
					</tbody>
				</table>
			</div>
		</div>

		<div class="row">
			<div class = "col-4">
				<h2>Remove a User</h2>
				<form method="post">
					<div class = "form-group">
						<div class="form-row">
							<label for="textEntry">Enter Name:</label>
							<input type="text" class = "form-control" id="removeUser" name="removeUser">
						</div>
						<div class = "form-row">
							<button type="submit" name="remove" class = "btn btn-dark btn-lg">Remove User</button>
						</div>
					</div>
				</form>
			</div>
			
			<div class = "col-8">
				<h2>Checked Out</h2>
				<table class="table table-striped table-dark">
					<thead>
					<tr>
						<th>Name</th>
					</tr>
					</thead>
					<tbody>
					<?php 
						$data = json_decode($checkedOut, true);
						foreach($data as $name) { ?>
						<tr>
						<td><?php echo $name; ?></td>
						</tr>
					<?php }?>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<!-- Include Bootstrap JavaScript -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

</body>
</html>