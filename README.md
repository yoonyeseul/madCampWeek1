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

ContactItem Class를 만들어 연락처 정보를 파일에서 읽은 후 저장하였다. 

```Java
public class ContactItem {
    String name;
    String number;
    String email;
    String web;
    String job;
    String sns;
    String address;
    int id;
    ...
```

## 연락처 Fragment

<img src="https://user-images.githubusercontent.com/78538108/148012979-d568fc6f-8f7c-4e89-88ca-69c09ecc84f4.png" width="200" height="400"/>

연락처에는 기본적으로 이름만으로 각 연락처를 나타내었으며 recyclerView를 이용하여 연락처 List를 구현하였다. 이름을 클릭하면 그 연락처의 상세 페이지로 넘어가며 이때 intent를 이용하여 recyclerView에 있는 연락처 정보를 contactDetail Activity로 전달하였다.
각 연락처는 가나다 순으로 나열하였다. 그리고 위 검색창을 이용하여 연락처를 검색할 수 있게 하였고 위 툴바에 있는 + 버튼을 누르게 되면 연락처를 추가 할 수 있게 하였다. 

## 연락처 추가
<img src="https://user-images.githubusercontent.com/78538108/148014266-7f61dccc-0f67-4820-a282-a8fcd6a9e2d4.gif" width="200" height = "400"/>


이름, 전화번호, 이메일, 웹사이트, 직업, SNS, 주소 정보를 EditText로 입력 받은 후 툴바에 있는 완료 아이콘을 클릭하면 먼저 contact.json을 읽어 String을 만든후 이를 이용하여 JSONObject obj를 만든다. 그 뒤 "contact"에 있는 JSONArray에 새로 연락처를 추가하기 위해 새로 JSONObject sObject를 만들었다. contact.json 안에 들어있는 "count"를 새로 만든 sObject의 "id"로 put하였고 "count"의 값을 +1하여 다시 obj에 저장하였다. 각 EditText안에 있는 정보들을 이용하여 아래의 형태로 JSONObject를 만들었다.

{"id": int, "name": String, "number": String, "email": String, "web": String, "job": String, "sns": String, "address": String}

ex> {"contact":[{"name":"문석훈", "number":"010-3088-8447", "email":"moonx011@gmail.com", "web":"www.naver.com", "job":"학생","sns":"msh_0113", "address":"대전광역시 유성구 대학로 291 지혜관", "id":2}], "count":2}

```Java
String json = readFile();
        try {
            JSONArray oldJson = jsonParsing(json);

            JSONArray newJson = new JSONArray();
            JSONObject sObject = new JSONObject();
            COUNT = COUNT + 1;
            int pos = -1;
            sObject.put("name", name);
            sObject.put("number", number);
            sObject.put("email", email);
            sObject.put("web", web);
            sObject.put("job", job);
            sObject.put("sns", sns);
            sObject.put("address", address);
            sObject.put("id", COUNT);
            if (oldJson.length() == 0) {
                oldJson.put(sObject);
                return oldJson;
            }
            ...
```

그 뒤 가나다 순서대로 JSONObject를 배열하기 위해서 for 문을 이용하여 obj안에서 sObject가 들어갈 자리를 찾을 후에 그 자리에 sObject를 put 하였다.
다음으로 obj를 새로 contact.json에 write하여 연락처 정보를 update하였다.

```Java
for (int i = 0; i < oldJson.length(); i++) {

    if (oldJson.getJSONObject(i).getString("name").compareTo(sObject.getString("name")) >= 0) {
        pos = i;
        newJson.put(i, sObject);
        break;
     }
     newJson.put(i, oldJson.getJSONObject(i));
}
if (pos == -1) {
    pos = oldJson.length() - 1;
    newJson.put(pos + 1, sObject);
}
else {
    for (int j = pos + 1; j <= oldJson.length(); j++) {
        newJson.put(j, oldJson.getJSONObject(j - 1));
    }
}
```

## 연락처 수정

<img src="https://user-images.githubusercontent.com/78538108/148020115-1b56bca9-0692-4e87-b9b1-0da35e287b2d.gif" width="200" height="400"/>

