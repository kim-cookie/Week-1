# <img width="30" height="30" alt="Image" src="https://github.com/user-attachments/assets/f1443f32-13a8-440a-bae8-3e2980cf446b" /> DAYWEAR

---

## 📝 프로젝트 소개

 이 앱은 사용자가 상품을 장바구니에 담고, 구매한 다음 리뷰까지 남길 수 있으며 달력을 이용해 데일리룩을 정할 수 있는 쇼핑몰 앱입니다. Kotlin + Jetpack Compose로 개발되었고, 사용자 친화적인 UI와 간단한 회원 기능이 포함되어 있습니다.

### 🔧 사용 기술
- Kotlin
- Jetpack Compose
- SharedPreferences
- Firebase Firestore
- Glide
- Android SDK 34

### 📄 주요 기능

| 화면 | 설명 |
|------|------|
| 🔐 로그인 / 회원가입 | 사용자는 아이디와 비밀번호로 계정을 생성하고 로그인할 수 있으며 각 사용자별로 개인화된 정보(장바구니, 구매내역 등)가 저장됩니다. |
| 🏠 홈 화면 | 전체 상품 목록을 카테고리별로 볼 수 있으며 원하는 상품을 클릭해 상세 정보를 확인할 수 있습니다. 이미지를 기반으로 한 직관적인 UI를 제공합니다. |
| ❤️ 위시리스트 |	상품 이미지에 있는 하트 아이콘을 눌러 위시리스트에 상품을 저장할 수 있습니다. 나중에 관심 있는 상품을 다시 쉽게 확인할 수 있습니다. |
| 🛒 장바구니 | 선택한 상품을 장바구니에 담아둘 수 있으며 총 가격 확인 및 구매 진행이 가능합니다. 선택 항목의 총합이 자동 계산되어 보여집니다. |
| 🧾 구매 내역 | 사용자가 결제한 상품들을 날짜별로 묶어 확인할 수 있습니다. 각 날짜마다 어떤 상품을 구매했는지 손쉽게 파악할 수 있습니다. |
| ⭐ 리뷰 | 구매한 상품에 한해서 리뷰를 작성할 수 있으며 별점 및 텍스트 입력을 통해 다른 사용자들과 의견을 공유할 수 있습니다. 리뷰는 읽기 전용으로도 제공됩니다. |
| 📅 달력	| 달력 화면에서 특정 날짜를 선택해 해당 날짜에 입을 옷을 미리 저장해둘 수 있습니다. 일기처럼 활용할 수 있는 코디 캘린더 기능입니다. |

---

