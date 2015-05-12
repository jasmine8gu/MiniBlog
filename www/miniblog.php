<?php
	require_once('360_safe3.php');

	$fp = fopen("log.txt", "w+");
	fputs($fp, "Begin\n"); 

	function sendEmail($to, $subject, $content) {
		$from = "jasmine8_gu@yahoo.ca";
		ini_set('sendmail_from', $from);
		
		$headers  = 'MIME-Version: 1.0' . "\r\n";
		$headers .= 'Content-type: text/html; charset=utf-8' . "\r\n";
		$headers .= 'From: ' . $from . "\r\n";
	
		$message = '<html><body>';
		$message .= $content;
		$message .= '</body></html>';
	
		if (@mail($to, $subject, $message, $headers)) {
			return true;
		}
		else {
			return false;
		}	
	}
	
	$myconn=mysql_connect("localhost", "miniblog", "Tianwawa1+");
	mysql_select_db("miniblog");
	mysql_query("set names 'utf8'");
	$curdate = date("Y-m-d H:i:s");
	
	$strFunction = $_REQUEST['strFunction'];
	fputs($fp, $strFunction . "\n"); 
	
	if ($strFunction == "UserList") {
		$startIdx = $_REQUEST['startIdx'];
		$count = $_REQUEST['count'];
		$strSql = "select user.id, nickname, user.pictureid, picture.url " . 
					"from user left join picture " .
					"on user.pictureid=picture.id " . 
					"order by updatedatetime desc limit " . $startIdx . ", " . $count;
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		while($row = mysql_fetch_array($result)) {
			$output[]=$row;
		}
		print(json_encode($output));
	}
	else if ($strFunction == "Profile") {
		$userid = $_REQUEST['userid'];
		
		$strSql = "SELECT user.id, nickname, age, user.pictureid, picture.url as url, gender, breed, email, location " . 
					"FROM user left join picture on user.pictureid=picture.id " . 
					"where user.id=" . $userid;
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		while($row = mysql_fetch_array($result)) {
			$output[]=$row;
		}

		print(json_encode($output));
	}
	else if ($strFunction == "SignIn") {
		$email = $_REQUEST['email'];
		$password = $_REQUEST['password'];
		$strSql = "select id, nickname from user where email='" . $email . "' and password='" . $password . "'";
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		while($row = mysql_fetch_array($result)) {
			$output[]=$row;
		}
		print(json_encode($output));
	}	
	else if ($strFunction == "SignUpCheck") {
		$email = $_REQUEST['email'];
		$strSql = "select userid from user where email='" . $email . "'";
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		while($row = mysql_fetch_array($result)) {
			$output[]=$row;
		}
		print(json_encode($output));
	}
	else if ($strFunction == "SignUp") {
		$nickname = urldecode($_REQUEST['nickname']);
		$email = $_REQUEST['email'];
		$password = $_REQUEST['password'];
		$strSql = "insert into user(nickname, email, password) values('" . $nickname . "', '" . $email . "', '" . $password . "');";
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		if ($result) {
			$userid = mysql_insert_id();
			$output[]=$userid;
		}
		else {
			$output[]=-1;
		}
		print(json_encode($output));
	}
	else if ($strFunction == "ProfileUpdate") {
		$userid = $_REQUEST['userid'];
		$nickname = urldecode($_REQUEST['nickname']);
		$age = $_REQUEST['age'];
		$gender = urldecode($_REQUEST['gender']);
		$breed = urldecode($_REQUEST['breed']);
		
		$strSql = "update user set nickname='" . $nickname . "', age=" . $age . ", gender='" . $gender . "', breed='" . $breed . "' where id=" . $userid;
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		if ($result) {
			$output[]=1;
		}
		else {
			$output[]=0;
		}
		print(json_encode($output));
	}
	else if ($strFunction == "ForgetPassword") {
		$email = $_REQUEST['email'];
		
		$strSql = "SELECT * from user where email='" . $email . "'";
		$result = mysql_query($strSql, $myconn);
		fputs($fp, $strSql . "\n");
		if ($row = mysql_fetch_array($result)) {
			$title = "Retrieve password for MiniBlog";
			$content = "Hello " . $row['nickname'] . "</br></br>";
			$content .= "Your password for MiniBlog is : " . $row['password'] . "</br></br>";
			$content .= "Admin</br>";
			if (sendEmail($email, $title, $content)) {
				fputs($fp, "sendemail true\n");
				$output[]=1;
			}
			else {
				fputs($fp, "sendemail false\n");
				$output[]=0;
			}
		}
		else {
			fputs($fp, "sendemail false\n");
			$output[]=0;
		}
		print(json_encode($output));
	}
	else if ($strFunction == "BlogList") {
		$userid = $_REQUEST['userid'];

		$strSql = "select * " . 
					"from blog " .
					"where userid=" . $userid;
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		while($row = mysql_fetch_array($result)) {
			$output[]=$row;
		}
		
		print(json_encode($output));
	}	
	else if ($strFunction == "BlogDetail") {
		$blogid = $_REQUEST['blogid'];

		$strSql = "select blog.*, picture.url " . 
					"from blog left join picture " .
					"on blog.pictureid=picture.id " . 
					"where blog.id=" . $blogid;
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		while($row = mysql_fetch_array($result)) {
			$output[]=$row;
		}
		
		print(json_encode($output));
	}	
	else if ($strFunction == "BlogNew") {
		$userid = $_REQUEST['userid'];
		$title = $_REQUEST['title'];
		$content = $_REQUEST['content'];
		
		$strSql = "insert into blog(userid, title, content, updatedatetime) values(" . $userid . ", '" . $title . "', '" . $content . "', '" . $curdate . "');";
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		
		if ($result) {
			$blogid = mysql_insert_id();
			$output[]=$blogid;
		}
		else {
			$output[]=-1;
		}
		
		print(json_encode($output));
	}
	else if ($strFunction == "BlogUpdate") {
		$blogid = $_REQUEST['blogid'];
		$title = $_REQUEST['title'];
		$content = $_REQUEST['content'];
		
		$strSql = "update blog set title='" . $title . "', content='" . $content. "', updatedatetime='" . $curdate . "' where id=" . $blogid;
		fputs($fp, $strSql . "\n");
		$result = mysql_query($strSql, $myconn);
		
		if ($result) {
			$output[]=1;
		}
		else {
			$output[]=0;
		}
		
		print(json_encode($output));
	}
	
	fputs($fp, "End\n");
	
	mysql_close();  
	fclose($fp);
?>
