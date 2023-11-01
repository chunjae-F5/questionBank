$(document).ready(function () {
    let formIsValid = true;

    $('#floatingInput').on('input', validateUserId);
    $('#floatingPassword').on('input', validatePassword);
    $('#floatingPasswordCheck').on('input', validatePasswordCheck);
    $('#floatingInputEmail').on('input', validateEmail);
    $('#floatingInputName').on('input', validateUserName);
    $('#inputGroupFile01').on('input', validateFileExtention);


    $(".form").submit(function (e) {
        formIsValid = true;

        formIsValid &= validateUserId();
        formIsValid &= validatePassword();
        formIsValid &= validatePasswordCheck();
        formIsValid &= validateEmail();
        formIsValid &= validateUserName();
        formIsValid &= validateFileExtention();

        if (!formIsValid) {
            alert("회원가입 양식을 알맞게 입력해주세요.")
            e.preventDefault();
        }
    });
});

function validateUserId() {
    const userId = $('#floatingInput').val();
    const pattern = /^[a-zA-Z0-9_\-]{5,20}$/;

    $.ajax({
        url: '/check-id-duplication',
        type: 'POST',
        data: {
            'userId': userId
        },
        success: function (data) {
            if (data.isDuplicated) {
                $("#idDuplicationMessage").text("* 이미 사용중인 아이디입니다.").css("color", "red");
                return false;
            } else {
                $("#idDuplicationMessage").text("사용 가능한 아이디입니다!").css("color", "green");
                return true;
            }
        },
        error: function () {
            $("#idDuplicationMessage").text("* 서버 오류가 발생했습니다. 다시 시도해주세요.").css("color", "red");
            return false;
        }
    });

    if (!pattern.test(userId)) {
        $('#userIdError').text("아이디는 5~20자의 알파벳, 숫자, _, -만 허용됩니다.");
        return false;
    } else if ($("#idDuplicationMessage").css("color") === "red") {
        return false;
    }

    $('#userIdError').text("");
    return true;
}

function validatePassword() {
    const password = $('#floatingPassword').val();
    const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,16}$/;

    if (!pattern.test(password)) {
        $('#passwordError').text("* 비밀번호는 8~16자이며, 대문자, 소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.");
        return false;
    }

    $('#passwordError').text("");
    return true;
}

function validatePasswordCheck() {
    const password = $('#floatingPassword').val();
    const passwordCheck = $('#floatingPasswordCheck').val();

    if (password !== passwordCheck) {
        $('#passwordCheckError').text("* 비밀번호가 일치하지 않습니다.");
        return false;
    }

    $('#passwordCheckError').text("");
    return true;
}

function validateEmail() {
    const email = $('#floatingInputEmail').val();
    const pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

    if (!pattern.test(email)) {
        $('#emailError').text("* 유효한 이메일 형식이 아닙니다.");
        return false;
    }

    $('#emailError').text("");
    return true;
}

function validateUserName() {
    const userName = $('#floatingInputName').val();

    if (userName.length < 2 || userName.length > 50) {
        $('#userNameError').text("* 이름은 2~50자 내외로 입력해주세요.");
        return false;
    }

    $('#userNameError').text("");
    return true;
}

function validateFileExtention() {
    const fileInput = $('#inputGroupFile01');
    const fileName = fileInput.val();

    const ext = fileName.slice(((fileName.lastIndexOf(".") - 1) >>> 0) + 2);
    const allowedExtensions = ['jpg', 'jpeg', 'png'];

    if (!allowedExtensions.includes(ext.toLowerCase())) {
        $('#validateFileError').text('유효하지 않은 파일 확장자입니다. JPG, JPEG, PNG 확장자만 허용됩니다.');
        return false;
    }

    $('#validateFileError').text("");
    return true;

}