### 👥 팀원 소개
| 김재헌 | 하예영 |
|------|------|
| 카이스트 전산학부 | 숙명여대 데이터사이언스전공 |
| [Github Profile](https://github.com/kim-cookie) | [Github Profile](https://github.com/hayeyoung) |

---

## 🖼️ 화면 미리보기

### 🔐 로그인 / 회원가입 화면  
<img width="230" height="504" alt="Image" src="https://github.com/user-attachments/assets/be921508-4695-4c70-a911-59f661990085" />

> 사용자가 앱을 처음 사용할 때 계정을 생성하거나 로그인할 수 있는 화면입니다.  
> SharedPreferences를 통해 사용자 정보를 관리하며, 사용자별로 데이터를 분리 저장합니다.

---

### 🏠 홈 화면 (카테고리 리스트)  
<img width="229" height="500" alt="Image" src="https://github.com/user-attachments/assets/5e3e636d-75f1-436e-ba70-57b730953c6d" />

> 전체 상품을 카테고리 형태로 보여주는 메인 화면입니다.  
> 각 카테고리를 클릭하면 해당 카테고리에 포함된 상품들을 보여주는 `ImageList` 화면으로 이동합니다.

---

### 🗂️ ImageList 화면 (카테고리별 상품 리스트)  
<img width="230" height="498" alt="Image" src="https://github.com/user-attachments/assets/378ca656-c600-4193-b244-95defccc6a59" />

> 선택된 카테고리의 상품들을 그리드 형식으로 보여줍니다.  
> 각 상품에는 이미지, 이름, 가격, 하트(위시리스트 추가), 장바구니 버튼이 함께 표시됩니다.

---

### ❤️ 위시리스트 화면  
<img width="231" height="500" alt="Image" src="https://github.com/user-attachments/assets/6f6bd012-c1ff-4a03-8e8d-ed36ead55024" />

> 하트를 눌러 저장해둔 관심 상품들을 보여주는 화면입니다.  
> 나중에 다시 보고 싶은 상품들을 간편하게 모아 관리할 수 있습니다.

---

### 🛒 장바구니 화면  
<img width="230" height="498" alt="Image" src="https://github.com/user-attachments/assets/87f06197-05ba-47a3-ae29-555c7baf9530" />

> 담아둔 상품 목록을 보여주는 화면입니다.  
> 수량 변경, 항목 삭제, 선택된 상품의 총 가격 확인이 가능하며 ‘구매하기’ 버튼을 통해 결제 화면으로 이동합니다.

---

### 🧾 구매 내역 화면  
<img width="228" height="496" alt="Image" src="https://github.com/user-attachments/assets/c5366681-a288-4fef-a070-974d48b668d3" />

> 실제로 결제한 상품들을 날짜별로 묶어서 보여주는 화면입니다.  
> 각 날짜의 구매 내역을 쉽게 확인할 수 있으며 해당 상품에 대해 리뷰를 작성할 수도 있습니다.

---

### ⭐ 리뷰 화면  
<img width="229" height="501" alt="Image" src="https://github.com/user-attachments/assets/e150577c-86a8-4f2b-aedf-51dea2f0512c" />

<img width="230" height="504" alt="Image" src="https://github.com/user-attachments/assets/ca226ce4-d8f7-4d86-a117-e0584bbd63d6" />

> 구매한 상품에 한해서 리뷰를 작성할 수 있는 화면입니다.  
> 별점과 텍스트로 의견을 남길 수 있으며 다른 사용자의 리뷰도 함께 확인 가능합니다.

---

### 📅 달력 화면  
<img width="231" height="498" alt="Image" src="https://github.com/user-attachments/assets/662ee2c4-52a7-414f-8508-2a71f41d2da2" />

> 날짜별로 그날 입은 옷이나 코디를 기록할 수 있는 코디 캘린더 화면입니다.  
> 코디 일기처럼 사용할 수 있어 일상 속 스타일을 관리하기에 좋습니다.
> 해당 날짜의 코디를 미리 정해둘 수 있어 여행을 가거나 할 때 유용합니다.

---

### 🙋 회원 정보 화면  
<img width="228" height="499" alt="Image" src="https://github.com/user-attachments/assets/7baeec43-856a-4c00-be92-ef0562b79f53" />

> 로그인한 사용자의 이메일, 가입일, 앱 설정 정보를 확인할 수 있는 마이페이지입니다.  
> 로그아웃 기능 등도 이 화면에서 확장 가능하게 설계되었습니다.

---

## 📦 APK 다운로드

🔗 [앱 다운로드 링크](https://drive.google.com/file/d/1iHlx_z2d5pF2LlPoZ_Z-3KkfL2lWTCkh/view?usp=sharing)  

---

## ✅ 개선점
  
- 🛎️ **알림 기능**: 찜한 상품 할인 시 알림을 받을 수 있는 기능 도입  
- 🗂️ **카테고리 필터 기능**: 홈/리스트 화면에서 조건별 필터(가격, 색상 등) 제공  
- 🛠️ **마이페이지 개선**: 프로필 편집, 비밀번호 변경, 탈퇴 기능 추가  
- 📤 **Firebase Storage 연동**: 사용자 리뷰에 사진 첨부 기능 제공  
- 🌐 **소셜 로그인 기능**: 구글/카카오 계정으로 간편 로그인 제공 예정
- 💳 **실제 결제 연동**: 현재는 ‘구매하기’ 클릭 시 결제 없이 바로 구매내역으로 이동하지만 추후 실제 결제를 도입해 실제 구매 프로세스를 완성
