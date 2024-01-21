//메뉴바 드롭다운
$('.menu_bar > ul > li > span').mouseenter(function () {
  $('.header-bottom').css("height", "245px");
  $('.menu_bar > ul > li > .snb').css("height", "170px");
  $('.menu-outer .header-bottom').css("borderBottom", "1px solid #999");
});

//메뉴바 드롭다운 해제
$('.header-bottom').mouseleave(function () {
  $('.header-bottom').css("height", "60px");
  $('.menu_bar > ul > li > .snb').css("height", "0px");
  $('.menu-outer .header-bottom').css("borderBottom", "none");
});

//메뉴바 상단 고정
var topMenu = document.querySelector(".header-bottom");
var headerBottomInner = document.querySelector(".header-bottom .header-bottom-inner");
var stickyLogo = document.querySelector(".sticky-logo");
var footerOuter = document.querySelector(".footer-outer");
var aside_wrap = document.querySelector(".aside_wrap");

function menuBarFixed() {
  if (scrollY > 96) {
    topMenu.classList.add("sticky");
    // headerBottomInner.style.backgroundImage = "linear-gradient(to right, rgb(45, 45, 45), rgb(111, 111, 111) 60%, rgb(200, 200, 200))";
    headerBottomInner.style.backgroundColor = "rgb(51, 51, 51)";
    stickyLogo.style.display = "inline-block";
    footerOuter.style.backgroundColor = "rgb(51, 51, 51)";
    aside_wrap.style.backgroundColor = "rgb(51, 51, 51)";
    $('footer .footer-inner .footer-left').css("color", "#777");
    $('footer .footer-inner .footer-right').css("color", "#777");
    $('.menu_bar > ul > li > span > a').css("color", "whitesmoke")

  } else {
    topMenu.classList.remove("sticky");
    // headerBottomInner.style.backgroundImage = "linear-gradient(to right, rgb(111, 111, 111), rgb(166, 166, 166) 50%, rgb(222, 222, 222))";
    headerBottomInner.style.backgroundColor = "rgb(83, 83, 83)";
    stickyLogo.style.display = "none";
    footerOuter.style.backgroundColor = "rgb(83, 83, 83)";
    aside_wrap.style.backgroundColor = "rgb(83, 83, 83)";
    $('footer .footer-inner .footer-left').css("color", "black");
    $('footer .footer-inner .footer-right').css("color", "black");
    $('.menu_bar > ul > li > span > a').css("color", "black")
  };
}
document.addEventListener('scroll', menuBarFixed);

// 사이드 메뉴바 고정
var sideMenuBar = document.querySelector("aside");
function sideMenuBarFixed() {
  if (scrollY > 96) {
    sideMenuBar.style.position = "fixed";
    sideMenuBar.style.top = "10px";
    // document.querySelector(".aside-wrap").style.backgroundColor = "#333";
    sideMenuBar.style.color = "#dddddd";
    // document.querySelector("aside .aside-wrap .sideMenuBarBtn").style.backgroundColor = "#333";
    // $('aside .aside-wrap .sideMenuBar .mngBar > ul > li > a:not(.on)').css("color", "rgb(200, 200, 200)");

  } else {
    sideMenuBar.style.position = "absolute";
    sideMenuBar.style.top = "101px";
    // document.querySelector(".aside-wrap").style.backgroundColor = "#777";
    sideMenuBar.style.color = "black";
    // document.querySelector("aside .aside-wrap .sideMenuBarBtn").style.backgroundColor = "#777";
    // $('aside .aside-wrap .sideMenuBar .mngBar > ul > li > a:not(.on)').css("color", "black");
  }
}
document.addEventListener('scroll', sideMenuBarFixed);

// 사이드 메뉴바 탭 열고 닫기
function sideMenuBarOpenClose(e) {
  if (!e.target.classList.contains("smt")) return;

  if (e.target.nextElementSibling.style.height == "0px" || e.target.nextElementSibling.style.height == 0) {
    if (e.target.classList.contains("mngBar3")) {
      e.target.nextElementSibling.style.height = "80px";
      e.target.style.borderBottom = "2px solid black";
      e.target.nextElementSibling.style.border = "2px solid black";
    } else {
      e.target.nextElementSibling.style.height = "55px";
      e.target.style.borderBottom = "2px solid black";
      e.target.nextElementSibling.style.border = "2px solid black";
    }
  } else {
    e.target.nextElementSibling.style.height = 0;
  }
};
$('aside .aside-wrap .sideMenuBar .mngBar > .smt').click(sideMenuBarOpenClose);


//새로운 사이드 메뉴바
// 사이드 메뉴바 탭 열기
$(document).ready(function () {
  $('.aside_menu_bar_list_prod').hide();
  $('.aside_menu_bar_list').hide();

  $('.slide #slide_down_img').click(function () {

    if ($(this).closest("ul").hasClass("sideMenuBar4")) {
      $(this).closest('ul').next().slideDown();
      $(this).closest("div").css("margin-bottom", "140px");
      $(this).closest("div").css("transition", "all 2s ease 0s");
      $(this).css("display", "none");
      $(this).next('li').css("display", "block");

    } else if($(this).closest("ul").hasClass("sideMenuBar2")){
      $(this).closest('ul').next().slideDown();
      $(this).closest("div").css("margin-bottom", "60px");
      $(this).closest("div").css("transition", "all 1s ease 0s");
      $(this).css("display", "none");
      $(this).next('li').css("display", "block");
    
    } else if($(this).closest("ul").hasClass("sideMenuBar1")){
      $(this).closest('ul').next().slideDown();
      $(this).closest("div").css("margin-bottom", "30px");
      $(this).closest("div").css("transition", "all 1s ease 0s");
      $(this).css("display", "none");
      $(this).next('li').css("display", "block");
    }

    // 사이드 메뉴바 탭 닫기
    $('#slide_up_img ').click(function () {
      $(this).closest('ul').next().slideUp();
      $(this).closest("div").css("margin-bottom", "0px");
      $(this).closest("div").css("-o-animation", "fadeout 4s");
      $(this).css("display", "none");
      $(this).prev('li').css("display", "block");
    })
  });
})


