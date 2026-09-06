# GachaMacro

Android 1차 프로토타입.

## 현재 포함
- 베이스 결과 이미지 선택
- 베이스 영역을 10개 슬롯으로 분할하는 구조
- 캐릭터 이미지 여러 개 등록
- 슬롯별 템플릿 유사도 검사
- 캐릭터별 검출 개수 표시
- AccessibilityService 기반 터치
- AccessibilityService 기반 스와이프
- GitHub Actions APK 빌드

## 다음 개발 단계
1. MediaProjection 실시간 화면 캡처 연결
2. 실제 게임 화면에서 베이스 영역을 드래그하여 지정
3. 10개 슬롯을 베이스 영역 기준으로 자동 계산
4. 캐릭터 등록 이미지에서 얼굴/상반신 ROI를 자동 추출
5. 밝기/스케일 변화에 강한 템플릿 매칭
6. 캐릭터별 임계값 설정
7. A >= 2, A >= 2 AND B >= 1 등의 조건식
8. 뽑기 버튼 터치 → 결과 대기 → 분석 → 조건 충족 시 정지
9. 매크로 편집기(터치/스와이프/대기/반복)
10. 실행 중 오버레이와 긴급정지 버튼

## 빌드
GitHub Actions에서 `Build Android APK` workflow를 실행하면
`GachaMacro-debug-apk` Artifact로 APK를 받을 수 있습니다.

이 프로젝트는 프로토타입이며 게임별 UI/약관을 확인한 뒤 사용하세요.