연락처는 ContactDetail Activity에서 수정 아이콘을 클릭했을 때 시작되는 Activity로 연락처 정보들을 먼저 intent로 ContactDetail Activity에서 intent에 넣어준 후에 ContactEdit Activity에서 정보들을 get하여 각 정보들의 EditText에 초기값으로 setText 하였다. 그 뒤 정보들을 수정하고 완료 아이콘을 클릭하게 되면 각 정보들와 현재 연락처의 "id"로 새로운 객체를 만든뒤 연락처를 추가했을 때와 마찬가지로 JSONArray에 넣어주었다. 이때 기존에 존재하던 수정 이전의 연락처 JSONObject는 현재 "id"를 이용하여 찾은 후 제거해 주고 수정된 연락처 JSONObject를 추가해주었고 연락처 추가와 마찬가지로 가나다 순서로 정렬을 해주기 위해 for문으로 수정된 연락처가 들어갈 위치를 탐색한 후 넣어주었다. 

```Java
for(int i = 0; i < oldJson.length(); i++) {
    if(oldJson.getJSONObject(i).getInt("id") == id) {
        pos = i;
        sObject.put("name", name);
        sObject.put("number", number);
        sObject.put("email", email);
        sObject.put("web", web);
        sObject.put("job", job);
        sObject.put("sns", sns);
        sObject.put("address", address);
        sObject.put("id", id);
        continue;
    }
    t.put(oldJson.getJSONObject(i));
}

for(int i = 0; i < t.length(); i++) {
    if(t.getJSONObject(i).getString("name").compareTo(sObject.getString("name")) >= 0) {
        newpos = i;
        newJson.put(i, sObject);
        break;
    }
    newJson.put(i, t.getJSONObject(i));
}

if (newpos == -1) {
    newpos = t.length() - 1;
    newJson.put(newpos + 1, sObject);
}
else {
    for (int j = newpos + 1; j <= t.length(); j++) {
        newJson.put(j, t.getJSONObject(j - 1));
    }
}
```

## 연락처 삭제

<img src="https://user-images.githubusercontent.com/78538108/148026193-ea0b5d4d-4376-4476-847d-a631052c04e6.gif" width="200" height="400"/>

연락처 삭제는 ContactDetail Activity에서 삭제 아이콘을 클릭하면 contact.json 파일을 읽어 JSONObject를 만든 후 "contact"에 들어어있는 JSONArray에서 현재 보고 있는 연락처의 "id"를 가지고 있는 JSONObject를 지운 후 다시 JSONObject를 만들어 contact.json에 write하여 구현하였다.

```Java
public JSONArray deleteData(int id) {
        String json = readFile();
        int i;
        try {
            JSONArray oldJson = jsonParsing(json);
            JSONArray newJson = new JSONArray();
            for(i = 0; i < oldJson.length(); i++) {
                if(oldJson.getJSONObject(i).getInt("id") == id) {
                    continue;
                }
                newJson.put(oldJson.getJSONObject(i));
            }
            return newJson;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
```

삭제 아이콘을 클릭하였을 때 Alert를 이용하여 먼저 삭제를 정말로 하는 건지 확인을 한 후 확인을 클릭하면 삭제를 진행하게 하였다. 


## 연락처 검색

<img src="https://user-images.githubusercontent.com/78538108/148023471-4eed85c2-2939-4268-9c61-cbc16b8f7599.gif" width="200" height="400"/>

연락처 검색은 연락처 recyclerViewAdapter에 filter 함수를 만들어 구현하였다. 이 함수에서는 editText(검색창)에서 입력된 String을 포함하고 있는 이름을 가진 연락처들만으로 filteredList를 만들고 이 filteredList로 recyclerView를 다시 만들어 검색한 이름만이 view에 보이도록 하였다. 

```Java
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = list;
                } else {
                    ArrayList<ContactItem> filteringList = new ArrayList<>();
                    for(ContactItem contact : list) {
                        if(contact.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(contact);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<ContactItem>)results.values;
                notifyDataSetChanged();
            }
        };
    }
```
editText에서 Text가 변화할때 마다 filteredList가 바뀔 수 있도록 editText에 TextChangeListner을 아래와 같이 추가하여주었다. 
```Java
editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRecyclerAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
```

# 두 번째 탭 : 갤러리
## 추가 버튼
<img src="https://user-images.githubusercontent.com/78538108/148030777-093f70c9-3317-44e8-bdc8-80cea7565d51.gif" width="200" height="400"/>

갤러리에서 이미지 하나를 가져와 추가할 수 있다.

## 이미지 상세화면
<img src="https://user-images.githubusercontent.com/78538108/148030844-d8fd6a36-05d9-4fc4-a55f-bba036bbb04d.gif" width="200" height="400"/>

## 레이아웃 변경 버튼
<img src="https://user-images.githubusercontent.com/78538108/148030785-2498ccfd-baf5-4ff7-ab46-f57f9c33e985.gif" width="200" height="400"/>

