# 몰입캠프 1주차 - 3개의 탭을 가진 안드로이드 앱

문석훈 : 연락처, To-do

윤예슬 : 갤러리, Diary

<br/><br/>

# 첫 번째 탭 : 연락처

<img src="https://user-images.githubusercontent.com/78538108/148012979-d568fc6f-8f7c-4e89-88ca-69c09ecc84f4.png" width="200" height="400"/>  <img src="https://user-images.githubusercontent.com/78538108/148014266-7f61dccc-0f67-4820-a282-a8fcd6a9e2d4.gif" width="200" height = "400"/>  <img src="https://user-images.githubusercontent.com/78538108/148020115-1b56bca9-0692-4e87-b9b1-0da35e287b2d.gif" width="200" height="400"/>

<img src="https://user-images.githubusercontent.com/78538108/148026193-ea0b5d4d-4376-4476-847d-a631052c04e6.gif" width="200" height="400"/>  <img src="https://user-images.githubusercontent.com/78538108/148023471-4eed85c2-2939-4268-9c61-cbc16b8f7599.gif" width="200" height="400"/>

연락처 정보 추가, 수정, 삭제 및 검색 가능

<br/><br/>

# 두 번째 탭 : 갤러리

<img src="https://user-images.githubusercontent.com/78538108/148050640-19862f59-8dad-4f0c-9b1b-d5a19856bcae.gif" width="200" height="400"/>  <img src="https://user-images.githubusercontent.com/78538108/148030844-d8fd6a36-05d9-4fc4-a55f-bba036bbb04d.gif" width="200" height="400"/>  <img src="https://user-images.githubusercontent.com/78538108/148030785-2498ccfd-baf5-4ff7-ab46-f57f9c33e985.gif" width="200" height="400"/>

이미지 추가 : 갤러리에서 이미지 하나를 가져와 추가.

레이아웃 변경 : 초기 레이아웃의 columnCount는 3이다. columnCount가 3인 경우 1로, 1인 경우 3으로 변경. GridLayout 사용.

이미지 상세 페이지: 이미지 확대, 축소, 삭제, 페이지 돌아가기 기능.

<img src="https://user-images.githubusercontent.com/78538108/148050584-53f18751-3c62-4729-90ac-6c088f5dfd36.gif" width="200" height="400"/>  <img src="https://user-images.githubusercontent.com/78538108/148050597-74972745-a80b-4a4c-ae9c-39a4e3b3f32b.gif" width="200" height="400"/>

이미지 삭제: 삭제 여부를 되묻고 삭제.

## 이미지 해상도 문제
이미지를 불러올 때 원래 해상도를 그대로 불러오는 경우 시간이 너무 오래 걸리는 문제가 생긴다.
이미지 크기를 레이아웃에 맞춰 가져오도록 하였다.
또한 이미지 개수가 많아질 경우 갤러리 탭이 로딩되는 시간이 오래 걸리므로
이를 Thread로 만들어 이미지가 로딩될 때마다 바로 띄워지도록 하였다.
Thread로 만드는 경우 Thread가 종료되기 이전에 탭을 이동하거나 같은 탭을 다시 누르는 경우 앱이 다운되는 문제가 생기므로
onDestroy()에서 Thread에 interrupt() 호출하고 wait() 하도록 했다.

<br/><br/>

# 세 번째 탭 : 캘린더

## To-do list

앱의 내부저장소에 todo.json에 JSONObject를 아래와 같은 구조로 만들어 저장하였다. 
```
{date: JSONArray}
```
각 날짜를 String으로 하여 키로 설정한 후 각 키에 값으로 JSONArray을 할당하였다. 각 JSONArray는 아래와 같은 구조의 JSONObject들의 배열이다. 
```
{"check": Boolean,
"content": String}
```
<img src="https://user-images.githubusercontent.com/78538108/148038869-e77dab99-9bb4-4102-bf6d-74a2e3e9753f.gif" width = "200" height=""/>   <img src="https://user-images.githubusercontent.com/78538108/148038889-cb6d8a4b-0312-46fd-b841-9fca2291fa33.gif" width = "200" height=""/>  <img src="https://user-images.githubusercontent.com/78538108/148038881-b38b45a3-ff3f-46e4-9dd6-bd19524ce0ef.gif" width = "200" height=""/>

To-do list 추가, 수정, 체크 기능.


## Diary

앱 내부 저장소에 diary.json파일을 만들어 일기 정보 저장. JSONObject에 아래 예시와 같이 각 날짜를 key, 일기 내용을 value로 저장. 
```
{"2022.01.03": "오늘은 코딩을 했다",
"2022.01.04": "오늘도 코딩을 했다"} 
```
### Diary 작성
<img src="https://user-images.githubusercontent.com/78538108/148047692-83ed2306-0519-45a4-b481-57d34d822811.gif" width="200" height="400"/>  <img src="https://user-images.githubusercontent.com/78538108/148047699-3b57d253-a206-4b7c-b45e-afdc56bdd6fa.gif" width="200" height="400"/>  <img src="https://user-images.githubusercontent.com/78538108/148050794-b8f64b19-4962-4896-9cb7-1bd0c8dc1cd1.gif" width="200" height="400"/>

일기 추가, 편집 기능. 완료 버튼을 누르면 일기 정보를 로컬에 저장.

