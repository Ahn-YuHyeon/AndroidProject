package com.project.john.mygoogle.component;

import java.util.Locale;
public class Constant {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "membership.db";
    public static final String TAG = "MYGOOGLE";
    public static final String OK = "확인";
    public static final String CANCEL = "취소";
    public static final String OKGOOGLE = "오케이구글";
    public static final String START_STUDY = "학습시작";
    public static final String END_STUDY = "학습종료";
    public static final Locale LANGUAGE = Locale.KOREA;
    public static final int PITCH = 10;
    public static final int RATE = 8;
    public static final String GOOGLE_PACKAGE = "com.google.android.googlequicksearchbox";

    public static final String REGISTER_CMD_MSG = "기본 명령어 등록가 등록되었습니다.";
    public static final String INPUT_TEXT_MSG = "추가할 명령어를 입력하세요.";
    public static final String START_STUDY_MSG = "학습모드가 시작되었습니다.";
    public static final String END_STUDY_MSG = "학습모드가 종료되었습니다.";
    public static final String FINISHED_APP_MSG = "앱을 종료하시려면 한번 더 눌러주세요.";
    public static final String SUCCESSED_INIT_MSG = "초기화에 성공하였습니다.";
    public static final String FAILED_INIT_MSG = "초기화에 실패하였습니다";
    public static final String SUCCESSED_ADD_MSG = "명령어 추가에 성공하였습니다.";
    public static final String SHAKE_ERROR_MSG = "3초 이내에 흔들림이 감지되었습니다.";
    public static final String FAILED_SUPPORT_lANG_MSG = "언어 지원에 실패하였습니다.";
    public static final String FAILED_DELETE_MSG = "명령어 삭제에 실패하였습니다.";
    public static final String FAILED_ADD_MSG = "명령어 추가에 실패하였습니다.";
    public static final String FAILED_QUERY_MSG = "명령어 읽기에 실패하였습니다.";
    public static final String FAILED_CLICK_MSG = "잠시 후 클릭해주세요.";
    public static final String EMPTY_TEXT_MSG = "명령어가 입력되지 않았습니다.";

    public static final String CMDS[] = new String[] {
            "엄마에게 전화해", "오늘날씨", "다음카카오 주가", "엄마에게 사랑해 문자보내", "엄마에게 사랑해 메일보내", "오전7시 알람설정",
            "인계동 한신포차 구글맵", "최신영화", "근처맛집", "올레 멤버십 실행"};
}
