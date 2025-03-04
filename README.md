# 공유 화이트 보드 프로그램
## 개요
**목표**
- CM 라이브러리를 활용하여 사용자끼리의 화이트 보드 공유 프로그램 제작
  
**일정**
- 2024년 3월 14일 - 2024년 6월 21일

**구성인원**
- 본인 포함 2명

## 사용 기술 및 개발 환경
- Language : Java
- Library : CM
- Tool : github

## 주요 기능
클라이언트-서버 구조 기본 기능
- 클라이언트(화이트보드)의 서버 접속 (또는 로그인)
- 클라이언트 종료시 서버는 해당 클라이언트 접속 해제 처리
- 서버는 클라이언트의 접속/해제 정보를 다른 클라이언트들에게 통보
- 같은 클라이언트 및 여러 클라이언트들의 반복적 서버 접속/해제 테스트

공유 화이트보드 기본 기능
- 원, 사각형, 선, 텍스트 추가
- 도형 수정 (선색, 선굵기, 색채우기)
- 각 클라이언트는 다른 클라이언트의 그려진 객체 정보 공유

공유 화이트보드 기능 확장
- 클라이언트 2개 이상 지원
- 그림 객체 저장 및 로드 기능
- Late-comer 클라이언트 지원
- 늦게 세션에 참여하는 클라이언트는 지금까지의 화이트보드 그림 정보 공유됨
- 동시성 지원
- 여러 클라이언트가 동시에 같은 객체를 수정하는 문제 해결
- 그리기 과정 공유
- 도형의 결과물만이 아닌 그려지는 과정도 공유