// fragment 부분 window.innerWidth에 따른 UI 설정
function resizeFragment2() {
  if ($('aside .aside-wrap .sideMenuBarBtn').hasClass("active")) {
    console.log(this.window.innerWidth);
    if (this.window.innerWidth < 1585) {
      // $('.section-outer1').css({ marginLeft: "250px", padding: "60px 50px 30px 30px" });
      $('.section-outer1').css({ padding: "60px 50px 30px 280px !important" });
      $('.section-outer2').css("marginTop", "10px");
      // $('.fragment').css("width", window.innerWidth - 250 + "px");

      $('.main-header').css({ marginLeft: "80px", padding: "10px 0 0 0" });
      $('.main-header-contents').css("width", window.innerWidth - 250 + "px");

      $('.footer-outer').css({ marginLeft: "200px" });
      $('footer').css("width", "auto");

      $('.menuTable-outer').css({ marginLeft: "235px" });
      $('.menuTable-header').css("width", "auto");

    } else {
      $('.section-outer1').css({ marginLeft: "0px", padding: "0px" });
      $('.section-outer2').css("marginTop", "70px");
      $('.fragment').css("width", "1080px");

      $('.main-header').css({ marginLeft: "0px" });
      $('.main-header-contents').css("width", "1080px");

      $('.footer-outer').css({ marginLeft: "0px" });
      $('footer').css("width", "1080px");

      $('.menuTable-outer').css({ marginLeft: "0px" });
      $('.menuTable-header').css("width", "1080px");
    }

  } else {

    if (this.window.innerWidth < 1200) {
      $('.section-outer1').css({ width: "100%", marginLeft: "60px", padding: "0px" });
      $('.section-outer2').css("marginTop", "70px");
      $('.fragment').css("width", "auto");

      $('.main-header').css({ marginLeft: "60px" });
      $('.main-header-contents').css("width", "auto");

      $('.footer-outer').css({ marginLeft: "35px" });
      $('footer').css("width", "auto");

      $('.menuTable-outer').css({ marginLeft: "60px" });
      $('.menuTable-header').css("width", "auto");

    } else {
      $('.section-outer1').css({ width: "100%", marginLeft: "0px", padding: "0px" });
      $('.section-outer2').css("marginTop", "70px");
      $('.fragment').css("width", "1080px");

      $('.main-header').css({ marginLeft: "0px" });
      $('.main-header-contents').css("width", "1080px");

      $('.footer-outer').css({ marginLeft: "0px" });
      $('footer').css("width", "1080px");

      $('.menuTable-outer').css({ marginLeft: "0px" });
      $('.menuTable-header').css("width", "1080px");
    }
  }
}

// fragment 부분 window.innerWidth에 따른 UI 설정
function resizeFragment() {
  if (this.window.innerWidth < 1585) {
    $('.section-outer1').css({ marginLeft: "140px", padding: "0px 50px 30px 30px" });
    $('.section-outer2').css("marginTop", "0px");
    $('.fragment').css("width", window.innerWidth - 250 + "px");

    $('.main-header').css({ marginLeft: "80px", padding: "10px 0 0 0" });
    $('.main-header-contents').css("width", window.innerWidth - 250 + "px");

    $('.footer-outer').css({ marginLeft: "200px" });
    $('footer').css("width", "auto");

    $('.menuTable-outer').css({ marginLeft: "235px" });
    $('.menuTable-header').css("width", "auto");

  } else {
    $('.section-outer1').css({ marginLeft: "0px", padding: "0px" });
    $('.section-outer2').css("marginTop", "70px");
    $('.fragment').css("width", "1080px");
    $('.fragment').css("paddingTop", "10px");

    $('.main-header').css({ marginLeft: "0px" });
    $('.main-header-contents').css("width", "1080px");

    $('.footer-outer').css({ marginLeft: "0px" });
    $('footer').css("width", "1080px");

    $('.menuTable-outer').css({ marginLeft: "0px" });
    $('.menuTable-header').css("width", "1080px");
    }
  }
window.addEventListener('resize', resizeFragment);

// 사이드 메뉴바 전체 열고 닫기
$('aside .aside-wrap .sideMenuBarBtn').click(function () {

  if ($('aside .aside-wrap .sideMenuBarBtn').hasClass("active")) {
    $('aside .aside-wrap').css("width", "26px");
    $('aside .aside-wrap .sideMenuBarBtn').removeClass("active");

  } else {
    $('aside .aside-wrap').css("width", "200px");
    $('aside .aside-wrap .sideMenuBarBtn').addClass("active");
    // $('.section-outer1').css("marginLeft", "160px");
  }
  resizeFragment();
});

// 푸터 고정
var fragmentHeight = document.querySelector(".fragment").scrollHeight;
function footerFixed() {

  if (fragmentHeight + 272 < window.innerHeight) {
    $(".footer-outer").css({ position: "fixed", bottom: "0px" });
    $("body").css("overflow", "hidden");

  } else {
    $(".footer-outer").css({ position: "static", })
  }
}
window.addEventListener('resize', footerFixed);

//화면 렌더링 완료 시, view size 측정 후 적용
$(document).ready(resizeFragment());
$(document).ready(footerFixed());
// $(document).on('change', document.querySelector(".fragment").scrollHeight, function(e) {
//   footerFixed();
// });
