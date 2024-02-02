<?php
global $connection;
include 'config/db_config.php';

$data = file_get_contents("php://input");

$request = json_decode($data);

$response = array();

$isValidRequest = false;


if (isset($request->{'action'})) {
    if ($request->{'action'} == 'REGISTER_USER') {
        $isValidRequest = true;

        $userName = mysqli_real_escape_string($connection, $request->{'userName'});
        $password = mysqli_real_escape_string($connection, $request->{'password'});
        $email = mysqli_real_escape_string($connection, $request->{'email'});

        $query = "INSERT INTO blogUser (userName, userEmail,userPassword) VALUES ('$userName', '$email','$password')";
        $result = mysqli_query($connection, $query);

        if ($result) {
            $response['userId'] = mysqli_insert_id($connection);
            $response['status'] = true;
            $response['responseCode'] = 0; // success
            $response['message'] = "User Registered Successfully";
        } else {
            $response['status'] = false;
            $response['responseCode'] = 101; // user registration fail
            $response['message'] = "User Registration Failed: " . mysqli_error($connection);
        }
    } elseif ($request->{'action'} == 'LOGIN_USER') {
        $isValidRequest = true;
        $email = $request->{'email'};
        $password = $request->{'password'};

        $query = "SELECT * FROM blogUser WHERE userEmail = '$email' AND userPassword = '$password'";
        $result = mysqli_query($connection, $query);

        if ($result && mysqli_num_rows($result) > 0) {
            $row = mysqli_fetch_assoc($result);
            $response['status'] = true;
            $response['responseCode'] = 0; // success
            $response['message'] = "Login Successful";
            $response['userId'] = $row['userId'];
        } else {
            // User not found
            $response['status'] = false;
            $response['responseCode'] = 106; // login failed
            $response['message'] = "User not found";
        }
    } elseif ($request->{'action'} == 'CREATE_BLOG') {
        $isValidRequest = true;
        $blogName = mysqli_real_escape_string($connection, $request->{'blogName'});
        $blogDescription = mysqli_real_escape_string($connection, $request->{'blogDescription'});

        $query = "INSERT INTO Blog (blogName, blogDescription) VALUES ('$blogName', '$blogDescription')";
        $result = mysqli_query($connection, $query);

        if ($result) {
            $response['blogId'] = mysqli_insert_id($connection);
            $response['status'] = true;
            $response['responseCode'] = 0; // success
            $response['message'] = "Blog Created Successfully";
        } else {
            $response['status'] = false;
            $response['responseCode'] = 201; // blog creation fail
            $response['message'] = "Blog Creation Failed";
        }
    } elseif ($request->{'action'} == 'GET_ALL_BLOGS') {
        $isValidRequest = true;

        $query = "SELECT * FROM Blog";
        $result = mysqli_query($connection, $query);

        if ($result && mysqli_num_rows($result) > 0) {
            $blogs = [];
            while ($row = mysqli_fetch_assoc($result)) {
                $blogs[] = $row;
            }
            $response['status'] = true;
            $response['responseCode'] = 0; // success
            $response['message'] = "Blogs Retrieved Successfully";
            $response['blogs'] = $blogs;
        } else {
            $response['status'] = false;
            $response['responseCode'] = 202; // no blogs found
            $response['message'] = "No Blogs Found";
        }
    } elseif ($request->{'action'} == 'UPDATE_BLOG') {
        $isValidRequest = true;
        $blogId = mysqli_real_escape_string($connection, $request->{'blogId'});
        $blogName = mysqli_real_escape_string($connection, $request->{'blogName'});
        $blogDescription = mysqli_real_escape_string($connection, $request->{'blogDescription'});

        $query = "UPDATE Blog SET blogName='$blogName', blogDescription='$blogDescription' WHERE blogId='$blogId'";
        $result = mysqli_query($connection, $query);

        if ($result) {
            $response['status'] = true;
            $response['responseCode'] = 0; // success
            $response['message'] = "Blog Updated Successfully";
        } else {
            $response['status'] = false;
            $response['responseCode'] = 203; // blog update fail
            $response['message'] = "Blog Update Failed";
        }
    } elseif ($request->{'action'} == 'DELETE_BLOG') {
        $isValidRequest = true;
        $blogId = mysqli_real_escape_string($connection, $request->{'blogId'});

        $query = "DELETE FROM Blog WHERE blogId='$blogId'";
        $result = mysqli_query($connection, $query);

        if ($result) {
            $response['status'] = true;
            $response['responseCode'] = 0; // success
            $response['message'] = "Blog Deleted Successfully";
        } else {
            $response['status'] = false;
            $response['responseCode'] = 204; // blog deletion fail
            $response['message'] = "Blog Deletion Failed";
        }
    }
}


echo json_encode($response);
?>