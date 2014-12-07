<?php
$nam = $_POST["name"];
$sco = $_POST["score"];
$lev = $_POST["level"];
$dif = $_POST["difficulty"];
$level = "Level_" . $dif . "_" . $lev;


$con = mysql_connect("localhost","root","steroid1");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
mysql_select_db("mergeballshiscore", $con); 
$result = mysql_query( "INSERT INTO `$level` (Name, Score)
VALUES ( '" . $nam . "', '" . $sco . "' ) " );

mysql_close($con);
//header( 'Location: http://www.giskeskaaren.com/mergeballs/dbconnect.php' ) ;
?>
