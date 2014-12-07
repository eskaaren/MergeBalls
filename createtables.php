<?php

$con = mysql_connect("localhost","xxxxx","xxxxxxxx");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
mysql_select_db("mergeballshiscore", $con); 

for ($i = 2; $i < 21; $i++) {
	$table = "Level_0_" . $i;
	$result = mysql_query("create table `$table` like Level_0_1;");
}


for ($i = 1; $i < 21; $i++) {
	$table = "Level_1_" . $i;
	$result = mysql_query("create table `$table` like Level_0_1;");
}


for ($i = 1; $i < 21; $i++) {
	$table = "Level_2_" . $i;
	$result = mysql_query("create table `$table` like Level_0_1;");
}

mysql_close($con);
?>