초기 레이아웃의 columnCount는 3이다. columnCount가 3인 경우 1로, 1인 경우 3으로 변경한다.
## 이미지 해상도 문제
이미지를 불러올 때 원래 해상도를 그대로 불러오는 경우 시간이 너무 오래 걸리는 문제가 생긴다.
이미지 크기를 레이아웃에 맞춰 가져오도록 하였다.
또한 이미지 개수가 많아질 경우 갤러리 탭이 로딩되는 시간이 오래 걸리므로
이를 Thread로 만들어 이미지가 로딩될 때마다 바로 띄워지도록 하였다.
Thread로 만드는 경우 Thread가 종료되기 이전에 탭을 이동하거나 같은 탭을 다시 누르는 경우 앱이 다운되는 문제가 생기므로
onDestroy()에서 Thread에 interrupt() 호출하고 wait() 하도록 했다.

## 이미지 삭제
<img src="https://user-images.githubusercontent.com/78538108/148030903-d6374615-6b7e-4a68-b221-e128cff29d48.gif" width="200" height="400"/>
<img src="https://user-images.githubusercontent.com/78538108/148030921-6f3a45fa-cbf2-4e9d-b417-c7ae5b9cd071.gif" width="200" height="400"/>


# 세 번째 탭 : 캘린더

## 캘린더 

## Todo Fragment

선택한 날짜에 해당하는 TodoList를 화면에 보여준다. 각 Todo 항목은 checkBox를 이용하여 완료하였을 경우 클릭하면 줄이 처지고 박스의 색이 변하며 완료되었음을 나타낸다. edit버튼을 클릭하면 그 날짜의 TodoList를 수정할 수 있다.

### TodoList DB 구조

TodoList의 구조는 앱의 내부저장소에 todo.json에 JSONObject를 아래와 같은 구조로 만들어 저장하였다. 

{date: JSONArray}

각 날짜를 String으로 하여 키로 설정한 후 각 키에 값으로 JSONArray을 할당하였다. 각 JSONArray는 아래와 같은 구조의 JSONObject들의 배열이다. 

{"check": Boolean, "content": String}

각 JSONObject는 todoList의 각 todo Item이며 그 항목의 완료 여부를 "check"키에 할당하여 Boolean에 저장하였고, 그 todo Item의 내용을 "content"에 저장하였다.

json파일에서 읽은 데이터를 아래와 같은 TodoItem Class에 저장하여 이후 RecyclerView에 사용하였다.

```Java
public class TodoItem {
    Boolean check;
    String content;
    ...
```

### Todo Item

Todo Fragment에서 각 날짜에 해당하는 Todo List를 RecyclerView를 이용하여 구현하였다. RecyclerView에서 각 item은 checkBox로 구현하여 check가 될 수 있도록 하였으며 "content"에 있는 항목은 checkBox의 Text로 설정하였다. 각 checkBox가 클릭돼었을 때 todo.json에서 그 todoItem의 "check"를 현재 check 상태로 update에 주어 todo.json 파일이 현재 각 todo Item의 상태를 저장하고 있을 수 있도록 하였다.

<img src="https://user-images.githubusercontent.com/78538108/148038869-e77dab99-9bb4-4102-bf6d-74a2e3e9753f.gif" width = "200" height=""/>   <img src="https://user-images.githubusercontent.com/78538108/148038889-cb6d8a4b-0312-46fd-b841-9fca2291fa33.gif" width = "200" height=""/>


### TodoList 수정

<img src="https://user-images.githubusercontent.com/78538108/148038881-b38b45a3-ff3f-46e4-9dd6-bd19524ce0ef.gif" width = "200" height=""/>

edit 버튼을 누르면 현재 선택된 날짜의 todoList를 편잡히는 Activity로 이동하게 된다. 이 Activity에서는 todo.json에서 현재 선택된 날짜를 key로 가지는 TodoList를 읽어온 뒤 각 todo Item을 EditText의 초기값으로 설정하였다. 각 content를 편집하는 EditText와 그 항목을 삭제할 수 있는 삭제 버튼을 묶어 item을 만든 후에 recyclerView를 이용하여 TodoList의 모든 항목에 대해 편집 item을 만들었다. 그리고 recyclerView 아래에는 TodoList에 새로운 item을 추가할 수 있는 버튼을 만들었다.

오른쪽 아래에 있는 완료 버튼을 누르게 되면 현재 각 EditText에 입력되어 있는 값들로 가시 JSONArray를 만들어 todo.json에 넣어 수정한 todoList를 저장하였다. 


## Diary

