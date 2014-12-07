<?php
echo "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"
\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">
<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">
	<head>
			<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />
					<title>MergeBalls</title>
						</head>
							<body>";


function printTable($dif, $lev) {
	$table = "Level_" . $dif . "_" . $lev;	
	$result = mysql_query("SELECT * FROM `$table`  ORDER BY Score LIMIT 10");
	$numRows = mysql_num_rows($result);
	
	if ($numRows > 0) {
		echo "<table border='1'>";
		echo "<tr><th colspan=\"3\">Level $lev</th></tr>";
		echo "<tr><th>Place</th><th>Name</th><th>Score</th></tr>";
		$num = 1;
		while($row = mysql_fetch_array($result)) {
  			echo "<tr><td>$num</td>";
			echo "<td>" . $row['Name'] . "</td>";
			echo "<td>" . $row['Score'] . "</td></tr>";
			$num++;
  		}
		echo "</table>";
	}
}

function printDif($dif, $tables) {
	if ($tables >= 1) {
		for ($i = 1; $i < $tables+1; $i++) {
	    	printTable($dif, $i);
			echo "<h5> </h5>";
		}
	}
}

echo "<h2>MergeBalls<br />Online Hi-Scores</h2>";
echo "<h5> </h5>";
echo "<p><a href=\"#easy\">Easy</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#medium\">Medium</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"#hard\">Hard</a><br /></p>";

$con = mysql_connect("localhost","xxxxxx","xxxxxxxx");
if (!$con) {
  die('Could not connect: ' . mysql_error());
}

mysql_select_db("mergeballshiscore", $con);

echo "<h3><a name=\"easy\">Easy</a></h3>";
$tables = mysql_query("SHOW TABLES LIKE '%Level_0%';");
$res = mysql_num_rows($tables);
printDif(0, $res);

echo "<h3><a name=\"medium\">Medium</a></h3>";
$tables = mysql_query("SHOW TABLES LIKE '%Level_1%';");
$res = mysql_num_rows($tables);
printDif(1, $res);

echo "<h3><a name=\"hard\">Hard</a></h3>";
$tables = mysql_query("SHOW TABLES LIKE '%Level_2%';");
$res = mysql_num_rows($tables);
printDif(2, $res);

echo "<h5> </h5>";
echo "</body>
</html>";
mysql_close($con);
?>
