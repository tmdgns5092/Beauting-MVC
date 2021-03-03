<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="http://unpkg.com/swiper/css/swiper.css">
    <link rel="stylesheet" href="http://unpkg.com/swiper/css/swiper.min.css">
    <%@include file="../include/include-header.jspf" %>
    <%@include file="../include/css/include-stylesheet.jspf" %>
</head>

<style>
    .popup {position: fixed;left: 1%;bottom: 2.5%;width: 340px;height: 132px;-webkit-transition: -webkit-transform 0.4s;transition: transform 0.4s;-webkit-transform: translateY(150%);transform: translateY(150%);z-index: 2;border-radius: 4px;box-shadow: 1px 2px 4px 0 rgba(158,178,195,.24);background: #343a40;color: #fff;box-sizing: border-box;padding: 5px 5px 5px 5px;text-align: center;}
    .popup.active {-webkit-transform: translateY(0);transform: translateY(0);}
    /*.popup form {*/
    /*float: left;*/
    /*padding-left: 16px;*/
    /*}*/
    .popup .text input[type=checkbox] {margin-right: 6px;vertical-align: middle;}
    .popup .text span {display: inline-block;line-height: 30px;vertical-align: middle;font-family: "돋움", Dotum, sans-serif;font-size: 12px;}
    .popup .top a {text-decoration: none;text-shadow: none;background: none;color: #fff;width: auto;height: auto;top: 10px;right: 10px;font-weight: normal;}
    /*.popup img {*/
    /*padding-left: 10px;*/
    /*padding-right: 10px;*/
    /*}*/
    @media (min-width: 1200px) {
        .container {width: 1060px;}
    }

</style>

<%--<a href="" class="open">팝업열기</a>--%>
<body class="index_body">

<!-- 구글 태그매니저 -->
<noscript><iframe src="http://www.googletagmanager.com/ns.html?id=GTM-5XJB2VN" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<!-- 구글 태그매니저 끝 -->

<%@include file="../include/include-nav.jspf" %>
<div class="landing_section01">
    <h1>우리샵 예약, 매출 관리<br>이제는 무료로 사용하세요.</h1>
    <div class="landing_section01_btn">
        <button type="button" onclick="location.href = 'login'">지금 시작하기</button>
        <%--<button type="button" class="index-top-btn" onclick="location.href = 'joinTerms'">무료 가입하기</button>--%>
        <button class="index-top-btn" onclick="TrialVersionLogin()">체험하기</button>
        <script>
            function TrialVersionLogin(){
                $.ajax({
                    url : "/loginCheckAjax",
                    type: "post",
                    data : {
                        "id" : "beauting",
                        "password" : "beauting123",
                        "autologin" : false
                    },
                    dataType : "json",
                    success : function(data){
                        if(data.code == 200) {
                            location.href = '/Reservation/calendar';
                            return false;
                        } else if(data.code == 900){
                            alert("아이디나 비밀번호가 존재하지 않습니다\n다시 시도해 주십시오");
                            $('#inputID').focus();
                            return false;
                        } else if(data.code == 901){
                            alert("서버의 상태가 좋지 않습니다. 잠시 후 다시 시도해 주십시오.");
                            $('#inputID').focus();
                            return false;
                        }
                    },
                    error : function(request,error){
                        //console.log("data : "+data);
                        alert("잠시 후 다시 시도해 주십시오");
                        location.href = document.URL;
                    }
                });
            }
        </script>
    </div>

    <div class="container index_swiper01">
        <!-- Slider main container -->
        <div class="swiper-container01">
            <!-- Additional required wrapper -->
            <div class="swiper-wrapper">
                <!-- Slides -->
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/11/22/06/05/girl-1848454__480.jpg)">
                        <div></div>
                        <h3>헤어</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2017/03/24/13/25/roses-2171193__480.jpg)">
                        <div></div>
                        <h3>네일</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/08/20/02/50/acne-1606765__480.jpg)">
                        <div></div>
                        <h3>피부</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2014/10/13/11/46/massage-486700__480.jpg)">
                        <div></div>
                        <h3>마사지</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/03/27/20/51/bed-1284238__480.jpg)">
                        <div></div>
                        <h3>애견미용</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2019/04/28/23/14/eye-4164712__480.jpg)">
                        <div></div>
                        <h3>속눈썹</h3>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/11/22/06/05/girl-1848454__480.jpg)">
                        <div></div>
                        <h3>헤어</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2017/03/24/13/25/roses-2171193__480.jpg)">
                        <div></div>
                        <h3>네일</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/08/20/02/50/acne-1606765__480.jpg)">
                        <div></div>
                        <h3>피부</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2014/10/13/11/46/massage-486700__480.jpg)">
                        <div></div>
                        <h3>마사지</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/03/27/20/51/bed-1284238__480.jpg)">
                        <div></div>
                        <h3>애견미용</h3>
                    </a>
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2019/04/28/23/14/eye-4164712__480.jpg)">
                        <div></div>
                        <h3>속눈썹</h3>
                    </a>
                </div>
            </div>
        </div><!-- swiper-container -->
        <div class="swiper-container01-sub">
            <!-- Additional required wrapper -->
            <div class="swiper-wrapper">
                <!-- Slides -->
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/11/22/06/05/girl-1848454__480.jpg)">
                        <div></div>
                        <h3>헤어</h3>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2017/03/24/13/25/roses-2171193__480.jpg)">
                        <div></div>
                        <h3>네일</h3>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/08/20/02/50/acne-1606765__480.jpg)">
                        <div></div>
                        <h3>피부</h3>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2014/10/13/11/46/massage-486700__480.jpg)">
                        <div></div>
                        <h3>마사지</h3>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2016/03/27/20/51/bed-1284238__480.jpg)">
                        <div></div>
                        <h3>애견미용</h3>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;" style="background: url(http://cdn.pixabay.com/photo/2019/04/28/23/14/eye-4164712__480.jpg)">
                        <div></div>
                        <h3>속눈</h3>
                    </a>
                </div>
            </div>
        </div><!-- swiper-container -->
    </div>
</div>

<div class="popup">
    <div class="text">
        <div style="margin-top: 8px;">
            <h3 style="    font-weight: 700;
    font-size: 17px;;">고객정보 이전 문의</h3>
            <p style="color: rgba(255,255,255,0.6);">안전하고 빠르게 해결해 드리겠습니다.</p>
            <button type="button" style="display: inline-block;position: absolute;right: 20px;border-radius: 2px;
    padding: 10px 20px;
    color: #fff;
    bottom: 15px;
    width: 300px;
    background: #51abf3;
    font-weight: 700;" onclick="location.href='http://pf.kakao.com/_iNMxdxb';">문의하기</button>
        </div>
        <%--<form>--%>
        <%--<label><input type="checkbox" name="todayClose"><span>오늘 하루 그만보기</span></label>--%>
        <%--</form>--%>
        <div class="top"><a href="" class="close">&times;</a></div>
    </div>
    <%--<img src="images/slide_img1.png">--%>
</div>

<div class="container banner-container">
    <!-- Slider main container -->
    <div class="swiper-container02">
        <!-- Additional required wrapper -->
        <div class="swiper-wrapper">
            <!-- Slides -->
            <div class="swiper-slide" onclick="location.href = 'joinTerms'">
                <img src="/static/test-img/index_banner04.png" alt="">
                <img src="/static/test-img/index_banner04.png" alt="">
            </div>
            <div class="swiper-slide" onclick="location.href = 'joinTerms'">
                <img src="/static/test-img/index_banner02.png" alt="">
                <img src="/static/test-img/index_banner02.png" alt="">
            </div>
            <div class="swiper-slide" onclick="location.href = 'joinTerms'">
                <img src="/static/test-img/index_banner03.png" alt="">
                <img src="/static/test-img/index_banner03.png" alt="">
            </div>
        </div>

        <%--<!-- If we need navigation buttons -->--%>
        <%--<div class="swiper-button-prev"></div>--%>
        <%--<div class="swiper-button-next"></div>--%>
    </div>
</div>

<div class="container">
    <div class="">
        <a href="javascript:void(0);" class="index_event_section">
            <img src="http://www.myrealtrip.com/webpack/04805fd6ae2e4dd1317124b6cf1c89fb.svg" alt="">
            <div>
                <p><span>지금 가입하면 </span><strong>10,000 문자 포인트</strong><span> 증정!</span></p>
                <p><span>평생 무료, 네일샵 전용 앞치마 증정 포함</span><span>주의 : 혜택 문의시에 한</span></p>
            </div>
            <button data-toggle="modal" data-target="#questionModal">이벤트 혜택 문의</button>
        </a>
    </div>
    <div class="index_features_section">
        <h4>뷰팅 고객관 특장점 🌟<span><a href="javascript:;" onclick="location.href = 'introduce'">서비스 더보기 <i class="fas fa-angle-right"></i></a></span></h4>
        <div>
            <div>
                <div>
                    <img src="/static/test-img/index-img01.png" alt="">
                    <a href="">개인별 목표 / 매출 관리</a>
                </div>
                <p>목표지향적인 매장 만들기 뷰팅과 함께하면 매장 PC에서도 모바일에서도 매장 목표, 달성 매출, 일목표매출, 소진차감을 실시간 확인 할수 있어요.</p>
                <div class="index-line"></div>
            </div>
            <div>
                <div>
                    <img src="/static/test-img/index-img02.png" alt="">
                    <a href="">월별 스케줄 캘린더</a>
                </div>
                <p>근무일, 휴무일 확인, 모바일로 내 예약 스케줄 확인, 스케줄 만으로 예약판 연동, 스케줄 짜기 쉽고 빠르게 관리 스케줄표 캘린더에 표기만으로 예약판에 자동 설정되니 너무 편하네~</p>
                <div class="index-line"></div>
            </div>
            <div>
                <div>
                    <img src="/static/test-img/index-img03.png" alt="">
                    <a href="">직원간 채팅창 / 공지사항</a>
                </div>
                <p>오늘 밥 뭐 먹을래?, 뷰팅 고객님 오늘 결제하셔야해요~, 오늘 원장님 안오신데요. 뷰팅쌤 오늘 청소좀 잘해주세요. 매장 예약 관리 철저히 해주세요.</p>
            </div>
        </div>
    </div>
    <div class="index_field">
        <h1>뷰팅 지원 분야</h1>
        <ul>
            <li>헤어</li>
            <li>네일</li>
            <li>바버</li>
            <li>반영구</li>
            <li>피부</li>
            <li>마사지</li>
            <li>왁싱</li>
            <li>속눈썹</li>
            <li>애견미용</li>
            <li>타투</li>
            <li>메이크업</li>
            <li>예약제 샵</li>
        </ul>
    </div>

    <div class="index_swiper03">
        <h1>뷰팅 BEST 고객님 리뷰 👧</h1>
        <!-- Slider main container -->
        <div class="swiper-container03">
            <!-- Additional required wrapper -->
            <div class="swiper-wrapper">
                <!-- Slides -->
                <div class="swiper-slide">
                    <a href="javascript:;">
                        <div class="sw_img">
                        <span>
                            <img src="http://cdn.pixabay.com/photo/2019/12/22/14/19/car-4712664_1280.jpg" alt="">
                            <img src="" alt="">
                        </span>
                            <div>
                                <p>네일</p>
                                <p>부산 서면 가농네일</p>
                            </div>
                            <h4>너무 좋아요 감사합니다.</h4>
                            <p>이런 프로그램이 공짜라니 진짜 네일에 딱 맞는 내용이라서 너무 공감 갑니다. 잘 쓸께요~</p>
                        </div>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;">
                        <div class="sw_img">
                        <span>
                            <img src="http://cdn.pixabay.com/photo/2019/12/22/14/19/car-4712664_1280.jpg" alt="">
                            <img src="" alt="">
                        </span>
                            <div>
                                <p>네일</p>
                                <p>부산 센텀 GK네일</p>
                            </div>
                            <h4>다른 프로그램도 다 봤는데 이만한게 없더라고요</h4>
                            <p>처음엔 이런 저런 프로그램을 다 찾아봤어요. 근데 비용문제를 떠나서 쓸만한 프로그램이 없더라고요.
                                그러다 콜라보살롱을 깔게 됐는데 일정 입력부터 너무 편한 거예요. 이거다 싶었어요. 그래서 지금은 콜라보살롱 하나로만 운영하고 있어요.</p>
                        </div>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;">
                        <div class="sw_img">
                        <span>
                            <img src="http://cdn.pixabay.com/photo/2019/12/22/14/19/car-4712664_1280.jpg" alt="">
                            <img src="" alt="">
                        </span>
                            <div>
                                <p>네일</p>
                                <p>울산 북구 올리네</p>
                            </div>
                            <h4>공짜로 사용할 수 있어 좋아요^^</h4>
                            <p>매장을 최근에 오픈했는데 고객님들 예약을 어떻게 관리하는지 검색해 보다가 발견했습니다. 다른 사이트들과 다르게 공짜로 사용할 수 있어 좋았습니다. 저처럼 유료 프로그램이 부담스러운 사람에게 사용하기 좋은 것 같아요.^^~</p>
                        </div>
                    </a>
                </div>
                <div class="swiper-slide">
                    <a href="javascript:;">
                        <div class="sw_img">
                        <span>
                            <img src="http://cdn.pixabay.com/photo/2019/12/22/14/19/car-4712664_1280.jpg" alt="">
                            <img src="" alt="">
                        </span>
                            <div>
                                <p>네일</p>
                                <p>부산 부산대 GK네일</p>
                            </div>
                            <h4>다른 프로그램도 다 봤는데 이만한게 없더라고요</h4>
                            <p>처음엔 이런 저런 프로그램을 다 찾아봤어요. 근데 비용문제를 떠나서 쓸만한 프로그램이 없더라고요.
                                그러다 콜라보살롱을 깔게 됐는데 일정 입력부터 너무 편한 거예요. 이거다 싶었어요. 그래서 지금은 콜라보살롱 하나로만 운영하고 있어요.</p>
                        </div>
                    </a>
                </div>
            </div>
        </div><!-- swiper-container -->
    </div>

    <div class="index_good_section">
        <h4>뷰팅의 전체기능</h4>
        <div style="margin-bottom: 16px">
            <div>
                <div>
                    <i class="fas fa-address-book"></i>
                    <a href="">뷰팅 고객 관리 <i class="fas fa-angle-right"></i></a>
                </div>
                <p>고객 등록 / 객단가 관리 / 고객 메모 / 시술 메모 / 마케팅 문자 보내기 / 나의 메시지 저장 / 노쇼 고객 관리 / 고객별 시술 내역</p>
            </div>
            <div>
                <div>
                    <i class="far fa-comment-dots"></i>
                    <a href="">뷰팅 비밀 Live 채팅 <i class="fas fa-angle-right"></i></a>
                </div>
                <p>전달사항 체크 / 비밀 채팅 / 간단한 메모</p>
            </div>
            <div>
                <div>
                    <i class="far fa-calendar-alt"></i>
                    <a href="">뷰팅 간편 예약 관리 <i class="fas fa-angle-right"></i></a>
                </div>
                <p>네이버 예약 / 카카오 예약 / 발신자표시기 예약 / 예약 알림</p>
            </div>
        </div>
        <div>
            <div>
                <div>
                    <i class="fas fa-cash-register"></i>
                    <a href="">뷰팅 매출 관리 <i class="fas fa-angle-right"></i></a>
                </div>
                <p>시술 계산기 / 자동 정산 / 일 매출 알림 / 월별 매출 알림 / 고객별 결제내역 / 추가매출 입력 / 동료와 매출 공유 / 동료의 매출 대신 등록</p>
            </div>
            <div>
                <div>
                    <i class="far fa-chart-bar"></i>
                    <a href="">뷰팅 통계 관리 <i class="fas fa-angle-right"></i></a>
                </div>
                <p>월별 목표 매출 / 현금, 신용카드 매출 / 시술별 매출 기여도 / 고객 매출 랭킹</p>
            </div>
            <div>
                <div>
                    <i class="fas fa-cog"></i>
                    <a href="">뷰팅 설정 <i class="fas fa-angle-right"></i></a>
                </div>
                <p>활용 팁 / 공지사항 / 문의하기 / 디자이너 관리 / 캘린더 시간대 설정 / 문자 충전 / 시술, 티켓, 선불권 관리 / 오픈, 마감 시간대 설정<img src="" alt=""></p>
            </div>
        </div>
    </div>

    <script>
        $(document).ready(function(){
            var swiper = new Swiper('.swiper-container01', {
                direction: 'horizontal',
                pagination: '.swiper-pagination',
                paginationClickable: true,
                slidesPerView: 1, // 한번에 보이는 슬라이드 갯수
                autoplay: false,
                width: 1070,
                loop : false,
                grabCursor: false,
                centeredSlides: 1,  //활성 슬라이드가 왼쪽 또는 가운데 배치 (1,0,true,false)
                // slidesPerView:"auto"  // 한번에 보이는 슬라이드 갯수. 반응형 및 다양한 디자인은 auto가 나음.
            });
        });
    </script>
    <script>
        $(document).ready(function(){
            var swiper = new Swiper('.swiper-container01-sub', {
                direction: 'horizontal',
                pagination: '.swiper-pagination',
                paginationClickable: true,
                slidesPerView: 1, // 한번에 보이는 슬라이드 갯수
                autoplay: false,
                width: 125,
                loop : false,
                grabCursor: false,
                centeredSlides: true,  //활성 슬라이드가 왼쪽 또는 가운데 배치 (1,0,true,false)
                // slidesPerView:"auto"  // 한번에 보이는 슬라이드 갯수. 반응형 및 다양한 디자인은 auto가 나음.
            });
        });
    </script>

    <script>
        $(document).ready(function(){
            var swiper = new Swiper('.swiper-container02', {
                direction: 'horizontal',
                pagination: {
                    el: '.swiper-pagination',
                    type: 'bullets',
                },
                paginationClickable: true,
                slidesPerView: 1, // 한번에 보이는 슬라이드 갯수
                autoplay: {
                    delay: 3400,
                    disableOnInteraction: false,
                },
                loop : true,
                grabCursor: false,
                centeredSlides: 0,  //활성 슬라이드가 왼쪽 또는 가운데 배치 (1,0,true,false)
                navigation: {
                    nextEl: '.swiper-button-next',
                    prevEl: '.swiper-button-prev',
                },
                centeredSlides: true,  //활성 슬라이드가 왼쪽 또는 가운데 배치 (1,0,true,false)
                // slidesPerView:"auto"  // 한번에 보이는 슬라이드 갯수. 반응형 및 다양한 디자인은 auto가 나음.
            });
        });
    </script>
    <%--푸터영역 include--%>
    <%@include file="../include/include-menu-footer.jspf" %>

    <script>
        function test(){
            $.ajax({
                url : "BaseService/checkIsMember",
                type: "get",
                data : {
                    "corpNum" : "test",
                    "linkId" : "test"
                },
                dataType : "json",
                success : function(data){
                    //console.log(data);
                    if(data.code == 200){
                        alert("code : " + data.Response.code + "\nstatus : " + data.Response.message);
                    }else{
                        alert("code : " +data.code);
                    }
                },
                error : function(){
                    alert("에러가 발생했습니다.");
                }
            });
        }
    </script>


    <script>
        $(function(){
            $(".close").click(function(e){
                e.preventDefault();
                if($("input[name=todayClose]").is(":checked")){ // 체크박스가 선택되어 있다면,
                    setCookie("close", "yes", 1); // close cookie를 yes 값으로 저장합니다.
                    // cookie 이름 : close
                    // cookie 값 : yes
                }
                $(".popup").removeClass("active");
            });

            if(GetCookie("close") == "yes"){ // 화면 로딩 시에 close cookie 값이 yes이면,
                // $(".popup").hide(); // 팝업을 닫습니다.
            }else{
                // $(".popup").show(); // 아니라면 팝업을 열어줍니다.
                $(".popup").addClass("active");
            }

            function setCookie(name, value, expiredays){
                var days=expiredays;
                if(days){
                    var date=new Date();
                    date.setTime(date.getTime()+(days*24*60*60*1000));
                    var expires="; expires="+date.toGMTString();
                }else{
                    var expires="";
                }
                document.cookie=name+"="+value+expires+"; path=/";
            }
            function GetCookie(name){
                var value=null, search=name+"=";
                if(document.cookie.length > 0){
                    var offset=document.cookie.indexOf(search);
                    if(offset != -1){
                        offset+=search.length;
                        var end=document.cookie.indexOf(";", offset);
                        if(end == -1) end=document.cookie.length;
                        value=unescape(document.cookie.substring(offset, end));
                    }
                } return value;
            }
        });
    </script>

    <%-- AJAX Loading--%>
    <%@include file="../include/utils/ajaxLoading.jspf" %>

    <%--네이버 애널리틱스 스크립트--%>
    <script type="text/javascript" src="//wcs.naver.net/wcslog.js"></script>
    <script type="text/javascript">
        if(!wcs_add) var wcs_add = {};
        wcs_add["wa"] = "34c6ec5c64372e";
        wcs_do();
    </script>
    <%--네이버 애널리틱스 스크립트 end--%>

    <script>
        $(document).ready(function(){
            var swiper = new Swiper('.swiper-container03', {
                direction: 'horizontal',
                pagination: '.swiper-pagination',
                paginationClickable: true,
                slidesPerView: 3, // 한번에 보이는 슬라이드 갯수
                autoplay: {
                    delay: 2400,
                    disableOnInteraction: false,
                },
                width: 990,
                loop : true,
                grabCursor: false,
                centeredSlides: true,  //활성 슬라이드가 왼쪽 또는 가운데 배치 (1,0,true,false)
                // slidesPerView:"auto"  // 한번에 보이는 슬라이드 갯수. 반응형 및 다양한 디자인은 auto가 나음.
            });
        });
    </script>


    <script src="http://unpkg.com/swiper/js/swiper.js"></script>
    <script src="http://unpkg.com/swiper/js/swiper.min.js"></script>

    <!-- 문의 Modal -->
    <div class="point-inquiry-modal modal fade" id="questionModal" tabindex="-1" role="dialog" aria-labelledby="questionModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="questionModalLabel">문의하기</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <ul>
                        <li>
                            <div class="col-md-3">
                                이메일
                            </div>
                            <div class="col-md-9">
                                <input type="email" id="question-mail" placeholder="ex : beauting@naver.com">
                            </div>
                        </li>
                        <li>
                            <div class="col-md-3">
                                이름
                            </div>
                            <div class="col-md-9">
                                <input type="text" id="question-name" maxlength="5" oninput="maxLengthCheck(this)" >
                            </div>
                        </li>
                        <li>
                            <div class="col-md-3">
                                전화번호
                            </div>
                            <div class="col-md-9">
                                <input type="tel" id="question-tel" maxlength="18" oninput="maxLengthCheck(this)" onkeydown="onlyTel(this)" onfocus="OnCheckPhone(this, event)" onkeyup="OnCheckPhone(this, event)" placeholder="전화번호">
                            </div>
                        </li>
                        <%--<li>
                            <div class="col-md-3">
                                매장명
                            </div>
                            <div class="col-md-9">
                                <input type="text">
                            </div>
                        </li>--%>
                        <li>
                            <div class="col-md-3">
                                문의내용
                            </div>
                            <div class="col-md-9">
                                <textarea name="" id="question-textarea" style="height: 160px"></textarea>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" id="questionSend" class="btn btn-primary">전송</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        $('#questionSend').click(function (){
            var question_mail = $('#question-mail').val();
            var question_name = $('#question-name').val();
            var question_tel = $('#question-tel').val();
            var question_type = 'question-radio-2';
            var question_text = $("#question-textarea").val();

            if(!chkEmail(question_mail)){
                alert("답변을 드리기 위해서는 이메일을 입력해 주셔야 합니다.");
                $('#question-mail').focus();
                return false;
            }
            if(uk(question_name) == ""){
                alert("답변을 드리기 위해서는 성함을 입력해 주셔야 합니다.");
                $('#question-name').focus();
                return false;
            }
            if(uk(question_tel) == ""){
                alert("답변을 드리기 위해서는 연락처를 입력해 주셔야 합니다.");
                $('#question-tel').focus();
                return false;
            }
            if(uk(question_text) == ""){
                alert("문의사항을 작성해 주십시오.");
                $('#question-textarea').focus();
                return false;
            }

            question_text = "이메일 : " + question_mail +  "\n이름 : " + question_name + "\n연락처 : "+ question_tel + "\n문의사항 : " + question_text;
            submitAjax("/questionSend", {type : question_type, questionText : question_text});

            alert("문의사항이 발송되었습니다.");
            $('#questionModal').modal("hide");
        });
    </script>

</body>
</html>
