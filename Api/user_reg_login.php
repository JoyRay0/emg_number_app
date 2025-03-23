<?php

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");
header("Content-Security-Policy: default-src 'self'; script-src 'self' https://rksoftwares.xyz; object-src 'none';");
header("X-Content-Type-Options: nosniff");
header("Strict-Transport-Security: max-age=31536000; includeSubDomains; preload");
header("X-XSS-Protection: 1; mode=block");
header("X-Frame-Options: DENY");
header("Referrer-Policy: no-referrer");
header("Cache-Control: no-store, no-cache, must-revalidate, proxy-revalidate");
header("Expect-CT: max-age=86400, enforce, report-uri='https://rksoftwares.xyz/report'");

require 'db.php';

$method = $_SERVER['REQUEST_METHOD'];
$res = $_GET['res'] ?? '';

$data = json_decode(file_get_contents("php://input"), true);

if ($method == 'POST' || $method == 'PUT' || $method == 'DELETE') {

    $check_data = !empty($data['name']) || !empty($data['email']) || 
                  !empty($data['password']) || !empty($data['date_of_birth']) || 
                  !empty($data['new_password']) || !empty($data['access_token']) ||
                  !empty($data['user_id']);

    //Registration & OAuth 
    if(!empty($data) && ($check_data)){

                $user_id = $data['user_id'];
                $name = $data['name'];
                $email = $data['email'];
                $date_of_birth = $data['date_of_birth'];
                $password = $data['password'];
                $new_password = $data['new_password'];
                $access_token = $data['access_token'];

                //sanitizing user data
                $sanitize_name = trim(strip_tags(filter_var($name, FILTER_SANITIZE_FULL_SPECIAL_CHARS)));
                $sanitize_email = filter_var($email, FILTER_SANITIZE_EMAIL);
                $sanitize_date_of_birth = trim($date_of_birth);
                $sanitize_password = password_hash($password, PASSWORD_DEFAULT);
                $sanitize_new_password = password_hash($new_password, PASSWORD_DEFAULT);
            
        //checking res method
        switch($res){

            //post method for registration
            case('post_reg') :
                
                $sql = "INSERT INTO user_info(name, email, date_of_birth, password) VALUES (?, ?, ?, ?)";
                $result = $database_connect->prepare($sql);
                $result->bind_param('ssss', $sanitize_name, $sanitize_email, $sanitize_date_of_birth, $sanitize_password);

                if($result->execute()){

                    $user_id = $database_connect->insert_id;

                    echo json_encode([

                        'status' => 'Successfull',
                        'message' => 'Account created successfully',
                        'user_id' => $user_id,
                        'name' => $sanitize_name,

                    ]);

                }else{

                    echo json_encode([

                        'status' => 'Failed',
                        'message' => 'Connection timed out'

                    ]);

                }
                $result->close(); 

                break;

            //put method for reset password
            case('put_user_password'):

                $sql = "UPDATE user_info SET password = ? WHERE id = ?";
                $result = $database_connect->prepare($sql);
                $result->bind_param("si", $sanitize_new_password, $user_id);

                if($result->execute()){

                    echo json_encode([

                        'status' => 'Successfull',
                        'message' => 'Password changed successfully'

                    ]);
                    
                }else{

                    echo json_encode([

                    'status' => 'Failed',
                    'message' => 'Failed to update password'
                    ]);
                    exit();
                }

                $result->close();
                break;

            //Log in user
            case ('post_userLogin'):

                $sql = "SELECT user_id, name, password FROM user_info WHERE email = ?";
                $result = $database_connect->prepare($sql);
                $result->bind_param("s", $sanitize_email);
                $result->execute();
                $user_info =  $result->get_result();

                if($user_info->num_rows > 0){

                    $user_data = $user_info->fetch_assoc();

                    if(password_verify($password, $user_data['password'])){

                        echo json_encode([

                            'status' => 'Successfull',
                            'message' => 'Login successfully',
                            'user_id' => $user_data['user_id'],
                            'name' => $user_data['name']

                        ]);

                    }else{

                        echo json_encode([

                            'status' => 'Failed',
                            'message' => 'Invalid password'

                        ]);
                        exit();
                    }
                    
                }else{

                    echo json_encode([

                        'status' => 'Failed',
                        'message' => 'Wrong email and password'

                    ]);
                    exit();
                }
                $result->close();
                break;

            //google login and register
            case ('post_google_OAuth'):

                if(!$access_token){

                    echo json_encode([

                        'status' => 'Failed',
                        'message' => 'Google login failed'

                    ]);
                    exit();

                }else{

                    getGoogleAuthToken($access_token);

                    $user_email = $user_info['email'];
                    $user_name = $user_info['name'];

                    //google login
                    $sql = "SELECT user_id, name FROM user_info WHERE email = ?";
                    $result = $database_connect->prepare($sql);
                    $result->bind_param("s", $user_email);
                    $result->execute();
                    $user_result = $result->get_result();

                    if($user_result->num_rows > 0){

                        $user_data = $user_result->fetch_assoc();
                    
                        echo json_encode([

                            'status' => 'Successfull',
                            'message' => 'Login successfull',
                            'user_id' => $user_data['user_id'],
                            'name' => $user_name

                        ]);

                    }else{

                        //google registration
                        $sql = "INSERT INTO user_info(email, name) VALUES (?, ?)";
                        $result = $database_connect->prepare($sql);
                        $result->bind_param("ss", $user_email, $user_name);

                        if($result->execute()){

                            $user_id = $database_connect->insert_id;

                            echo json_encode([

                                'status' => 'Successful',
                                'message' => 'Account created successfully',
                                'user_id' => $user_id,
                                'name' => $user_name

                            ]);

                        }

                    }
                    $result->close();
                    
                }
                break;

            //deleting user information
            case ('delete_user'):

                $sql = "DELETE FROM user_info WHERE id = ?";
                $result = $database_connect->prepare($sql);
                $result->bind_param("i", $user_id);

                if($result->execute()){

                    echo json_encode([

                        'status' => 'Successfull',
                        'message' => 'Account deleted successfully'

                    ]);

                }else{

                    echo json_encode([

                        'status' => 'Failed',
                        'message' => 'Failed to delete your account'

                    ]);
                    exit();

                }
                $result->close();

                break;

            //default value 
            default:
                echo json_encode([

                    'status' => 'Error',
                    'message' => 'Wrong res method'

                ]);
                
        }//switch statement

    }else{

        echo json_encode([

            'status' => 'Empty',
            'message' => 'Some field are missing'

        ]);
        exit();
    }
    
}else{

    echo json_encode([

        'status' => 'Error',
        'message' => 'Invalid Request'

    ]);
    exit();
}
function getGoogleAuthToken($access_Token){

    $user_infoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=". $access_Token;

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $user_infoUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER,true);

    $response = curl_exec($ch);
    curl_close($ch);
    $user_info = json_decode($response, true);

    if(empty($user_info)){

        echo json_encode([

            'status' => 'Failed',
            'message' => 'Access denied'

        ]);
        exit();

    }

    if(curl_error($ch)){

        echo json_encode([

            'status' => 'Error',
            'message' => 'curl error'

        ]);
        exit();
    }

}

?>