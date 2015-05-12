<?php
	require_once('360_safe3.php');
	
	$fp = fopen("log.txt", "w+");
	fputs($fp, "Begin\n"); 

	$myconn=mysql_connect("localhost", "miniblog", "Tianwawa1+");
	mysql_select_db("miniblog");
	mysql_query("set names 'utf8'");
	$curdate = date("Y-m-d H:i:s");		 
	
	$strFunction = $_REQUEST['strFunction'];
	fputs($fp, $strFunction . "\n"); 
	
	if ($strFunction == "UserPicture") {
		$userid = $_REQUEST['userid'];
		$filename = $userid . '.jpg';
	}
	else if ($strFunction == "BlogPicture") {
		$blogid = $_REQUEST['blogid'];
		$userid = $_REQUEST['userid'];
		$filename = $userid . '_' . $blogid . '.jpg';
	}
	
	$base = $_REQUEST['image'];
	$binary = base64_decode($base);
	header('Content-Type: bitmap; charset=utf-8');
	$file = fopen($filename, 'wb');
	fwrite($file, $binary);
	fclose($file);

	$resize = 1;
	if ($resize == '1') {
		$dstHeight = 600;
		
		$imgSize = getimagesize($filename);
		$imgInfo = getimagesize($filename, $imgInfo);
		$imgWidth = $imgSize[0];
		$imgHeight = $imgSize[1];
		$imgType = $imgInfo[2];

		if ($imgHeight > $dstHeight) {
			chmod($filename, 0644);
			switch ($imgType) {
				case 1:
					$imgOri =imagecreatefromgif($filename);
					break;
				case 2:
					$imgOri =imagecreatefromjpeg($filename);
					break;
				case 3:
					$imgOri =imagecreatefrompng($filename);
					break;
				case 6:
					$imgOri =imagecreatefromwbmp($filename);
					break;
				default:
					die("<font color='red'>File type not supported!</a>");
					exit;
			}
			
			$dstWidth = $dstHeight * $imgWidth / $imgHeight;
			$imgNew = imagecreatetruecolor($dstWidth, $dstHeight);
			$white = imagecolorallocate($imgNew, 255, 255, 255);
			imagefill($imgNew, 0, 0, $white);

			imagecopyresized($imgNew, $imgOri, 0, 0, 0, 0, $dstWidth, $dstHeight, $imgWidth, $imgHeight);
			
			switch ($imgType) {
				case 1:
					imagejpeg($imgNew, $filename);
					break;
				case 2:
					imagejpeg($imgNew, $filename);
					break;
				case 3:
					imagepng($imgNew, $filename);
					break;
				case 6:
					imagewbmp($imgNew, $filename);
					break;
			}
			imagedestroy($imgNew);
			imagedestroy($imgOri);
		}
	}
	
	$strSql = "insert into picture(url, uploaddatetime) values('" . $filename . "', '" . $curdate . "');";
	fputs($fp, $strFunction . "\n"); 	
	$result = mysql_query($strSql, $myconn);
	$pictureid = mysql_insert_id();
	
	if ($strFunction == "UserPicture") {
		$strSql = "update user set pictureid=" . $pictureid . ", updatedatetime='" . $curdate . "' where id=" . $userid;
		fputs($fp, $strFunction . "\n"); 
		$result1 = mysql_query($strSql, $myconn);
	}
	else if ($strFunction == "BlogPicture") {
		$strSql = "update blog set pictureid=" . $pictureid . ", updatedatetime='" . $curdate . "' where id=" . $blogid;
		fputs($fp, $strFunction . "\n"); 
		$result1 = mysql_query($strSql, $myconn);
	}
	
	if ($result && $result1) {
		$output[]=1;
	}
	else {
		$output[]=0;
	}
	print(json_encode($output));
	
	mysql_close();  
	fputs($fp, "End\n");	
	fclose($fp);	
?>
