# madCampWeek1
몰입캠프 1주차 - 3개의 탭을 가진 안드로이드 앱

# 첫 번째 탭 : 연락처
연락처 정보를 추가, 수정 및 삭제 가능
## 연락처 정보 DB 구조
앱의 내부 저장소에 contact.json 이라는 파일을 만들어 이 안에 JSONObject로 각 연락처의 정보를 저장하였다. JSONObject의 구조는 다음과 같다. 
{"contact": JSONArray, "count": int}
"contact"키에는 JSONArray가 삽으로 존재하며 이 JSONArray 안에는 각 연락처를 JSONObject로 하여 배열의 형태 저장해 두었다. 각 연락처는 다음과 같은 구조로 저장해두었다. 
{"id": int, "name": String, "number": String, "email": String, "web": String, "job": String, "sns": String, "address": String}
"id"는 각 연락처의 고유한 값이며 이를 이용하여 이후 각 연락처를 수정, 삭제하는 기능을 구현하였다.
"count"는 연락처를 추가할 때 각 추가되는 연락처에 고유한 "id"를 부여하기 위해 추가한 값이며, 각 연락처가 추가 될 때 마다 "count"의 값을 "id"로 주고 "count"에는 기존의 값에 1을 더한 값을 새로 저장하여 바꿔주었다.

## 연락처 추가
![Screenshot_1641272461](https://user-images.githubusercontent.com/78538108/148011666-079300e5-b8af-4d9c-8c3a-18a77810b8ca.png)
이름, 전화번호, 이메일, 웹사이트, 직업, SNS, 주소 정보를 EditText로 입력 받은 후 툴바에 있는 완료 아이콘을 클릭하면 먼저 contact.json을 읽어 String을 만든후 이를 이용하여 JSONObject obj를 만든다. 그 뒤 "contact"에 있는 JSONArray에 새로 연락처를 추가하기 위해 새로 JSONObject sObject를 만들었다. contact.json 안에 들어있는 "count"를 새로 만든 sObject의 "id"로 put하였고 "count"의 값을 +1하여 다시 obj에 저장하였다. 각 EditText안에 있는 정보들을 이용하여 아래의 형태로 JSONObject를 만들었다.
{"id": int, "name": String, "number": String, "email": String, "web": String, "job": String, "sns": String, "address": String}
의 형태로 JSONObject를 만들어 앱의 내부 저장소에 저장되어 있는 contact.json에 있는 JSONObject에 "contact"에 

# 두 번째 탭 : 갤러리
## 추가 버튼
갤러리에서 이미지 하나를 가져와 추가할 수 있다.
## 레이아웃 변경 버튼
초기 레이아웃의 columnCount는 3이다. columnCount가 3인 경우 1로, 1인 경우 3으로 변경한다.
## 이미지 해상도 문제
이미지를 불러올 때 원래 해상도를 그대로 불러오는 경우 시간이 너무 오래 걸리는 문제가 생긴다.
이미지 크기를 레이아웃에 맞춰 가져오도록 하였다.
또한 이미지 개수가 많아질 경우 갤러리 탭이 로딩되는 시간이 오래 걸리므로
이를 Thread로 만들어 이미지가 로딩될 때마다 바로 띄워지도록 하였다.
Thread로 만드는 경우 Thread가 종료되기 이전에 탭을 이동하거나 같은 탭을 다시 누르는 경우 앱이 다운되는 문제가 생기므로
onDestroy()에서 Thread에 interrupt() 호출하고 wait() 하도록 했다.

# 세 번째 탭 : 캘린더
캘린더에 To-do 리스트, diary를 구현하였다.
