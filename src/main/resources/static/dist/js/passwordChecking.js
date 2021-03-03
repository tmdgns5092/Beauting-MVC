$(document).ready(function()
    {
        var strPassword = "";
        var charPassword = "";
        var complexity = $("#complexity");
        var minPasswordLength = 8;
        var baseScore = 0, score = 0;

        var num = {};
        num.Excess = 0;
        num.Upper = 0;
        num.Numbers = 0;
        num.Symbols = 0;

        var bonus = {};
        bonus.Excess = 3;
        bonus.Upper = 4;
        bonus.Numbers = 5;
        bonus.Symbols = 5;
        bonus.Combo = 0;
        bonus.FlatLower = 0;
        bonus.FlatNumber = 0;

        outputResult();
        $("#password").bind("keyup", checkVal);

        function checkVal()
        {
            init();

            if (charPassword.length >= minPasswordLength)
            {
                baseScore = 50;
                analyzeString();
                calcComplexity();
            }
            else
            {
                baseScore = 0;
            }

            outputResult();
        }

        function init()
        {
            strPassword= $("#password").val();
            charPassword = strPassword.split("");

            num.Excess = 0;
            num.Upper = 0;
            num.Numbers = 0;
            num.Symbols = 0;
            bonus.Combo = 0;
            bonus.FlatLower = 0;
            bonus.FlatNumber = 0;
            baseScore = 0;
            score =0;
        }

        function analyzeString ()
        {
            for (i=0; i<charPassword.length;i++)
            {
                if (charPassword[i].match(/[A-Z]/g)) {num.Upper++;}
                if (charPassword[i].match(/[0-9]/g)) {num.Numbers++;}
                if (charPassword[i].match(/(.*[!,@,#,$,%,^,&,*,?,_,~])/)) {num.Symbols++;}
            }

            num.Excess = charPassword.length - minPasswordLength;

            if (num.Upper && num.Numbers && num.Symbols)
            {
                bonus.Combo = 25;
            }

            else if ((num.Upper && num.Numbers) || (num.Upper && num.Symbols) || (num.Numbers && num.Symbols))
            {
                bonus.Combo = 15;
            }

            if (strPassword.match(/^[\sa-z]+$/))
            {
                bonus.FlatLower = -15;
            }

            if (strPassword.match(/^[\s0-9]+$/))
            {
                bonus.FlatNumber = -35;
            }
        }

        function calcComplexity()
        {
            score = baseScore + (num.Excess*bonus.Excess) + (num.Upper*bonus.Upper) + (num.Numbers*bonus.Numbers) + (num.Symbols*bonus.Symbols) + bonus.Combo + bonus.FlatLower + bonus.FlatNumber;

        }

        function outputResult()
        {
            if ($("#password").val()== "")
            {
                complexity.html("패스워드로 사용할 값 입력").removeClass("weak strong stronger strongest").addClass("default");
            }
            else if (charPassword.length < minPasswordLength)
            {
                complexity.html(+ minPasswordLength+ " 자 이상 입력하세요!").removeClass("strong stronger strongest").addClass("weak");
                passCheck1 = 0;
            }
            else if (score<50)
            {
                complexity.html("부적합!").removeClass("strong stronger strongest").addClass("weak");
                passCheck1 = 0;
            }
            else if (score>=50 && score<75)
            {
                complexity.html("평균!").removeClass("stronger strongest").addClass("strong");
                passCheck1 = 1;
            }
            else if (score>=75 && score<100)
            {
                complexity.html("좋음!").removeClass("strongest").addClass("stronger");
                passCheck1 = 1;
            }
            else if (score>=100)
            {
                complexity.html("보안성뛰어남!").addClass("strongest");
                passCheck1 = 1;
            }


            $("#details").html("Base Score :<span class=\"value\">" + baseScore  + "</span>"
                + "<br />Length Bonus :<span class=\"value\">" + (num.Excess*bonus.Excess) + " ["+num.Excess+"x"+bonus.Excess+"]</span> "
                + "<br />Upper case bonus :<span class=\"value\">" + (num.Upper*bonus.Upper) + " ["+num.Upper+"x"+bonus.Upper+"]</span> "
                + "<br />Number Bonus :<span class=\"value\"> " + (num.Numbers*bonus.Numbers) + " ["+num.Numbers+"x"+bonus.Numbers+"]</span>"
                + "<br />Symbol Bonus :<span class=\"value\"> " + (num.Symbols*bonus.Symbols) + " ["+num.Symbols+"x"+bonus.Symbols+"]</span>"
                + "<br />Combination Bonus :<span class=\"value\"> " + bonus.Combo + "</span>"
                + "<br />Lower case only penalty :<span class=\"value\"> " + bonus.FlatLower + "</span>"
                + "<br />Numbers only penalty :<span class=\"value\"> " + bonus.FlatNumber + "</span>"
                + "<br />Total Score:<span class=\"value\"> " + score  + "</span>" );
        }

    }
);


/* 비밀번호 확인 체크 */
function confirmPassword(obj) {
    var password = $('#password').val();
    var passwordCheck = $(obj).val();

    if(passwordCheck == password && $('#password').val() != "") {$('#passwordCheckText').html("비밀번호가 일치합니다."); passCheck2=1;}                                       //비밀번호가 일치할 때
    else if(passwordCheck != password && $('#password').val() != "" && passwordCheck != ""){ $('#passwordCheckText').html("비밀번호가 틀립니다.");passCheck2=0;}            //비밀번호가 일치하지 않을 때
    else if(passwordCheck == "") {$('#passwordCheckText').html("");passCheck2=0;}                                                                                           //비밀번호 확인을 입력하지 않았을 때
}